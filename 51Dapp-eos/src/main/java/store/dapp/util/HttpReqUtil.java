package store.dapp.util;

import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: liulu3
 * @Date: 2018/11/1 20:42
 * @Description:
 */
public class HttpReqUtil {
    private static final Logger logger = Logger.getLogger(HttpReqUtil.class);

    public static String getHttpServiceDataByGet(String url, int connectTimeout, int socketTimeout){
        String result = null;
        try {
            CloseableHttpClient httpclient = HttpClients.custom().build();
            HttpGet httpGet = new HttpGet(url);
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(connectTimeout).setSocketTimeout(socketTimeout).build();
            httpGet.setConfig(requestConfig);

            CloseableHttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                if (entity != null) {
                    result = EntityUtils.toString(entity,"UTF-8");
                }
            }
            response.close();
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return result;
    }

    public static String getHttpServiceDataByPostJson(String url, String json, int connectTimeout, int socketTimeout){
        String result = null;
        try {
            CloseableHttpClient httpclient = HttpClients.custom().build();
            HttpPost httpPost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(connectTimeout).setSocketTimeout(socketTimeout).build();
            httpPost.setConfig(requestConfig);
            StringEntity params = new StringEntity(json,"UTF-8");
            httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");
            httpPost.setEntity(params);

            CloseableHttpResponse response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                if (entity != null) {
                    result = EntityUtils.toString(entity,"UTF-8");
                }
            }
            response.close();
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }

        return result;
    }

    public static String getHttpServiceDataByPostXWFU(String url, List<NameValuePair> postParams, int connectTimeout, int socketTimeout){
        String result = null;
        try {
            CloseableHttpClient httpclient = HttpClients.custom().build();
            HttpPost httpPost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(connectTimeout).setSocketTimeout(socketTimeout).build();
            httpPost.setConfig(requestConfig);
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.setEntity(new UrlEncodedFormEntity(postParams,"UTF-8"));

            CloseableHttpResponse response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                if (entity != null) {
                    result = EntityUtils.toString(entity,"UTF-8");
                }
            }
            response.close();
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }

        return result;
    }


    public static String getHttpServiceData(String url, int connectTimeout, int socketTimeout) {
        String result = null;
        try {
            CloseableHttpClient httpclient = HttpClients.custom().build();
            HttpGet httpget = new HttpGet(url);
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(connectTimeout).setSocketTimeout(socketTimeout).build();
            httpget.setConfig(requestConfig);

            CloseableHttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                if (entity != null) {
                    result = EntityUtils.toString(entity);
                }
            }
            response.close();
            httpclient.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return result;

    }

    public static void main(String[] args) {
        System.out.println(getHttpServiceData("http://172.30.160.89:9090/tc-supply-api/ifengnews/fiveEight", 10000, 10000));
    }
}
