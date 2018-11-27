package store.dapp.dao;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import store.dapp.ESFactory.EsClientFactory;
import java.io.IOException;
import java.util.*;

/**
 * @Auther: liulu3
 * @Date: 2018/11/1 20:46
 * @Description: 查询Account的信息
 */
public class AcountInfoDao {
    private static final Logger logger = Logger.getLogger(AcountInfoDao.class);
    private static RestClient client;
    private static final String INDEX_DAPP = "eos_dapp_info"; //索引名称
    private static final String INDEX_DAPP_V1 = "dapp_account_record"; //索引名称

    private static final String TYPE = "account_related_trx_info";
    private static RestHighLevelClient highLevelClient = EsClientFactory.getRestHighLevelClient();


    static {
        client = EsClientFactory.getClient();
    }

    /**
     * 根据合约名从库中查询最新交易的交易记录
     * @param accountName
     * @return
     */
    public static JSONObject getRecentlyRecord(String accountName) throws IOException {
        JSONObject map = new JSONObject();
        try {
            String method = "GET";
            String endpoint = "/" + INDEX_DAPP_V1 + "/_search";
            HttpEntity entity = new NStringEntity("{\n" +
                    "  \"query\": {\n" +
                    "    \"term\" : {\n" +
                    "        \"account\" : \"" + accountName + "\"\n" +
                    "    }\n" +
                    "  }\n" +
                    "  , \"sort\": [\n" +
                    "    {\n" +
                    "      \"timestamp\": {\"order\": \"desc\"}\n" +
                    "    }\n" +
                    "  ]\n" +
                    "   , \"size\": 1\n" +
                    "}", ContentType.APPLICATION_JSON);

            Response response = client.performRequest(method,endpoint, Collections.<String, String>emptyMap(), entity);
            JSONObject json = JSONObject.fromObject(EntityUtils.toString(response.getEntity()));
            JSONObject hitsObj = json.containsKey("hits") && json.get("hits") != null ?
                    json.getJSONObject("hits") : new JSONObject();
            JSONArray hitsArray = hitsObj.containsKey("hits") && hitsObj.get("hits") != null ?
                    hitsObj.getJSONArray("hits") : new JSONArray();

            for (int i = 0; i < hitsArray.size(); i++){
                map = hitsArray.getJSONObject(i);
//                map = obj.containsKey("_source") && obj.get("_source") != null ?
//                        obj.getJSONObject("_source") : new JSONObject();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }


    public static void UpdateByScript(String id, Map<String, Object> esMap) throws Exception {
        IndexRequest indexRequest = new IndexRequest(INDEX_DAPP_V1, TYPE, id).source(esMap);
        highLevelClient.index(indexRequest);
    }

    /**
     * 查询所有合约号
     * @return
     * @throws IOException
     */
    public static Map<String, Set<String>> getAllAcounts() throws IOException {
        Map<String, Set<String>> result = new HashMap<>();
        try{
            String method = "GET";
            String endpoint = "/" + INDEX_DAPP + "/_search";
            HttpEntity entity = new NStringEntity("{\n" +
                    "  \"query\": {\n" +
                    "      \"match_all\": {}\n" +
                    "    }\n" +
                    "    ,\"size\": 10000\n" +
                    "}", ContentType.APPLICATION_JSON);

            Response response = client.performRequest(method,endpoint, Collections.<String, String>emptyMap(), entity);
            JSONObject json = JSONObject.fromObject(EntityUtils.toString(response.getEntity()));
            JSONObject hitsObj = json.containsKey("hits") && json.get("hits") != null ?
                    json.getJSONObject("hits") : new JSONObject();
            JSONArray hitsArray = hitsObj.containsKey("hits") && hitsObj.get("hits") != null ?
                    hitsObj.getJSONArray("hits") : new JSONArray();

            for (int i = 0; i < hitsArray.size(); i++){
                Set<String> contractsSet = new HashSet<>();
                JSONObject obj = hitsArray.getJSONObject(i);
                String _id = obj.containsKey("_id") && obj.get("_id") != null ?
                        obj.getString("_id") : "";
                JSONObject _source = obj.containsKey("_source") && obj.get("_source") != null ?
                        obj.getJSONObject("_source") : new JSONObject();
                String contracts = _source.containsKey("contracts") && _source.get("contracts") != null ?
                        _source.getString("contracts") : "";

                if (!contracts.equals("")){
                    for (int j = 0; j < contracts.split(" ").length; j ++){
                        contractsSet.add(contracts.split(" ")[j]);
                    }
                    result.put(_id, contractsSet);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
//            client.close();
        }
        return result;
    }



    public static void main(String[] args) throws IOException {
//        Map<String, Object> map = new HashMap<>();
//        map.put("trx_id", "trx_id-004");
//        map.put("timestamp", "2018-11-21T07:41:15.500");
//        map.put("receiver", "receiver1");
//        map.put("sender", "sender1");
//        map.put("code", "eosio.token");
//        map.put("quantity", "1");
//        map.put("memo", "memo");
//        map.put("symbol", "EOS");
//        map.put("status", "executed");
//        map.put("name", "name1");
//        map.put("account", "account1");
//        IndexRequest indexRequest = new IndexRequest("dapp_v1", "account_related_trx_info", "trx_id-004").source(map);
//        highLevelClient.index(indexRequest);

        System.out.println(getRecentlyRecord("eosplaybrand"));
        client.close();
        highLevelClient.close();


    }
}
