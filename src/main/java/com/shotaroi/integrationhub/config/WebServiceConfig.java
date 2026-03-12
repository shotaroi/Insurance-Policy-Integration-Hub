package com.shotaroi.integrationhub.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import com.shotaroi.integrationhub.soap.CreatePolicyLegacyRequest;
import com.shotaroi.integrationhub.soap.CreatePolicyLegacyResponse;
import com.shotaroi.integrationhub.soap.GetPolicyByNumberRequest;
import com.shotaroi.integrationhub.soap.GetPolicyByNumberResponse;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

/**
 * Spring Web Services configuration for SOAP endpoints.
 */
@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext context) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(context);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(
                GetPolicyByNumberRequest.class,
                GetPolicyByNumberResponse.class,
                CreatePolicyLegacyRequest.class,
                CreatePolicyLegacyResponse.class,
                com.shotaroi.integrationhub.soap.SoapPolicyType.class
        );
        return marshaller;
    }

    @Bean(name = "policy")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema policySchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("PolicyPort");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace("http://shotaroi.com/integrationhub/soap/policy");
        wsdl11Definition.setSchema(policySchema);
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema policySchema() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/policy.xsd"));
    }
}
