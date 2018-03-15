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
package de.hybris.platform.catalog;

import de.hybris.bootstrap.annotations.DemoTest;
import de.hybris.platform.catalog.data.CatalogVersionOverview;
import de.hybris.platform.catalog.exceptions.CatalogAwareObjectResolvingException;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * This test class tests {@link CatalogTypeService}. It also demonstrates the most common use cases.
 */
@DemoTest
public class CatalogTypeServiceTest extends ServicelayerTransactionalTest
{
	protected final static String TEST_CATALOG_1 = "testCatalog1";
	protected final static String TEST_CATALOG_2 = "testCatalog2";

	protected final static String WINTER_VERSION = "Winter";
	protected final static String SPRING_VERSION = "Spring";

	protected final static String TEST_USER = "testUser1";

	private final static String TYPE_A = "TYPE_A";
	private final static String TYPE_B = "TYPE_B";
	private final static String TYPE_C = "TYPE_C";
	private final static String TYPE_X = "TYPE_X";


	@Resource
	private CatalogTypeService catalogTypeService;
	@Resource
	private CatalogVersionService catalogVersionService;
	@Resource
	private FlexibleSearchService flexibleSearchService;
	@Resource
	private ModelService modelService;
	@Resource
	private TypeService typeService;

	private CatalogVersionModel test1Spring = null;
	private CatalogVersionModel test1Winter = null;
	private CatalogVersionModel test2Spring = null;

	private ComposedTypeModel typeA = null;
	private ComposedTypeModel typeB = null;
	private ComposedTypeModel typeC = null;
	private ComposedTypeModel typeX = null;

	private ComposedTypeModel variantProductType = null;
	private ComposedTypeModel productType = null;
	private AttributeDescriptorModel uniqueAttributeA;
	private AttributeDescriptorModel uniqueAttributeC;
	private AttributeDescriptorModel uniqueProductAttributeForC;

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		importCsv("/platformservices/test/catalog/testdata_catalogVersion.csv", "UTF-8");

		//DAO fetching already tested in CatalogVersionDao. The following models are needed as test data for the following test cases.
		test1Spring = catalogVersionService.getCatalogVersion(TEST_CATALOG_1, SPRING_VERSION);
		Assert.assertEquals("Unexpected catalog ID", TEST_CATALOG_1, test1Spring.getCatalog().getId());
		Assert.assertEquals("Unexpected catalog Version", SPRING_VERSION, test1Spring.getVersion());

		test1Winter = catalogVersionService.getCatalogVersion(TEST_CATALOG_1, WINTER_VERSION);
		Assert.assertEquals("Unexpected catalog ID", TEST_CATALOG_1, test1Winter.getCatalog().getId());
		Assert.assertEquals("Unexpected catalog Version", WINTER_VERSION, test1Winter.getVersion());

		test2Spring = catalogVersionService.getCatalogVersion(TEST_CATALOG_2, SPRING_VERSION);
		Assert.assertEquals("Unexpected catalog ID", TEST_CATALOG_2, test2Spring.getCatalog().getId());
		Assert.assertEquals("Unexpected catalog Version", SPRING_VERSION, test2Spring.getVersion());

		catalogVersionService.setSessionCatalogVersions(Collections.EMPTY_SET);
		productType = typeService.getComposedTypeForCode("Product");
		variantProductType = typeService.getComposedTypeForCode("VariantProduct");

		//For the purpose of this test few new types were created in the initialization code
		//
		//typeA (unique attribute ('unique_A') and catalogVersion attribute ('catalogVersion_A') defined)
		//  |
		//  *--typeB (no additional unique attributes introduced by the tybeB)
		//       |
		//       *--typeC (additional unique attribute ('unique_C'), + ('unique_A') from inheritance)
		//
		// typeX (not catalog version capable)


		typeA = createType(TYPE_A, typeService.getComposedTypeForCode(ItemModel._TYPECODE));

		uniqueAttributeA = createAttributeDescriptor(typeA, typeService.getTypeForCode("java.lang.String"), "unique_A", true);

		final AttributeDescriptorModel cvA = createAttributeDescriptor(typeA,
				typeService.getComposedTypeForCode(CatalogVersionModel._TYPECODE), "catalogVersion_A", false);

		typeA.setUniqueKeyAttributes(Collections.singletonList(uniqueAttributeA));
		typeA.setCatalogVersionAttribute(cvA);

		typeB = createType(TYPE_B, typeA);
		typeC = createType(TYPE_C, typeB);

		uniqueAttributeC = createAttributeDescriptor(typeC, typeService.getTypeForCode("java.lang.String"), "unique_C", true);

		typeC.setUniqueKeyAttributes(Arrays.asList(uniqueAttributeA, uniqueAttributeC));

		typeX = createType(TYPE_X, typeService.getComposedTypeForCode(ItemModel._TYPECODE));

		uniqueProductAttributeForC = createAttributeDescriptor(typeC, productType, "unique_product", true);

		modelService.saveAll();
	}

	/**
	 * This method tests {@link CatalogTypeService#getCatalogVersionItem(CatalogVersionModel, String, java.util.Map)} for
	 * corner cases of <code>version<code> argument.
	 *
	 * @throws CatalogAwareObjectResolvingException
	 */
	@Test
	public void testGetCatalogVersionItemCornerCasesForCatalogVersion() throws CatalogAwareObjectResolvingException
	{
		//we know there is 'Product' catalog version aware type. And we know that 'code' is a unique attribute.

		// try null target catalog version
		assertCornerCase(null, "Product", Collections.<String, Object> singletonMap("code", "shoes"),
				IllegalArgumentException.class, "Should have failed with IllegalArgumentException");

		//now lets try for catalog version which is not persisted
		final CatalogModel targetCatalog = new CatalogModel();
		targetCatalog.setId(TEST_CATALOG_1);
		final CatalogVersionModel targetCatalogVersion = new CatalogVersionModel();
		targetCatalogVersion.setCatalog(targetCatalog);
		targetCatalogVersion.setVersion(SPRING_VERSION);

		//check previous API
		Assert.assertNull(
				"As CatalogVersion is not persisted, the getCatalogVersionItem for new catalog version should return null",
				catalogTypeService.getCatalogVersionItem(targetCatalogVersion, "Product",
						Collections.<String, Object> singletonMap("code", "shoes")));

		//check current implementation with checked exception
		assertCornerCase(targetCatalogVersion, "Product", Collections.<String, Object> singletonMap("code", "shoes"),
				CatalogAwareObjectResolvingException.class, "Should have failed with CatalogItemResolvingException");

		//now lets try for catalog version which was removed
		final CategoryModel tmplCategory = new CategoryModel();
		tmplCategory.setCatalogVersion(test1Spring);
		final ProductModel tmplProduct = new ProductModel();
		tmplProduct.setCatalogVersion(test1Spring);
		final Collection<ProductModel> products2remove = flexibleSearchService.getModelsByExample(tmplProduct);
		modelService.removeAll(products2remove);
		final Collection<CategoryModel> category2remove = flexibleSearchService.getModelsByExample(tmplCategory);
		modelService.removeAll(category2remove);
		modelService.remove(test1Spring);

		final ProductModel productFromRemovedCV = (ProductModel) catalogTypeService.getCatalogVersionAwareModel(test1Spring,
				"Product", Collections.<String, Object> singletonMap("code", "shoes"));
		Assert.assertNull("After removal of catalog version, the method should return null", productFromRemovedCV);
	}

	/**
	 * This method tests {@link CatalogTypeService#getCatalogVersionItem(CatalogVersionModel, String, java.util.Map)} for
	 * corner cases of <code>typeCode<code> argument.
	 *
	 * @throws CatalogAwareObjectResolvingException
	 */
	@Test
	public void testGetCatalogVersionItemCornerCasesForType() throws CatalogAwareObjectResolvingException
	{
		//We know there is TEST1:spring catalog version

		//Let's try null typeCode
		assertCornerCase(test1Spring, null, Collections.<String, Object> singletonMap("code", "shoes"),
				IllegalArgumentException.class, "Should have failed with IllegalArgumentException for null typecode");

		//Let's try non catalog version aware type.
		assertCornerCase(test1Spring, "Principal", Collections.<String, Object> singletonMap("code", "shoes"),
				IllegalArgumentException.class, "Should have failed with IllegalArgumentException for non catalog version aware type");

		//Let's try invalid typeCode
		assertCornerCase(test1Spring, "any string", Collections.<String, Object> singletonMap("code", "shoes"),
				UnknownIdentifierException.class, "Should have failed with UnknownIdentifierException for unknown typecode");
	}

	/**
	 * This method tests {@link CatalogTypeService#getCatalogVersionItem(CatalogVersionModel, String, java.util.Map)} for
	 * corner cases of <code>uniqueKeyValues<code> argument.
	 *
	 * @throws CatalogAwareObjectResolvingException
	 */
	@Test
	public void testGetCatalogVersionItemCornerCasesForUniqueKeyValues() throws CatalogAwareObjectResolvingException
	{
		//We know there is TEST1:spring catalog version

		//Let's try null unique attributes map
		assertCornerCase(test1Spring, "Product", null, IllegalArgumentException.class,
				"Should have failed with IllegalArgumentException for null attributes map");

		//Let's try empty unique attributes map - so it doesn't cover all unique attributes of the Product type ([code]).
		assertCornerCase(test1Spring, "Product", Collections.EMPTY_MAP, IllegalArgumentException.class,
				"Should have failed with IllegalArgumentException for wrong attributes maps");

		//Let's try the ugly case - a non null map with null content - so it doesn't cover all unique attributes of the Product type ([code]).
		assertCornerCase(test1Spring, "Product", Collections.<String, Object> singletonMap(null, null),
				IllegalArgumentException.class, "Should have failed with IllegalArgumentException for wrong attributes maps");

		//Let's try the map with invalid parameter - so it doesn't cover all unique attributes of the Product type ([code]).
		assertCornerCase(test1Spring, "Product", Collections.<String, Object> singletonMap("invalid", null),
				IllegalArgumentException.class, "Should have failed with IllegalArgumentException");

		//Let's try the map with one valid parameter, which doesn't cover all unique attributes of the TypeC type ([unique_A], [unique_C]).
		//lets make the TYPE_C catalog version aware first:
		typeC.setCatalogItemType(Boolean.TRUE);
		modelService.save(typeC);
		assertCornerCase(test1Spring, TYPE_C, Collections.<String, Object> singletonMap("unique_A", null),
				IllegalArgumentException.class,
				"Should have failed with IllegalArgumentException , not all unique attributes present in the argument map");

		//Let's create a unique attribute for TYPE_C, which is model type - Product

		//adding unique Product type attribute for typeC
		typeC.setUniqueKeyAttributes(Arrays.asList(uniqueAttributeA, uniqueAttributeC, uniqueProductAttributeForC));
		modelService.saveAll();

		final ProductModel productForC = new ProductModel();

		final Map<String, Object> uniqueAttributesForC = new HashMap<String, Object>(3);
		uniqueAttributesForC.put("unique_A", "A");
		uniqueAttributesForC.put("unique_C", "C");
		//non persisted model
		uniqueAttributesForC.put("unique_product", productForC);
		assertCornerCase(test1Spring, TYPE_C, uniqueAttributesForC, CatalogAwareObjectResolvingException.class,
				"Should have failed with CatalogItemResolvingException for non persisted attribute model");

		//this should be fine as we support null values.
		final ProductModel nullProduct = (ProductModel) catalogTypeService.getCatalogVersionAwareModel(test1Spring, "Product",
				Collections.<String, Object> singletonMap("code", null));
		Assert.assertNull("No such product expected", nullProduct);

	}

	@Test
	public void testGetCatalogVersionItem() throws CatalogAwareObjectResolvingException
	{

		ProductModel product = (ProductModel) catalogTypeService.getCatalogVersionAwareModel(test1Winter, "Product",
				Collections.<String, Object> singletonMap("code", "shoes"));
		Assert.assertEquals("shoes", product.getCode());
		Assert.assertEquals(test1Winter, product.getCatalogVersion());

		product = (ProductModel) catalogTypeService.getCatalogVersionAwareModel(test1Spring, "Product",
				Collections.<String, Object> singletonMap("code", "shoes"));
		Assert.assertEquals("shoes", product.getCode());
		Assert.assertEquals(test1Spring, product.getCatalogVersion());

		CategoryModel category = (CategoryModel) catalogTypeService.getCatalogVersionAwareModel(test1Winter, "Category",
				Collections.<String, Object> singletonMap("code", "shoes"));
		Assert.assertEquals("shoes", category.getCode());
		Assert.assertEquals(test1Winter, category.getCatalogVersion());

		category = (CategoryModel) catalogTypeService.getCatalogVersionAwareModel(test1Spring, "Category",
				Collections.<String, Object> singletonMap("code", "shoes"));
		Assert.assertEquals("shoes", category.getCode());
		Assert.assertEquals(test1Spring, category.getCatalogVersion());
	}

	/**
	 * Tests {@link CatalogTypeService#getAllCatalogVersionAwareTypes(boolean)}
	 */
	@Test
	public void testGetAllCatalogItemTypes()
	{
		//For the purpose of this test few new types were created in the initialization code
		//
		//typeA (unique attribute ('unique_A') and catalogVersion attribute ('catalogVersion_A') defined)
		//  |
		//  *--typeB (no additional unique attributes introduced by the tybeB)
		//       |
		//       *--typeC (additional unique attribute ('unique_C'), + ('unique_A') from inheritance)
		//
		// typeX (not catalog version capable)


		//service allows to fetch all composed types that are catalog version aware.
		Collection<ComposedTypeModel> catalogAwareTypes = catalogTypeService.getAllCatalogVersionAwareTypes(false);

		//you may want to return only root types... Root type is such that its super-type is not catalog version capable or it adds a new attribute for the item uniqueness.
		Collection<ComposedTypeModel> catalogAwareRootTypes = catalogTypeService.getAllCatalogVersionAwareTypes(true);
		//please note the argument: false means that all types (also sub-types) are returned form the method.

		//common types can be found there..
		Assert.assertTrue("'product' should not be among all catalog version aware types", catalogAwareTypes.contains(productType));
		Assert.assertTrue("'variantProduct' should be among all catalog version aware types",
				catalogAwareTypes.contains(variantProductType));

		//when fetching only root types, variant product type should not be found. Its super-type: Product is catalog version aware and variant itself do not introduce any new unique attribute..
		Assert.assertTrue("'product' should not be among root catalog version aware types",
				catalogAwareRootTypes.contains(productType));
		Assert.assertFalse("'variantProduct' should be among root catalog version aware types",
				catalogAwareRootTypes.contains(variantProductType));

		//but what about our 'new' types? Since none of them was initialized as catalog version capable they cannot be found in the collection: 
		Assert.assertFalse("'typeA' should not be among catalog version aware types", catalogAwareTypes.contains(typeA));
		Assert.assertFalse("'typeB' should not be among catalog version aware types", catalogAwareTypes.contains(typeB));
		Assert.assertFalse("'typeC' should not be among catalog version aware types", catalogAwareTypes.contains(typeC));
		Assert.assertFalse("'typeX' should not be among catalog version aware types", catalogAwareTypes.contains(typeX));

		//Let's set the flag on our main type :  'typeA'
		typeA.setCatalogItemType(Boolean.TRUE);
		modelService.saveAll();
		//now typeB.getSuperType() changed to catalog aware
		modelService.refresh(typeB);

		catalogAwareTypes = catalogTypeService.getAllCatalogVersionAwareTypes(false);
		catalogAwareRootTypes = catalogTypeService.getAllCatalogVersionAwareTypes(true);

		//how are the 'new' types changed?
		//TypeA has necessary attributes :unique_A, catalogVersion_A  and it's flag is TRUE.
		Assert.assertTrue("'typeA' should be among catalog version aware (root) types", catalogAwareTypes.contains(typeA)
				&& catalogAwareRootTypes.contains(typeA));
		//'typeB' has the required attributes inherited from the 'typeA', but has flag=FALSE..
		Assert.assertFalse("'typeB' should not be among catalog version aware types", catalogAwareTypes.contains(typeB));
		//the same here...
		Assert.assertFalse("'typeC' should not be among catalog version aware types", catalogAwareTypes.contains(typeC));

		//Let's set the flag for : 'typeB'
		typeB.setCatalogItemType(Boolean.TRUE);
		modelService.saveAll();
		modelService.refresh(typeC);

		catalogAwareTypes = catalogTypeService.getAllCatalogVersionAwareTypes(false);
		catalogAwareRootTypes = catalogTypeService.getAllCatalogVersionAwareTypes(true);

		//Nothing changed for typeA
		Assert.assertTrue("'typeA' should be among catalog version aware (root) types", catalogAwareTypes.contains(typeA)
				&& catalogAwareRootTypes.contains(typeA));
		//'typeB' has the required attributes inherited from the 'typeA' and additionally flag=TRUE.. BUT, it's not a 'root catalog version type', as is's unique attributes are the same as from the supertype (typeA) and typeA is also catalog aware type. 
		Assert.assertTrue("'typeB' should be among catalog version aware types, but not root types",
				catalogAwareTypes.contains(typeB) && !catalogAwareRootTypes.contains(typeB));

		//Let's set the flag for : 'typeB' and 'typeC', but reset for 'typeA'
		typeA.setCatalogItemType(Boolean.FALSE);
		typeB.setCatalogItemType(Boolean.TRUE);
		typeC.setCatalogItemType(Boolean.TRUE);
		modelService.saveAll();
		modelService.refresh(typeA);
		modelService.refresh(typeB);
		modelService.refresh(typeC);

		final ComposedTypeModel tempType = typeService.getComposedTypeForCode(TYPE_B);
		Assert.assertFalse("Type A schoud not be a vatalog version aware",
				catalogTypeService.isCatalogVersionAwareType(tempType.getSuperType()));

		catalogAwareTypes = catalogTypeService.getAllCatalogVersionAwareTypes(false);
		catalogAwareRootTypes = catalogTypeService.getAllCatalogVersionAwareTypes(true);

		//super type 'typeA' has the flag = false
		Assert.assertTrue("'typeA' should not be among catalog version aware (root) types", !catalogAwareTypes.contains(typeA)
				&& !catalogAwareRootTypes.contains(typeA));
		//'typeB' has flag=TRUE, and the required attributes inherited from the 'typeA'. Now, as TypeA is no longer 'catalog version item type', it should be recognized as 'root' tpe as well.
		Assert.assertTrue("'typeB' should  be among catalog version aware (root) types", catalogAwareTypes.contains(typeB)
				&& catalogAwareRootTypes.contains(typeB));
		//'typeC' has flag=True, and besides the unique_A inherited from typeA it introduces unique attribute 'unique_C', hence - a root type. New constraints on item uniqueness.
		Assert.assertTrue("'typeC' should  be among catalog version aware (root) types", catalogAwareTypes.contains(typeC)
				&& catalogAwareRootTypes.contains(typeC));

		//Let's set the flag for all types.. also typeX'
		typeA.setCatalogItemType(Boolean.TRUE);
		typeB.setCatalogItemType(Boolean.TRUE);
		typeC.setCatalogItemType(Boolean.TRUE);
		typeX.setCatalogItemType(Boolean.TRUE);
		modelService.saveAll();
		modelService.refresh(typeA);
		modelService.refresh(typeB);
		modelService.refresh(typeC);
		modelService.refresh(typeX);

		catalogAwareTypes = catalogTypeService.getAllCatalogVersionAwareTypes(false);
		catalogAwareRootTypes = catalogTypeService.getAllCatalogVersionAwareTypes(true);

		//typeX doesn't have unique attributes.. but it still returned because of the TRUE flag.
		Assert.assertTrue("'typeX' should be among catalog version aware types", catalogAwareTypes.contains(typeX));

		Assert.assertTrue("'typeA' should be among catalog version aware (root) types", catalogAwareTypes.contains(typeA)
				&& catalogAwareRootTypes.contains(typeA));
		//'typeB' doesn't add anything to the definition of item uniqueness provided by 'typeA' -> not a 'root' type.
		Assert.assertTrue("'typeB' should  be among catalog version types, BUT not root types", catalogAwareTypes.contains(typeB)
				&& !catalogAwareRootTypes.contains(typeB));
		//'typeC' provides additional 'unique attribute'. -> a 'root' type.
		Assert.assertTrue("'typeC' should be among catalog version aware (root) types", catalogAwareTypes.contains(typeC)
				&& catalogAwareRootTypes.contains(typeC));
	}

	/**
	 * Tests and demonstrates {@link CatalogTypeService#getCatalogVersionForCatalogVersionAwareModel(ItemModel)}
	 */
	@Test
	public void testGetCatalogVersion()
	{
		boolean success = false;
		try
		{
			catalogTypeService.getCatalogVersionForCatalogVersionAwareModel(null);
			Assert.fail("Should have failed with IllegalArgumentException with null item argument");
		}
		catch (final IllegalArgumentException e)
		{
			//expected
			success = true;
		}
		Assert.assertTrue("Should have failed with IllegalArgumentException with null item argument", success);
		final ProductModel tmplProduct = new ProductModel();
		tmplProduct.setCatalogVersion(test1Spring);
		tmplProduct.setCode("shoes");

		final ProductModel springShoes = flexibleSearchService.getModelByExample(tmplProduct);

		Assert.assertEquals("Unexpected catalog version", test1Spring,
				catalogTypeService.getCatalogVersionForCatalogVersionAwareModel(springShoes));

		//test after model modification
		springShoes.setCatalogVersion(test2Spring);
		modelService.save(springShoes);

		Assert.assertEquals("Unexpected catalog version", test2Spring,
				catalogTypeService.getCatalogVersionForCatalogVersionAwareModel(springShoes));
	}


	@Test(expected = IllegalArgumentException.class)
	public void testIsCatalogItemTypeNullType()
	{
		catalogTypeService.isCatalogVersionAwareType((ComposedTypeModel) null);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testIsCatalogVersionAwareTypeByNullString()
	{
		catalogTypeService.isCatalogVersionAwareType((String) null);
	}

	/**
	 * According to {@link TypeService#getComposedTypeForCode(String)} we should get here
	 * {@link UnknownIdentifierException}. PLA-10519
	 */
	@Test(expected = UnknownIdentifierException.class)
	public void testIsCatalogVersionAwareTypeByUnknownString()
	{
		catalogTypeService.isCatalogVersionAwareType("unknown type");
	}

	/**
	 * Tests and demonstrates usage of {@link CatalogTypeService#isCatalogVersionAwareType(String)}.
	 */
	@Test
	public void testIsCatalogVersionAwareTypeByValidString()
	{
		Assert.assertTrue("Product must be recognized as catalogItem - type.",
				catalogTypeService.isCatalogVersionAwareType("Product"));
		//Let's check typeA
		Assert.assertFalse("TypeA must not be recognized as catalogItem - type.",
				catalogTypeService.isCatalogVersionAwareType(TYPE_A));
		//.. now setting the catalogItemType flag
		typeA.setCatalogItemType(Boolean.TRUE);
		modelService.save(typeA);
		Assert.assertTrue("TypeA must be recognized as catalogItem - type.", catalogTypeService.isCatalogVersionAwareType(TYPE_A));

		//Let's check typeX
		Assert.assertFalse("TypeX must not be recognized as catalogItem - type.",
				catalogTypeService.isCatalogVersionAwareType(TYPE_X));
		//.. now setting the catalogItemType flag
		typeX.setCatalogItemType(Boolean.TRUE);
		modelService.save(typeX);
		Assert.assertFalse("TypeX still must not be recognized as catalogItem - type... Doesn't have required attributes",
				catalogTypeService.isCatalogVersionAwareType(TYPE_X));
	}


	/**
	 * Tests {@link CatalogTypeService#isCatalogVersionAwareType(ComposedTypeModel)}
	 */
	@Test
	public void testIsCatalogItemType()
	{

		//For the purpose of this test few new types were created in the init code
		//
		//typeA (unique attribute ('uid') and catalogVersion attribute ('catalogVersion') defined)
		//  |
		//  *--typeB
		//       |
		//       *--typeC
		//
		// typeX (not catalog version capable)
		//

		//Initially, all 4 types should not be recognized as CatalogItemTypes, due to the ComposedType.catalogItemType flag:
		Assert.assertFalse("Initialized to false", catalogTypeService.isCatalogVersionAwareType(typeA));
		Assert.assertFalse("Initialized to false", catalogTypeService.isCatalogVersionAwareType(typeB));
		Assert.assertFalse("Initialized to false", catalogTypeService.isCatalogVersionAwareType(typeC));
		Assert.assertFalse("Initialized to false", catalogTypeService.isCatalogVersionAwareType(typeX));

		//now setting the flag to TRUE for the 'typeA' and 'typeB'
		typeA.setCatalogItemType(Boolean.TRUE);
		typeB.setCatalogItemType(Boolean.TRUE);
		modelService.saveAll();

		//The 2 types should now be recognized as CatalogItemTypes, the ComposedType.catalogItemType flag is true AND super type has required 'catalogVersion' and unique attributed modifiers.
		//The 'typeB' has also the required attributes by means of inheritance
		Assert.assertTrue("Super-type should be catalog version capable", catalogTypeService.isCatalogVersionAwareType(typeA));
		Assert.assertTrue("Sub-type should be catalog version capable", catalogTypeService.isCatalogVersionAwareType(typeB));

		//the flag modification should not have affected remaining types:
		Assert.assertFalse("Initialized to false", catalogTypeService.isCatalogVersionAwareType(typeC));
		Assert.assertFalse("Initialized to false", catalogTypeService.isCatalogVersionAwareType(typeX));

		//Now, ..if I set the flag on the 'typeX' which is independent on 'typeA' (hence: not having proper attributes for "unique id" and "catalogVersion") 
		// the type should still be recognized as not capable.
		typeX.setCatalogItemType(Boolean.TRUE);
		modelService.saveAll();

		Assert.assertFalse("'typeX' should not be catalog version aware as it doesnt have required attributes",
				catalogTypeService.isCatalogVersionAwareType(typeX));

		//But for 'typeC' which get required attributes from inheritance, setting the flag changes the type to 'Catalog version aware'.
		typeC.setCatalogItemType(Boolean.TRUE);
		modelService.saveAll();
		Assert.assertTrue("'typeC' should now be catalog version aware as it doesnt have required attributes",
				catalogTypeService.isCatalogVersionAwareType(typeC));

		//If I reset flags on the 'typeA', 'typeB' the type being on the bottom if inheritance tree should still remain catalog version capabilities. 
		typeA.setCatalogItemType(Boolean.FALSE);
		typeB.setCatalogItemType(Boolean.FALSE);
		modelService.saveAll();

		Assert.assertFalse("'typeA' should not be catalog version aware anymore",
				catalogTypeService.isCatalogVersionAwareType(typeA));
		Assert.assertFalse("'typeB' should not be catalog version aware anymore",
				catalogTypeService.isCatalogVersionAwareType(typeB));
		Assert.assertTrue("'typeC' should still be catalog version aware", catalogTypeService.isCatalogVersionAwareType(typeC));

		// Let's examine some common types: Product, VariantProduct.
		// The former was  defined as catalogVersion aware type...
		Assert.assertTrue("Product type should be recognized as catalog version aware",
				catalogTypeService.isCatalogVersionAwareType(productType));
		//.. the later inherits the capability.
		Assert.assertTrue("Variant product type should be recognized as catalog version aware",
				catalogTypeService.isCatalogVersionAwareType(variantProductType));
	}

	/**
	 * Tests and demonstrates
	 * {@link CatalogTypeService#isCatalogVersionAwareModel(de.hybris.platform.core.model.ItemModel)}
	 */
	@Test
	public void testIsCatalogItem()
	{
		boolean success = false;
		try
		{
			catalogTypeService.isCatalogVersionAwareModel(null);
			Assert.fail("IllegalArgumentException expected for null model");
		}
		catch (final IllegalArgumentException e)
		{
			success = true;
		}
		Assert.assertTrue("IllegalArgumentException expected for null model", success);

		//Lest try on a concrete model.. i.e categoryModel form the test data
		final CategoryModel templateCategory = new CategoryModel();
		templateCategory.setCode("shoes");
		templateCategory.setCatalogVersion(test1Winter);
		final CategoryModel winterShoes = flexibleSearchService.getModelByExample(templateCategory);
		Assert.assertTrue("test category is a catalog version aware model",
				catalogTypeService.isCatalogVersionAwareModel(winterShoes));

		//other type.. users
		final UserModel tmplUser = new UserModel();
		tmplUser.setUid(TEST_USER);

		final UserModel testUser = flexibleSearchService.getModelByExample(tmplUser);
		Assert.assertFalse("test user should  not be catalog version aware model",
				catalogTypeService.isCatalogVersionAwareModel(testUser));

	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetCatalogVersionContainerAttributeForNullTypeCode()
	{
		catalogTypeService.getCatalogVersionContainerAttribute(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetCatalogVersionContainerAttributeForBadType()
	{
		catalogTypeService.getCatalogVersionContainerAttribute("User");
	}

	@Test
	public void testGetCatalogVersionContainerAttribute()
	{
		//TypeC is not marked 'catalogVersionItem' with the flag, but you can use this method to check the catalogVersionContainer.
		Assert.assertEquals("Unexpected catalog version container attribute qualifier", "catalogVersion_A",
				catalogTypeService.getCatalogVersionContainerAttribute(TYPE_C));

		//Product is marked 'catalogVersionItem' with the flag
		Assert.assertEquals("Unexpected catalog version container attribute qualifier", "catalogVersion",
				catalogTypeService.getCatalogVersionContainerAttribute("Product"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetCatalogVersionUniqueAttributesModifiersNullType()
	{
		catalogTypeService.getCatalogVersionUniqueKeyAttribute(null);
	}

	@Test
	public void testGetCatalogVersionUniqueAttributesModifiers()
	{

		final Set<String> typeA_UIDS = catalogTypeService.getCatalogVersionUniqueKeyAttribute(TYPE_A);
		Assert.assertTrue("Type A unique attributes is wrong", typeA_UIDS.contains("unique_A"));

		final Set<String> typeB_UIDS = catalogTypeService.getCatalogVersionUniqueKeyAttribute(TYPE_B);
		Assert.assertTrue("Type B unique attributes is wrong", typeB_UIDS.contains("unique_A"));

		//TypeC is not marked 'catalogVersionItem' with the flag, but you can use this method to check the catalogVersionContainer.
		final Set<String> typeC_UIDS = catalogTypeService.getCatalogVersionUniqueKeyAttribute(TYPE_C);
		Assert.assertTrue("Type C unique attributes is wrong", typeC_UIDS.contains("unique_A"));
		Assert.assertTrue("Type C unique attributes is wrong", typeC_UIDS.contains("unique_C"));

		//Product is marked 'catalogVersionItem' with the flag
		final Set<String> product_UIDS = catalogTypeService.getCatalogVersionUniqueKeyAttribute("Product");
		Assert.assertTrue("Products unique attributes is wrong", product_UIDS.contains("code"));

		//VariantProduct - doesnt introduce attribute by itself, but it's a Product's sub type
		final Set<String> variantProduct_UIDS = catalogTypeService.getCatalogVersionUniqueKeyAttribute("VariantProduct");
		Assert.assertTrue("VariantProduct' s unique attributes is wrong", variantProduct_UIDS.contains("code"));

		Assert.assertTrue("Users unique attributes is wrong - expected empty", catalogTypeService
				.getCatalogVersionUniqueKeyAttribute("User").isEmpty());

	}

	@Test
	public void testGetCatalogVersionOverview()
	{
		final ProductModel tmplProduct = new ProductModel();
		tmplProduct.setCatalogVersion(test1Spring);
		final Collection<ProductModel> productsSpring2 = flexibleSearchService.getModelsByExample(tmplProduct);
		Assert.assertTrue(CollectionUtils.isNotEmpty(productsSpring2));
		productsSpring2.stream().forEach(e -> e.setCatalogVersion(test2Spring));
		modelService.saveAll(productsSpring2);

		final long expectedProductsAmount = productsSpring2.size();

		CatalogVersionOverview catalogVersionOverview = catalogTypeService.getCatalogVersionOverview(test2Spring);
		Assert.assertNotNull(catalogVersionOverview);
		Assert.assertEquals(test2Spring, catalogVersionOverview.getCatalogVersion());
		final Map<ComposedTypeModel, Long> types = catalogVersionOverview.getTypeAmounts();

		final ComposedTypeModel productType = typeService.getComposedTypeForClass(ProductModel.class);
		Assert.assertTrue(types.containsKey(productType));
		Assert.assertEquals(types.get(productType), Long.valueOf(expectedProductsAmount));

		modelService.removeAll(productsSpring2);
		catalogVersionOverview = catalogTypeService.getCatalogVersionOverview(test2Spring);
		Assert.assertNotNull(catalogVersionOverview);
		Assert.assertEquals(test2Spring, catalogVersionOverview.getCatalogVersion());
		Assert.assertFalse(catalogVersionOverview.getTypeAmounts().containsKey(productType));
	}

	private AttributeDescriptorModel createAttributeDescriptor(final ComposedTypeModel enclosingType,
			final TypeModel attributeType, final String qualifier, final boolean unique)
	{
		final AttributeDescriptorModel attribute = modelService.create(AttributeDescriptorModel.class);
		attribute.setAttributeType(attributeType);
		attribute.setName(qualifier);
		attribute.setEnclosingType(enclosingType);
		attribute.setQualifier(qualifier);
		attribute.setPartOf(Boolean.FALSE);
		attribute.setGenerate(Boolean.FALSE);
		if (unique)
		{
			attribute.setUnique(Boolean.TRUE);
		}
		return attribute;
	}

	private ComposedTypeModel createType(final String code, final ComposedTypeModel superType)
	{
		final ComposedTypeModel newType = modelService.create(ComposedTypeModel.class);
		newType.setCatalogItemType(Boolean.FALSE);
		newType.setCode(code);
		newType.setSuperType(superType);
		newType.setGenerate(Boolean.FALSE);
		newType.setSingleton(Boolean.FALSE);
		return newType;
	}

	private void assertCornerCase(final CatalogVersionModel catalogVersionModel, final String typeCode,
			final Map<String, Object> params, final Class expectedException, final String msg)
	{
		boolean success = false;
		try
		{
			catalogTypeService.getCatalogVersionAwareModel(catalogVersionModel, typeCode, params);
			Assert.fail(msg);
		}
		catch (final Exception e)
		{
			success = expectedException.isInstance(e);
		}
		Assert.assertTrue(msg, success);
	}

}
