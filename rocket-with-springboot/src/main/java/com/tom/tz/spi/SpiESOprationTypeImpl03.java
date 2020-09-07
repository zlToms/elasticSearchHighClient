package com.tom.tz.spi;

import com.alibaba.fastjson.JSONObject;
import com.tom.tz.entity.ProtocalEntity;
import com.tom.tz.enums.ESOperationType;
import com.tom.tz.es.ESClient;
import com.tom.tz.util.JavaBeanToMap;
import com.tom.tz.util.ESUtils;


import java.util.HashMap;
import java.util.Map;


public class SpiESOprationTypeImpl03 implements SpiESOprationType {


    private ESOperationType esType = ESOperationType.ADD;

    @Override
    public boolean isSupport(ESOperationType esType) {
        return this.esType == esType;
    }

    @Override
    public void doWork(ProtocalEntity protocalEntity,ESClient esClient) throws Exception {

        Map<String, Object> map = new HashMap<>();
        JSONObject jsonObject= (JSONObject) (protocalEntity.getObject());
        Object object1 = jsonObject.toJavaObject(jsonObject, Class.forName(protocalEntity.getClazz()));
        map= JavaBeanToMap.beanToMap(object1);
        new ESUtils().addESWithData02(map,protocalEntity.getEsEntity(),esClient);
    }


}
