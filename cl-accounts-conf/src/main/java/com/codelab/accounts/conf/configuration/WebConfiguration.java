package com.codelab.accounts.conf.configuration;

import com.codelab.accounts.conf.interceptors.RequestPrincipalInterceptor;
import com.codelab.accounts.jwt.TokenService;
import com.codelab.accounts.service.principal.RequestPrincipal;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.pojo.javassist.JavassistLazyInitializer;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.inject.Inject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;

/**
 * @author lordUhuru 18/11/2019
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Inject
    private RequestPrincipalInterceptor requestPrincipalInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestPrincipalInterceptor);
    }

    @Bean
    public FactoryBean<RequestPrincipal> requestPrincipal() {
        return RequestPrincipalInterceptor.requestPrincipal();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(new StdSerializer<JavassistLazyInitializer>(JavassistLazyInitializer.class) {

            @Override
            public void serialize(JavassistLazyInitializer value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                gen.writeNull();
            }
        });
        simpleModule.addSerializer(new StdSerializer<HibernateProxy>(HibernateProxy.class) {

            @Override
            public void serialize(HibernateProxy value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                gen.writeObject(Collections.singletonMap("id", value.getHibernateLazyInitializer().getIdentifier()));
//                gen.writeNull();
            }
        });
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(simpleModule);

        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new Jdk8Module());

        return objectMapper;
    }

    @Bean
    public MappingJackson2HttpMessageConverter customJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        jsonConverter.setObjectMapper(objectMapper());
        return jsonConverter;
    }
}
