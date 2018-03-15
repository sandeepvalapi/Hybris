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
package de.hybris.platform.test;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

import de.hybris.platform.core.threadregistry.RegistrableThread;
import org.apache.log4j.Logger;


/**
 * Allows to let any number of threads start at the same time by starting them up and (internally) waiting until all of
 * them are up and running. This is especially useful when larger numbers of threads are being created for testing
 * purpose because otherwise often creation time outnumbers shorter testing time which at worst would make a parallel
 * test run nearly single threaded.
 * 
 * Also this class provides means to either wait for test threads to finish or to stop them manually via
 * {@link Thread#interrupt()}.
 */
public class TestThreadsHolder<T extends Runnable> implements de.hybris.platform.test.RunnerCreator<Runnable>
{
	private static final Logger LOG = Logger.getLogger(TestThreadsHolder.class.getName());

	private final CountDownLatch prepared;
	private final CyclicBarrier start;
	private final CountDownLatch end;
	private volatile long allStartedTimeMillis;
	private volatile long allFinishedTimeMillis;
	private final boolean busyWaiting;

	private final de.hybris.platform.test.RunnerCreator<T> creator;

	private final RunnerThread<T>[] threads;

	/**
	 * @deprecated since ages - use{@link de.hybris.platform.test.RunnerCreator} directly
	 */
	@Deprecated
	public interface RunnerCreator<T extends Runnable> extends de.hybris.platform.test.RunnerCreator<T>
	{
		// for backward compatibilioty
	}

	/**
	 * Creates a specified number of threads that will perform the given runnable logic as soon as {@link #startAll()}
	 * has been invoked.
	 * 
	 * <b>Please note that this constructor is intended for creating inner classes only. The {@link #newRunner(int)}
	 * method of this inner class *must* be overwritten!</b>
	 */
	public TestThreadsHolder(final int numberOfThreads)
	{
		this(numberOfThreads, (de.hybris.platform.test.RunnerCreator<T>) null);
	}

	/**
	 * Creates a specified number of threads that will perform the given runnable logic as soon as {@link #startAll()}
	 * has been invoked.
	 * 
	 * <b>Please note that this constructor is intended for creating inner classes only. The {@link #newRunner(int)}
	 * method of this inner class *must* be overwritten!</b>
	 */
	public TestThreadsHolder(final int numberOfThreads, final boolean inheritTenant)
	{
		this(numberOfThreads, (de.hybris.platform.test.RunnerCreator<T>) null, inheritTenant);
	}

	/**
	 * Creates a specified number of threads that will perform the given runnable logic as soon as {@link #startAll()}
	 * has been invoked.
	 * 
	 * Please note that technically the actual threads will be immediately created and started. Calling
	 * {@link #startAll()} only releases them from a waiting state.
	 * 
	 * @param numberOfThreads
	 *           the number of runner threads to use
	 * @param runnable
	 *           the runnable instance being used by each runner thread
	 */
	public TestThreadsHolder(final int numberOfThreads, final T runnable)
	{
		this(numberOfThreads, new SingletonRunnerCreator(runnable));
	}

	/**
	 * Creates a specified number of threads that will perform the given runnable logic as soon as {@link #startAll()}
	 * has been invoked.
	 * 
	 * Please note that technically the actual threads will be immediately created and started. Calling
	 * {@link #startAll()} only releases them from a waiting state.
	 * 
	 * @param numberOfThreads
	 *           the number of runner threads to use
	 * @param runnable
	 *           the runnable instance being used by each runner thread
	 * @param inheritTenant
	 *           if true all runner threads will inherit the current callers tenant, if false no thread will have a
	 *           active tenant
	 */
	public TestThreadsHolder(final int numberOfThreads, final T runnable, final boolean inheritTenant)
	{
		this(numberOfThreads, new SingletonRunnerCreator(runnable), inheritTenant);
	}

	/**
	 * Creates a specified number of threads that will perform the logic created by the specified runnable creator as
	 * soon as {@link #startAll()} has been invoked.
	 * 
	 * Please note that technically the actual threads will be immediately created and started. Calling
	 * {@link #startAll()} only releases them from a waiting state.
	 * 
	 * @param numberOfThreads
	 *           the number of runner threads to use
	 * @param creator
	 *           the creator responsible for providing a runnable instance for each runner thread
	 */
	public TestThreadsHolder(final int numberOfThreads, final de.hybris.platform.test.RunnerCreator<T> creator)
	{
		this(numberOfThreads, creator, false);
	}

	/**
	 * Creates a specified number of threads that will perform the logic created by the specified runnable creator as
	 * soon as {@link #startAll()} has been invoked.
	 * 
	 * Please note that technically the actual threads will be immediately created and started. Calling
	 * {@link #startAll()} only releases them from a waiting state.
	 * 
	 * @param numberOfThreads
	 *           the number of runner threads to use
	 * @param creator
	 *           the creator responsible for providing a runnable instance for each runner thread
	 * @param inheritTenant
	 *           if true each runner thread will inherit the current callers tenant, if false all threads will have no
	 *           active tenant set
	 */
	public TestThreadsHolder(final int numberOfThreads, final de.hybris.platform.test.RunnerCreator<T> creator,
			final boolean inheritTenant)
	{
		if (numberOfThreads < 1)
		{
			throw new IllegalArgumentException(numberOfThreads + " < 1");
		}
		this.creator = creator;
		// we need this for everyone who wants to wait until all threads are ready but not processing yet
		prepared = new CountDownLatch(numberOfThreads);

		// make all runners AND master wait for each other
		start = new CyclicBarrier(numberOfThreads + 1);
		// wait for all runners on this
		end = new CountDownLatch(numberOfThreads);

		busyWaiting = useBusyWaiting();

		threads = new RunnerThread[numberOfThreads];

		final Tenant inheritedTenant = inheritTenant ? Registry.getCurrentTenantNoFallback() : null;

		for (int i = 0; i < numberOfThreads; i++)
		{
			final RunnerThread newThread = new RunnerThread(this.newRunner(i), prepared, start, end, inheritedTenant);
			threads[i] = newThread;
			newThread.start();
		}
	}

	/**
	 * Override to change latch <b>timeout guarded</b> waiting method.
	 * 
	 * As default waiting is done via {@link CountDownLatch#await(long, TimeUnit)} which is most efficient for not
	 * disturbing workers and returning as quick as possible.
	 * 
	 * However if you're <b>suffering from JVM being blocked often</b> (not getting computing time for minutes) for
	 * instance when running in a virtual environment it may be better to change timeout method to busily counting
	 * 'ticks'. In that case the latch is tested many times waiting at most one second. In case the JVM is being blocked
	 * for a longer time the timeout mechanism simply continues where it was blocked giving workers a fair chance to
	 * finished their work, even if it means that the actual timeout period is greatly exceeded!
	 */
	protected boolean useBusyWaiting()
	{
		return false;
	}

	@Override
	public Runnable newRunner(final int threadNumber)
	{
		if (this.creator != null)
		{
			return this.creator.newRunner(threadNumber);
		}
		else
		{
			throw new IllegalStateException("threads holder " + this
					+ " is lacking runner creator - please provide or override newRunner() method");
		}
	}

	public boolean waitForPrepared(final long timeout, final TimeUnit unit)
	{
		try
		{
			return waitForLatch(prepared, timeout, unit);
		}
		catch (final InterruptedException e)
		{
			Thread.currentThread().interrupt();
			throw new IllegalStateException("interrupted while waiting for prepare", e);
		}
	}

	/**
	 * Allows all runner threads to perform the given test logic. Ensures that all threads are up and running, which
	 * means that thread creation and startup overhead does not affect the tested logic at all.
	 * 
	 * You may either wait for them to terminate on their own by calling {@link #waitForAll(long, TimeUnit)} or stop them
	 * after some time via {@link #stopAll()}
	 * 
	 * @see #stopAll()
	 * @see #waitForAll(long, TimeUnit)
	 */
	public void startAll()
	{
		// All runners AND the caller must pass this barrier by calling await()
		// which makes them wait until all (runners AND caller) have arrived.
		// This way we ensure runners to start at the same time.
		try
		{
			start.await();
			allStartedTimeMillis = System.currentTimeMillis();
		}
		catch (final BrokenBarrierException e)
		{
			throw new IllegalStateException(e);
		}
		catch (final InterruptedException e)
		{
			Thread.currentThread().interrupt();
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Starts all threads via {@link #startAll()} and lets them run for the specified duration.
	 * 
	 * After that all threads are stopped via {@link #stopAndDestroy(int)} and the stop result is being returned.
	 * 
	 * @param duration
	 *           the time to run
	 * @param unit
	 *           the unit of time to run
	 * @param stopWaitSeconds
	 *           the time to wait after stopping threads before killing them 'the hard way'
	 */
	public boolean runAll(final long duration, final TimeUnit unit, final int stopWaitSeconds)
	{
		startAll();
		try
		{
			Thread.sleep(unit.toMillis(duration));
		}
		catch (final InterruptedException e)
		{
			Thread.currentThread().interrupt(); // restore flag
		}
		return stopAndDestroy(stopWaitSeconds);
	}

	/**
	 * Stops all runner threads by invoking {@link Thread#interrupt()}.
	 * 
	 * <b> Please make sure that the tested logic is handling thread interrupts correctly, which means either to pass on
	 * the {@link InterruptedException} or restoring the interrupted flag via {@link Thread#interrupt()}!</b>
	 */
	public void stopAll()
	{
		for (final Thread t : threads)
		{
			t.interrupt();
		}
	}

	/**
	 * Does the same as {@link #stopAndDestroy(int)} using 10 seconds wait time.
	 * 
	 * @deprecated since ages - Please use either{@link #waitAndDestroy(int)} or {@link #stopAndDestroy(int)}.
	 */
	@Deprecated
	public void destroy()
	{
		stopAndDestroy(10);
	}


	/**
	 * Attempts to stop all runners via {@link #stopAll()} and waits for completion for 10 seconds. If for some reason
	 * this doesn't work it will unsafely stop all runners via {@link Thread#stop()}.
	 * 
	 * @return true in case all runners finished orderly, false otherwise
	 */
	public boolean stopAndDestroy(final int timeWaitSeconds)
	{
		stopAll();
		final boolean ok = waitForAll(timeWaitSeconds, TimeUnit.SECONDS);
		if (!ok)
		{
			killAll();
		}
		return ok;
	}

	/**
	 * Waits for runner termination for the specified time. If for some reason this doesn't work it will first try to
	 * stop them via interrupt, wait for terminationWaitSeconds/2 and only if this doesn't succeed all remaining workers
	 * will be unsafely stopped via {@link Thread#stop()}.
	 * 
	 * @param terminationWaitSeconds
	 *           the time to wait for runner thread termination in seconds; after this time has elapsed it will unsafely
	 *           kill them via {@link Thread#stop()}
	 */
	public boolean waitAndDestroy(final int terminationWaitSeconds)
	{
		final boolean ok = waitForAll(terminationWaitSeconds, TimeUnit.SECONDS);
		if (!ok)
		{
			stopAll(); // first try to abort runners via interrupt
			if (!waitForAll(Math.max(terminationWaitSeconds / 2, 10), TimeUnit.SECONDS))
			{
				killAll(); // now we've got to kill thread the bad way :( 
			}
		}
		return ok;
	}

	@SuppressWarnings("deprecation")
	private void killAll()
	{
		for (final Thread t : threads)
		{
			if (t.isAlive())
			{
				try
				{
					LOG.error("###################################################################################");
					LOG.error("### Attention: Stopping thread "+t+" since it didnt finish in time. This may    ###");
					LOG.error("### leave the system in a unstable state!!!                                     ###");
					LOG.error("###                                                                             ###");
					t.stop();
					LOG.error("###################################################################################");
				}
				catch (final Exception e)
				{
					// swallow
				}
			}
		}
	}

	/**
	 * If all threads finished in
	 */
	public long getStartToFinishMillis()
	{
		if (allStartedTimeMillis == 0)
		{
			throw new IllegalStateException("got no start time");
		}
		if (allFinishedTimeMillis == 0)
		{
			throw new IllegalStateException("got no finish time");
		}

		return allFinishedTimeMillis - allStartedTimeMillis;
	}

	/**
	 * Returns the current number of threads that did not complete their testing logic.
	 * 
	 * Technically more threads than the returned number may be still alive, but as soon as they have completed their
	 * 'payload' they will definitely terminate so there is no need to wait for that.
	 */
	public int getAlive()
	{
		return (int) end.getCount();
	}

	/**
	 * Waits for all runners to finish their work for a specified amount of time.
	 * 
	 * @return true in case all runners did really finish, false otherwise
	 */
	public boolean waitForAll(final long timeout, final TimeUnit unit)
	{
		try
		{
			final int before = getAlive();
			final boolean allDone = waitForLatch(end, timeout, unit);

			if (allDone)
			{
				allFinishedTimeMillis = System.currentTimeMillis();

				// now wait for runner threads to die too - after the passed the 'end' barrier they can't but stop any way 
				for (final Thread t : threads)
				{
					t.join();
				}
			}
			else
			{
				final int current = getAlive();
				System.err.println("still got " + current + " threads alive (stopped " + (before - current) + ")");
			}
			return allDone;
		}
		catch (final InterruptedException e)
		{
			Thread.currentThread().interrupt();
			throw new IllegalStateException("interrupted while waiting", e);
		}
	}

	private boolean waitForLatch(final CountDownLatch latch, final long timeout, final TimeUnit unit) throws InterruptedException
	{
		boolean finished = false;
		// 'busy waiting'
		// Use this method in case you expect the JVM being blocked for longer time.
		// In that case workers would have no chance to process anything. Here instead
		// of waiting until a fixed time has passed ( say 60 sec ) it would be much better
		// to wait for a fixed number of 'ticks' that suffer from blocking the same way
		// as the workers do ( say sleep 60 times for one second ).
		if (busyWaiting)
		{
			final long ticksToWait = Math.max(1, unit.toSeconds(timeout));
			for (int tick = 0; tick < ticksToWait; tick++)
			{
				if (latch.await(1, TimeUnit.SECONDS))
				{
					finished = true;
					break;
				}
			}
		}
		// 'time waiting'
		// Here the caller simply sleeps until either all workers have finished or a specific time
		// has run up. This method should be most efficient for not disturbing workers.
		else
		{
			finished = latch.await(timeout, unit);
		}
		return finished;
	}

	/**
	 * Tells whether any runner has recorded a error.
	 * 
	 * @see #getErrors()
	 */
	public boolean hasErrors()
	{
		for (final RunnerThread runner : threads)
		{
			final Throwable e = runner.error;
			if (e != null)
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns all errors recorded from runner threads.
	 * 
	 * Please note that {@link InterruptedException} will <b>not</b> be recorded since this we prefer thread interrupt as
	 * valid way of stopping runners.
	 * 
	 * @see #hasErrors()
	 */
	public Map<Integer, Throwable> getErrors()
	{
		final Map<Integer, Throwable> ret = new LinkedHashMap<Integer, Throwable>(threads.length);
		int runnerNumber = 0;
		for (final RunnerThread runner : threads)
		{
			final Throwable e = runner.error;
			if (e != null)
			{
				ret.put(Integer.valueOf(runnerNumber), e);
			}
			runnerNumber++;
		}
		return ret;
	}

	public List<T> getRunners()
	{
		final List<T> ret = new ArrayList<T>(threads.length);
		for (final RunnerThread<T> runnerThread : threads)
		{
			ret.add(runnerThread.runner);
		}
		return ret;
	}

	public T getRunner(final int pos)
	{
		return threads[pos].runner;
	}

	private static class RunnerThread<T extends Runnable> extends RegistrableThread
	{
		private final CountDownLatch prepared;
		private final CyclicBarrier startGate;
		private final CountDownLatch end;
		private final T runner;
		private final Tenant tenant;

		private volatile Throwable error = null;

		RunnerThread(final T r, final CountDownLatch prepared, final CyclicBarrier startGate, final CountDownLatch end,
				final Tenant tenantToRunIn)
		{
			super(r);
			this.runner = r;
			this.prepared = prepared;
			this.startGate = startGate;
			this.end = end;
			this.tenant = tenantToRunIn;
		}

		@Override
		public void internalRun()
		{
			try
			{
				if (tenant != null)
				{
					Registry.setCurrentTenant(tenant);
				}

				prepared.countDown();
				// prepare for all threads to be ready: this requires all
				// runner threads PLUS master one to call await() on this barrier!
				startGate.await();
				// perform logic
				super.internalRun();
			}
			catch (final InterruptedException e)
			{
				Thread.currentThread().interrupt();
				LOG.debug("test thread " + this + " has been interrupted - possibly runner has not been invoked!");
			}
			catch (final AssertionError e)
			{
				this.error = e;
			}
			catch (final Error e)
			{
				this.error = e;
				throw e;
			}
			catch (final Throwable e)
			{
				this.error = e;
				e.printStackTrace();
			}
			finally
			{
				end.countDown();
				if (tenant != null)
				{
					Registry.unsetCurrentTenant();
				}
			}
		}
	}

	private static class SingletonRunnerCreator<T extends Runnable> implements de.hybris.platform.test.RunnerCreator<T>
	{
		private final T runnable;

		SingletonRunnerCreator(final T runnable)
		{
			this.runnable = runnable;
		}

		@Override
		public T newRunner(final int threadNumber)
		{
			return runnable;
		}
	}

}
