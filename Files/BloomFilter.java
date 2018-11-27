package com.ifeng.recom.mixrecall.common.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ifeng.recom.mixrecall.common.cache.BloomFilterCache;
import com.ifeng.recom.mixrecall.common.tool.ServiceLogUtil;
import org.elasticsearch.common.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.collect.Iterables;
import com.google.gson.Gson;
import com.ifeng.recom.mixrecall.common.model.Document;
import com.ifeng.recom.mixrecall.common.model.RecallResult;
import com.ifeng.recom.mixrecall.common.util.bloomfilter.LocalBloomFilter;
import com.ifeng.recom.mixrecall.common.util.http.HttpClientUtil;
import com.ifeng.recom.tools.common.logtools.utils.timer.TimerEntityUtil;


/**
 * 远端的集中过滤
 * Created by geyl on 2017/10/30.
 */
@Service
public class BloomFilter {
    private static final Logger logger = LoggerFactory.getLogger(BloomFilter.class);
    private static final Logger timeLogger = LoggerFactory.getLogger(TimerEntityUtil.class);

    private static Gson gson = new Gson();
    private static final String URL = "http://local.recom.bloom.collector1.ifengidc.com/filter/check";
    private static final String STATUS_OK = "ok";
    private static final int TIMEOUT = 300;





    public static Set<String> queryByLocalBloom(String uid, Set<String> simIds) {
        if (simIds != null) {
            //romoveIf 替代removeAll  采取迭代删除 防止抛出ConcurrentModificationException
            simIds.removeIf(s -> LocalBloomFilter.onlyCheck(uid, s));
        }
        return simIds;
    }

    /**
     * 通过http获取bloom数据
     *
     * @param uid
     * @param simIds
     * @return
     */
    private static Set<String> requestBloomFilter(String uid, Set<String> simIds) {
        simIds = queryByLocalBloom(uid, simIds);
//        logger.debug("bloom cache filter size:{} uid:{}", (bloomBefore - simIds.size()), uid);

        String url = URL + "?uid=" + uid;
        String simId = String.join(",", simIds);

        Map<String, String> postMap = new HashMap<>();
        postMap.put("uid", uid);
        postMap.put("simids", simId);

        long start = System.currentTimeMillis();
        try {
            String rt = HttpClientUtil.httpPost(url, HttpClientUtil.transMapToPairs(postMap), TIMEOUT);

            if (Strings.isNullOrEmpty(rt)) {
                logger.error("bloom filter return empty, uid:" + uid);
                return simIds;
            }

            BloomResult result = gson.fromJson(rt, BloomResult.class);

            if (result.getStatus().equalsIgnoreCase(STATUS_OK)) {
                List<String> filteredSimIds = result.getSimids();
                Set<String> noReadSimId = new HashSet<>(filteredSimIds);

                //simIds 当中存储的成为看过的id;
                simIds.removeAll(noReadSimId);
                //将用户看过的结果入本机布隆
                simIds.stream().forEach(tmpSimId -> LocalBloomFilter.onlyPut(uid, tmpSimId));

                return noReadSimId;
            }

        } catch (Exception e) {
            logger.error("filter simId error:{}", e);
        }

        long cost = System.currentTimeMillis() - start;
        if (cost > 50) {
            ServiceLogUtil.debug("bloom {}, cost:{}", uid, cost);
        }

        return simIds;
    }

    /**
     * 分段请求bloom filter
     *
     * @param uid
     * @param simIds
     * @return
     */
    private static Set<String> requestBloomFilterByPartitions(String uid, Set<String> simIds) {
        Set<String> resultSimIds = new HashSet<>();
        for (List<String> partitionSimIds : Iterables.partition(simIds, 500)) {
            resultSimIds.addAll(requestBloomFilter(uid, new HashSet<>(partitionSimIds)));
        }
        return resultSimIds;
    }

    public static Set<String> filterSimIdByBloomFilter(String uid, Set<String> simIdSet) {
        return requestBloomFilter(uid, simIdSet);
    }

    /**
     * 根据Document对象获取simId后才可进行布隆过滤
     * @param uid
     * @param documentList
     * @return
     */
    public static List<Document> filterSimIdByBloomFilter(String uid, List<Document> documentList) {
        if (Strings.isNullOrEmpty(uid) || documentList == null || documentList.isEmpty()) {
            return Collections.emptyList();
        }

        Set<String> simIdSet = new HashSet<>();
        for (Document document : documentList) {
            simIdSet.add(document.getSimId());
        }

        Set<String> resultSimIds = requestBloomFilterByPartitions(uid, simIdSet);

        List<Document> filteredList = new ArrayList<>();
        for (Document document : documentList) {
            if (resultSimIds.contains(document.getSimId())) {
                filteredList.add(document);
            }
        }

        return filteredList;
    }

    /**
     * 保证原顺序，分段请求bloom
     *
     * @param uid
     * @param recallResults
     * @return
     */
    public static List<RecallResult> bloomFilterForRecallResult(String uid, List<RecallResult> recallResults) {
        if (Strings.isNullOrEmpty(uid) || recallResults == null || recallResults.isEmpty()) {
            return Collections.emptyList();
        }

        Set<String> simIdSet = new HashSet<>();
        for (RecallResult recallResult : recallResults) {
            simIdSet.add(recallResult.getDocument().getSimId());
        }

        Set<String> resultSimIds = requestBloomFilterByPartitions(uid, simIdSet);

        List<RecallResult> filteredList = new ArrayList<>();
        for (RecallResult recallResult : recallResults) {
            if (resultSimIds.contains(recallResult.getDocument().getSimId())) {
                filteredList.add(recallResult);
            }
        }

        return filteredList;
    }


    public static void main(String[] args) {
        Document document = new Document();
        document.setSimId("clusterId_48453344");

        Document document1 = new Document();
        document1.setSimId("clusterId_24855408");


        Document document2 = new Document();
        document2.setSimId("clusterId_1111");

        List<Document> documentList = new ArrayList<>();
        documentList.add(document);
        documentList.add(document1);
        documentList.add(document2);

        filterSimIdByBloomFilter("867305035296545", documentList).forEach(x -> System.out.println(x.getSimId()));
        filterSimIdByBloomFilter("867305035296545", documentList).forEach(x -> System.out.println(x.getSimId()));
        System.out.println("finish");
    }
}
