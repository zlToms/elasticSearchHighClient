package com.tom.tz.spi;

import com.alibaba.fastjson.JSONObject;
import com.tom.tz.entity.ProtocalEntity;
import com.tom.tz.enums.ESOperationType;
import com.tom.tz.es.ESClient;
import com.tom.tz.util.JavaBeanToMap;
import com.tom.tz.util.ESUtils;

import java.util.HashMap;
import java.util.Map;


public class SpiESOprationTypeImpl02 implements SpiESOprationType {

    private ESOperationType esType = ESOperationType.UPDATE;

    @Override
    public boolean isSupport(ESOperationType esType) {
        return this.esType == esType;
    }

    /**
        post http://localhost:9200/post/doc/105/_update
        {
         "script": "ctx._source.username='dkshdjs'"
         }
     还没找到java代码局部刷新的功能
     两个方向：
           1、修改数据库的时候返回修改后要放到es里面的内容，然后就直接刷新
           2、按查询条件在es里面找到要修改的原始数据，修改的内容替换上去，再刷新进去
     * 这里选择第二种 数据部分由生产者提供，部分是从es捞取
     */
    @Override
    public void doWork(ProtocalEntity protocalEntity,ESClient esClient ) throws Exception {
        Map<String, Object> map = new HashMap<>();
        JSONObject jsonObject= (JSONObject) (protocalEntity.getObject());
        Object object1 = jsonObject.toJavaObject(jsonObject, Class.forName(protocalEntity.getClazz()));
        map= JavaBeanToMap.beanToMap(object1);
        new ESUtils().updateOne(protocalEntity.getEsEntity(),map,esClient);
    }


}
