package de.hybris.platform.servicelayer.cronjob.impl;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.catalog.model.synchronization.CatalogVersionSyncJobModel;
import de.hybris.platform.core.model.product.ProductModel;
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
import de.hybris.platform.impex.model.cronjob.ImpExExportCronJobModel;
import de.hybris.platform.impex.model.cronjob.ImpExExportJobModel;
import de.hybris.platform.impex.model.cronjob.ImpExImportCronJobModel;
import de.hybris.platform.impex.model.cronjob.ImpExImportJobModel;
import de.hybris.platform.servicelayer.cronjob.CronJobHistoryDao;
import de.hybris.platform.servicelayer.cronjob.CronJobHistoryInclude;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;



@IntegrationTest
public class DefaultCronJobHistoryDaoTest extends AbstractCronJobHistoryTest
{
	@Resource
	private ModelService modelService;
	@Resource
	private UserService userService;
	@Resource
	private CronJobHistoryDao cronJobHistoryDao;

	private final List<CronJobHistoryModel> cronJobHistoryModelList = new ArrayList<>();
	private JobModel jobOne;
	private JobModel jobTwo;
	private UserModel adminUser;
	private UserModel anonymousUser;
	private Date baseStartDate;
	private Date baseFinishDate;

	@Before
	public void setUp()
	{

		adminUser = userService.getAdminUser();
		anonymousUser = userService.getAnonymousUser();
		baseStartDate = createDate(2016, 2, 11, 1, 0, 0);
		baseFinishDate = createDate(2016, 2, 11, 12, 3, 30);
		jobOne = modelService.create(CompositeJobModel.class);
		jobOne.setCode(String.format("%s_%s", "jobOne", RandomStringUtils.randomAlphanumeric(3)));
		jobTwo = modelService.create(CompositeJobModel.class);
		jobTwo.setCode(String.format("%s_%s", "jobTwo", RandomStringUtils.randomAlphanumeric(3)));

		cronJobHistoryModelList
				.add(createEntity(jobOne, adminUser, baseStartDate, baseFinishDate, CronJobResult.SUCCESS, CronJobStatus.FINISHED));
		cronJobHistoryModelList.add(createEntity(jobOne, anonymousUser, DateUtils.setMinutes(baseStartDate, 10),
				DateUtils.setMinutes(baseFinishDate, 14), CronJobResult.SUCCESS, CronJobStatus.FINISHED));
		cronJobHistoryModelList.add(createEntity(jobTwo, anonymousUser, DateUtils.setMinutes(baseStartDate, 3),
				DateUtils.setMinutes(baseFinishDate, 15), CronJobResult.SUCCESS, CronJobStatus.FINISHED));
		cronJobHistoryModelList.add(createEntity(jobTwo, adminUser, DateUtils.setSeconds(baseStartDate, 1),
				DateUtils.setSeconds(baseFinishDate, 59), CronJobResult.ERROR, CronJobStatus.FINISHED));
		cronJobHistoryModelList.add(createEntity(jobTwo, adminUser, DateUtils.setHours(baseStartDate, 13),
				DateUtils.setHours(baseFinishDate, 14), CronJobResult.ERROR, CronJobStatus.FINISHED));

	}

	@Test
	public void testFindCronJobHistoryByCronJobCode()
	{
		// given
		final CronJobHistoryModel expected = cronJobHistoryModelList.iterator().next();
		// when
		final CronJobHistoryModel result = cronJobHistoryDao.findCronJobHistoryBy(expected.getCronJobCode()).iterator().next();
		// then
		assertThat(result).isEqualTo(expected);
	}

	@Test
	public void testFindAllCronJobHistoryByJob()
	{
		// given
		final CronJobHistoryModel expected1 = cronJobHistoryModelList.get(0);
		final CronJobHistoryModel expected2 = cronJobHistoryModelList.get(1);

		// when
		final List<CronJobHistoryModel> result = cronJobHistoryDao.findCronJobHistoryBy(null, jobOne.getCode());

		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(2);
		assertThat(result).contains(expected1, expected2);
	}


	@Test
	public void testFindAllCronJobHistoryByUserAndJob()
	{
		// given
		final CronJobHistoryModel expected1 = cronJobHistoryModelList.get(0);

		// when
		final List<CronJobHistoryModel> result = cronJobHistoryDao.findCronJobHistoryBy(adminUser.getUid(), jobOne.getCode());

		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(1);
		assertThat(result).contains(expected1);
	}

	@Test
	public void testFindAllCronJobHistoryByUserAndCronJobStatus()
	{
		// given
		final CronJobStatus expectedStatus = CronJobStatus.FINISHED;

		// when
		List<CronJobHistoryModel> result = cronJobHistoryDao.findCronJobHistoryBy(null, null, null, null, expectedStatus);

		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(5);
		assertThat(result).containsAll(cronJobHistoryModelList);


		// when
		result = cronJobHistoryDao.findCronJobHistoryBy(anonymousUser.getUid(), null, null, null, expectedStatus);

		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(2);
		assertThat(result).contains(cronJobHistoryModelList.get(1), cronJobHistoryModelList.get(2));
	}

	@Test
	public void testFindAllCronJobHistoryByUserAndStartDateAndCronJobStatus()
	{
		// given
		final CronJobStatus expectedStatus = CronJobStatus.FINISHED;
		final Date startDate = DateUtils.setMinutes(baseStartDate, 10);


		// when
		List<CronJobHistoryModel> result = cronJobHistoryDao.findCronJobHistoryBy(null, null, startDate, null, expectedStatus);

		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(2);
		assertThat(result).contains(cronJobHistoryModelList.get(1), cronJobHistoryModelList.get(4));

		// when
		result = cronJobHistoryDao.findCronJobHistoryBy(anonymousUser.getUid(), null, startDate, null, expectedStatus);

		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(1);
		assertThat(result).contains(cronJobHistoryModelList.get(1));
	}



	@Test
	public void testFindAllCronJobHistoryByCronJobResult()
	{
		// given
		final CronJobResult cronJobResult = CronJobResult.SUCCESS;

		// when
		List<CronJobHistoryModel> result = cronJobHistoryDao.findCronJobHistoryBy(null, null, null, null, cronJobResult);

		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(3);
		assertThat(result).contains(cronJobHistoryModelList.get(0), cronJobHistoryModelList.get(1), cronJobHistoryModelList.get(2));

		result = cronJobHistoryDao.findCronJobHistoryBy(adminUser.getUid(), null, null, null, cronJobResult);

		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(1);
		assertThat(result).contains(cronJobHistoryModelList.get(0));
	}


	@Test
	public void testFindAllCronJobHistoryByUser()
	{
		// given
		final UserModel givenUser = anonymousUser;

		// when
		final List<CronJobHistoryModel> result = cronJobHistoryDao.findCronJobHistoryBy(givenUser.getUid(), null, null, null);

		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(2);
		assertThat(result).contains(cronJobHistoryModelList.get(1), cronJobHistoryModelList.get(2));
	}


	@Test
	public void testFindAllCronJobHistoryByUserAndJobItemType()
	{
		// given
		final UserModel givenUser = anonymousUser;

		// when
		final List<CronJobHistoryModel> result = cronJobHistoryDao.findCronJobHistoryBy(givenUser.getUid(), jobTwo.getItemtype(),
				null, null);

		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(2);
		assertThat(result).contains(cronJobHistoryModelList.get(1), cronJobHistoryModelList.get(2));
	}

	@Test
	public void testFindAllCronJobHistoryByUserAndJobItemTypeAndStartDate()
	{
		// given
		final Date startDate = DateUtils.setMinutes(baseStartDate, 15);

		// when
		final List<CronJobHistoryModel> result = cronJobHistoryDao.findCronJobHistoryBy(adminUser.getUid(), jobTwo.getItemtype(),
				startDate, null);

		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(1);
		assertThat(result).contains(cronJobHistoryModelList.get(4));

	}


	@Test
	public void testFindAllCronJobHistoryByJobItemType()
	{
		// when
		List<CronJobHistoryModel> result = cronJobHistoryDao.findCronJobHistoryBy(null, jobTwo.getItemtype(), null, null);

		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(5);
		assertThat(result).containsAll(getCronJobHistoryModelList());


		// when
		result = cronJobHistoryDao.findCronJobHistoryBy(null, BatchJobModel._TYPECODE, null, null);

		// then
		assertThat(result).isNotNull();
		assertThat(result).isEmpty();

		// given
		final CatalogModel catalog = modelService.create(CatalogModel.class);
		catalog.setId(RandomStringUtils.randomAlphanumeric(5));

		final CatalogVersionModel source = modelService.create(CatalogVersionModel.class);
		source.setVersion("staged");
		source.setCatalog(catalog);
		final CatalogVersionModel target = modelService.create(CatalogVersionModel.class);
		target.setVersion("online");
		target.setCatalog(catalog);

		final CatalogVersionSyncJobModel catalogSyncItemJob = createSyncItemJobModel(CatalogVersionSyncJobModel.class, source,
				target);

		final SyncItemJobModel syncItemJob = createSyncItemJobModel(SyncItemJobModel.class, source, target);

		modelService.saveAll(catalogSyncItemJob);

		final CronJobHistoryModel cronJobHistoryModel1 = createEntity(catalogSyncItemJob, adminUser, baseStartDate, baseFinishDate,
				null, null);
		cronJobHistoryModelList.add(cronJobHistoryModel1);
		final CronJobHistoryModel cronJobHistoryModel2 = createEntity(syncItemJob, adminUser, baseStartDate, baseFinishDate, null,
				null);
		cronJobHistoryModelList.add(cronJobHistoryModel2);

		// when
		result = cronJobHistoryDao.findCronJobHistoryBy(null, SyncItemJobModel._TYPECODE, null, null);

		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(2);
		assertThat(result).contains(cronJobHistoryModel1, cronJobHistoryModel2);


		// when
		result = cronJobHistoryDao.findCronJobHistoryBy(null, CatalogVersionSyncJobModel._TYPECODE, null, null);

		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(1);
		assertThat(result).contains(cronJobHistoryModel1);


		// when
		result = cronJobHistoryDao.findCronJobHistoryBy(null, String.format("%s%s", SyncItemJobModel._TYPECODE, "!"), null, null);

		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(1);
		assertThat(result).contains(cronJobHistoryModel2);

		// when
		result = cronJobHistoryDao.findCronJobHistoryBy(null, JobModel._TYPECODE, null, null);

		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(7);
		assertThat(result).containsAll(cronJobHistoryModelList);


	}

	private <T extends SyncItemJobModel> T createSyncItemJobModel(final Class<T> jobType, final CatalogVersionModel source,
			final CatalogVersionModel target)
	{
		final T syncItemJob = modelService.create(jobType);
		syncItemJob.setCode(String.format("%s_%s", "sync_job", RandomStringUtils.randomAlphanumeric(3)));
		syncItemJob.setSourceVersion(source);
		syncItemJob.setTargetVersion(target);
		return syncItemJob;
	}

	@Test
	public void testFindAllCronJobHistoryByJobCode()
	{
		final CronJobHistoryModel cjh1 = createCronJobHistoryForJobCode("abc");
		final CronJobHistoryModel cjh2 = createCronJobHistoryForJobCode("def");
		final CronJobHistoryModel cjh3 = createCronJobHistoryForJobCode("ghi");


		final Set<CronJobHistoryInclude> includes = new HashSet<>();
		includes.add(new CronJobHistoryInclude(Sets.newHashSet(cjh1.getJobCode()), null, null));
		includes.add(new CronJobHistoryInclude(Sets.newHashSet(cjh2.getJobCode(), cjh3.getJobCode()), null, null));

		final List<CronJobHistoryModel> result = cronJobHistoryDao.findCronJobHistoryBy(includes, null, null, null, null, null);

		assertThat(result).hasSize(3);
		assertThat(result).contains(cjh1, cjh2, cjh3);
	}

	private CronJobHistoryModel createCronJobHistoryForJobCode(final String code)
	{
		final CompositeJobModel job = modelService.create(CompositeJobModel.class);
		job.setCode(code);

		return createEntity(job, adminUser, baseStartDate, baseFinishDate, CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	@Test
	public void testFindAllCronJobHistoryByCronJobTypeCode()
	{
		final CronJobHistoryModel cjh1 = createCronJobHistoryForCronJobTypeCode(ImpExImportCronJobModel.class,
				ImpExImportJobModel.class);
		final CronJobHistoryModel cjh2 = createCronJobHistoryForCronJobTypeCode(ImpExExportCronJobModel.class,
				ImpExExportJobModel.class);
		//not included
		createCronJobHistoryForCronJobTypeCode(CronJobModel.class, CompositeJobModel.class);


		final Set<CronJobHistoryInclude> includes = new HashSet<>();
		includes.add(new CronJobHistoryInclude(null, null, ImpExImportCronJobModel._TYPECODE));
		includes.add(new CronJobHistoryInclude(null, null, ImpExExportCronJobModel._TYPECODE));

		final List<CronJobHistoryModel> result = cronJobHistoryDao.findCronJobHistoryBy(includes, null, null, null, null, null);

		assertThat(result).hasSize(2);
		assertThat(result).contains(cjh1, cjh2);
	}

	private CronJobHistoryModel createCronJobHistoryForCronJobTypeCode(final Class<? extends CronJobModel> cronJobType,
			final Class<? extends JobModel> jobType)
	{

		final JobModel job = modelService.create(jobType);
		job.setCode(String.format("%s_%s", "job", RandomStringUtils.randomAlphanumeric(3)));

		final CronJobModel cronJobModel = modelService.create(cronJobType);
		cronJobModel.setCode(String.format("%s_%s", "cronJobOne", RandomStringUtils.randomAlphanumeric(3)));
		cronJobModel.setJob(job);

		return createEntity(cronJobModel, job, adminUser, baseStartDate, baseFinishDate, CronJobResult.SUCCESS,
				CronJobStatus.FINISHED);
	}

	@Test
	public void testFindAllCronJobHistoryByMultipleIncludesForDifferentCriteria()
	{
		//given
		final CronJobHistoryModel cjhCronJobType = createCronJobHistoryForCronJobTypeCode(ImpExImportCronJobModel.class,
				ImpExImportJobModel.class);
		final CronJobHistoryModel cjhJobType = createCronJobHistoryForCronJobTypeCode(MoveMediaCronJobModel.class,
				MoveMediaJobModel.class);
		final CronJobHistoryModel cjhJobCode = createCronJobHistoryForJobCode("ghi");

		//when
		final Set<CronJobHistoryInclude> includes = new HashSet<>();
		includes.add(new CronJobHistoryInclude(null, null, ImpExImportCronJobModel._TYPECODE));
		includes.add(new CronJobHistoryInclude(null, MoveMediaJobModel._TYPECODE, null));
		includes.add(new CronJobHistoryInclude(Sets.newHashSet(cjhJobCode.getJobCode()), null, null));

		final List<CronJobHistoryModel> result = cronJobHistoryDao.findCronJobHistoryBy(includes, null, null, null, null, null);

		//then
		assertThat(result).hasSize(3);
		assertThat(result).contains(cjhCronJobType, cjhJobType, cjhJobCode);

		//when
		final CronJobHistoryInclude mergedInclude = new CronJobHistoryInclude();
		includes.forEach(inc -> {
			if (inc.getJobTypeCode() != null)
			{
				mergedInclude.setJobTypeCode(inc.getJobTypeCode());
			}
			else if (inc.getCronJobTypeCode() != null)
			{
				mergedInclude.setCronJobTypeCode(inc.getCronJobTypeCode());
			}
			else if (inc.getJobCodes() != null)
			{
				mergedInclude.setJobCodes(inc.getJobCodes());
			}
		});
		final List<CronJobHistoryModel> mergedIncludeResult = cronJobHistoryDao.findCronJobHistoryBy(Sets.newHashSet(mergedInclude),
				null, null, null, null, null);
		//then
		assertThat(mergedIncludeResult).isEmpty();
	}

	@Test
	public void testFindAllCronJobHistoryByMOneIncludeForDifferentCriteria()
	{
		//given
		final CronJobHistoryModel cjhCronJobType = createCronJobHistoryForCronJobTypeCode(ImpExImportCronJobModel.class,
				ImpExImportJobModel.class);

		//when
		final Set<CronJobHistoryInclude> includes = new HashSet<>();
		includes.add(new CronJobHistoryInclude(Sets.newHashSet(cjhCronJobType.getJobCode()), ImpExImportJobModel._TYPECODE,
				ImpExImportCronJobModel._TYPECODE));

		final List<CronJobHistoryModel> result = cronJobHistoryDao.findCronJobHistoryBy(includes, null, null, null, null, null);

		//then
		assertThat(result).hasSize(1);
		assertThat(result).contains(cjhCronJobType);
	}

	@Test
	public void testFindAllCronJobForEmptyInclude()
	{

		//when
		final Set<CronJobHistoryInclude> includes = new HashSet<>();
		includes.add(new CronJobHistoryInclude());

		final List<CronJobHistoryModel> result = cronJobHistoryDao.findCronJobHistoryBy(includes, null, null, null, null, null);

		//then
		assertThat(result).hasSize(cronJobHistoryModelList.size());
		assertThat(result).containsAll(cronJobHistoryModelList);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testFindAllCronJobHistoryByWrongJobItemType()
	{

		// when
		cronJobHistoryDao.findCronJobHistoryBy(null, ProductModel._TYPECODE, null, null);
	}
}
