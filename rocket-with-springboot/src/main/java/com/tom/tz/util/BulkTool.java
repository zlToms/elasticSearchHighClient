package com.tom.tz.util;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;

import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

import static org.elasticsearch.client.RequestOptions.DEFAULT;

public class BulkTool {

    public static BulkProcessor getBulkProcessor(RestHighLevelClient client) {

        BulkProcessor bulkProcessor = null;
        try {

            BulkProcessor.Listener listener = new BulkProcessor.Listener() {
                @Override
                public void beforeBulk(long executionId, BulkRequest request) {

                }

                @Override
                public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {

                }

                @Override
                public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
                    System.out.println("Bulk is unsuccess : " + failure + ", executionId: " + executionId);
                }
            };

            BiConsumer<BulkRequest, ActionListener<BulkResponse>> bulkConsumer = (request, bulkListener) -> client
                    .bulkAsync(request, DEFAULT, bulkListener);

            //	bulkProcessor = BulkProcessor.builder(bulkConsumer, listener).build();
            BulkProcessor.Builder builder = BulkProcessor.builder(bulkConsumer, listener);
            builder.setBulkActions(5000);
            builder.setBulkSize(new ByteSizeValue(100L, ByteSizeUnit.MB));
            builder.setConcurrentRequests(10);
            builder.setFlushInterval(TimeValue.timeValueSeconds(100L));
            builder.setBackoffPolicy(BackoffPolicy.constantBackoff(TimeValue.timeValueSeconds(1L), 3));
            bulkProcessor = builder.build();

        } catch (Exception e) {
            e.printStackTrace();
            try {
                bulkProcessor.awaitClose(100L, TimeUnit.SECONDS);
                client.close();
            } catch (Exception e1) {

            }
        }
        return bulkProcessor;
    }
}
