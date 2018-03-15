package de.hybris.platform.servicelayer.cronjob.impl;


import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.BatchJobModel;
import de.hybris.platform.cronjob.model.CompositeJobModel;
import de.hybris.platform.cronjob.model.CronJobHistoryModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.cronjob.model.MoveMediaCronJobModel;
import de.hybris.platform.cronjob.model.MoveMediaJobModel;
import de.hybris.platform.servicelayer.cronjob.CronJobHistoryInclude;
import de.hybris.platform.servicelayer.cronjob.CronJobHistoryService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;


public class DefaultCronJobHistoryServiceTest extends AbstractCronJobHistoryTest
{
	@Resource
	private ModelService modelService;
	@Resource
	private UserService userService;

	@Resource
	private CronJobHistoryService cronJobHistoryService;

	private final Date baseStartDate = createDate(2016, 2, 1, 10, 0, 0);
	private final Date baseFinishDate = createDate(2016, 2, 1, 14, 0, 0);
	private JobModel jobOne;
	private JobModel jobTwo;
	private UserModel adminUser;
	private UserModel anonymousUser;


	@Before
	public void setUp()
	{
		adminUser = userService.getAdminUser();
		anonymousUser = userService.getAnonymousUser();
		jobOne = modelService.create(CompositeJobModel.class);
		jobOne.setCode(String.format("%s_%s", "jobOne", RandomStringUtils.randomAlphanumeric(3)));
		jobTwo = modelService.create(CompositeJobModel.class);
		jobTwo.setCode(String.format("%s_%s", "jobTwo", RandomStringUtils.randomAlphanumeric(3)));

		addCronJobHistoryModel(
				createEntity(jobOne, anonymousUser, baseStartDate, baseFinishDate, CronJobResult.SUCCESS, CronJobStatus.FINISHED));
		addCronJobHistoryModel(createEntity(jobTwo, adminUser, DateUtils.setHours(baseStartDate, 11),
				DateUtils.setHours(baseFinishDate, 12), CronJobResult.SUCCESS, CronJobStatus.FINISHED));
		addCronJobHistoryModel(createEntity(jobOne, adminUser, DateUtils.setHours(baseStartDate, 11),
				DateUtils.setHours(baseFinishDate, 13), CronJobResult.UNKNOWN, CronJobStatus.ABORTED));
		addCronJobHistoryModel(createEntity(jobTwo, anonymousUser, DateUtils.setHours(baseStartDate, 12),
				DateUtils.setHours(baseFinishDate, 13), CronJobResult.SUCCESS, CronJobStatus.FINISHED));

	}

	@Test
	public void testGetCronJobHistoryByRange()
	{

		// given
		final Date finishDate = DateUtils.setHours(baseFinishDate, 18);

		// when
		final List<CronJobHistoryModel> results = cronJobHistoryService.getCronJobHistoryBy(null, null, baseStartDate, finishDate);

		// then
		Assertions.assertThat(results).isNotNull();
		Assertions.assertThat(results).hasSize(getCronJobHistoryModelList().size());
		Assertions.assertThat(results).containsAll(getCronJobHistoryModelList());

	}


	@Test
	public void testGetCronJobHistoryByRangeAndStatus()
	{

		// given
		final Date finishDate = DateUtils.setHours(baseFinishDate, 18);
		final CronJobStatus theStatus = CronJobStatus.FINISHED;

		// when
		final List<CronJobHistoryModel> results = cronJobHistoryService.getCronJobHistoryBy(null, (String) null, baseStartDate,
				finishDate, theStatus);

		// then
		Assertions.assertThat(results).isNotNull();
		Assertions.assertThat(results).hasSize(3);
		Assertions.assertThat(results).contains(getCronJobHistoryModelList().get(0), getCronJobHistoryModelList().get(1),
				getCronJobHistoryModelList().get(3));

	}


	@Test
	public void testGetCronJobHistoryByCronJobCode()
	{
		// given
		final CronJobHistoryModel expected = getCronJobHistoryModelList().get(0);

		// when
		final CronJobHistoryModel result = cronJobHistoryService.getCronJobHistoryBy(expected.getCronJobCode()).get(0);

		// then
		Assertions.assertThat(result).isNotNull();
	}

	@Test
	public void testGetCronJobHistoryByCronJobCodes()
	{
		// given
		final CronJobHistoryModel expected1 = getCronJobHistoryModelList().get(0);
		final CronJobHistoryModel expected2 = getCronJobHistoryModelList().get(1);


		// when
		final List<CronJobHistoryModel> list = cronJobHistoryService.getCronJobHistoryBy(
				Collections.unmodifiableList(Arrays.asList(expected1.getCronJobCode(), expected2.getCronJobCode())));

		// then
		Assertions.assertThat(list.size()).isEqualTo(2);
		final CronJobHistoryModel result1 = list.get(0);
		Assertions.assertThat(result1).isNotNull();
		final CronJobHistoryModel result2 = list.get(1);
		Assertions.assertThat(result2).isNotNull();
	}


	@Test
	public void testGetCronJobHistoryByUserAndJob()
	{

		// when
		List<CronJobHistoryModel> results = cronJobHistoryService.getCronJobHistoryBy(null, jobTwo);

		// then
		Assertions.assertThat(results).isNotNull();
		Assertions.assertThat(results).hasSize(2);
		Assertions.assertThat(results).contains(getCronJobHistoryModelList().get(1), getCronJobHistoryModelList().get(3));

		// when
		results = cronJobHistoryService.getCronJobHistoryBy(anonymousUser, jobTwo);

		// then
		Assertions.assertThat(results).isNotNull();
		Assertions.assertThat(results).hasSize(1);
		Assertions.assertThat(results).contains(getCronJobHistoryModelList().get(3));
	}

	@Test
	public void testGetCronJobHistoryByJobItemType()
	{
		// given
		final Date startDate = DateUtils.setHours(baseStartDate, 8);
		final Date finishDate = DateUtils.setHours(baseFinishDate, 18);


		// when
		List<CronJobHistoryModel> results = cronJobHistoryService.getCronJobHistoryBy(null, jobOne.getItemtype(), startDate,
				finishDate);

		// then
		Assertions.assertThat(results).isNotNull();
		Assertions.assertThat(results).hasSize(4);
		Assertions.assertThat(results).containsAll(getCronJobHistoryModelList());


		// given
		final JobModel jobModel = modelService.create(BatchJobModel.class);
		jobModel.setCode(String.format("%s_%s", "jobThree", RandomStringUtils.randomAlphanumeric(3)));
		final CronJobHistoryModel cronJobHistoryModel = createEntity(jobModel, anonymousUser, baseStartDate, baseFinishDate,
				CronJobResult.SUCCESS, CronJobStatus.FINISHED);


		// when
		results = cronJobHistoryService.getCronJobHistoryBy(null, jobModel.getItemtype(), startDate, finishDate);

		// then
		Assertions.assertThat(results).isNotNull();
		Assertions.assertThat(results).hasSize(1);
		Assertions.assertThat(results).contains(cronJobHistoryModel);
	}

	@Test
	public void testGetCronJobHistoryByJobCode()
	{
		// given
		final Date startDate = DateUtils.setHours(baseStartDate, 8);
		final Date finishDate = DateUtils.setHours(baseFinishDate, 18);


		final Set<CronJobHistoryInclude> includes = new HashSet<>();
		includes.add(new CronJobHistoryInclude(Sets.newHashSet(jobOne.getCode()), null, null));

		// when
		final List<CronJobHistoryModel> results = cronJobHistoryService.getCronJobHistoryBy(includes, null, startDate, finishDate,
				null);

		// then
		final List<CronJobHistoryModel> historyForJobOne = getCronJobHistoryModelList().stream()
				.filter(h -> StringUtils.equals(h.getJobCode(), jobOne.getCode())).collect(Collectors.toList());
		Assertions.assertThat(results).isNotEmpty();
		Assertions.assertThat(results).hasSize(historyForJobOne.size());
		Assertions.assertThat(results).containsAll(historyForJobOne);
	}

	@Test
	public void testGetCronJobHistoryByCronJobItemType()
	{
		// given
		final Date startDate = DateUtils.setHours(baseStartDate, 8);
		final Date finishDate = DateUtils.setHours(baseFinishDate, 18);

		final JobModel job = modelService.create(MoveMediaJobModel.class);
		job.setCode("abc");
		final CronJobModel cronJob = modelService.create(MoveMediaCronJobModel.class);
		cronJob.setCode("abc");
		cronJob.setJob(job);
		final CronJobHistoryModel cjh = createEntity(cronJob, job, anonymousUser, baseStartDate, baseFinishDate,
				CronJobResult.SUCCESS, CronJobStatus.FINISHED);

		final Set<CronJobHistoryInclude> includes = new HashSet<>();
		includes.add(new CronJobHistoryInclude(null, null, MoveMediaCronJobModel._TYPECODE));

		// when
		final List<CronJobHistoryModel> results = cronJobHistoryService.getCronJobHistoryBy(includes, null, startDate, finishDate,
				CronJobStatus.FINISHED);

		// then
		Assertions.assertThat(results).isNotNull();
		Assertions.assertThat(results).hasSize(1);
		Assertions.assertThat(results).containsOnly(cjh);
	}

	@Test(expected = UnknownIdentifierException.class)
	public void testGetCronJobHistoryByWrongJobItemtype()
	{
		// given
		final Date startDate = DateUtils.setHours(baseStartDate, 8);
		final Date finishDate = DateUtils.setHours(baseFinishDate, 18);


		// when
		cronJobHistoryService.getCronJobHistoryBy(null, "WrongItemType", startDate, finishDate);

	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetCronJobHistoryByNotSupportedItemtype()
	{
		// given
		final Date startDate = DateUtils.setHours(baseStartDate, 8);
		final Date finishDate = DateUtils.setHours(baseFinishDate, 18);


		// when
		cronJobHistoryService.getCronJobHistoryBy(null, "Product", startDate, finishDate);
	}


	@Test
	public void testGetCronJobHistoryByCronJobResult()
	{
		// given
		final CronJobResult theResult = CronJobResult.SUCCESS;
		final Date startDate = DateUtils.setHours(baseStartDate, 11);
		final Date finishDate = DateUtils.setHours(baseFinishDate, 18);

		// when
		final List<CronJobHistoryModel> results = cronJobHistoryService.getCronJobHistoryBy(null, null, startDate, finishDate,
				theResult);

		// then
		Assertions.assertThat(results).isNotNull();
		Assertions.assertThat(results).hasSize(2);
		Assertions.assertThat(results).contains(getCronJobHistoryModelList().get(1), getCronJobHistoryModelList().get(3));
	}


	@Test
	public void testGetCronJobHistoryByCronJobStatus()
	{
		// given
		final Date startDate = DateUtils.setHours(baseStartDate, 10);
		final Date finishDate = DateUtils.setHours(baseStartDate, 16);
		final CronJobStatus theStatus = CronJobStatus.ABORTED;

		// when
		final List<CronJobHistoryModel> results = cronJobHistoryService.getCronJobHistoryBy(null, (String) null, startDate,
				finishDate, theStatus);

		// then
		Assertions.assertThat(results).isNotNull();
		Assertions.assertThat(results).hasSize(1);
		Assertions.assertThat(results).contains(getCronJobHistoryModelList().get(2));
	}


	@Test
	public void testGetCronJobHistoryByUserAndStartAndFinishDate()
	{
		// given
		final Date startDate = DateUtils.setHours(baseStartDate, 11);
		final Date finishDate = DateUtils.setHours(baseFinishDate, 16);

		// when
		final List<CronJobHistoryModel> results = cronJobHistoryService.getCronJobHistoryBy(adminUser, null, startDate, finishDate);

		// then
		Assertions.assertThat(results).isNotNull();
		Assertions.assertThat(results).hasSize(2);
		Assertions.assertThat(results).contains(getCronJobHistoryModelList().get(1), getCronJobHistoryModelList().get(2));

	}

}
