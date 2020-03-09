/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.textfieldconfiguratortemplateaddon.controllers;

import de.hybris.platform.catalog.enums.ConfiguratorType;
import de.hybris.platform.textfieldconfiguratortemplateaddon.forms.TextFieldConfigurationForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Map;

@Component
public class TextFieldConfigurationValidator implements Validator
{
    @Override
    public boolean supports(final Class<?> aClass)
    {
        return TextFieldConfigurationForm.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(final Object o, final Errors errors)
    {
        final TextFieldConfigurationForm form = (TextFieldConfigurationForm) o;
        if (form.getQuantity() <= 0)
        {
            errors.rejectValue("quantity", "basket.error.quantity.invalid");
        }
        final Map<ConfiguratorType, Map<String, String>> values = form.getConfigurationsKeyValueMap();
        if (values != null)
        {
            values.entrySet().stream()
                    .filter(entry -> entry.getKey() == ConfiguratorType.TEXTFIELD)
                    .flatMap(entry -> entry.getValue().entrySet().stream())
                    .forEach(property -> {
                        if (property.getValue().length() > 255)
                        {
                            errors.rejectValue("configurationsKeyValueMap['" + ConfiguratorType.TEXTFIELD.getCode()
                                    + "']['" + property.getKey() + "']", "cart.product.configuration.toolong");
                        }
                    });
        }
    }
}
