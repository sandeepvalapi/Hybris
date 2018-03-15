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
package de.hybris.platform.servicelayer.web;

import com.sap.security.core.server.csi.XSSEncoder;
import de.hybris.bootstrap.annotations.DemoTest;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertFalse;

@DemoTest
public class XSSEncoderTest {

    private String SECURE = "SECURE: ";
    private String INSECURE = "INSECURE: ";

    @Test
    public void testEncodeHTML() throws UnsupportedEncodingException
    {
        // This context is valid if you insert untrusted data between HTML or XML tags or attributes.
        // Rules and Recommendations
        // The data needs to be HTML encoded.
        // This context combines the OWASP RULE #1 and RULE #2 as described
        // at http://www.owasp.org/index.php/XSS_(Cross_Site_Scripting)_Prevention_Cheat_Sheet#RULE_.231_-_HTML_Escape_Before_Inserting_Untrusted_Data_into_HTML_Element_Content.
        // This context should not be used for special tags or attributes which are opening different contexts like
        // <script>, <style>, href, src, style or any of the event handlers like onmouseover.
        // in all examples '%s' placeholder is used for user provided input

        // example 1 - untrusted data inside HTML tag
        String htmlContent = "<p>%s</p>";
        // attacker closes tag and injects malicious code
        String untrustedValue = "</p><script>malicious code</script><p>";

        String encodedValue;
        encodedValue = XSSEncoder.encodeHTML(untrustedValue);
        System.out.println(INSECURE + String.format(htmlContent, untrustedValue));
        System.out.println(SECURE + String.format(htmlContent, encodedValue));

        // example 2 - untrusted data inside HTML attribute
        htmlContent = "<div title=\"%s\">div content</div>";

        // attacker closes attribute and tag and injects malicious code
        untrustedValue = "\"></div><script>malicious code</script><div title=\"";
        encodedValue = XSSEncoder.encodeHTML(untrustedValue);
        System.out.println(INSECURE + String.format(htmlContent, untrustedValue));
        System.out.println(SECURE + String.format(htmlContent, encodedValue));

        assertFalse(encodedValue.contains("'"));
        assertFalse(encodedValue.contains("<"));
        assertFalse(encodedValue.contains(">"));
    }

    @Test
    public void testEncodeJavaScript() throws UnsupportedEncodingException
    {
        // This context is valid when inserting data within JavaScript string literals
        // Rules and Recommendations
        // This is the most common case where escaping inside JavaScript content is needed
        // as defined with OWASP rule #3 (see http://www.owasp.org/index.php/XSS_(Cross_Site_Scripting)_Prevention_Cheat_Sheet#RULE_.233_-_JavaScript_Escape_Before_Inserting_Untrusted_Data_into_HTML_JavaScript_Data_Values).
        // in all examples '%s' placeholder is used for user provided input

        // example 3 - untrusted data inside JavaScript content
        String javascriptContent = "alert(\"%s\")";
        // attacker closes attribute and method and injects malicious code
        String untrustedValue = "\");malicious code;alert(\"";

        String encodedValue;
        encodedValue = XSSEncoder.encodeHTML(untrustedValue);
        System.out.println(INSECURE + String.format(javascriptContent, untrustedValue));
        System.out.println(SECURE + String.format(javascriptContent, encodedValue));

        assertFalse(encodedValue.contains("'"));
        assertFalse(encodedValue.contains("<"));
        assertFalse(encodedValue.contains(">"));
    }

    @Test
    public void testEncodeURL() throws UnsupportedEncodingException
    {
        // This context is valid for parameter names and values contained in the query string of a URL
        // Rules and Recommendations
        // It is defined with regards to OWASP RULE #5
        // (see https://www.owasp.org/index.php/XSS_(Cross_Site_Scripting)_Prevention_Cheat_Sheet#RULE_.235_-_URL_Escape_Before_Inserting_Untrusted_Data_into_HTML_URL_Parameter_Values).
        // There is a special case where the query is a single data value instead of using named parameters that is also covered by this context.
        // in all examples '%s' placeholder is used for user provided input

        // example 4 - untruseted data in URL parameter names and values
        String urlContent = "q=%s";
        // attacker injects malicious code inside parameter's value
        String untrustedValue = "%27]%29;;alert%28%27HACKED%27%29//&page=0";

        String encodedValue;
        encodedValue = XSSEncoder.encodeHTML(untrustedValue);
        System.out.println(INSECURE + String.format(urlContent, untrustedValue));
        System.out.println(SECURE + String.format(urlContent, encodedValue));

        assertFalse(encodedValue.contains("'"));
        assertFalse(encodedValue.contains("<"));
        assertFalse(encodedValue.contains(">"));
    }
}
