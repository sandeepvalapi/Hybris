/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 * This class represents the WorkerValueQueue implementation before 4.2.2 and is here only for performance testing.
 * <p>
 * <b>Don't use it elsewhere!</b>
 */
public class DeprecatedWorkerValueQueue<E> extends AbstractWorkerValueQueue<E>
{
	public static final int TIME_WAIT_PUT = 500;
	public static final TimeUnit TIME_WAIT_PUT_UNIT = TimeUnit.MILLISECONDS;

	private final int maxSize;

	private final ReadWriteLock valueQueueLock;
	private final Condition valueQueueEmpty;
	private final Condition valueQueueFull;
	private final LinkedList<E> queue;

	private final boolean[] currentlyTaken;
	private volatile List<E> takenValues;
	private volatile List<E> takenValuesForExec;
	private volatile boolean stopped = false;

	public DeprecatedWorkerValueQueue(final int maxSize)
	{
		super();
		this.maxSize = maxSize;
		this.queue = new LinkedList<E>();
		this.takenValues = new ArrayList<E>(30);
		this.takenValuesForExec = Collections.unmodifiableList(takenValues);
		this.currentlyTaken = new boolean[maxSize];
		this.valueQueueLock = new ReentrantReadWriteLock();
		this.valueQueueEmpty = this.valueQueueLock.writeLock().newCondition();
		this.valueQueueFull = this.valueQueueLock.writeLock().newCondition();
	}

	@Override
	public Object executeOnTakenValues(final ExecuteOnTaken<E> exec)
	{
		if (exec == null)
		{
			throw new IllegalArgumentException("exec was null");
		}
		this.valueQueueLock.readLock().lock();
		try
		{
			return exec.execute(this, takenValuesForExec);
		}
		finally
		{
			this.valueQueueLock.readLock().unlock();
		}

	}

	@Override
	public void stop()
	{
		this.valueQueueLock.writeLock().lock();
		try
		{
			stopped = true;
		}
		finally
		{
			// wake up all threads waiting on empty condition
			this.valueQueueEmpty.signalAll();
			this.valueQueueLock.writeLock().unlock();
		}
	}

	@Override
	public E take(final int workerNumber)
	{
		E ret = null;
		if (!stopped)
		{
			this.valueQueueLock.writeLock().lock();
			try
			{
				if (isValueTakenBy(workerNumber))
				{
					throw new IllegalStateException("there is still a taken value for worker " + workerNumber);
				}

				while (!stopped && queue.isEmpty())
				{
					this.valueQueueEmpty.await(); // free lock and block until queue is not empty any more
				}
				if (!stopped)
				{
					ret = queue.remove();
					// mark as value take even if we're just waiting for a new one
					setValueTakenBy(workerNumber, true, ret);
				}
			}
			catch (final InterruptedException e)
			{
				// ok, worker has been interrupted -> reader detected end of file
			}
			finally
			{
				// wake up all threads waiting on full queue condition / taken values
				this.valueQueueFull.signalAll();
				this.valueQueueLock.writeLock().unlock();
			}
		}
		return ret;
	}

	@Override
	public void clearValueTaken(final int workerNumber)
	{
		this.valueQueueLock.writeLock().lock();
		try
		{
			setValueTakenBy(workerNumber, false, null); // clear worker value taken marker
		}
		finally
		{
			this.valueQueueFull.signalAll(); // wake up reader/master thread which waits for empty queue / non-taken values
			this.valueQueueLock.writeLock().unlock();
		}
	}

	@Override
	public void put(final E value)
	{
		put(value, null);
	}

	@Override
	public boolean put(final E value, final ExecuteWhileWaiting<E> exec)
	{
		this.valueQueueLock.writeLock().lock();
		try
		{
			while (queue.size() >= maxSize)
			{
				try
				{
					// free lock and block until queue is not full any more
					// but at most 3 seconds to allow processing of piled up results
					this.valueQueueFull.await(TIME_WAIT_PUT, TIME_WAIT_PUT_UNIT);
				}
				catch (final InterruptedException e)
				{
					// should not happen
				}
				// while we're here process some results -> abort if requested
				if (!execute(exec, value))
				{
					return false;
				}
			}
			queue.add(value);
			// wake up all threads waiting on empty queue
			this.valueQueueEmpty.signalAll();

			return true;
		}
		finally
		{
			this.valueQueueLock.writeLock().unlock();
		}
	}

	@Override
	public void waitUntilEmpty(final long time, final TimeUnit timeUnit, final ExecuteWhileWaiting exec)
	{
		while (waitIfNotEmpty(time, timeUnit))
		{
			if (!execute(exec, null))
			{
				return;
			}
		}
	}

	// atomic check and wait if queue is not empty
	private final boolean waitIfNotEmpty(final long time, final TimeUnit timeUnit)
	{
		boolean keepWaiting;
		this.valueQueueLock.writeLock().lock(); // need write lock to wait on condition
		try
		{
			keepWaiting = isValueTakenOrQueueNotEmptyInternal();
			if (keepWaiting)
			{
				this.valueQueueFull.await(time, timeUnit);
				keepWaiting = isValueTakenOrQueueNotEmptyInternal();
			}
		}
		catch (final InterruptedException e)
		{
			// should not happen
			keepWaiting = false;
		}
		finally
		{
			this.valueQueueLock.writeLock().unlock(); // release write lock soon
		}
		return keepWaiting;
	}

	@Override
	public void clear()
	{
		this.valueQueueLock.writeLock().lock();
		try
		{
			Arrays.fill(currentlyTaken, false);
			queue.clear();
			takenValues.clear();
		}
		finally
		{
			this.valueQueueFull.signalAll();
			this.valueQueueLock.writeLock().unlock();
		}
	}

	private boolean execute(final ExecuteWhileWaiting<E> exec, final E value)
	{
		boolean ret = true;
		if (exec != null)
		{
			ret = exec.execute(this, value);
		}
		return ret;
	}

	@Override
	public boolean isValueTakenOrQueueNotEmpty()
	{
		this.valueQueueLock.readLock().lock();
		try
		{
			return isValueTakenOrQueueNotEmptyInternal();
		}
		finally
		{
			this.valueQueueLock.readLock().unlock();
		}
	}

	private final boolean isValueTakenOrQueueNotEmptyInternal()
	{
		return !queue.isEmpty() || isValueTaken();
	}

	private boolean isValueTaken()
	{
		boolean result = false;
		for (final boolean oneTaken : currentlyTaken)
		{
			result = oneTaken || result;
		}
		return result;
	}

	private final boolean isValueTakenBy(final int workerNumber)
	{
		return currentlyTaken[workerNumber];
	}

	private final void setValueTakenBy(final int workerNumber, final boolean taken, final E value)
	{
		if (taken)
		{
			this.currentlyTaken[workerNumber] = true;
			assureValueSize(workerNumber);
			takenValues.set(workerNumber, value);
		}
		else
		{
			this.currentlyTaken[workerNumber] = false;
			if (takenValues.size() > workerNumber)
			{
				takenValues.set(workerNumber, null);
			}
		}
	}

	private final void assureValueSize(final int pos)
	{
		final int s = takenValues.size();
		if (s <= pos)
		{
			for (int i = s - 1; i <= pos; i++)
			{
				takenValues.add(null);
			}
		}
	}
}
