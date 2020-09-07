package com.tom.tz.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tom.tz.es.ESClient;
import com.tom.tz.vo.ResponseBean;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SendSearchRequest {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ESClient esClient;

    /*
    * 一般来说，非聚集的检索只需处理hits部分，直接将结果返回给前端
    * */
    public ResponseBean send(SearchRequest searchRequest,
                             RequestOptions options){
        JSONArray jsonArray = new JSONArray();
        try {
            sendAndProcessHits(searchRequest,options,jsonArray);
            return new ResponseBean(200,"查询文档",jsonArray);
        }catch (ConnectException e){
            logger.info("ES客户端异常");
            e.printStackTrace();
            return new ResponseBean(400,"查询文档失败",null);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseBean(400,"查询文档失败",null);
        }

    }

    /*处理返回结果中的hits部分*/
    public SearchResponse sendAndProcessHits(SearchRequest searchRequest,
                                             RequestOptions options,
                                             JSONArray jsonArray) throws IOException {

        RestHighLevelClient restHighLevelClient = esClient.getClient();
        SearchResponse search = restHighLevelClient.search(searchRequest, options);
        SearchHits hits = search.getHits();
        for(SearchHit hit:hits){
            String src = hit.getSourceAsString();
            JSONObject jsonObject = JSON.parseObject(src);
            jsonArray.add(jsonObject);
        }
        return search;
    }


    public ResponseBean send(SearchRequest searchRequest,
                             RequestOptions options,Object target){
        try {
            Object obj = sendAndProcessHits(searchRequest, options, target);
            return new ResponseBean(200,"查询文档",obj);
        }catch (ConnectException e){
            logger.info("ES客户端异常");
            e.printStackTrace();
            return new ResponseBean(400,"查询文档失败",null);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseBean(400,"查询文档失败",null);
        }

    }




    /*处理返回结果中的hits部分*/
    public Object sendAndProcessHits(SearchRequest searchRequest,
                                             RequestOptions options,
                                             Object target) throws IOException {

        RestHighLevelClient restHighLevelClient = esClient.getClient();
        SearchResponse search = restHighLevelClient.search(searchRequest, options);
        List<Object> list = new ArrayList<>();
        for(SearchHit hit:search.getHits()){
            Map<String ,Object> map = new HashMap<>();
            map = hit.getSourceAsMap();
            JSONObject jsonObject= (JSONObject) JSONObject.toJSON(map);
            Object obj = jsonObject.toJavaObject(jsonObject, target.getClass());
            list.add(obj);
        }
        return list;
    }
}
