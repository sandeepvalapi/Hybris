package com.hybris.backoffice.solrsearch.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum SolrItemModificationType declared at extension backofficesolrsearch.
 */
@SuppressWarnings("PMD")
public enum SolrItemModificationType implements HybrisEnumValue
{
	/**
	 * Generated enum value for SolrItemModificationType.DELETE declared at extension backofficesolrsearch.
	 */
	DELETE("DELETE"),
	/**
	 * Generated enum value for SolrItemModificationType.UPDATE declared at extension backofficesolrsearch.
	 */
	UPDATE("UPDATE");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "SolrItemModificationType";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "SolrItemModificationType";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private SolrItemModificationType(final String code)
	{
		this.code = code.intern();
	}
	
	
	/**
	 * Gets the code of this enum value.
	 *  
	 * @return code of value
	 */
	@Override
	public String getCode()
	{
		return this.code;
	}
	
	/**
	 * Gets the type this enum value belongs to.
	 *  
	 * @return code of type
	 */
	@Override
	public String getType()
	{
		return SIMPLE_CLASSNAME;
	}
	
}
