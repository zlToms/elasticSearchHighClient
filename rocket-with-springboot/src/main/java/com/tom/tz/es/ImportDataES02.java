package com.tom.tz.es;

import com.tom.tz.entity.ESEntity;
import com.tom.tz.util.BulkTool;
import com.tom.tz.util.DataUtils;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ImportDataES02 {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private  ESClient esClient;

    private RestHighLevelClient client;

    public void bulkImportToES() throws IOException {

        client = esClient.getClient();

        BulkRequest request = new BulkRequest();
        DataUtils dataUtils= new DataUtils();
        String sqlRow = "SELECT count(*) FROM t_user ";
        int rows= dataUtils.getRows(sqlRow);

        List<String> keyList= new ArrayList<>();
        keyList.add("id"+":"+"1");
        keyList.add("userName"+":"+"2");
        keyList.add("realName"+":"+"2");
        keyList.add("sex"+":"+"2");
        keyList.add("mobile"+":"+"2");
        keyList.add("email"+":"+"2");
        keyList.add("note"+":"+"2");

        BulkProcessor bulkProcessor = BulkTool.getBulkProcessor(client);
        if(rows<=100000){
            String sql = "SELECT * FROM t_user ";
            List<Map<String, String>> maps = dataUtils.queryData(sql,keyList);
            for(Map<String, String> map:maps){
                bulkProcessor.add(new IndexRequest("posts", "doc", map.get("id"))
                        .source(map));
            }
            //BulkResponse bulkResponse = client.bulk(request, DEFAULT);
            bulkProcessor.flush();

        }else{
            int count = rows/100000;
            for(int i =0;i<=count;i++){
                String sql = "SELECT * FROM t_user limit "+ i*100000+","+100000;

                List<Map<String, String>> maps = dataUtils.queryData(sql,keyList);
                for(Map<String, String> map:maps){
                    bulkProcessor.add(new IndexRequest("posts", "doc", map.get("id"))
                            .source(map));

                }
                bulkProcessor.flush();
                System.out.println("import ended perpect!");
            }
        }

    }


    //api提供根据id来删除ES中索引数据
    public void deleteDataES(ESEntity esEntity) {

        client = esClient.getClient();

        DeleteIndexRequest request = new DeleteIndexRequest(esEntity.getIndex());

        AcknowledgedResponse deleteResponse = null;
        try{
            deleteResponse = client.indices().delete(request, RequestOptions.DEFAULT);
        }catch(ConnectException e){
            logger.info("ES客户端异常");
            esClient.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
