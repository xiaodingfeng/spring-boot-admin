package com.ciicgat.circlefkbff.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.form.FormEncoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import io.github.lukehutch.fastclasspathscanner.scanner.ScanResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

@Component
public class FeignClientRegister implements BeanFactoryPostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeignClientRegister.class);
    //扫描的接口路径
    private final String scanPath = "com.ciicgat.circlefkbff.feign.api";

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        List<String> classes = scan(scanPath);
        if(classes == null) {
            return ;
        }
        Feign.Builder builder = getFeignBuilder();
        if(classes.size() > 0){
            for (String clazz : classes) {
                Class<?> targetClass = null;
                try {
                    targetClass = Class.forName(clazz);
                    FeignApi annotation = targetClass.getAnnotation(FeignApi.class);
                    if (annotation == null) {
                        continue;
                    }
                    String url = annotation.serviceUrl();
                    if (!StringUtils.hasText(url)) {
                        LOGGER.error(clazz +": feignApi url没设置");
                        throw new RuntimeException();
                    }
                    Object target = builder.target(targetClass, annotation.serviceUrl());
                    beanFactory.registerSingleton(targetClass.getName(), target);
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
    }

    public Feign.Builder getFeignBuilder(){
        Feign.Builder builder = Feign.builder()
                .client(new OkHttpClient())
                .encoder(new FormEncoder(new JacksonEncoder()))
                .errorDecoder(new StashErrorDecoder())
                .decoder(new StashDecoder(new ObjectMapper()))
                .options(new Request.Options(1000, 10000))
                .retryer(new Retryer.Default(5000, 5000, 3))
                .encoder(new FormEncoder(new JacksonEncoder()));
        return builder;
    }

    public List<String> scan(String path){
        ScanResult result = new FastClasspathScanner(path).matchClassesWithAnnotation(FeignApi.class, (Class<?> aClass) -> {
        }).scan();
        if(result != null){
            return result.getNamesOfAllInterfaceClasses();
        }
        return  null;
    }
}