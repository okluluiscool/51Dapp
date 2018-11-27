package store.dapp.dao;

import com.google.gson.Gson;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import store.dapp.bean.EosConstant;
import store.dapp.bean.TraceRecordRequest;
import store.dapp.bean.TraceRecordResponse;
import store.dapp.util.HttpReqUtil;
import store.dapp.util.MD5Util;
import store.dapp.util.TimeUtil;

import java.io.IOException;
import java.util.*;

import static store.dapp.ESFactory.EsClientFactory.closeClient;
import static store.dapp.ESFactory.EsClientFactory.closeRestHighLevelClient;

/**
 * @Auther: liulu3
 * @Date: 2018/11/4 17:41
 * @Description: 查询账户的代币转账记录
 */
public class TraceRecordDao {
    private final static Logger log = Logger.getLogger(TraceRecordDao.class);
    private static List<String> ApiKeyList = new ArrayList<>();

    static {
        ApiKeyList.add(EosConstant.APIKEY_1);
        ApiKeyList.add(EosConstant.APIKEY_2);
        ApiKeyList.add(EosConstant.APIKEY_3);
        ApiKeyList.add(EosConstant.APIKEY_4);
        ApiKeyList.add(EosConstant.APIKEY_5);
    }

    private static void getTraceRecord(){
        try{
            Map<String, List<TraceRecordRequest>> accountAddressMap = packRequest();
            for (Map.Entry<String, List<TraceRecordRequest>> accoutAddress : accountAddressMap.entrySet()){
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String ApiKey = accoutAddress.getKey();
                        List<TraceRecordRequest> accountAddressList = accoutAddress.getValue();

                        for (TraceRecordRequest request : accountAddressList){
                            TraceRecordResponse traceRecord = new TraceRecordResponse();
                            String account = request.getAccount();
                            String recentTrxId = "";
                            try {
                                JSONObject accountObj = AcountInfoDao.getRecentlyRecord(account);
                                recentTrxId = accountObj.containsKey("_id") && accountObj.get("_id") != null?
                                        accountObj.getString("_id") : "";
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            String recentDate = TimeUtil.getDaysBefore(1);

                            do {
                                try {
                                    List<TraceRecordResponse.Trace> list = getHttpResult(request.toString());
                                    boolean flag = false;
                                    if (list == null || list.size() == 0){
                                        for (int i = 0; i < 5; i ++){
                                            list = getHttpResult(request.toString());
                                            if (list!= null && list.size() > 0){
                                               break;
                                            }
                                            Thread.sleep(200);
                                        }
                                        if (list == null || list.size() == 0){
                                            break;
                                        }
                                    }
                                    for (TraceRecordResponse.Trace trace : list){
                                        log.info("Account record info:" + request.getName() + " " + account + " " + trace);
                                        String trx_id = trace.getTrx_id();
                                        String key = trx_id + "&&" + trace.getReceiver() + "&&" + trace.getSender() + "&&" + trace.getTimestamp();
                                        key = MD5Util.string2MD5(key, 32);
                                        String timestamp = trace.getTimestamp();
                                        if (!recentTrxId.equals("")){
                                            if (trx_id.equals(recentTrxId)){
                                                flag = true;
                                                break;
                                            }else {
                                                Map<String, Object> esMap = traceToMap(trace, request.getName(), request.getAccount());
                                                AcountInfoDao.UpdateByScript(key, esMap);
                                            }
                                        }else {
                                            if (TimeUtil.compare(timestamp, recentDate)){
                                                flag = true;
                                                break;
                                            }else {
                                                Map<String, Object> esMap = traceToMap(trace, request.getName(), request.getAccount());
                                                AcountInfoDao.UpdateByScript(key, esMap);
                                            }
                                        }
                                    }
                                    if (flag){
                                        break;
                                    }
                                    int page = request.getPage();
                                    request.setPage(page + 1);
                                    Thread.sleep(200);
                                }catch (Exception e){
                                    e.printStackTrace();
                                    break;
                                }
                            }while (true);
                        }
                    }
                });
                thread.start();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static List<TraceRecordResponse.Trace> getHttpResult(String request){
        log.info("request info:" + EosConstant.EOSPARK_URL + request);
        Gson gson = new Gson();

        String reqResult = HttpReqUtil.getHttpServiceData(EosConstant.EOSPARK_URL + request, 100000,100000);
        if (reqResult != null){
            JSONObject object = JSONObject.fromObject(reqResult);
            if (object.getInt("errno") == 0){
                JSONObject data = object.containsKey("data") && object.get("data") != null
                        ? object.getJSONObject("data") : null;
                if (data != null){
                    String str = data.containsKey("trace_list") && data.get("trace_list") != null ?
                            data.getString("trace_list") : "";
                    if (!str.equals("")){
                        TraceRecordResponse.Trace[] trace = gson.fromJson(str, TraceRecordResponse.Trace[].class);
                        List<TraceRecordResponse.Trace> trace_list = Arrays.asList(trace);
                        return trace_list;
                    }
                }
            }
        }
        return null;
    }


    /**
     * 请求包装方法 把接口请求包装成合约名+请求list的形式
     * @return
     */
    private static Map<String, List<TraceRecordRequest>> packRequest() throws IOException {
        Map<String, Set<String>> accountMap = AcountInfoDao.getAllAcounts();
        Map<String, List<TraceRecordRequest>> result = new HashMap<>();
        int count = 0;

        List<TraceRecordRequest> list_0 = new ArrayList<>();
        List<TraceRecordRequest> list_1 = new ArrayList<>();
        List<TraceRecordRequest> list_2 = new ArrayList<>();
        List<TraceRecordRequest> list_3 = new ArrayList<>();
        List<TraceRecordRequest> list_4 = new ArrayList<>();
        for (Map.Entry<String, Set<String>> map : accountMap.entrySet()){
            String accoutName = map.getKey();
            Set<String> accountAddresses = map.getValue();
            for (String accountAddress : accountAddresses){
                TraceRecordRequest traceRecordRequest = new TraceRecordRequest();
                traceRecordRequest.setAccount(accountAddress);
                traceRecordRequest.setSize(EosConstant.PAGE_SIZE_DEFAULT);
                traceRecordRequest.setPage(EosConstant.PAGE_DEFAULT);
                traceRecordRequest.setSymbol(EosConstant.EOS_SYBOL);
                traceRecordRequest.setCode(EosConstant.EOS_CODE);
                traceRecordRequest.setName(accoutName);
                int index = count % ApiKeyList.size();
                traceRecordRequest.setApikey(ApiKeyList.get(index));
                switch (index){
                    case 0 :
                        list_0.add(traceRecordRequest);
                        break;
                    case 1 :
                        list_1.add(traceRecordRequest);
                        break;
                    case 2 :
                        list_2.add(traceRecordRequest);
                        break;
                    case 3 :
                        list_3.add(traceRecordRequest);
                        break;
                    case 4 :
                        list_4.add(traceRecordRequest);
                        break;
                }
                count ++;
            }
        }
        result.put(ApiKeyList.get(0), list_0);
        result.put(ApiKeyList.get(1), list_1);
        result.put(ApiKeyList.get(2), list_2);
        result.put(ApiKeyList.get(3), list_3);
        result.put(ApiKeyList.get(4), list_4);
        return result;
    }

    public static Map<String, Object> traceToMap(TraceRecordResponse.Trace trace, String accoutName, String account){
        Map<String, Object> map = new HashMap<>();
        map.put("trx_id", trace.getTrx_id());
        map.put("timestamp", trace.getTimestamp());
        map.put("receiver", trace.getReceiver());
        map.put("sender", trace.getSender());
        map.put("code", trace.getCode());
        map.put("quantity", trace.getQuantity());
        map.put("memo", trace.getMemo().toString());
        map.put("symbol", trace.getSymbol());
        map.put("name", accoutName);
        map.put("account", account);
        return map;

    }

    public static void main(String[] args) throws IOException {
        getTraceRecord();

//        closeClient();
//        closeRestHighLevelClient();
    }
}
