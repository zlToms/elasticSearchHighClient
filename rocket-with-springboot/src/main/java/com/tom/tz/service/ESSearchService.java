package com.tom.tz.service;

import com.tom.tz.entity.CorditionES;
import com.tom.tz.entity.ESEntity;
import com.tom.tz.vo.ResponseBean;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;


@Service
public class ESSearchService {

    @Autowired
    private SendSearchRequest sendSearchRequest;

    public ResponseBean get(ESEntity esEntity, Map<String,Object> corMap, CorditionES cordition ,Object target) throws IOException {

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        Iterator<Map.Entry<String, Object>> it = corMap.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String, Object> entry = it.next();
            if(entry.getValue()==null){
                it.remove();
            }else{
                MatchPhraseQueryBuilder mpq = QueryBuilders.matchPhraseQuery(entry.getKey(),entry.getValue());
                boolQueryBuilder.must(mpq);
            }
        }

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQueryBuilder);

        if(cordition!=null){
            searchSourceBuilder= getCordition(searchSourceBuilder,cordition);
        }

        SearchRequest searchRequest = new SearchRequest()
                .indices(esEntity.getIndex())
                .types(esEntity.getType())
                .searchType(SearchType.QUERY_THEN_FETCH)
                .source(searchSourceBuilder);

        ResponseBean responseBean = sendSearchRequest.send(searchRequest, RequestOptions.DEFAULT,target);
        return responseBean;

    }


    public ResponseBean getShouldCor(ESEntity esEntity, Map<String,Object> corMap, CorditionES cordition ,Object target) throws IOException {

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        Iterator<Map.Entry<String, Object>> it = corMap.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String, Object> entry = it.next();
            if(entry.getValue()==null){
                it.remove();
            }else{
                boolQueryBuilder.should(QueryBuilders.termQuery(entry.getKey(),entry.getValue()));
            }
        }
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQueryBuilder);
        if(cordition!=null){
            searchSourceBuilder= getCordition(searchSourceBuilder,cordition);
        }

        SearchRequest searchRequest = new SearchRequest()
                .indices(esEntity.getIndex())
                .types(esEntity.getType())
                .searchType(SearchType.QUERY_THEN_FETCH)
                .source(searchSourceBuilder);


        ResponseBean responseBean = sendSearchRequest.send(searchRequest, RequestOptions.DEFAULT,target);
        return responseBean;

    }


    /**
     *
     * 模糊匹配
     */
    public ResponseBean getPuzzyCor(ESEntity esEntity, Map<String,Object> corMap, CorditionES cordition ,Object target) throws IOException {
        Iterator<Map.Entry<String, Object>> it = corMap.entrySet().iterator();
        FuzzyQueryBuilder fuzzyQueryBuilder = null;
        while(it.hasNext()){
            Map.Entry<String, Object> entry = it.next();
            if(entry.getValue()==null){
                it.remove();
            }else{
                fuzzyQueryBuilder=QueryBuilders.fuzzyQuery(entry.getKey(),entry.getValue());
                fuzzyQueryBuilder.fuzziness(Fuzziness.ONE);
            }
        }

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(fuzzyQueryBuilder);

        if(cordition!=null){
            searchSourceBuilder= getCordition(searchSourceBuilder,cordition);
        }
        SearchRequest searchRequest = new SearchRequest()
                .indices(esEntity.getIndex())
                .types(esEntity.getType())
                .searchType(SearchType.QUERY_THEN_FETCH)
                .source(searchSourceBuilder);

        ResponseBean responseBean = sendSearchRequest.send(searchRequest, RequestOptions.DEFAULT,target);
        return responseBean;

    }


    /**
     *
     * 精确匹配，完全匹配才有返回
     */
    public ResponseBean getKeyword(ESEntity esEntity, Map<String,Object> corMap, CorditionES cordition ,Object target) throws IOException {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        Iterator<Map.Entry<String, Object>> it = corMap.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String, Object> entry = it.next();
            if(entry.getValue()==null){
                it.remove();
            }else{
                MatchPhraseQueryBuilder mpq = QueryBuilders.matchPhraseQuery(entry.getKey()+".keyword",entry.getValue());
                boolQueryBuilder.must(mpq);
            }
        }

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQueryBuilder);

        if(cordition!=null){
            searchSourceBuilder= getCordition(searchSourceBuilder,cordition);
        }
        SearchRequest searchRequest = new SearchRequest()
                .indices(esEntity.getIndex())
                .types(esEntity.getType())
                .searchType(SearchType.QUERY_THEN_FETCH)
                .source(searchSourceBuilder);

        ResponseBean responseBean = sendSearchRequest.send(searchRequest, RequestOptions.DEFAULT,target);
        return responseBean;

    }



    private  SearchSourceBuilder getCordition(SearchSourceBuilder searchSourceBuilder,CorditionES cordition){
        searchSourceBuilder.from(cordition.getStart()).size(cordition.getSize());
        if(cordition.getOrderPropery()!=null){
            if(cordition.getOrder()==1){
                searchSourceBuilder.sort(cordition.getOrderPropery());
            }else{
                searchSourceBuilder.sort(cordition.getOrderPropery(),SortOrder.DESC);
            }
        }
        searchSourceBuilder.explain(true);
        return searchSourceBuilder;
    }
}
