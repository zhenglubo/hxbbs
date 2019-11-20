package com.lixin.utils;

import com.alibaba.fastjson.JSON;
import com.lixin.dto.es.BoolQueryBuilderDto;
import com.lixin.enums.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @description: ElasticSearchService
 * @author: zhenglubo
 * @create: 2019-10-22 10:54
 **/

@Component
@Slf4j
public class ElasticSearchService {

    @Autowired
    private RestHighLevelClient client;


    /**
     * 多字段查询
     *
     * @param tClass
     * @param keyword
     * @param isFuzzyQuery 是否模糊查询
     * @param fields
     * @param <T>
     * @return
     */
    public <T> List<T> search(Class<T> tClass, String keyword, boolean isFuzzyQuery, String... fields) {
        return this.searchWithIndices(tClass, null, keyword, isFuzzyQuery, fields);
    }

    /**
     * 多字段查询
     *
     * @param tClass
     * @param indicesName
     * @param keyword
     * @param isFuzzyQuery 是否模糊查询
     * @param fields
     * @param <T>
     * @return
     */
    public <T> List<T> searchWithIndices(Class<T> tClass, String indicesName, String keyword, boolean isFuzzyQuery, String[] fields) {
        List<T> list = new ArrayList<>();
        SearchRequest request = indicesName != null ? new SearchRequest(indicesName) : new SearchRequest();
        SearchSourceBuilder builder = new SearchSourceBuilder();
        if (isFuzzyQuery) {
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            for (String field : fields) {
                WildcardQueryBuilder queryBuilder1 = QueryBuilders.wildcardQuery(field, "*" + keyword + "*");
                boolQueryBuilder.should(queryBuilder1);
            }
            builder.query(boolQueryBuilder);
        } else {
            MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(keyword, fields);
            builder.query(multiMatchQueryBuilder);
        }
        request.source(builder);
        SearchResponse response = null;
        try {
            response = client.search(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("es查询出错，查询参数为：keyword：{}，fileds：{}", keyword, fields);
        }
        if (response != null && response.status().getStatus() == ErrorCode.OK.getCode()) {
            SearchHit[] hits = response.getHits().getHits();
            for (SearchHit hit : hits) {
                String sourceAsString = hit.getSourceAsString();
                T t = JSON.parseObject(sourceAsString, tClass);
                list.add(t);
            }
        }
        return list;
    }

    /**
     * 保存单个文档
     *
     * @param index
     * @param type
     * @param id
     * @param source
     * @return
     */
    public IndexResponse indexSingle(String index, String type, String id, Object source) {
        IndexRequest request = new IndexRequest(index, type, id).source(JSON.toJSONString(source), XContentType.JSON).create(true);
        IndexResponse response = null;
        try {
            response = client.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("es保存索引文件失败");
            log.error("请求参数为：index: {}，type: {}, id: {}, source: {}", index, type, id, source.toString());
            log.error(e.getMessage());
        }
        return response;
    }


    /**
     * 批量保存文档
     *
     * @param index
     * @param type
     * @param tList
     * @param <T>
     * @return
     */
    public <T> BulkResponse bulkIndex(String index, String type, List<T> tList) {

        BulkRequest bulkRequest = new BulkRequest();
        BulkResponse bulkResponse = null;
        try {
            for (T t : tList) {
                Class<?> tClass = t.getClass();
                Field field = tClass.getDeclaredField("id");
                String id = null;
                if (field != null) {
                    field.setAccessible(true);
                    id = field.get(t).toString();
                }
                IndexRequest request;
                if (StringUtils.isEmpty(id)) {
                    request = new IndexRequest(index, type).source(JSON.toJSONString(t)).create(true);
                } else {
                    request = new IndexRequest(index, type, id).source(JSON.toJSONString(t)).create(true);
                }
                bulkRequest.add(request);
            }
            bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("批量保存文档失败");
        }
        return bulkResponse;
    }

    /**
     * 删除文档
     *
     * @param index
     * @param type
     * @param id
     * @return
     */
    public DeleteResponse delete(String index, String type, String id) {
        DeleteResponse deleteResponse = null;
        DeleteRequest request = new DeleteRequest(index, type, id);
        try {
            deleteResponse = client.delete(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("删除文档失败");
            log.error(e.getMessage());
        }
        return deleteResponse;
    }


    /**
     * 修改文档
     *
     * @param index
     * @param type
     * @param id
     * @param source
     * @return
     */
    public UpdateResponse update(String index, String type, String id, Map<String, Object> source) {
        UpdateRequest updateRequest = new UpdateRequest(index, type, id).doc(source, XContentType.JSON);
        UpdateResponse response = null;
        try {
            response = client.update(updateRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("修改文档失败");
            log.error(e.getMessage());
        }
        return response;
    }

    /**
     * 精确查询
     *
     * @param field
     * @param keyword
     * @param tClass
     * @param <T>
     * @param isPreciseQuery 是否精确查询
     * @return
     */
    public <T> List<T> termSearch(String field, String keyword, Class<T> tClass, boolean isPreciseQuery) {
        List<T> list = new ArrayList<>();
        SearchRequest request = new SearchRequest();
        SearchSourceBuilder builder = new SearchSourceBuilder();
        if (isPreciseQuery) {
            builder.query(QueryBuilders.termQuery(field, keyword));
        } else {
            builder.query(QueryBuilders.matchQuery(field, keyword));
        }
        request.source(builder);
        SearchResponse response = null;
        try {
            response = client.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error(e.getMessage());
            log.error("es查询异常");
        }
        if (response != null && response.status().getStatus() == ErrorCode.OK.getCode()) {
            SearchHit[] hits = response.getHits().getHits();
            for (SearchHit hit : hits) {
                list.add(JSON.parseObject(hit.getSourceAsString(), tClass));
            }
        }
        return list;
    }

    /**
     * 组合查询
     * @param dto
     * @param tClass
     * @return
     */
    public List<T> query(BoolQueryBuilderDto dto, Class<T> tClass) {

        List<T> list = new ArrayList<>();
        SearchRequest request = new SearchRequest();
        SearchSourceBuilder builder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        if (dto.getMustMap() != null && dto.getMustMap().size() > 0) {
            dto.getMustMap().entrySet().stream().forEach(entry->boolQueryBuilder.must(QueryBuilders.matchQuery(entry.getKey(), entry.getValue())));
        }
        if (dto.getMustNotMap() != null && dto.getMustNotMap().size() > 0) {
            dto.getMustNotMap().entrySet().stream().forEach(entry->boolQueryBuilder.mustNot(QueryBuilders.matchQuery(entry.getKey(), entry.getValue())));
        }
        if (dto.getShouldMap() != null && dto.getShouldMap().size() > 0) {
            dto.getShouldMap().entrySet().stream().forEach(entry->boolQueryBuilder.should(QueryBuilders.matchQuery(entry.getKey(), entry.getValue())));
        }
        builder.query(boolQueryBuilder);
        request.source(builder);
        try {
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            if (response != null && response.status().getStatus() == ErrorCode.OK.getCode()) {
                SearchHit[] hits = response.getHits().getHits();
                list = JSON.parseArray(Arrays.toString(hits), tClass);
            }
        } catch (IOException e) {
            log.error("es查询异常");
            log.error(e.getMessage());
        }
        return list;
    }
}
