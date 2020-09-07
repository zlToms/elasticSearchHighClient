package com.tom.tz.spi;

import com.tom.tz.entity.ProtocalEntity;
import com.tom.tz.enums.ESOperationType;
import com.tom.tz.es.ESClient;

public interface SpiESOprationType {

    public boolean isSupport(ESOperationType esType);

    public void doWork(ProtocalEntity protocalEntity,ESClient esClient) throws Exception;

}
