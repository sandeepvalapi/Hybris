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
package de.hybris.platform.directpersistence;

import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.record.EntityRecord;
import de.hybris.platform.directpersistence.record.impl.DeleteRecord;
import de.hybris.platform.directpersistence.record.impl.InsertRecord;
import de.hybris.platform.directpersistence.record.impl.PropertyHolder;
import de.hybris.platform.directpersistence.record.impl.UpdateRecord;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@UnitTest
public class SimpleRecordTest
{
	private static final String TYPE = "product";

	private static final String INSERT_RECORD = "InsertRecord";
	private static final String UPDATE_RECORD = "UpdateRecord";
	private static final String DELETE_RECORD = "DeleteRecord";

	private List<EntityRecord> records;

	@Before
	public void before()
	{
		records = new ArrayList<EntityRecord>();

		final Set<PropertyHolder> changes = new LinkedHashSet<PropertyHolder>();
		changes.add(new PropertyHolder("Key1", "Value1"));
		changes.add(new PropertyHolder("Key2", "Value2"));
		changes.add(new PropertyHolder("Key3", "Value3"));

		final InsertRecord insertrecord = new InsertRecord(PK.fromLong(1l), TYPE, changes);
		final UpdateRecord updateRecord = new UpdateRecord(PK.fromLong(1l), TYPE, 0, changes);
		final DeleteRecord deleteRecord = new DeleteRecord(PK.fromLong(1l), TYPE, 0);

		records.add(insertrecord);
		records.add(updateRecord);
		records.add(deleteRecord);
	}

	@After
	public void after()
	{
		records = null;
	}

	@Test
	public void checkIfCreated()
	{
		assertTrue("There should be 3 records in collection.", 3 == records.size());
	}

	@Test
	public void checkType()
	{
		assertTrue("First element should be InsertRecord.", records.get(0) instanceof InsertRecord);
		assertTrue("Second element should be UpdateRecord,", records.get(1) instanceof UpdateRecord);
		assertTrue("Third element should be DeleteRecord", records.get(2) instanceof DeleteRecord);
	}

	@Test
	public void visitWithTypePrintln()
	{
		class TypeVisitor implements EntityRecord.EntityRecordVisitor<Void>
		{
			public boolean insert = false;
			private boolean update = false;
			private boolean delete = false;

			@Override
			public Void visit(final UpdateRecord record)
			{
				update = true;
				return null;
			}

			@Override
			public Void visit(final InsertRecord record)
			{
				insert = true;
				return null;
			}

			@Override
			public Void visit(final DeleteRecord record)
			{
				delete = true;
				return null;
			}

			public boolean isInsert()
			{
				return insert;
			}

			public boolean isUpdate()
			{
				return update;
			}

			public boolean isDelete()
			{
				return delete;
			}

		}

		final TypeVisitor visitor = new TypeVisitor();

		for (final EntityRecord record : records)
		{
			record.accept(visitor);
		}

		assertTrue("There should be each type visited at least once.",
				visitor.isInsert() && visitor.isUpdate() && visitor.isDelete());
	}

	@Test
	public void visitWithTypeReturn()
	{
		final EntityRecord.EntityRecordVisitor<String> visitor = new EntityRecord.EntityRecordVisitor<String>()
		{

			@Override
			public String visit(final UpdateRecord record)
			{
				return UPDATE_RECORD;
			}

			@Override
			public String visit(final InsertRecord record)
			{
				return INSERT_RECORD;
			}

			@Override
			public String visit(final DeleteRecord record)
			{
				return DELETE_RECORD;
			}

		};

		assertTrue("First element should be InsertRecord.", records.get(0).accept(visitor).equals(INSERT_RECORD));
		assertTrue("Second element should be UpdateRecord,", records.get(1).accept(visitor).equals(UPDATE_RECORD));
		assertTrue("Third element should be DeleteRecord", records.get(2).accept(visitor).equals(DELETE_RECORD));
	}
}
