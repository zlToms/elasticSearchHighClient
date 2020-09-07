package com.tom.tz.spi;

import com.tom.tz.entity.ProtocalEntity;
import com.tom.tz.enums.ESOperationType;
import com.tom.tz.es.ESClient;
import com.tom.tz.util.ESUtils;


import java.io.IOException;

public class SpiESOprationTypeImpl01 implements SpiESOprationType {

    private ESOperationType esType = ESOperationType.DELETE;

    @Override
    public boolean isSupport(ESOperationType esType) {
        return this.esType == esType;
    }

    @Override
    public void doWork(ProtocalEntity protocalEntity,ESClient esClient) throws IOException {
        new ESUtils().deleteES(protocalEntity.getEsEntity(),esClient);
    }


}
