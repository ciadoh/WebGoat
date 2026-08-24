/*
 * SPDX-FileCopyrightText: Copyright © 2
 */
package org.owasp.webgoat.container;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.AttributesBuilder;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.SafeMode;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;
import org.thymeleaf.templateresolver.TemplateResolver;

import java.nio.charset.StandardCharsets;

public class AsciiDoctorTemplateResolver extends TemplateResolver {

    private static final String CLASSPATH_PREFIX = "classpath:/";
    private static final String TEMPLATE_PATH = CLASSPATH_PREFIX + "templates/";
    private static final String THEME_PATH = CLASSPATH_PREFIX + "theme.yml";
    private static final String FONTS_PATH = CLASSPATH_PREFIX + "fonts.css";

    public AsciiDoctorTemplateResolver() {
        setPrefix(TEMPLATE_PATH);
        setSuffix(".adoc");
        setTemplateMode("ASCIIDOC");

        Asciidoctor asciidoctor = Asciidoctor.Factory.create();
        AttributesBuilder attributes = AttributesBuilder.attributes();
        attributes.attribute("imagesdir", CLASSPATH_PREFIX + "images/");
        attributes.attribute("stylesheet", FONTS_PATH);
        Resource themeResource = new ClassPathResource(THEME_PATH);
        try {
            String themeContent = StreamUtils.copyToString(themeResource.getInputStream(), StandardCharsets.UTF_8);
            attributes.attribute("theme", themeContent);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load theme", e);
        }

        OptionsBuilder options = OptionsBuilder.options()
                .backend("html5")
               .safe(SafeMode.SERVER)
               .attributes(attributes.asMap());

        setAsciidoctor(asciidoctor);
        setAsciidoctorOptions(options);
    }
}