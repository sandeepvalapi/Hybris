/*
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
package de.hybris.eventtracking.services.constants;

/**
* TrackingEventJsonFields
*/
public enum TrackingEventJsonFields
{

    /** <i>Generated property</i> for <code>TrackingEventJsonFields.COMMON_EVENT_TYPE</code> property defined at extension <code>eventtrackingservices</code>. */
    COMMON_EVENT_TYPE ("eventtype")  , 
    /** <i>Generated property</i> for <code>TrackingEventJsonFields.COMMON_TIMESTAMP</code> property defined at extension <code>eventtrackingservices</code>. */
    COMMON_TIMESTAMP ("timestamp")  , 
    /** <i>Generated property</i> for <code>TrackingEventJsonFields.COMMON_CVAR_PAGE</code> property defined at extension <code>eventtrackingservices</code>. */
    COMMON_CVAR_PAGE ("cvar")  , 
    /** <i>Generated property</i> for <code>TrackingEventJsonFields.DATA</code> property defined at extension <code>eventtrackingservices</code>. */
    DATA ("data")  , 
    /** <i>Generated property</i> for <code>TrackingEventJsonFields.COMMON_URL</code> property defined at extension <code>eventtrackingservices</code>. */
    COMMON_URL ("url")  , 
    /** <i>Generated property</i> for <code>TrackingEventJsonFields.COMMON_SESSION_ID</code> property defined at extension <code>eventtrackingservices</code>. */
    COMMON_SESSION_ID ("session_id")  , 
    /** <i>Generated property</i> for <code>TrackingEventJsonFields.COMMON_USER_ID</code> property defined at extension <code>eventtrackingservices</code>. */
    COMMON_USER_ID ("user_id")  , 
    /** <i>Generated property</i> for <code>TrackingEventJsonFields.COMMON_USER_EMAIL</code> property defined at extension <code>eventtrackingservices</code>. */
    COMMON_USER_EMAIL ("user_email")  , 
    /** <i>Generated property</i> for <code>TrackingEventJsonFields.COMMON_PIWIK_ID</code> property defined at extension <code>eventtrackingservices</code>. */
    COMMON_PIWIK_ID ("_id")  , 
    /** <i>Generated property</i> for <code>TrackingEventJsonFields.REF_URL</code> property defined at extension <code>eventtrackingservices</code>. */
    REF_URL ("urlref")  , 
    /** <i>Generated property</i> for <code>TrackingEventJsonFields.IDSITE</code> property defined at extension <code>eventtrackingservices</code>. */
    IDSITE ("idsite")  , 
    /** <i>Generated property</i> for <code>TrackingEventJsonFields.SCREEN_RESOLUTION</code> property defined at extension <code>eventtrackingservices</code>. */
    SCREEN_RESOLUTION ("res")  , 
    /** <i>Generated property</i> for <code>TrackingEventJsonFields.BASE_SITE_ID</code> property defined at extension <code>eventtrackingservices</code>. */
    BASE_SITE_ID ("base_site_id")  , 
    /** <i>Generated property</i> for <code>TrackingEventJsonFields.COMMERCE_PRODUCT_SKU</code> property defined at extension <code>eventtrackingservices</code>. */
    COMMERCE_PRODUCT_SKU ("_pks")  , 
    /** <i>Generated property</i> for <code>TrackingEventJsonFields.COMMERCE_PRODUCT_NAME</code> property defined at extension <code>eventtrackingservices</code>. */
    COMMERCE_PRODUCT_NAME ("_pkn")  , 
    /** <i>Generated property</i> for <code>TrackingEventJsonFields.COMMERCE_PRODUCT_CATEGORY</code> property defined at extension <code>eventtrackingservices</code>. */
    COMMERCE_PRODUCT_CATEGORY ("_pkc")  , 
    /** <i>Generated property</i> for <code>TrackingEventJsonFields.COMMERCE_PRODUCT_PRICE</code> property defined at extension <code>eventtrackingservices</code>. */
    COMMERCE_PRODUCT_PRICE ("_pkp")  , 
    /** <i>Generated property</i> for <code>TrackingEventJsonFields.COMMERCE_ORDER_ID</code> property defined at extension <code>eventtrackingservices</code>. */
    COMMERCE_ORDER_ID ("ec_id")  , 
    /** <i>Generated property</i> for <code>TrackingEventJsonFields.COMMERCE_CART_ABANDONMENT_REASON</code> property defined at extension <code>eventtrackingservices</code>. */
    COMMERCE_CART_ABANDONMENT_REASON ("cart_abandonment_reason")  , 
    /** <i>Generated property</i> for <code>TrackingEventJsonFields.COMMERCE_CART_ITEMS</code> property defined at extension <code>eventtrackingservices</code>. */
    COMMERCE_CART_ITEMS ("ec_items")  , 
    /** <i>Generated property</i> for <code>TrackingEventJsonFields.COMMERCE_ORDER_REVENUE</code> property defined at extension <code>eventtrackingservices</code>. */
    COMMERCE_ORDER_REVENUE ("revenue")  , 
    /** <i>Generated property</i> for <code>TrackingEventJsonFields.COMMERCE_ST</code> property defined at extension <code>eventtrackingservices</code>. */
    COMMERCE_ST ("ec_st")  , 
    /** <i>Generated property</i> for <code>TrackingEventJsonFields.COMMERCE_TX</code> property defined at extension <code>eventtrackingservices</code>. */
    COMMERCE_TX ("ec_tx")  , 
    /** <i>Generated property</i> for <code>TrackingEventJsonFields.COMMERCE_DT</code> property defined at extension <code>eventtrackingservices</code>. */
    COMMERCE_DT ("ec_dt")  , 
    /** <i>Generated property</i> for <code>TrackingEventJsonFields.SEARCH_TERMS</code> property defined at extension <code>eventtrackingservices</code>. */
    SEARCH_TERMS ("search")  , 
    /** <i>Generated property</i> for <code>TrackingEventJsonFields.SEARCH_CATEGORY</code> property defined at extension <code>eventtrackingservices</code>. */
    SEARCH_CATEGORY ("search_cat")  , 
    /** <i>Generated property</i> for <code>TrackingEventJsonFields.SEARCH_COUNT</code> property defined at extension <code>eventtrackingservices</code>. */
    SEARCH_COUNT ("search_count")  , 
    /** <i>Generated property</i> for <code>TrackingEventJsonFields.SEARCH_FACETS</code> property defined at extension <code>eventtrackingservices</code>. */
    SEARCH_FACETS ("search_facets")  , 
    /** <i>Generated property</i> for <code>TrackingEventJsonFields.SEARCH_RESULTS_PAGE</code> property defined at extension <code>eventtrackingservices</code>. */
    SEARCH_RESULTS_PAGE ("search_results_page")  , 
    /** <i>Generated property</i> for <code>TrackingEventJsonFields.EVENT_CATEGORY</code> property defined at extension <code>eventtrackingservices</code>. */
    EVENT_CATEGORY ("e_c")  , 
    /** <i>Generated property</i> for <code>TrackingEventJsonFields.EVENT_ACTION</code> property defined at extension <code>eventtrackingservices</code>. */
    EVENT_ACTION ("e_a")  , 
    /** <i>Generated property</i> for <code>TrackingEventJsonFields.EVENT_NAME</code> property defined at extension <code>eventtrackingservices</code>. */
    EVENT_NAME ("e_n")  , 
    /** <i>Generated property</i> for <code>TrackingEventJsonFields.EVENT_VALUE</code> property defined at extension <code>eventtrackingservices</code>. */
    EVENT_VALUE ("e_v")  , 
    /** <i>Generated property</i> for <code>TrackingEventJsonFields.BANNER</code> property defined at extension <code>eventtrackingservices</code>. */
    BANNER ("banner")  , 
    /** <i>Generated property</i> for <code>TrackingEventJsonFields.BANNER_ID</code> property defined at extension <code>eventtrackingservices</code>. */
    BANNER_ID ("bannerid")  , 
    /** <i>Generated property</i> for <code>TrackingEventJsonFields.CONSENT_REFERENCE</code> property defined at extension <code>yprofileeventtrackingws</code>. */
    CONSENT_REFERENCE ("consent_reference")  , 
    /** <i>Generated property</i> for <code>TrackingEventJsonFields.USER_AGENT</code> property defined at extension <code>yprofileeventtrackingws</code>. */
    USER_AGENT ("user_agent")  , 
    /** <i>Generated property</i> for <code>TrackingEventJsonFields.ACCEPT</code> property defined at extension <code>yprofileeventtrackingws</code>. */
    ACCEPT ("accept")  , 
    /** <i>Generated property</i> for <code>TrackingEventJsonFields.ACCEPT_LANGUAGE</code> property defined at extension <code>yprofileeventtrackingws</code>. */
    ACCEPT_LANGUAGE ("accept_language")  , 
    /** <i>Generated property</i> for <code>TrackingEventJsonFields.REFERER</code> property defined at extension <code>yprofileeventtrackingws</code>. */
    REFERER ("referer")  ; 

    private String key;

    private TrackingEventJsonFields(final String key)
    {
        this.key = key;
    }

    public String getKey()
    {
        return key;
    }

}