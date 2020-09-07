package com.tom.tz.config;

import com.tom.tz.es.ESClient;
import com.tom.tz.spi.EsOprationType;
import com.tom.tz.spi.SpiESOprationType;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

@Configuration
public class SpiConfig  {

    @Bean
    public EsOprationType getEsOprationType() {
        EsOprationType esTpye = new EsOprationType();
        List<SpiESOprationType> list = new ArrayList<>();
        //采用spi加载扩展更方便
        ServiceLoader<SpiESOprationType> serviceLoader =ServiceLoader.load(SpiESOprationType.class);
        for(SpiESOprationType service: serviceLoader){
            list.add(service);
        }
        esTpye.setList(list);
        return esTpye;
    }

    

}
