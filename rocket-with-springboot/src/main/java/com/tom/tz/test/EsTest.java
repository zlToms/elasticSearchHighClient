package com.tom.tz.test;

import com.tom.tz.RocketWithESApplication;
import com.tom.tz.es.ESClient;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RocketWithESApplication.class)
public class EsTest {

    @Autowired
    private ESClient esClient;

    @Test
    public void test01(){
        RestHighLevelClient restHighLevelClient = esClient.getClient();
        Map<String,Object> map = new HashMap<>();
        map.put("beginDate",new Date());
        map.put("name","tz");
        map.put("er","dadj2");
        IndexRequest indexRequest = new IndexRequest("estest", "test", null);
        indexRequest.source(map);
        IndexResponse index = null;
        try {
             index = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(index.getId());
    }


    public void test02(){
        System.out.println(1234);
    }
}
