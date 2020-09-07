package com.tom.tz.util;

import com.tom.tz.entity.ESEntity;
import com.tom.tz.es.ESClient;
import com.tom.tz.es.ESOperationTool;

import java.io.IOException;
import java.util.Map;

public class ESUtils {

    //删除ES逻辑
    public void deleteES(ESEntity esEntity, ESClient esClient) throws IOException {

        new ESOperationTool().deleteDataES(esEntity,esClient);
    }

    //修改逻辑  数据部分由生产者提供，待从es获取原始数据加工后形成最终上传数据
    public void updateOne(ESEntity esEntity, Map<String,Object> map , ESClient esClient) throws IOException {

        new ESOperationTool().updateES(esEntity,map,esClient);
    }



    //增加，带数据的map
    public void addESWithData(Map<String ,Object> map, ESClient esClient) throws IOException {

        ESEntity esEntity = new ESEntity();
        esEntity.setIndex("post");
        esEntity.setType("doc");
        esEntity.setId((int)map.get("id"));
        new ESOperationTool().addESWithData(esEntity,map,esClient);
    }


    //增加，带数据的map
    public void addESWithData02(Map<String ,Object> map,ESEntity esEntity,ESClient esClient) throws IOException {
        esEntity.setId((int)map.get("id"));
        new ESOperationTool().addESWithData(esEntity,map,esClient);
    }
}
