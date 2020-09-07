package com.tom.tz.es;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

@Component
public  class ESClient {
    private  static final Logger LOGGER = LoggerFactory.getLogger(ESClient.class);

    @Value("${elasticsearch.ip}")
    private String ip;

    @Value("${elasticsearch.port}")
    private int port;

    private static RestHighLevelClient client ;

    public RestHighLevelClient getClient() {
        if(this.client==null){
            this.client = new RestHighLevelClient(RestClient.builder(
                    new HttpHost(ip, port, "http")));
        }
        return this.client;
    }
    /*
     * 初始化
     */
    @PostConstruct
    public void  start(){
        this.client = new RestHighLevelClient(RestClient.builder(
                new HttpHost(ip, port, "http")));
        LOGGER.info("初始化ES客户端");
    }

    @PreDestroy
    public void stop(){
        if(client !=null){
            try {
                client.close();
                client= null;
                LOGGER.info("关闭ES客户端");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }



}
