package com.tom.tz.es;

import com.tom.tz.entity.ESEntity;
import com.tom.tz.util.BulkTool;
import com.tom.tz.util.DataUtils;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.WatcherClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ImportDataES {

    private RestHighLevelClient client;

    public void bulkTest() throws IOException {

        client = new ESClient().getClient();

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
            }
        }

    }


    //api提供根据id来删除ES中索引数据
    public void deleteDataES(ESEntity esEntity) {
        ESClient client01=new ESClient();
        client = client01.getClient();


        DeleteIndexRequest request = new DeleteIndexRequest(esEntity.getIndex());

        AcknowledgedResponse deleteResponse = null;
        try {
            deleteResponse = client.indices().delete(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (client != null) {
                client01.stop();
            }
        }



    }
}
