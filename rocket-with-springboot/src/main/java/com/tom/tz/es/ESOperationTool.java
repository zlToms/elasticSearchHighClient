package com.tom.tz.es;
import com.tom.tz.entity.ESEntity;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ConnectException;
import java.util.*;


@Component
public class ESOperationTool {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private RestHighLevelClient client;



    //更新数据，根据id 从es里面捞出原始数据，替换掉更改的内容，再发往ES
    public void updateES(ESEntity esEntity,Map<String,Object> map,ESClient esClient) throws IOException {
        Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String, Object> entry = it.next();
            if(entry.getValue()==null){
                it.remove();
            }
        }
        client = esClient.getClient();
        UpdateRequest updateRequest = new UpdateRequest (esEntity.getIndex(), esEntity.getType(), "" + esEntity.getId());
        updateRequest.doc(map);
        updateRequest.docAsUpsert(true);

        try{
            UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
        }catch(ConnectException e){
            logger.info("ES客户端异常");
            esClient.stop();
        }

    }





    //api提供根据id来删除ES中索引数据
    public void deleteDataES(ESEntity esEntity,ESClient esClient) throws IOException {
        client = esClient.getClient();
        DeleteRequest request = new DeleteRequest (
                esEntity.getIndex(),
                esEntity.getType(),
                ""+esEntity.getId());
        try{
            DeleteResponse deleteResponse = client.delete(request, RequestOptions.DEFAULT);
        }catch(ConnectException e){
            logger.info("ES客户端异常");
            esClient.stop();
        }
    }


    //向ES增加一条
    public void addESWithData(ESEntity esEntity,Map<String,Object> map, ESClient esClient) throws IOException {
        client = esClient.getClient();
        IndexRequest indexRequest = new IndexRequest(esEntity.getIndex(), esEntity.getType(), "" + esEntity.getId());
        indexRequest.source(map);
        try{
            IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
        }catch(ConnectException e){
            logger.info("ES客户端异常");
            esClient.stop();
        }
    }


    //根据id查询ES原始信息
    public Map<String,Object>  selectES(ESEntity esEntity) throws IOException {
        Map<String,Object> map= new HashMap<>();
        client = new ESClient().getClient();
        GetRequest getRequest = new GetRequest  (esEntity.getIndex(), esEntity.getType(), "" + esEntity.getId());
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        if (getResponse.isExists()) {
            map = getResponse.getSourceAsMap();
        }
        return map;
    }



}
