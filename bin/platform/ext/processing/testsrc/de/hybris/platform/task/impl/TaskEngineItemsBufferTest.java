package de.hybris.platform.task.impl;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.task.impl.DefaultTaskService.ItemsBuffer;
import de.hybris.platform.task.impl.DefaultTaskService.VersionPK;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import org.junit.Before;
import org.junit.Test;


@UnitTest
public class TaskEngineItemsBufferTest
{

	private ItemsBuffer itemsBuffer;

	@Before
	public void setUp()
	{
		itemsBuffer = new ItemsBuffer();
	}

	@Test
	public void shouldExpireAfterGivenDuration() throws InterruptedException
	{
		itemsBuffer.reset(givenListOfVersionPKs(0), Duration.ofSeconds(1));
		final Instant start = Instant.now();

		assertThat(itemsBuffer.isExpired()).isFalse();

		final Instant testTimeout = start.plusSeconds(10);
		while (!itemsBuffer.isExpired() && start.isBefore(testTimeout))
		{
			Thread.sleep(50);
		}

		assertThat(itemsBuffer.isExpired()).isTrue();
	}

	@Test
	public void shouldBeAlwaysExpiredWhenIntervalIsZero()
	{
		itemsBuffer.reset(givenListOfVersionPKs(0), Duration.ofSeconds(0));

		assertThat(itemsBuffer.isExpired()).isTrue();
	}

	@Test
	public void shouldBeAlwaysExpiredWhenIntervalIsNegative()
	{
		itemsBuffer.reset(givenListOfVersionPKs(0), Duration.ofSeconds(-1));

		assertThat(itemsBuffer.isExpired()).isTrue();
	}

	@Test
	public void shouldBeAlwaysExpiredWhenIntervalIsNull()
	{
		itemsBuffer.reset(givenListOfVersionPKs(0), null);

		assertThat(itemsBuffer.isExpired()).isTrue();
	}

	@Test
	public void shouldBeExpiredWhenDurationChangedToNull()
	{
		itemsBuffer.reset(givenListOfVersionPKs(0), Duration.ofSeconds(100));
		assertThat(itemsBuffer.isExpired()).isFalse();

		itemsBuffer.reset(givenListOfVersionPKs(0), null);
		assertThat(itemsBuffer.isExpired()).isTrue();
	}

	@Test
	public void shouldBeExpiredWhenDurationChangedToZero()
	{
		itemsBuffer.reset(givenListOfVersionPKs(0), Duration.ofSeconds(100));
		assertThat(itemsBuffer.isExpired()).isFalse();

		itemsBuffer.reset(givenListOfVersionPKs(0), Duration.ofSeconds(0));
		assertThat(itemsBuffer.isExpired()).isTrue();
	}

	@Test
	public void shouldBeExpiredWhenDurationChangedToNegativeValue()
	{
		itemsBuffer.reset(givenListOfVersionPKs(0), Duration.ofSeconds(100));
		assertThat(itemsBuffer.isExpired()).isFalse();

		itemsBuffer.reset(givenListOfVersionPKs(0), Duration.ofSeconds(-1));
		assertThat(itemsBuffer.isExpired()).isTrue();
	}

	@Test
	public void shouldBeNotExpiredWhenDurationChangedFromZeroToPositiveValue()
	{
		itemsBuffer.reset(givenListOfVersionPKs(0), Duration.ofSeconds(0));
		assertThat(itemsBuffer.isExpired()).isTrue();

		itemsBuffer.reset(givenListOfVersionPKs(0), Duration.ofSeconds(100));
		assertThat(itemsBuffer.isExpired()).isFalse();
	}

	@Test
	public void shouldProvideRequestedNumberOfTasksWhenMoreIsAvailable()
	{
		itemsBuffer.reset(givenListOfVersionPKs(500), Duration.ofSeconds(1));

		final List<VersionPK> nextPart = itemsBuffer.getNextItems(10);

		assertThat(nextPart).isNotNull().hasSize(10).doesNotContainNull().containsExactly( //
				versionPK(1), versionPK(2), versionPK(3), versionPK(4), versionPK(5), //
				versionPK(6), versionPK(7), versionPK(8), versionPK(9), versionPK(10));

		assertThat(itemsBuffer.size()).isEqualTo(490);

	}

	@Test
	public void shouldProvideAvailableNumberOfTasksWhenLessThanRequestedIsAvailable()
	{
		itemsBuffer.reset(givenListOfVersionPKs(15), Duration.ofSeconds(1));

		final List<VersionPK> nextPart = itemsBuffer.getNextItems(20);

		assertThat(nextPart).isNotNull().hasSize(15).doesNotContainNull().containsExactly( //
				versionPK(1), versionPK(2), versionPK(3), versionPK(4), versionPK(5), //
				versionPK(6), versionPK(7), versionPK(8), versionPK(9), versionPK(10), //
				versionPK(11), versionPK(12), versionPK(13), versionPK(14), versionPK(15));
		assertThat(itemsBuffer.size()).isZero();
	}

	@Test
	public void shouldntProvideTasksWhenBufferIsEmpty()
	{
		itemsBuffer.reset(givenListOfVersionPKs(0), Duration.ofSeconds(1));

		final List<VersionPK> nextPart = itemsBuffer.getNextItems(20);

		assertThat(nextPart).isNotNull().isEmpty();
	}

	@Test
	public void shouldntProvideTasksWhenSetToNull()
	{
		itemsBuffer.reset(null, Duration.ofSeconds(1));

		final List<DefaultTaskService.VersionPK> nextPart = itemsBuffer.getNextItems(20);

		assertThat(nextPart).isNotNull().isEmpty();
	}

	@Test
	public void shouldntBeReadedFullyByConsecutiveReads()
	{
		itemsBuffer.reset(givenListOfVersionPKs(8), Duration.ofSeconds(1));

		List<DefaultTaskService.VersionPK> nextPart = itemsBuffer.getNextItems(3);
		assertThat(nextPart).isNotNull().hasSize(3).containsExactly(versionPK(1), versionPK(2), versionPK(3));
		assertThat(itemsBuffer.size()).isEqualTo(5);

		nextPart = itemsBuffer.getNextItems(2);
		assertThat(nextPart).isNotNull().hasSize(2).containsExactly(versionPK(4), versionPK(5));
		assertThat(itemsBuffer.size()).isEqualTo(3);

		nextPart = itemsBuffer.getNextItems(4);
		assertThat(nextPart).isNotNull().hasSize(3).containsExactly(versionPK(6), versionPK(7), versionPK(8));
		assertThat(itemsBuffer.size()).isZero();

		nextPart = itemsBuffer.getNextItems(2);
		assertThat(nextPart).isNotNull().isEmpty();
		assertThat(itemsBuffer.size()).isZero();
	}

	@Test
	public void shouldntBeReadedFullyAndConsistentlyByMultipleThreads() throws InterruptedException
	{
		itemsBuffer.reset(givenListOfVersionPKs(1234567), Duration.ofSeconds(200));
		final CountDownLatch startSignal = new CountDownLatch(1);
		final Set<Long> processedPKs = new ConcurrentSkipListSet<>();
		final List<Thread> threads = IntStream.range(0, 10).mapToObj(id -> new Thread("TestThread-" + id)
		{
			@Override
			public void run()
			{
				try
				{
					startSignal.await();
				}
				catch (final InterruptedException e)
				{
					Thread.interrupted();
					return;
				}
				while (itemsBuffer.size() > 0)
				{
					itemsBuffer.getNextItems((int) (Math.random() * 10)).forEach(v -> processedPKs.add(v.pk.getLong()));
				}
			}
		}).collect(Collectors.toList());

		threads.forEach(Thread::start);
		startSignal.countDown();

		for (final Thread t : threads)
		{
			t.join(10_000);
		}

		threads.forEach(t -> assertThat(t.isAlive()).isFalse());
		assertThat(itemsBuffer.size()).isZero();
		assertThat(processedPKs).hasSize(1234567).doesNotContainNull();
	}

	private List<VersionPK> givenListOfVersionPKs(final int size)
	{
		return LongStream.range(1, size + 1).mapToObj(this::versionPK).collect(Collectors.toList());
	}

	private VersionPK versionPK(final long pk)
	{
		return new VersionPK(PK.fromLong(pk), 1);
	}

}
