package com.fosun.data.cleanup.comment.tag.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author: liumch
 * @create: 2019/8/7 10:24
 **/
@Configuration
@Slf4j
public class RestTemplateConfig {

    @Value("${HTTP_CLIENT_RETRY_COUNT}")
    private int HTTP_CLIENT_RETRY_COUNT ;
    @Value("${MAXIMUM_TOTAL_CONNECTION}")
    private int MAXIMUM_TOTAL_CONNECTION ;
    @Value("${MAXIMUM_CONNECTION_PER_ROUTE}")
    private int MAXIMUM_CONNECTION_PER_ROUTE ;
    @Value("${CONNECTION_VALIDATE_AFTER_INACTIVITY_MS}")
    private int CONNECTION_VALIDATE_AFTER_INACTIVITY_MS ;

    @Value("${CONNECT_TIMEOUT}")
    private int CONNECT_TIMEOUT;
    @Value("${READ_TIMEOUT}")
    private int READ_TIMEOUT;
    @Value("${CONNECT_REQUSET_TIMEOUT}")
    private int CONNECT_REQUSET_TIMEOUT;


    @Bean
    public  HttpComponentsClientHttpRequestFactory getSimpleHttpRequestFactory(){
        HttpClientBuilder clientBuilder = HttpClients.custom();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        // Set the maximum number of total open connections.
        connectionManager.setMaxTotal(MAXIMUM_TOTAL_CONNECTION);
        // Set the maximum number of concurrent connections per route, which is 2 by default.
        connectionManager.setDefaultMaxPerRoute(MAXIMUM_CONNECTION_PER_ROUTE);

        connectionManager.setValidateAfterInactivity(CONNECTION_VALIDATE_AFTER_INACTIVITY_MS);

        clientBuilder.setConnectionManager(connectionManager);

        clientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(HTTP_CLIENT_RETRY_COUNT, true, new ArrayList<>()) {
            @Override
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                log.info(String.format("Retry request, execution count: %d, exception: %s", executionCount, exception));
                return super.retryRequest(exception, executionCount, context);
            }

        });
        HttpComponentsClientHttpRequestFactory  httpRequestFactory = new HttpComponentsClientHttpRequestFactory(HttpClients.createDefault());
        httpRequestFactory.setConnectTimeout(CONNECT_TIMEOUT);
        httpRequestFactory.setReadTimeout(READ_TIMEOUT);
        httpRequestFactory.setConnectionRequestTimeout(CONNECT_REQUSET_TIMEOUT);
        return httpRequestFactory;
    }

    @Bean
    public  RestTemplate getRestTemplate(){
        RestTemplate restTemplate = new RestTemplate(getSimpleHttpRequestFactory());
        // 使用 utf-8 编码集的 conver 替换默认的 conver（默认的 string conver 的编码集为"ISO-8859-1"）
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();

        Iterator<HttpMessageConverter<?>> iterator = messageConverters.iterator();
        while (iterator.hasNext()) {
            HttpMessageConverter<?> converter = iterator.next();
            if (converter instanceof StringHttpMessageConverter) {
                iterator.remove();
            }
        }

        messageConverters.add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.asList(new MediaType[]{MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM}));
        messageConverters.add(1,converter);

        return restTemplate;
    }

}
