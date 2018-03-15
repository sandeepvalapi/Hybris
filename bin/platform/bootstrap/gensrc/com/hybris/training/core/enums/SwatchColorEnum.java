package com.hybris.training.core.enums;

import de.hybris.platform.core.HybrisEnumValue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Generated enum SwatchColorEnum declared at extension trainingcore.
 */
@SuppressWarnings("PMD")
public class SwatchColorEnum implements HybrisEnumValue
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "SwatchColorEnum";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "SwatchColorEnum";
	private static final ConcurrentMap<String, SwatchColorEnum> cache = new ConcurrentHashMap<String, SwatchColorEnum>();
	/**
	* Generated enum value for SwatchColorEnum.BLACK declared at extension trainingcore.
	*/
	public static final SwatchColorEnum BLACK = valueOf("BLACK");
	
	/**
	* Generated enum value for SwatchColorEnum.BLUE declared at extension trainingcore.
	*/
	public static final SwatchColorEnum BLUE = valueOf("BLUE");
	
	/**
	* Generated enum value for SwatchColorEnum.BROWN declared at extension trainingcore.
	*/
	public static final SwatchColorEnum BROWN = valueOf("BROWN");
	
	/**
	* Generated enum value for SwatchColorEnum.GREEN declared at extension trainingcore.
	*/
	public static final SwatchColorEnum GREEN = valueOf("GREEN");
	
	/**
	* Generated enum value for SwatchColorEnum.GREY declared at extension trainingcore.
	*/
	public static final SwatchColorEnum GREY = valueOf("GREY");
	
	/**
	* Generated enum value for SwatchColorEnum.ORANGE declared at extension trainingcore.
	*/
	public static final SwatchColorEnum ORANGE = valueOf("ORANGE");
	
	/**
	* Generated enum value for SwatchColorEnum.PINK declared at extension trainingcore.
	*/
	public static final SwatchColorEnum PINK = valueOf("PINK");
	
	/**
	* Generated enum value for SwatchColorEnum.PURPLE declared at extension trainingcore.
	*/
	public static final SwatchColorEnum PURPLE = valueOf("PURPLE");
	
	/**
	* Generated enum value for SwatchColorEnum.RED declared at extension trainingcore.
	*/
	public static final SwatchColorEnum RED = valueOf("RED");
	
	/**
	* Generated enum value for SwatchColorEnum.SILVER declared at extension trainingcore.
	*/
	public static final SwatchColorEnum SILVER = valueOf("SILVER");
	
	/**
	* Generated enum value for SwatchColorEnum.WHITE declared at extension trainingcore.
	*/
	public static final SwatchColorEnum WHITE = valueOf("WHITE");
	
	/**
	* Generated enum value for SwatchColorEnum.YELLOW declared at extension trainingcore.
	*/
	public static final SwatchColorEnum YELLOW = valueOf("YELLOW");
	
	
	/** The code of this enum.*/
	private final String code;
	private final String codeLowerCase;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private SwatchColorEnum(final String code)
	{
		this.code = code.intern();
		this.codeLowerCase = this.code.toLowerCase().intern();
	}
	
	
	/**
	 * Compares this object to the specified object. The result is <code>true</code>
	 * if and only if the argument is not <code>null</code> and is an <code>SwatchColorEnum
	 * </code> object that contains the enum value <code>code</code> as this object.
	 *  
	 * @param obj the object to compare with.
	 * @return <code>true</code> if the objects are the same;
	 *         <code>false</code> otherwise.
	 */
	@Override
	public boolean equals(final Object obj)
	{
		try
		{
			final HybrisEnumValue enum2 = (HybrisEnumValue) obj;
			return this == enum2
			|| (enum2 != null && !this.getClass().isEnum() && !enum2.getClass().isEnum()
			&& this.getType().equalsIgnoreCase(enum2.getType()) && this.getCode().equalsIgnoreCase(enum2.getCode()));
		}
		catch (final ClassCastException e)
		{
			return false;
		}
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
	
	/**
	 * Returns a hash code for this <code>SwatchColorEnum</code>.
	 *  
	 * @return a hash code value for this object, equal to the enum value <code>code</code>
	 *         represented by this <code>SwatchColorEnum</code> object.
	 */
	@Override
	public int hashCode()
	{
		return this.codeLowerCase.hashCode();
	}
	
	/**
	 * Returns the code representing this enum value.
	 *  
	 * @return a string representation of the value of this object.
	 */
	@Override
	public String toString()
	{
		return this.code.toString();
	}
	
	/**
	 * Returns a <tt>SwatchColorEnum</tt> instance representing the specified enum value.
	 *  
	 * @param code an enum value
	 * @return a <tt>SwatchColorEnum</tt> instance representing <tt>value</tt>. 
	 */
	public static SwatchColorEnum valueOf(final String code)
	{
		final String key = code.toLowerCase();
		SwatchColorEnum result = cache.get(key);
		if (result == null)
		{
			SwatchColorEnum newValue = new SwatchColorEnum(code);
			SwatchColorEnum previous = cache.putIfAbsent(key, newValue);
			result = previous != null ? previous : newValue;
		}
		return result;
	}
	
}
