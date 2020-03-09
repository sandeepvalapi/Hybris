/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorstorefrontcommons.forms;


public class RestoreSaveCartForm
{
    private boolean preventSaveActiveCart;
    private boolean keepRestoredCart;
    private String cartName;

    public boolean isKeepRestoredCart() {
        return keepRestoredCart;
    }

    public void setKeepRestoredCart(boolean keepRestoredCart) {
        this.keepRestoredCart = keepRestoredCart;
    }

    public boolean isPreventSaveActiveCart() {
        return preventSaveActiveCart;
    }

    public void setPreventSaveActiveCart(boolean preventSaveActiveCart) {
        this.preventSaveActiveCart = preventSaveActiveCart;
    }

    public String getCartName() {
        return cartName;
    }

    public void setCartName(String cartName) {
        this.cartName = cartName;
    }


}
