package store.dapp.ESFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;


public class EsClientFactory {
    private static final Logger logger = Logger.getLogger(EsClientFactory.class);

    private static RestClientBuilder restClientBuilder;
    private static RestClient restClient;
    private static RestHighLevelClient highLevelClient;

    static  {
		initEsClient();
    }

    private static void initEsClient() {
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials("elastic", "51DApp6666"));

        restClientBuilder = RestClient.builder(new HttpHost("es-cn-4590v0ixf0001ynym.public.elasticsearch.aliyuncs.com", 9200))
                .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                    @Override
                    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                        return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    }
                });
        restClient = restClientBuilder.build();
        highLevelClient = new RestHighLevelClient(restClientBuilder);
    }

    public static RestClient getClient() {
        if (restClient == null) {
            logger.error("get es client error, client is null");
            return null;
        } else {
            return restClient;
        }
    }

    public static RestHighLevelClient getRestHighLevelClient() {
        if (highLevelClient == null) {
            logger.error("get es client error, highLevelClient is null");
            return null;
        } else {
            return highLevelClient;
        }
    }

    public static void closeClient() throws IOException {
        restClient.close();
    }

    public static void closeRestHighLevelClient() throws IOException {
        highLevelClient.close();
    }

    public static void main(String[] args) throws IOException {

    }
}