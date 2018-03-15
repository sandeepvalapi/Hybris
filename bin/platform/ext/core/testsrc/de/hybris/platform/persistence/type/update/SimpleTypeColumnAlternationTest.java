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
package de.hybris.platform.persistence.type.update;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.persistence.type.update.misc.UpdateModelException;
import de.hybris.platform.util.Config;

import java.sql.SQLException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class SimpleTypeColumnAlternationTest extends AbstractColumnAlternationTest
{

	private static final Logger LOG = Logger.getLogger(SimpleTypeColumnAlternationTest.class.getName());


	@Before
	public void setUp()
	{
		setTableNameSubfix(String.valueOf(System.currentTimeMillis()));
	}

	//############TEXT/BINARY TYPES###############################
	@Test
	public void alterVarchar() throws SQLException, UpdateModelException
	{
		checkTypeChange(new ColumnDefinitionImpl("VARCHAR", Integer.valueOf(100)),
		//"varchar"varchar(100)",
				new ColumnDefinitionImpl("VARCHAR", Integer.valueOf(200)),
				//		"varchar(200)", 
				getData(null));
	}

	@Test
	public void alterNChar2Varchar() throws SQLException, UpdateModelException
	{
		if (Config.isSQLServerUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("NCHAR", Integer.valueOf(100)),
			//"varchar"varchar(100)",
					new ColumnDefinitionImpl("VARCHAR", Integer.valueOf(200)),
					//		"varchar(200)", 
					getData(null));
		}
	}

	@Test
	public void alterNChar2NVarchar() throws SQLException, UpdateModelException
	{
		if (Config.isSQLServerUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("NCHAR", Integer.valueOf(100)),
			//"varchar"varchar(100)",
					new ColumnDefinitionImpl("NVARCHAR", Integer.valueOf(200)),
					//		"varchar(200)", 
					getData(null));
		}
	}


	@Test
	public void alterNChar2Binary() throws SQLException, UpdateModelException
	{
		if (Config.isSQLServerUsed())
		{
			try
			{
				checkTypeChange(new ColumnDefinitionImpl("NCHAR", Integer.valueOf(100)),
				//"varchar"varchar(100)",
						new ColumnDefinitionImpl("BINARY", Integer.valueOf(200)),
						//		"varchar(200)", 
						getData(null));
			}
			catch (final UpdateModelException e)
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug(e.getMessage());
				}
				if (!Config.isHSQLDBUsed() && !Config.isSQLServerUsed())
				{
					Assert.fail("conversion should be possible from CHAR->BINARY");
				}
			}
		}
	}

	@Test
	public void alterNChar2VarBinary() throws SQLException, UpdateModelException
	{
		if (Config.isSQLServerUsed())
		{
			try
			{
				checkTypeChange(new ColumnDefinitionImpl("NCHAR", Integer.valueOf(100)),
				//"varchar"varchar(100)",
						new ColumnDefinitionImpl("VARBINARY", Integer.valueOf(200)),
						//		"varchar(200)", 
						getData(null));
			}
			catch (final UpdateModelException e)
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug(e.getMessage());
				}
				if (!Config.isHSQLDBUsed() && !Config.isSQLServerUsed())
				{
					Assert.fail("conversion should be possible from CHAR->BINARY");
				}
			}
		}
	}



	@Test
	public void alterNChar2Text() throws SQLException, UpdateModelException
	{
		if (Config.isSQLServerUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("NCHAR", Integer.valueOf(100)),
			//"varchar"varchar(100)",
					new ColumnDefinitionImpl("TEXT"),
					//		"varchar(200)", 
					getData(null));
		}
	}


	@Test
	public void alterNVarchar2Char() throws SQLException, UpdateModelException
	{
		if (Config.isSQLServerUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("NVARCHAR", Integer.valueOf(100)),
			//"varchar"varchar(100)",
					new ColumnDefinitionImpl("CHAR", Integer.valueOf(200)),
					//		"varchar(200)", 
					getData(null));
		}
	}

	@Test
	public void alterNVarchar2NChar() throws SQLException, UpdateModelException
	{
		if (Config.isSQLServerUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("NVARCHAR", Integer.valueOf(100)),
			//"varchar"varchar(100)",
					new ColumnDefinitionImpl("NCHAR", Integer.valueOf(200)),
					//		"varchar(200)", 
					getData(null));
		}
	}

	@Test
	public void alterNVarchar2Varchar() throws SQLException, UpdateModelException
	{
		if (Config.isSQLServerUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("NVARCHAR", Integer.valueOf(100)),
			//"varchar"varchar(100)",
					new ColumnDefinitionImpl("VARCHAR", Integer.valueOf(200)),
					//		"varchar(200)", 
					getData(null));
		}
	}


	@Test
	public void alterNVarchar2Binary() throws SQLException, UpdateModelException
	{
		if (Config.isSQLServerUsed())
		{
			try
			{
				checkTypeChange(new ColumnDefinitionImpl("NVARCHAR", Integer.valueOf(100)),
				//"varchar"varchar(100)",
						new ColumnDefinitionImpl("BINARY", Integer.valueOf(200)),
						//		"varchar(200)", 
						getData(null));
			}
			catch (final UpdateModelException e)
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug(e.getMessage());
				}
				if (!Config.isHSQLDBUsed() && !Config.isSQLServerUsed())
				{
					Assert.fail("conversion should be possible from CHAR->BINARY");
				}
			}
		}
	}

	@Test
	public void alterNVarchar2VarBinary() throws SQLException, UpdateModelException
	{
		if (Config.isSQLServerUsed())
		{
			try
			{
				checkTypeChange(new ColumnDefinitionImpl("NVARCHAR", Integer.valueOf(100)),
				//"varchar"varchar(100)",
						new ColumnDefinitionImpl("VARBINARY", Integer.valueOf(200)),
						//		"varchar(200)", 
						getData(null));
			}
			catch (final UpdateModelException e)
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug(e.getMessage());
				}
				if (!Config.isHSQLDBUsed() && !Config.isSQLServerUsed())
				{
					Assert.fail("conversion should be possible from CHAR->BINARY");
				}
			}
		}
	}



	@Test
	public void alterNVarchar2Text() throws SQLException, UpdateModelException
	{
		if (Config.isSQLServerUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("NVARCHAR", Integer.valueOf(100)),
			//"varchar"varchar(100)",
					new ColumnDefinitionImpl("TEXT"),
					//		"varchar(200)", 
					getData(null));
		}
	}


	@Test
	public void alterChar2Varchar() throws SQLException, UpdateModelException
	{
		checkTypeChange(new ColumnDefinitionImpl("CHAR", Integer.valueOf(100)),
		//"varchar"varchar(100)",
				new ColumnDefinitionImpl("VARCHAR", Integer.valueOf(200)),
				//		"varchar(200)", 
				getData(null));
	}

	@Test
	public void alterChar2Binary() throws SQLException, UpdateModelException
	{
		try
		{
			if (Config.isHSQLDBUsed())
			{
				checkTypeChange(new ColumnDefinitionImpl("CHAR", Integer.valueOf(100)),
						//"varchar"varchar(100)",
						new ColumnDefinitionImpl("BINARY", Integer.valueOf(200)),
						new ColumnDefinitionImpl("CHAR", Integer.valueOf(100)),
						//		"varchar(200)", 
						getData(null));
			}
			else
			{
				checkTypeChange(new ColumnDefinitionImpl("CHAR", Integer.valueOf(100)),
				//"varchar"varchar(100)",
						new ColumnDefinitionImpl("BINARY", Integer.valueOf(200)),
						//		"varchar(200)", 
						getData(null));
			}
		}
		catch (final UpdateModelException e)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug(e.getMessage());
			}
			if (!Config.isHSQLDBUsed() && !Config.isSQLServerUsed())
			{
				Assert.fail("conversion should be possible from CHAR->BINARY");
			}
		}
	}

	@Test
	public void alterChar2VarBinary() throws SQLException, UpdateModelException
	{
		try
		{
			if (Config.isHSQLDBUsed())
			{
				checkTypeChange(new ColumnDefinitionImpl("CHAR", Integer.valueOf(100)),
						//"varchar"varchar(100)",
						new ColumnDefinitionImpl("VARBINARY", Integer.valueOf(200)),
						new ColumnDefinitionImpl("CHAR", Integer.valueOf(100)),
						//		"varchar(200)", 
						getData(null));
			}
			else
			{
				checkTypeChange(new ColumnDefinitionImpl("CHAR", Integer.valueOf(100)),
				//"varchar"varchar(100)",
						new ColumnDefinitionImpl("VARBINARY", Integer.valueOf(200)),
						//		"varchar(200)", 
						getData(null));
			}
		}
		catch (final UpdateModelException e)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug(e.getMessage());
			}
			if (!Config.isHSQLDBUsed() && !Config.isSQLServerUsed())
			{
				Assert.fail("conversion should be possible from CHAR->BINARY");
			}
		}
	}

	@Test
	public void alterChar2TinyBlob() throws SQLException, UpdateModelException
	{
		if (Config.isMySQLUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("CHAR", Integer.valueOf(100)),
			//"varchar"varchar(100)",
					new ColumnDefinitionImpl("TINYBLOB"),
					//		"varchar(200)", 
					getData(null));
		}
	}

	@Test
	public void alterChar2TinyText() throws SQLException, UpdateModelException
	{
		if (Config.isMySQLUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("CHAR", Integer.valueOf(100)),
			//"varchar"varchar(100)",
					new ColumnDefinitionImpl("TINYTEXT"),
					//		"varchar(200)", 
					getData(null));
		}
	}

	@Test
	public void alterChar2Blob() throws SQLException, UpdateModelException
	{
		if (Config.isMySQLUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("CHAR", Integer.valueOf(100)),
			//"varchar"varchar(100)",
					new ColumnDefinitionImpl("BLOB"),
					//		"varchar(200)", 
					getData(null));
		}
	}

	@Test
	public void alterChar2Text() throws SQLException, UpdateModelException
	{
		if (Config.isMySQLUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("CHAR", Integer.valueOf(100)),
			//"varchar"varchar(100)",
					new ColumnDefinitionImpl("TEXT"),
					//		"varchar(200)", 
					getData(null));
		}
	}

	@Test
	public void alterChar2MediumBlob() throws SQLException, UpdateModelException
	{
		if (Config.isMySQLUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("CHAR", Integer.valueOf(100)),
			//"varchar"varchar(100)",
					new ColumnDefinitionImpl("MEDIUMBLOB"),
					//		"varchar(200)", 
					getData(null));
		}
	}

	@Test
	public void alterChar2MediumText() throws SQLException, UpdateModelException
	{
		if (Config.isMySQLUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("CHAR", Integer.valueOf(100)),
			//"varchar"varchar(100)",
					new ColumnDefinitionImpl("MEDIUMTEXT"),
					//		"varchar(200)", 
					getData(null));
		}
	}

	@Test
	public void alterChar2LongBlob() throws SQLException, UpdateModelException
	{
		if (Config.isMySQLUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("CHAR", Integer.valueOf(100)),
			//"varchar"varchar(100)",
					new ColumnDefinitionImpl("LONGBLOB"),
					//		"varchar(200)", 
					getData(null));
		}
	}

	@Test
	public void alterChar2LongText() throws SQLException, UpdateModelException
	{
		if (Config.isMySQLUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("CHAR", Integer.valueOf(100)),
			//"varchar"varchar(100)",
					new ColumnDefinitionImpl("LONGTEXT"),
					//		"varchar(200)", 
					getData(null));
		}
	}


	@Test
	public void alterVarChar2Char() throws SQLException, UpdateModelException
	{
		checkTypeChange(new ColumnDefinitionImpl("VARCHAR", Integer.valueOf(200)),
		//"varchar"varchar(100)",
				new ColumnDefinitionImpl("CHAR", Integer.valueOf(200)),
				//		"varchar(200)", 
				getData(null));
	}

	@Test
	public void alterVarchar2Binary() throws SQLException, UpdateModelException
	{
		try
		{
			if (Config.isHSQLDBUsed())
			{
				checkTypeChange(new ColumnDefinitionImpl("VARCHAR", Integer.valueOf(100)),
						//"varchar"varchar(100)",
						new ColumnDefinitionImpl("BINARY", Integer.valueOf(200)),
						new ColumnDefinitionImpl("VARCHAR", Integer.valueOf(100)),
						//		"varchar(200)", 
						getData(null));
			}
			else
			{
				checkTypeChange(new ColumnDefinitionImpl("VARCHAR", Integer.valueOf(100)),
				//"varchar"varchar(100)",
						new ColumnDefinitionImpl("BINARY", Integer.valueOf(200)),
						//		"varchar(200)", 
						getData(null));
			}
		}
		catch (final UpdateModelException e)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug(e.getMessage());
			}
			if (!Config.isHSQLDBUsed() && !Config.isSQLServerUsed())
			{
				Assert.fail("conversion should be possible from VARCHAR->BINARY");
			}
		}
	}

	@Test
	public void alterVarchar2VarBinary() throws SQLException, UpdateModelException
	{
		try
		{
			if (Config.isHSQLDBUsed())
			{
				checkTypeChange(new ColumnDefinitionImpl("VARCHAR", Integer.valueOf(100)),
						//"varchar"varchar(100)",
						new ColumnDefinitionImpl("VARBINARY", Integer.valueOf(200)),
						new ColumnDefinitionImpl("VARCHAR", Integer.valueOf(100)),
						//		"varchar(200)", 
						getData(null));
			}
			else
			{
				checkTypeChange(new ColumnDefinitionImpl("VARCHAR", Integer.valueOf(100)),
				//"varchar"varchar(100)",
						new ColumnDefinitionImpl("VARBINARY", Integer.valueOf(200)),
						//		"varchar(200)", 
						getData(null));
			}
		}
		catch (final UpdateModelException e)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug(e.getMessage());
			}
			if (!Config.isHSQLDBUsed() && !Config.isSQLServerUsed())
			{
				Assert.fail("conversion should be possible from VARCHAR->BINARY");
			}
		}
	}

	@Test
	public void alterVarchar2TinyBlob() throws SQLException, UpdateModelException
	{
		if (Config.isMySQLUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("VARCHAR", Integer.valueOf(100)),
			//"varchar"varchar(100)",
					new ColumnDefinitionImpl("TINYBLOB"),
					//		"varchar(200)", 
					getData(null));
		}
	}

	@Test
	public void alterVarchar2TinyText() throws SQLException, UpdateModelException
	{
		if (Config.isMySQLUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("VARCHAR", Integer.valueOf(100)),
			//"varchar"varchar(100)",
					new ColumnDefinitionImpl("TINYTEXT"),
					//		"varchar(200)", 
					getData(null));
		}
	}

	@Test
	public void alterVarchar2Blob() throws SQLException, UpdateModelException
	{
		if (Config.isMySQLUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("VARCHAR", Integer.valueOf(100)),
			//"varchar"varchar(100)",
					new ColumnDefinitionImpl("BLOB"),
					//		"varchar(200)", 
					getData(null));
		}
	}

	@Test
	public void alterVarchar2Text() throws SQLException, UpdateModelException
	{
		if (Config.isMySQLUsed() || Config.isSQLServerUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("VARCHAR", Integer.valueOf(100)),
			//"varchar"varchar(100)",
					new ColumnDefinitionImpl("TEXT"),
					//		"varchar(200)", 
					getData(null));
		}
	}

	@Test
	public void alterVarchar2MediumBlob() throws SQLException, UpdateModelException
	{
		if (Config.isMySQLUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("VARCHAR", Integer.valueOf(100)),
			//"varchar"varchar(100)",
					new ColumnDefinitionImpl("MEDIUMBLOB"),
					//		"varchar(200)", 
					getData(null));
		}
	}

	@Test
	public void alterVarchar2MediumText() throws SQLException, UpdateModelException
	{
		if (Config.isMySQLUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("VARCHAR", Integer.valueOf(100)),
			//"varchar"varchar(100)",
					new ColumnDefinitionImpl("MEDIUMTEXT"),
					//		"varchar(200)", 
					getData(null));
		}
	}

	@Test
	public void alterVarchar2LongBlob() throws SQLException, UpdateModelException
	{
		if (Config.isMySQLUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("VARCHAR", Integer.valueOf(100)),
			//"varchar"varchar(100)",
					new ColumnDefinitionImpl("LONGBLOB"),
					//		"varchar(200)", 
					getData(null));
		}
	}

	@Test
	public void alterVarchar2LongText() throws SQLException, UpdateModelException
	{
		if (Config.isMySQLUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("VARCHAR", Integer.valueOf(100)),
			//"varchar"varchar(100)",
					new ColumnDefinitionImpl("LONGTEXT"),
					//		"varchar(200)", 
					getData(null));
		}
	}


	@Test
	public void alterBinary2Char() throws SQLException, UpdateModelException
	{
		try
		{
			if (Config.isHSQLDBUsed())
			{
				checkTypeChange(new ColumnDefinitionImpl("BINARY", Integer.valueOf(200)),
						//"varchar"varchar(100)",
						new ColumnDefinitionImpl("CHAR", Integer.valueOf(200)),
						new ColumnDefinitionImpl("BINARY", Integer.valueOf(200)),
						//		"varchar(200)", 
						getData(null));
			}
			else
			{
				checkTypeChange(new ColumnDefinitionImpl("BINARY", Integer.valueOf(200)),
				//"varchar"varchar(100)",
						new ColumnDefinitionImpl("CHAR", Integer.valueOf(200)),
						//		"varchar(200)", 
						getData(null));
			}
		}
		catch (final SQLException e)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug(e.getMessage());
			}
			if (!Config.isHSQLDBUsed() && !Config.isSQLServerUsed())
			{
				Assert.fail("conversion should be possible from BINARY->CHAR");
			}
		}
	}

	@Test
	public void alterBinary2Varchar() throws SQLException, UpdateModelException
	{
		//for sqlserver 
		//Implicit conversion from data type varchar to binary is not allowed. Use the CONVERT function to run this query.
		if (!Config.isSQLServerUsed())
		{
			try
			{
				if (Config.isHSQLDBUsed())
				{
					checkTypeChange(new ColumnDefinitionImpl("BINARY", Integer.valueOf(100)),
							//"varchar"varchar(100)",
							new ColumnDefinitionImpl("VARCHAR", Integer.valueOf(200)),
							new ColumnDefinitionImpl("BINARY", Integer.valueOf(100)),
							//		"varchar(200)", 
							getData(null));
				}
				else
				{
					checkTypeChange(new ColumnDefinitionImpl("BINARY", Integer.valueOf(100)),
					//"varchar"varchar(100)",
							new ColumnDefinitionImpl("VARCHAR", Integer.valueOf(200)),
							//		"varchar(200)", 
							getData(null));
				}
			}
			catch (final UpdateModelException e)
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug(e.getMessage());
				}
				if (!Config.isHSQLDBUsed() && !Config.isSQLServerUsed())
				{
					Assert.fail("conversion should be possible from VARCHAR->BINARY");
				}
			}
		}
	}

	@Test
	public void alterBinary2VarBinary() throws SQLException, UpdateModelException
	{
		//for sqlserver 
		//Implicit conversion from data type varchar to binary is not allowed. Use the CONVERT function to run this query.
		if (!Config.isSQLServerUsed())
		{
			try
			{
				if (Config.isHSQLDBUsed())
				{
					checkTypeChange(new ColumnDefinitionImpl("BINARY", Integer.valueOf(100)),
							//"varchar"varchar(100)",
							new ColumnDefinitionImpl("VARBINARY", Integer.valueOf(200)),
							new ColumnDefinitionImpl("BINARY", Integer.valueOf(100)),
							//		"varchar(200)", 
							getData(null));
				}
				else
				{
					checkTypeChange(new ColumnDefinitionImpl("BINARY", Integer.valueOf(100)),
					//"varchar"varchar(100)",
							new ColumnDefinitionImpl("VARBINARY", Integer.valueOf(200)),
							//		"varchar(200)", 
							getData(null));
				}
			}
			catch (final UpdateModelException e)
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug(e.getMessage());
				}
				if (!Config.isHSQLDBUsed() && !Config.isSQLServerUsed())
				{
					Assert.fail("conversion should be possible from VARCHAR->BINARY");
				}
			}
		}
	}

	@Test
	public void alterBinary2TinyBlob() throws SQLException, UpdateModelException
	{
		if (Config.isMySQLUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("BINARY", Integer.valueOf(100)),
			//"varchar"varchar(100)",
					new ColumnDefinitionImpl("TINYBLOB"),
					//		"varchar(200)", 
					getData(null));
		}
	}

	@Test
	public void alterBinary2TinyText() throws SQLException, UpdateModelException
	{
		if (Config.isMySQLUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("BINARY", Integer.valueOf(100)),
			//"varchar"varchar(100)",
					new ColumnDefinitionImpl("TINYTEXT"),
					//		"varchar(200)", 
					getData(null));
		}
	}

	@Test
	public void alterBinary2Blob() throws SQLException, UpdateModelException
	{
		if (Config.isMySQLUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("BINARY", Integer.valueOf(100)),
			//"varchar"varchar(100)",
					new ColumnDefinitionImpl("BLOB"),
					//		"varchar(200)", 
					getData(null));
		}
	}

	@Test
	public void alterBinary2Text() throws SQLException, UpdateModelException
	{
		//for sqlserver 
		//Implicit conversion from data type varchar to binary is not allowed. Use the CONVERT function to run this query.
		if (Config.isMySQLUsed() /* || Config.isSQLServerUsed() */)
		{
			checkTypeChange(new ColumnDefinitionImpl("BINARY", Integer.valueOf(100)),
			//"varchar"varchar(100)",
					new ColumnDefinitionImpl("TEXT"),
					//		"varchar(200)", 
					getData(null));
		}
	}

	@Test
	public void alterBinary2MediumBlob() throws SQLException, UpdateModelException
	{
		if (Config.isMySQLUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("BINARY", Integer.valueOf(100)),
			//"varchar"varchar(100)",
					new ColumnDefinitionImpl("MEDIUMBLOB"),
					//		"varchar(200)", 
					getData(null));
		}
	}

	@Test
	public void alterBinary2MediumText() throws SQLException, UpdateModelException
	{
		if (Config.isMySQLUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("BINARY", Integer.valueOf(100)),
			//"varchar"varchar(100)",
					new ColumnDefinitionImpl("MEDIUMTEXT"),
					//		"varchar(200)", 
					getData(null));
		}
	}

	@Test
	public void alterBinary2LongBlob() throws SQLException, UpdateModelException
	{
		if (Config.isMySQLUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("BINARY", Integer.valueOf(100)),
			//"varchar"varchar(100)",
					new ColumnDefinitionImpl("LONGBLOB"),
					//		"varchar(200)", 
					getData(null));
		}
	}

	@Test
	public void alterBinary2LongText() throws SQLException, UpdateModelException
	{
		if (Config.isMySQLUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("BINARY", Integer.valueOf(100)),
			//"varchar"varchar(100)",
					new ColumnDefinitionImpl("LONGTEXT"),
					//		"varchar(200)", 
					getData(null));
		}
	}

	@Test
	public void alterVarBinary2Char() throws SQLException, UpdateModelException
	{
		//for sqlserver 
		//Implicit conversion from data type varchar to binary is not allowed. Use the CONVERT function to run this query.
		if (!Config.isSQLServerUsed())
		{
			try
			{
				if (Config.isHSQLDBUsed())
				{
					checkTypeChange(new ColumnDefinitionImpl("VARBINARY", Integer.valueOf(200)),
							//"varchar"varchar(100)",
							new ColumnDefinitionImpl("CHAR", Integer.valueOf(200)),
							new ColumnDefinitionImpl("VARBINARY", Integer.valueOf(200)),
							//		"varchar(200)", 
							getData(null));
				}
				else
				{
					checkTypeChange(new ColumnDefinitionImpl("VARBINARY", Integer.valueOf(200)),
					//"varchar"varchar(100)",
							new ColumnDefinitionImpl("CHAR", Integer.valueOf(200)),
							//		"varchar(200)", 
							getData(null));
				}
			}
			catch (final UpdateModelException e)
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug(e.getMessage());
				}
				if (!Config.isHSQLDBUsed() && !Config.isSQLServerUsed())
				{
					Assert.fail("conversion should be possible from VARBINARY->CHAR");
				}
			}
		}
	}

	@Test
	public void alterVarBinary2Varchar() throws SQLException, UpdateModelException
	{
		//for sqlserver 
		//Implicit conversion from data type varchar to binary is not allowed. Use the CONVERT function to run this query.
		if (!Config.isSQLServerUsed())
		{
			try
			{
				if (Config.isHSQLDBUsed())
				{
					checkTypeChange(new ColumnDefinitionImpl("VARBINARY", Integer.valueOf(100)),
							//"varchar"varchar(100)",
							new ColumnDefinitionImpl("VARCHAR", Integer.valueOf(200)),
							new ColumnDefinitionImpl("VARBINARY", Integer.valueOf(100)),
							//		"varchar(200)", 
							getData(null));
				}
				else
				{
					checkTypeChange(new ColumnDefinitionImpl("VARBINARY", Integer.valueOf(100)),
					//"varchar"varchar(100)",
							new ColumnDefinitionImpl("VARCHAR", Integer.valueOf(200)),
							//		"varchar(200)", 
							getData(null));
				}
			}
			catch (final UpdateModelException e)
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug(e.getMessage());
				}
				if (!Config.isHSQLDBUsed() && !Config.isSQLServerUsed())
				{
					Assert.fail("conversion should be possible from VARBINARY->VARCHAR");
				}
			}
		}
	}

	@Test
	public void alterVarBinary2VarBinary() throws SQLException, UpdateModelException
	{
		//for sqlserver 
		//Implicit conversion from data type varchar to binary is not allowed. Use the CONVERT function to run this query.
		if (!Config.isSQLServerUsed())
		{
			try
			{

				checkTypeChange(new ColumnDefinitionImpl("VARBINARY", Integer.valueOf(100)),
				//"varchar"varchar(100)",
						new ColumnDefinitionImpl("VARBINARY", Integer.valueOf(200)),
						//		"varchar(200)", 
						getData(null));
			}
			catch (final UpdateModelException e)
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug(e.getMessage());
				}
				if (!Config.isHSQLDBUsed() && !Config.isSQLServerUsed())
				{
					Assert.fail("conversion should be possible from VARBINARY->BINARY");
				}
			}
		}
	}

	@Test
	public void alterVarBinary2TinyBlob() throws SQLException, UpdateModelException
	{
		if (Config.isMySQLUsed())
		{
			if (Config.isHSQLDBUsed())
			{
				checkTypeChange(new ColumnDefinitionImpl("VARBINARY", Integer.valueOf(100)),
				//"varchar"varchar(100)",
						new ColumnDefinitionImpl("TINYBLOB"), new ColumnDefinitionImpl("VARBINARY", Integer.valueOf(100)),
						//		"varchar(200)", 
						getData(null));
			}
			else
			{
				checkTypeChange(new ColumnDefinitionImpl("VARBINARY", Integer.valueOf(100)),
				//"varchar"varchar(100)",
						new ColumnDefinitionImpl("TINYBLOB"),
						//		"varchar(200)", 
						getData(null));
			}
		}
	}

	@Test
	public void alterVarBinary2TinyText() throws SQLException, UpdateModelException
	{
		if (Config.isMySQLUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("VARBINARY", Integer.valueOf(100)),
			//"varchar"varchar(100)",
					new ColumnDefinitionImpl("TINYTEXT"),
					//		"varchar(200)", 
					getData(null));
		}
	}

	@Test
	public void alterVarBinary2Blob() throws SQLException, UpdateModelException
	{
		if (Config.isMySQLUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("VARBINARY", Integer.valueOf(100)),
			//"varchar"varchar(100)",
					new ColumnDefinitionImpl("BLOB"),
					//		"varchar(200)", 
					getData(null));
		}
	}

	@Test
	public void alterVarBinary2Text() throws SQLException, UpdateModelException
	{
		//for sqlserver 
		//Implicit conversion from data type varchar to binary is not allowed. Use the CONVERT function to run this query.
		if (Config.isMySQLUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("VARBINARY", Integer.valueOf(100)),
			//"varchar"varchar(100)",
					new ColumnDefinitionImpl("TEXT"),
					//		"varchar(200)", 
					getData(null));
		}
	}

	@Test
	public void alterVarBinary2MediumBlob() throws SQLException, UpdateModelException
	{
		if (Config.isMySQLUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("VARBINARY", Integer.valueOf(100)),
			//"varchar"varchar(100)",
					new ColumnDefinitionImpl("MEDIUMBLOB"),
					//		"varchar(200)", 
					getData(null));
		}
	}

	@Test
	public void alterVarBinary2MediumText() throws SQLException, UpdateModelException
	{
		if (Config.isMySQLUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("VARBINARY", Integer.valueOf(100)),
			//"varchar"varchar(100)",
					new ColumnDefinitionImpl("MEDIUMTEXT"),
					//		"varchar(200)", 
					getData(null));
		}
	}

	@Test
	public void alterVarBinary2LongBlob() throws SQLException, UpdateModelException
	{
		if (Config.isMySQLUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("VARBINARY", Integer.valueOf(100)),
			//"varchar"varchar(100)",
					new ColumnDefinitionImpl("LONGBLOB"),
					//		"varchar(200)", 
					getData(null));
		}
	}

	@Test
	public void alterVarBinary2LongText() throws SQLException, UpdateModelException
	{
		if (Config.isMySQLUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("VARBINARY", Integer.valueOf(100)),
			//"varchar"varchar(100)",
					new ColumnDefinitionImpl("LONGTEXT"),
					//		"varchar(200)", 
					getData(null));
		}
	}


	//############NUMERIC TYPES###############################

	@Test
	public void alterIntTruncation() throws SQLException, UpdateModelException
	{
		if (Config.isMySQLUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("INT"), new ColumnDefinitionImpl("TINYINT"), new ColumnDefinitionImpl("INT"),
					Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(45),
					Integer.valueOf(5), Integer.valueOf(6456));
			//update frame work won't throw exception
		}

	}

	@Test
	public void alterInt2Dec() throws SQLException, UpdateModelException
	{
		if (Config.isMySQLUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("INT"), new ColumnDefinitionImpl("DEC"), Integer.valueOf(1),
					Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(45), Integer.valueOf(5),
					Integer.valueOf(6456), Integer.valueOf(Integer.MAX_VALUE), Integer.valueOf(Integer.MIN_VALUE));
		}
	}

	@Test
	public void alterInt2Float() throws SQLException, UpdateModelException
	{
		checkTypeChange(new ColumnDefinitionImpl("INT"), new ColumnDefinitionImpl("FLOAT"), Integer.valueOf(1), Integer.valueOf(2),
				Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(45), Integer.valueOf(5), Integer.valueOf(6456),
				Integer.valueOf(Integer.MAX_VALUE), Integer.valueOf(Integer.MIN_VALUE));
	}

	@Test
	public void alterInt2Double() throws SQLException, UpdateModelException
	{
		if (!Config.isSQLServerUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("INT"), new ColumnDefinitionImpl("DOUBLE"), Integer.valueOf(1),
					Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(45), Integer.valueOf(5),
					Integer.valueOf(6456), Integer.valueOf(Integer.MAX_VALUE), Integer.valueOf(Integer.MIN_VALUE));
		}
	}

	@Test
	public void alterInt2Bigint() throws SQLException, UpdateModelException
	{
		checkTypeChange(new ColumnDefinitionImpl("INT"), new ColumnDefinitionImpl("BIGINT"), Integer.valueOf(1),
				Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(45), Integer.valueOf(5),
				Integer.valueOf(6456), Integer.valueOf(Integer.MAX_VALUE), Integer.valueOf(Integer.MIN_VALUE));
	}


	@Test
	public void alterInt2Medint() throws SQLException, UpdateModelException
	{

		if (Config.isMySQLUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("INT"), new ColumnDefinitionImpl("MEDIUMINT"), new ColumnDefinitionImpl("INT"),
					Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(45),
					Integer.valueOf(5), Integer.valueOf(6456), Integer.valueOf(Integer.MAX_VALUE), Integer.valueOf(Integer.MIN_VALUE));
		}
		//		catch (final Exception e)
		//		{
		//			Assert.assertTrue("com.mysql.jdbc.MysqlDataTruncation".compareToIgnoreCase(e.getClass().getName()) == 0);
		//		}
	}

	@Test
	public void alterInt2Smallint() throws SQLException, UpdateModelException
	{
		if (!Config.isOracleUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("INT"), new ColumnDefinitionImpl("SMALLINT"), new ColumnDefinitionImpl("INT"),
					Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(45),
					Integer.valueOf(5), Integer.valueOf(6456), Integer.valueOf(Integer.MAX_VALUE), Integer.valueOf(Integer.MIN_VALUE));
			//update frame work won't throw exception
		}
	}

	@Test
	public void alterInt2Bool() throws SQLException, UpdateModelException
	{

		if (!Config.isOracleUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("INT"), new ColumnDefinitionImpl("BOOL"), new ColumnDefinitionImpl("INT"),
					Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(45),
					Integer.valueOf(5), Integer.valueOf(6456), Integer.valueOf(Integer.MAX_VALUE), Integer.valueOf(Integer.MIN_VALUE));
		}

	}

	@Test
	public void alterInt2Tinyint() throws SQLException, UpdateModelException
	{

		if (!Config.isOracleUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("INT"), new ColumnDefinitionImpl("TINYINT"), new ColumnDefinitionImpl("INT"),
					Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(45),
					Integer.valueOf(5), Integer.valueOf(6456), Integer.valueOf(Integer.MAX_VALUE), Integer.valueOf(Integer.MIN_VALUE));
		}
	}

	@Test
	public void alterInt2Bit() throws SQLException, UpdateModelException
	{
		if (Config.isHSQLDBUsed()) //for hssqldb bit===boolean
		{
			checkTypeChange(new ColumnDefinitionImpl("INT"), new ColumnDefinitionImpl("BOOLEAN"), Integer.valueOf(1),
					Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(45), Integer.valueOf(5),
					Integer.valueOf(6456), Integer.valueOf(Integer.MAX_VALUE), Integer.valueOf(Integer.MIN_VALUE));
		}
		else if (Config.isMySQLUsed())
		{

			checkTypeChange(new ColumnDefinitionImpl("INT"), new ColumnDefinitionImpl("BIT"), new ColumnDefinitionImpl("INT"),
					Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(45),
					Integer.valueOf(5), Integer.valueOf(6456), Integer.valueOf(Integer.MAX_VALUE), Integer.valueOf(Integer.MIN_VALUE));
			//Assert.fail(" Should not be possible INT->BIT alternation");			
		}
		else if (Config.isSQLServerUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("INT"), new ColumnDefinitionImpl("BIT"), Integer.valueOf(1),
					Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(45), Integer.valueOf(5),
					Integer.valueOf(6456), Integer.valueOf(Integer.MAX_VALUE), Integer.valueOf(Integer.MIN_VALUE));
		}
	}

	@Test
	//(expected = MysqlDataTruncation.class)
	public void alterIntRange() throws SQLException, UpdateModelException
	{
		try
		{
			checkTypeChange(new ColumnDefinitionImpl("TINYINT"), new ColumnDefinitionImpl("INT"), Integer.valueOf(1),
					Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(45), Integer.valueOf(5),
					Integer.valueOf(6456));
		}
		catch (final Exception e)
		{
			if (Config.isMySQLUsed())
			{
				Assert.assertTrue("com.mysql.jdbc.MysqlDataTruncation".compareToIgnoreCase(e.getClass().getName()) == 0);
			}
		}
	}

	@Test
	public void alterInt() throws SQLException, UpdateModelException
	{
		if (Config.isSQLServerUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("TINYINT"), new ColumnDefinitionImpl("INT"), Integer.valueOf(0),
					Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(45),
					Integer.valueOf(5), Integer.valueOf(126), Integer.valueOf(127));
		}
		else
		{
			checkTypeChange(new ColumnDefinitionImpl("TINYINT"),
					(!Config.isHSQLDBUsed() ? new ColumnDefinitionImpl("INT", Integer.valueOf(10)) : new ColumnDefinitionImpl("INT")),
					Integer.valueOf(-127), Integer.valueOf(-23), Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2),
					Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(45), Integer.valueOf(5), Integer.valueOf(126),
					Integer.valueOf(127));
		}
	}

	@Test
	public void alterFloat() throws SQLException, UpdateModelException
	{
		if (Config.isSQLServerUsed())
		{
			//in msqsql there is no double only real
			checkTypeChange(new ColumnDefinitionImpl("FLOAT"), new ColumnDefinitionImpl("REAL"), Float.valueOf(-1.34535f),
					Float.valueOf(2.3453f), Float.valueOf(32435f), Float.valueOf(0.000043f), Float.valueOf(4537846734.323f),
					Float.valueOf(5f), Float.valueOf(645623924763f));
		}
		else
		{
			checkTypeChange(new ColumnDefinitionImpl("FLOAT"),
					(!Config.isHSQLDBUsed() ? new ColumnDefinitionImpl("DOUBLE", Integer.valueOf(22), Integer.valueOf(0))
							: new ColumnDefinitionImpl("DOUBLE")), Float.valueOf(-1.34535f), Float.valueOf(2.3453f),
					Float.valueOf(32435f), Float.valueOf(0.000043f), Float.valueOf(4537846734.323f), Float.valueOf(5f),
					Float.valueOf(645623924763f));
		}
	}

	//############DATE TYPES###############################

	@Test
	public void alterDate2Timestamp() throws SQLException, UpdateModelException
	{
		if (!Config.isSQLServerUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("DATE"), new ColumnDefinitionImpl("TIMESTAMP"),
					new Date(System.currentTimeMillis() - 10000), new Date(System.currentTimeMillis() - 100),
					new Date(System.currentTimeMillis() - 1), new Date(System.currentTimeMillis() + 100),
					new Date(System.currentTimeMillis() + 10000));
		}
	}

	@Test
	public void alterDate2SmallDateTime() throws SQLException, UpdateModelException
	{
		if (Config.isSQLServerUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("DATE"), new ColumnDefinitionImpl("SMALLDATETIME"),
					new Date(System.currentTimeMillis() - 10000), new Date(System.currentTimeMillis() - 100),
					new Date(System.currentTimeMillis() - 1), new Date(System.currentTimeMillis() + 100),
					new Date(System.currentTimeMillis() + 10000));
		}
	}

	@Test
	public void alterTime2SmallDateTime() throws SQLException, UpdateModelException
	{
		if (Config.isSQLServerUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("TIME"), new ColumnDefinitionImpl("SMALLDATETIME"),
					new Date(System.currentTimeMillis() - 10000), new Date(System.currentTimeMillis() - 100),
					new Date(System.currentTimeMillis() - 1), new Date(System.currentTimeMillis() + 100),
					new Date(System.currentTimeMillis() + 10000));
		}
	}

	@Test
	public void alterSmallDateTime2Time() throws SQLException, UpdateModelException
	{
		if (Config.isSQLServerUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("SMALLDATETIME"), new ColumnDefinitionImpl("TIME"),
					new Date(System.currentTimeMillis() - 10000), new Date(System.currentTimeMillis() - 100),
					new Date(System.currentTimeMillis() - 1), new Date(System.currentTimeMillis() + 100),
					new Date(System.currentTimeMillis() + 10000));
		}
	}

	@Test
	public void alterSmallDateTime2Date() throws SQLException, UpdateModelException
	{
		if (Config.isSQLServerUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("SMALLDATETIME"), new ColumnDefinitionImpl("DATE"),
					new Date(System.currentTimeMillis() - 10000), new Date(System.currentTimeMillis() - 100),
					new Date(System.currentTimeMillis() - 1), new Date(System.currentTimeMillis() + 100),
					new Date(System.currentTimeMillis() + 10000));
		}
	}


	@Test
	public void alterDate2Time() throws SQLException, UpdateModelException
	{
		try
		{
			checkTypeChange(new ColumnDefinitionImpl("DATE"), new ColumnDefinitionImpl("TIME"), new ColumnDefinitionImpl("DATE"),
					new Date(System.currentTimeMillis() - 10000), new Date(System.currentTimeMillis() - 100),
					new Date(System.currentTimeMillis() - 1), new Date(System.currentTimeMillis() + 100),
					new Date(System.currentTimeMillis() + 10000));
		}
		catch (final UpdateModelException e)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug(e.getMessage());
			}
			if (Config.isOracleUsed())
			{
				Assert.fail("conversion should be possible from DATE->TIME ??");
			}
		}
	}

	@Test
	public void alterTimestamp2Date() throws SQLException, UpdateModelException
	{
		//cannot insert timestamp for mssql 
		if (!Config.isSQLServerUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("TIMESTAMP"), new ColumnDefinitionImpl("DATE"),
					new Date(System.currentTimeMillis() - 10000), new Date(System.currentTimeMillis() - 100),
					new Date(System.currentTimeMillis() - 1), new Date(System.currentTimeMillis() + 100),
					new Date(System.currentTimeMillis() + 10000));
		}
	}

	@Test
	public void alterTimestamp2Time() throws SQLException, UpdateModelException
	{
		//cannot insert timestamp for mssql
		if (!Config.isSQLServerUsed())
		{
			checkTypeChange(new ColumnDefinitionImpl("TIMESTAMP"), new ColumnDefinitionImpl("TIME"),
					new Date(System.currentTimeMillis() - 10000), new Date(System.currentTimeMillis() - 100),
					new Date(System.currentTimeMillis() - 1), new Date(System.currentTimeMillis() + 100),
					new Date(System.currentTimeMillis() + 10000));
		}
	}

	@Test
	public void alterTime2Date() throws SQLException, UpdateModelException
	{
		try
		{
			checkTypeChange(new ColumnDefinitionImpl("TIME"), new ColumnDefinitionImpl("DATE"), new ColumnDefinitionImpl("TIME"),
					new Date(System.currentTimeMillis() - 10000), new Date(System.currentTimeMillis() - 100),
					new Date(System.currentTimeMillis() - 1), new Date(System.currentTimeMillis() + 100),
					new Date(System.currentTimeMillis() + 10000));
		}
		catch (final UpdateModelException e)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug(e.getMessage());
			}
			if (Config.isOracleUsed())
			{
				Assert.fail("conversion should be possible from TIME->DATE ??");
			}
		}
	}

	@Test
	public void alterTime2Timestamp() throws SQLException, UpdateModelException
	{
		try
		{
			checkTypeChange(new ColumnDefinitionImpl("TIME"), new ColumnDefinitionImpl("TIMESTAMP"),
					new ColumnDefinitionImpl("TIME"), new Date(System.currentTimeMillis() - 10000),
					new Date(System.currentTimeMillis() - 100), new Date(System.currentTimeMillis() - 1),
					new Date(System.currentTimeMillis() + 100), new Date(System.currentTimeMillis() + 10000));
		}
		catch (final UpdateModelException e)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug(e.getMessage());
			}
			if (Config.isOracleUsed())
			{
				Assert.fail("conversion should be possible from TIME->DATE ??");
			}
		}
	}

	protected Object[] getData(@SuppressWarnings("unused") final String metaType)
	{
		return new String[]
		{ "ala ma kota", "kot", "ala", "pies", "alalalalalalalalal", "0981892656156456!@~$$!%^&^*&(**)())" };
	}
}
