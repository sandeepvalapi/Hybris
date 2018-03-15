/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.servicelayer.impex;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.threadregistry.RegistrableThread;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.impex.ImportConfig.ValidationMode;
import de.hybris.platform.util.ThreadUtilities;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;


@IntegrationTest
public class ImportServiceLoadTest extends ServicelayerBaseTest
{
	@Test
	public void multipleImportsShouldImportNotEmptyScript() throws InterruptedException
	{
		testMultipleImports("INSERT_UPDATE Title;code[unique=true]\n;T__UNQ__");
	}

	@Test
	public void multipleImportsShouldImportEmptyScript() throws InterruptedException
	{
		testMultipleImports("###################");
	}

	@Test
	public void testMultipleImportsWithParallelHeaderChangedMultipleTimesInTheMiddleOfTheScript() throws InterruptedException
	{
		testMultipleImports("INSERT Title;code[unique=true]\n"//
				+ ";f1___UNQ__;\n" //
				+ "INSERT Title[parallel=false];code[unique=true]\n" //
				+ ";f2___UNQ__;\n" //
				+ "INSERT Title[parallel=false];code[unique=true]\n" //
				+ ";f3___UNQ__;\n" //
				+ "INSERT Title[parallel=true];code[unique=true]\n" //
				+ ";f4___UNQ__;\n" //
				+ "INSERT Title[parallel=true];code[unique=true]\n" //
				+ ";f5___UNQ__;\n" //
				+ "INSERT Title[parallel=false];code[unique=true]\n" //
				+ ";f6___UNQ__;\n" //
				+ "INSERT Title[parallel=true];code[unique=true]\n" //
				+ ";f7___UNQ__;\n" //
				+ "INSERT Title[parallel=false];code[unique=true]\n" //
				+ ";f8___UNQ__;\n" //
				+ "INSERT Title[parallel=true];code[unique=true]\n" //
				+ ";f9___UNQ__;\n" //
				+ "INSERT Title[parallel=false];code[unique=true]\n" //
				+ ";f10___UNQ__;\n" //
				+ "INSERT Title[parallel=true];code[unique=true]\n" //
				+ ";f11___UNQ__;\n" //
				+ "INSERT Title[parallel=false];code[unique=true]\n" //
				+ ";f12___UNQ__;\n" //
				+ "INSERT Title[parallel=true];code[unique=true]\n" //
				+ ";f13___UNQ__;\n" //
				+ "INSERT Title[parallel=false];code[unique=true]\n" //
				+ ";f14___UNQ__;\n" //
				+ "INSERT Title[parallel=true];code[unique=true]\n" //
				+ ";f15___UNQ__;\n" //
				+ "INSERT Title[parallel=false];code[unique=true]\n" //
				+ ";f16___UNQ__;\n" //
				+ "INSERT Title[parallel=true];code[unique=true]\n" //
				+ ";f17___UNQ__;\n" //
				+ "INSERT Title[parallel=false];code[unique=true]\n" //
				+ ";f18___UNQ__;\n" //
				+ "INSERT Title[parallel=true];code[unique=true]\n" //
				+ ";f19___UNQ__;\n" //
				+ ";f20___UNQ__;\n");
	}

	@Test
	public void testMultipleImportsWithParallelHeaderChangedToFalseInTheMiddleOfTheScript() throws InterruptedException
	{
		testMultipleImports("INSERT Title;code[unique=true]\n"//
				+ ";f1___UNQ__;\n" //
				+ ";f2___UNQ__;\n" //
				+ ";f3___UNQ__;\n" //
				+ ";f4___UNQ__;\n" //
				+ ";f5___UNQ__;\n" //
				+ ";f6___UNQ__;\n" //
				+ ";f7___UNQ__;\n" //
				+ ";f8___UNQ__;\n" //
				+ ";f9___UNQ__;\n" //
				+ ";f10___UNQ__;\n" //
				+ "INSERT Title[parallel=false];code[unique=true]\n" //
				+ ";f11___UNQ__;\n" //
				+ ";f12___UNQ__;\n" //
				+ ";f13___UNQ__;\n" //
				+ ";f14___UNQ__;\n" //
				+ ";f15___UNQ__;\n" //
				+ ";f16___UNQ__;\n" //
				+ ";f17___UNQ__;\n" //
				+ ";f18___UNQ__;\n" //
				+ ";f19___UNQ__;\n" //
				+ ";f20___UNQ__;\n");
	}

	@Test
	public void testMultipleImportsWithParallelHeaderChangedToFalseInTheBeginningOfTheScript() throws InterruptedException
	{
		testMultipleImports("INSERT Title[parallel=false];code[unique=true]\n"//
				+ ";f1___UNQ__;\n" //
				+ ";f2___UNQ__;\n" //
				+ ";f3___UNQ__;\n" //
				+ ";f4___UNQ__;\n" //
				+ ";f5___UNQ__;\n" //
				+ ";f6___UNQ__;\n" //
				+ ";f7___UNQ__;\n" //
				+ ";f8___UNQ__;\n" //
				+ ";f9___UNQ__;\n" //
				+ ";f10___UNQ__;\n" //
				+ ";f11___UNQ__;\n" //
				+ ";f12___UNQ__;\n" //
				+ ";f13___UNQ__;\n" //
				+ ";f14___UNQ__;\n" //
				+ ";f15___UNQ__;\n" //
				+ ";f16___UNQ__;\n" //
				+ ";f17___UNQ__;\n" //
				+ ";f18___UNQ__;\n" //
				+ ";f19___UNQ__;\n" //
				+ ";f20___UNQ__;\n");
	}

	@Test
	public void testMultipleImportsWithMaxThreadChangedToOneInTheBeginningOfTheScript() throws InterruptedException
	{
		testMultipleImports("\"#% impex.setMaxThreads(1);\";\n"//
				+ "INSERT Title;code[unique=true]\n"//
				+ ";f1___UNQ__;\n" //
				+ ";f2___UNQ__;\n" //
				+ ";f3___UNQ__;\n" //
				+ ";f4___UNQ__;\n" //
				+ ";f5___UNQ__;\n" //
				+ ";f6___UNQ__;\n" //
				+ ";f7___UNQ__;\n" //
				+ ";f8___UNQ__;\n" //
				+ ";f9___UNQ__;\n" //
				+ ";f10___UNQ__;\n" //
				+ ";f11___UNQ__;\n" //
				+ ";f12___UNQ__;\n" //
				+ ";f13___UNQ__;\n" //
				+ ";f14___UNQ__;\n" //
				+ ";f15___UNQ__;\n" //
				+ ";f16___UNQ__;\n" //
				+ ";f17___UNQ__;\n" //
				+ ";f18___UNQ__;\n" //
				+ ";f19___UNQ__;\n" //
				+ ";f20___UNQ__;\n");
	}

	@Test
	public void testMultipleImportsWithMaxThreadChangedToOneInTheMiddleOfTheScript() throws InterruptedException
	{
		testMultipleImports("INSERT Title;code[unique=true]\n"//
				+ ";f1___UNQ__;\n" //
				+ ";f2___UNQ__;\n" //
				+ ";f3___UNQ__;\n" //
				+ ";f4___UNQ__;\n" //
				+ ";f5___UNQ__;\n" //
				+ ";f6___UNQ__;\n" //
				+ ";f7___UNQ__;\n" //
				+ ";f8___UNQ__;\n" //
				+ ";f9___UNQ__;\n" //
				+ ";f10___UNQ__;\n" //
				+ "\"#% impex.setMaxThreads(1);\";\n"//
				+ ";f11___UNQ__;\n" //
				+ ";f12___UNQ__;\n" //
				+ ";f13___UNQ__;\n" //
				+ ";f14___UNQ__;\n" //
				+ ";f15___UNQ__;\n" //
				+ ";f16___UNQ__;\n" //
				+ ";f17___UNQ__;\n" //
				+ ";f18___UNQ__;\n" //
				+ ";f19___UNQ__;\n" //
				+ ";f20___UNQ__;\n");
	}

	public void testMultipleImports(final String script) throws InterruptedException
	{
		final TestThread[] threads = new TestThread[64];

		final TestThread warmUpThread = new TestThread(null, Registry.getCurrentTenant().getTenantID(),
				script.replace("__UNQ__", UUID.randomUUID().toString()), 5);
		warmUpThread.internalRun();
		assertThat(warmUpThread.result).isNotNull();
		assertThat(warmUpThread.result.isFinished()).isTrue();
		assertThat(warmUpThread.result.isSuccessful()).isTrue();

		System.out.println("Number of active threads: " + Registry.getCurrentTenant().getWorkersThreadPool().getNumActive());

		final CountDownLatch startLatch = new CountDownLatch(1);

		for (int i = 0; i < threads.length; i++)
		{
			threads[i] = new TestThread(startLatch, Registry.getCurrentTenant().getTenantID(),
					script.replace("__UNQ__", UUID.randomUUID().toString()), 7);
			threads[i].start();
		}

		startLatch.countDown();

		for (int i = 0; i < threads.length; i++)
		{
			final TestThread t = threads[i];
			t.join((threads.length - i) * 1000 + 60000);
			final SoftAssertions soft = new SoftAssertions();

			soft.assertThat(t.isAlive()).isFalse();
			soft.assertThat(t.result).isNotNull();
			if (soft.wasSuccess())
			{
				soft.assertThat(t.result.isFinished()).isTrue();
				soft.assertThat(t.result.isSuccessful()).isTrue();
			}

			if (!soft.errorsCollected().isEmpty())
			{
				ThreadUtilities.printThreadDump(System.out);
			}
			soft.assertAll();
		}

		final TestThread coolDownThread = new TestThread(null, Registry.getCurrentTenant().getTenantID(),
				script.replace("__UNQ__", UUID.randomUUID().toString()), 10);
		coolDownThread.internalRun();
		assertThat(coolDownThread.result).isNotNull();
		assertThat(coolDownThread.result.isFinished()).isTrue();
		assertThat(coolDownThread.result.isSuccessful()).isTrue();

		System.out.println("Number of active threads: " + Registry.getCurrentTenant().getWorkersThreadPool().getNumActive());
	}

	private static class TestThread extends RegistrableThread
	{
		private final CountDownLatch startLatch;
		private final String tenantId;
		private final String script;
		private final int numberOfThreads;
		private volatile ImportResult result;

		public TestThread(final CountDownLatch startLatch, final String tenantId, final String script, final int numberOfThreads)
		{
			this.startLatch = startLatch;
			this.tenantId = tenantId;
			this.script = script;
			this.numberOfThreads = numberOfThreads;
		}

		@Override
		public void internalRun()
		{
			if (!waitForStartSignal())
			{
				return;
			}

			Registry.setCurrentTenantByID(tenantId);
			final ImportService importService = Registry.getApplicationContext().getBean(ImportService.class);
			final ImportConfig config = createConfig(script, numberOfThreads);
			result = importService.importData(config);
		}

		private boolean waitForStartSignal()
		{
			if (startLatch == null)
			{
				return true;
			}
			try
			{
				startLatch.await();
				return true;
			}
			catch (final InterruptedException e)
			{
				Thread.currentThread().interrupt();
				e.printStackTrace();
				return false;
			}
		}

		private ImportConfig createConfig(final String script, final int numberOfThreads)
		{
			final ImportConfig config = new ImportConfig();
			config.setMaxThreads(numberOfThreads);
			config.setSynchronous(true);
			config.setLegacyMode(Boolean.FALSE);
			config.setEnableCodeExecution(Boolean.TRUE);
			config.setDistributedImpexEnabled(false);
			config.setValidationMode(ValidationMode.STRICT);

			config.setScript(script);
			return config;
		}
	}
}
