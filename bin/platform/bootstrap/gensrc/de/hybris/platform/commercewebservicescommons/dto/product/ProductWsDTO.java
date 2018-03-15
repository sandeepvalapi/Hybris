/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at Mar 15, 2018 5:02:38 PM
 * ----------------------------------------------------------------
 *
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of SAP 
 * Hybris ("Confidential Information"). You shall not disclose such 
 * Confidential Information and shall use it only in accordance with the 
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.commercewebservicescommons.dto.product;

import java.io.Serializable;
import de.hybris.platform.commercewebservicescommons.dto.product.BaseOptionWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.CategoryWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.ClassificationWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.FutureStockWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.ImageWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.PriceRangeWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.PriceWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.ProductReferenceWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.PromotionWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.ReviewWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.StockWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.VariantMatrixElementWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.VariantOptionWsDTO;
import java.util.Collection;
import java.util.List;

public  class ProductWsDTO  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
 	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>ProductWsDTO.code</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String code;

	/** <i>Generated property</i> for <code>ProductWsDTO.name</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>ProductWsDTO.url</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String url;

	/** <i>Generated property</i> for <code>ProductWsDTO.description</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String description;

	/** <i>Generated property</i> for <code>ProductWsDTO.purchasable</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Boolean purchasable;

	/** <i>Generated property</i> for <code>ProductWsDTO.stock</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private StockWsDTO stock;

	/** <i>Generated property</i> for <code>ProductWsDTO.futureStocks</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private List<FutureStockWsDTO> futureStocks;

	/** <i>Generated property</i> for <code>ProductWsDTO.availableForPickup</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Boolean availableForPickup;

	/** <i>Generated property</i> for <code>ProductWsDTO.averageRating</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Double averageRating;

	/** <i>Generated property</i> for <code>ProductWsDTO.numberOfReviews</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Integer numberOfReviews;

	/** <i>Generated property</i> for <code>ProductWsDTO.summary</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String summary;

	/** <i>Generated property</i> for <code>ProductWsDTO.manufacturer</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String manufacturer;

	/** <i>Generated property</i> for <code>ProductWsDTO.variantType</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String variantType;

	/** <i>Generated property</i> for <code>ProductWsDTO.price</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private PriceWsDTO price;

	/** <i>Generated property</i> for <code>ProductWsDTO.baseProduct</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String baseProduct;

	/** <i>Generated property</i> for <code>ProductWsDTO.images</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Collection<ImageWsDTO> images;

	/** <i>Generated property</i> for <code>ProductWsDTO.categories</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Collection<CategoryWsDTO> categories;

	/** <i>Generated property</i> for <code>ProductWsDTO.reviews</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Collection<ReviewWsDTO> reviews;

	/** <i>Generated property</i> for <code>ProductWsDTO.classifications</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Collection<ClassificationWsDTO> classifications;

	/** <i>Generated property</i> for <code>ProductWsDTO.potentialPromotions</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Collection<PromotionWsDTO> potentialPromotions;

	/** <i>Generated property</i> for <code>ProductWsDTO.variantOptions</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private List<VariantOptionWsDTO> variantOptions;

	/** <i>Generated property</i> for <code>ProductWsDTO.baseOptions</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private List<BaseOptionWsDTO> baseOptions;

	/** <i>Generated property</i> for <code>ProductWsDTO.volumePricesFlag</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Boolean volumePricesFlag;

	/** <i>Generated property</i> for <code>ProductWsDTO.volumePrices</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private List<PriceWsDTO> volumePrices;

	/** <i>Generated property</i> for <code>ProductWsDTO.productReferences</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private List<ProductReferenceWsDTO> productReferences;

	/** <i>Generated property</i> for <code>ProductWsDTO.variantMatrix</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private List<VariantMatrixElementWsDTO> variantMatrix;

	/** <i>Generated property</i> for <code>ProductWsDTO.priceRange</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private PriceRangeWsDTO priceRange;

	/** <i>Generated property</i> for <code>ProductWsDTO.multidimensional</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Boolean multidimensional;
	
	public ProductWsDTO()
	{
		// default constructor
	}
	
		
	
	public void setCode(final String code)
	{
		this.code = code;
	}

		
	
	public String getCode() 
	{
		return code;
	}
	
		
	
	public void setName(final String name)
	{
		this.name = name;
	}

		
	
	public String getName() 
	{
		return name;
	}
	
		
	
	public void setUrl(final String url)
	{
		this.url = url;
	}

		
	
	public String getUrl() 
	{
		return url;
	}
	
		
	
	public void setDescription(final String description)
	{
		this.description = description;
	}

		
	
	public String getDescription() 
	{
		return description;
	}
	
		
	
	public void setPurchasable(final Boolean purchasable)
	{
		this.purchasable = purchasable;
	}

		
	
	public Boolean getPurchasable() 
	{
		return purchasable;
	}
	
		
	
	public void setStock(final StockWsDTO stock)
	{
		this.stock = stock;
	}

		
	
	public StockWsDTO getStock() 
	{
		return stock;
	}
	
		
	
	public void setFutureStocks(final List<FutureStockWsDTO> futureStocks)
	{
		this.futureStocks = futureStocks;
	}

		
	
	public List<FutureStockWsDTO> getFutureStocks() 
	{
		return futureStocks;
	}
	
		
	
	public void setAvailableForPickup(final Boolean availableForPickup)
	{
		this.availableForPickup = availableForPickup;
	}

		
	
	public Boolean getAvailableForPickup() 
	{
		return availableForPickup;
	}
	
		
	
	public void setAverageRating(final Double averageRating)
	{
		this.averageRating = averageRating;
	}

		
	
	public Double getAverageRating() 
	{
		return averageRating;
	}
	
		
	
	public void setNumberOfReviews(final Integer numberOfReviews)
	{
		this.numberOfReviews = numberOfReviews;
	}

		
	
	public Integer getNumberOfReviews() 
	{
		return numberOfReviews;
	}
	
		
	
	public void setSummary(final String summary)
	{
		this.summary = summary;
	}

		
	
	public String getSummary() 
	{
		return summary;
	}
	
		
	
	public void setManufacturer(final String manufacturer)
	{
		this.manufacturer = manufacturer;
	}

		
	
	public String getManufacturer() 
	{
		return manufacturer;
	}
	
		
	
	public void setVariantType(final String variantType)
	{
		this.variantType = variantType;
	}

		
	
	public String getVariantType() 
	{
		return variantType;
	}
	
		
	
	public void setPrice(final PriceWsDTO price)
	{
		this.price = price;
	}

		
	
	public PriceWsDTO getPrice() 
	{
		return price;
	}
	
		
	
	public void setBaseProduct(final String baseProduct)
	{
		this.baseProduct = baseProduct;
	}

		
	
	public String getBaseProduct() 
	{
		return baseProduct;
	}
	
		
	
	public void setImages(final Collection<ImageWsDTO> images)
	{
		this.images = images;
	}

		
	
	public Collection<ImageWsDTO> getImages() 
	{
		return images;
	}
	
		
	
	public void setCategories(final Collection<CategoryWsDTO> categories)
	{
		this.categories = categories;
	}

		
	
	public Collection<CategoryWsDTO> getCategories() 
	{
		return categories;
	}
	
		
	
	public void setReviews(final Collection<ReviewWsDTO> reviews)
	{
		this.reviews = reviews;
	}

		
	
	public Collection<ReviewWsDTO> getReviews() 
	{
		return reviews;
	}
	
		
	
	public void setClassifications(final Collection<ClassificationWsDTO> classifications)
	{
		this.classifications = classifications;
	}

		
	
	public Collection<ClassificationWsDTO> getClassifications() 
	{
		return classifications;
	}
	
		
	
	public void setPotentialPromotions(final Collection<PromotionWsDTO> potentialPromotions)
	{
		this.potentialPromotions = potentialPromotions;
	}

		
	
	public Collection<PromotionWsDTO> getPotentialPromotions() 
	{
		return potentialPromotions;
	}
	
		
	
	public void setVariantOptions(final List<VariantOptionWsDTO> variantOptions)
	{
		this.variantOptions = variantOptions;
	}

		
	
	public List<VariantOptionWsDTO> getVariantOptions() 
	{
		return variantOptions;
	}
	
		
	
	public void setBaseOptions(final List<BaseOptionWsDTO> baseOptions)
	{
		this.baseOptions = baseOptions;
	}

		
	
	public List<BaseOptionWsDTO> getBaseOptions() 
	{
		return baseOptions;
	}
	
		
	
	public void setVolumePricesFlag(final Boolean volumePricesFlag)
	{
		this.volumePricesFlag = volumePricesFlag;
	}

		
	
	public Boolean getVolumePricesFlag() 
	{
		return volumePricesFlag;
	}
	
		
	
	public void setVolumePrices(final List<PriceWsDTO> volumePrices)
	{
		this.volumePrices = volumePrices;
	}

		
	
	public List<PriceWsDTO> getVolumePrices() 
	{
		return volumePrices;
	}
	
		
	
	public void setProductReferences(final List<ProductReferenceWsDTO> productReferences)
	{
		this.productReferences = productReferences;
	}

		
	
	public List<ProductReferenceWsDTO> getProductReferences() 
	{
		return productReferences;
	}
	
		
	
	public void setVariantMatrix(final List<VariantMatrixElementWsDTO> variantMatrix)
	{
		this.variantMatrix = variantMatrix;
	}

		
	
	public List<VariantMatrixElementWsDTO> getVariantMatrix() 
	{
		return variantMatrix;
	}
	
		
	
	public void setPriceRange(final PriceRangeWsDTO priceRange)
	{
		this.priceRange = priceRange;
	}

		
	
	public PriceRangeWsDTO getPriceRange() 
	{
		return priceRange;
	}
	
		
	
	public void setMultidimensional(final Boolean multidimensional)
	{
		this.multidimensional = multidimensional;
	}

		
	
	public Boolean getMultidimensional() 
	{
		return multidimensional;
	}
	


}
