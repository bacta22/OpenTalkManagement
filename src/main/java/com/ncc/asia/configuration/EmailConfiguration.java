package com.ncc.asia.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.Properties;

@Configuration
public class EmailConfiguration {

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("bactanuce@gmail.com");
        mailSender.setPassword("krbwlykywwkhomfl");

        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol","smtp");
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.debug","true");
        return mailSender;
    }

    // create the factory method for the Thymeleaf engine. We'll need to tell the engine which
    // TemplateResolver we've chosen, which we can inject via a parameter to the bean factory method
    @Bean
    public SpringTemplateEngine thymeleafTemplateEngine (ITemplateResolver templateResolver) {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        templateEngine.setTemplateEngineMessageSource(emailMessageSource());
        return templateEngine;
    }


    // Provide a template resolver to locate the template files directory.
    @Bean
    @Primary
    public ITemplateResolver thymeleafTemplateResolver () {
        ClassLoaderTemplateResolver templateResolver
                = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("mail-templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCharacterEncoding("UTF-8");
        return templateResolver;
    }

    // In order to manage translations with Thymeleaf, we can specify a MessageSource instance to the engine
    // Then, we'd create resource bundles for each locale we support: src/main/resources/mailMessages_xx_YY.properties
    @Bean
    public ResourceBundleMessageSource emailMessageSource () {
        final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("mailMessages");
        return messageSource;
    }






}

// **** Provide a template resolver to locate the template files directory.

// + Templates as Classpath Resources: Template files can be shipped within the JAR file.
// To locate templates from the JAR, we use the ClassLoaderTemplateResolver.
// Our templates are in the main/resources/mail-templates directory, so we set the Prefix
// attribute relative to the resource directory:
/* @Bean
    public ITemplateResolver thymeleafClassLoaderTemplateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix(mailTemplatesPath + "/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCharacterEncoding("UTF-8");
        return templateResolver;
    }*/

// + Templates From External Directory: In other cases, we may want to modify templates
// without having to rebuild and deploy. To achieve this, we can put the templates on the filesystem instead.
/*In other cases, we may want to modify templates without having to rebuild and deploy.
To achieve this, we can put the templates on the filesystem instead.
It might be useful to configure this path in application.properties so that we can modify it for each deployment.
This property can be accessed using the @Value annotation:
@Value("${spring.mail.templates.path}")
private String mailTemplatesPath;*/
/*    @Bean
    public ITemplateResolver thymeleafFilesystemTemplateResolver() {
        FileTemplateResolver templateResolver = new FileTemplateResolver();
        templateResolver.setPrefix(mailTemplatesPath + "/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCharacterEncoding("UTF-8");
        return templateResolver;
    }*/

