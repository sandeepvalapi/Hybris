/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Mar 15, 2018 5:02:29 PM                     ---
 * ----------------------------------------------------------------
 */
package com.hybris.training.core.model;

import com.hybris.training.core.enums.SwatchColorEnum;
import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.variants.model.VariantProductModel;
import java.util.Locale;
import java.util.Set;

/**
 * Generated model class for type ApparelStyleVariantProduct first defined at extension trainingcore.
 * <p>
 * Apparel style variant type that contains additional attribute describing variant style.
 */
@SuppressWarnings("all")
public class ApparelStyleVariantProductModel extends VariantProductModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ApparelStyleVariantProduct";
	
	/** <i>Generated constant</i> - Attribute key of <code>ApparelStyleVariantProduct.style</code> attribute defined at extension <code>trainingcore</code>. */
	public static final String STYLE = "style";
	
	/** <i>Generated constant</i> - Attribute key of <code>ApparelStyleVariantProduct.swatchColors</code> attribute defined at extension <code>trainingcore</code>. */
	public static final String SWATCHCOLORS = "swatchColors";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ApparelStyleVariantProductModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ApparelStyleVariantProductModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _baseProduct initial attribute declared by type <code>VariantProduct</code> at extension <code>catalog</code>
	 * @param _catalogVersion initial attribute declared by type <code>Product</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Product</code> at extension <code>core</code>
	 */
	@Deprecated
	public ApparelStyleVariantProductModel(final ProductModel _baseProduct, final CatalogVersionModel _catalogVersion, final String _code)
	{
		super();
		setBaseProduct(_baseProduct);
		setCatalogVersion(_catalogVersion);
		setCode(_code);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _baseProduct initial attribute declared by type <code>VariantProduct</code> at extension <code>catalog</code>
	 * @param _catalogVersion initial attribute declared by type <code>Product</code> at extension <code>catalog</code>
	 * @param _code initial attribute declared by type <code>Product</code> at extension <code>core</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public ApparelStyleVariantProductModel(final ProductModel _baseProduct, final CatalogVersionModel _catalogVersion, final String _code, final ItemModel _owner)
	{
		super();
		setBaseProduct(_baseProduct);
		setCatalogVersion(_catalogVersion);
		setCode(_code);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ApparelStyleVariantProduct.style</code> attribute defined at extension <code>trainingcore</code>. 
	 * @return the style - Color/Pattern of the product.
	 */
	@Accessor(qualifier = "style", type = Accessor.Type.GETTER)
	public String getStyle()
	{
		return getStyle(null);
	}
	/**
	 * <i>Generated method</i> - Getter of the <code>ApparelStyleVariantProduct.style</code> attribute defined at extension <code>trainingcore</code>. 
	 * @param loc the value localization key 
	 * @return the style - Color/Pattern of the product.
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "style", type = Accessor.Type.GETTER)
	public String getStyle(final Locale loc)
	{
		return getPersistenceContext().getLocalizedValue(STYLE, loc);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ApparelStyleVariantProduct.swatchColors</code> attribute defined at extension <code>trainingcore</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the swatchColors - A normalized color mapping to a standardized front-end navigable name.
	 */
	@Accessor(qualifier = "swatchColors", type = Accessor.Type.GETTER)
	public Set<SwatchColorEnum> getSwatchColors()
	{
		return getPersistenceContext().getPropertyValue(SWATCHCOLORS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ApparelStyleVariantProduct.style</code> attribute defined at extension <code>trainingcore</code>. 
	 *  
	 * @param value the style - Color/Pattern of the product.
	 */
	@Accessor(qualifier = "style", type = Accessor.Type.SETTER)
	public void setStyle(final String value)
	{
		setStyle(value,null);
	}
	/**
	 * <i>Generated method</i> - Setter of <code>ApparelStyleVariantProduct.style</code> attribute defined at extension <code>trainingcore</code>. 
	 *  
	 * @param value the style - Color/Pattern of the product.
	 * @param loc the value localization key 
	 * @throws IllegalArgumentException if localization key cannot be mapped to data language
	 */
	@Accessor(qualifier = "style", type = Accessor.Type.SETTER)
	public void setStyle(final String value, final Locale loc)
	{
		getPersistenceContext().setLocalizedValue(STYLE, loc, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ApparelStyleVariantProduct.swatchColors</code> attribute defined at extension <code>trainingcore</code>. 
	 *  
	 * @param value the swatchColors - A normalized color mapping to a standardized front-end navigable name.
	 */
	@Accessor(qualifier = "swatchColors", type = Accessor.Type.SETTER)
	public void setSwatchColors(final Set<SwatchColorEnum> value)
	{
		getPersistenceContext().setPropertyValue(SWATCHCOLORS, value);
	}
	
}
