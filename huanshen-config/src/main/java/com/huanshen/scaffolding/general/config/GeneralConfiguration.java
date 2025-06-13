package com.huanshen.scaffolding.general.config;

import com.huanshen.scaffolding.common.utils.RedisDelayQueue;
import com.huanshen.scaffolding.common.utils.RedisLock;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @since 2023/11/21 14:10
 */
@Configuration
public class GeneralConfiguration {
    //    @LoadBalance
    @Bean
    public RestTemplate restTemplate() {
        ConnectionPool connectionPool = new ConnectionPool(300, 5, TimeUnit.MINUTES);
        OkHttpClient.Builder builder = new OkHttpClient.Builder().connectionPool(connectionPool);
        // 忽略证书验证
        builder.sslSocketFactory(createSSLSocketFactory(), creatTrustAllCerts());
        builder.hostnameVerifier(createTrustAllHostnameVerifier());
        OkHttp3ClientHttpRequestFactory okhttp3 = new OkHttp3ClientHttpRequestFactory(builder.build());
        //设置连接超时时间

        okhttp3.setConnectTimeout(3000);
        //设置读取超时时间
        okhttp3.setWriteTimeout(3000);
        RestTemplate restTemplate = new RestTemplate(okhttp3);
        // 解决中文乱码
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        messageConverters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));

        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) {
                return false;
            }
        });
        return restTemplate;

    }

    @Bean
    public RestTemplate restTemplateNoBalance() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // 忽略证书验证
        builder.sslSocketFactory(createSSLSocketFactory(), creatTrustAllCerts());
        builder.hostnameVerifier(createTrustAllHostnameVerifier());
        OkHttp3ClientHttpRequestFactory okhttp3 = new OkHttp3ClientHttpRequestFactory(builder.build());
        //设置连接超时时间

        okhttp3.setConnectTimeout(3000);
        //设置读取超时时间
        okhttp3.setReadTimeout(3000);
        okhttp3.setWriteTimeout(3000);
        RestTemplate restTemplate = new RestTemplate(okhttp3);
        // 解决中文乱码
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        messageConverters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }

    @Bean
    public RestTemplate restTemplateLoadBalance() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // 忽略证书验证
        builder.sslSocketFactory(createSSLSocketFactory(), creatTrustAllCerts());
        builder.hostnameVerifier(createTrustAllHostnameVerifier());
        OkHttp3ClientHttpRequestFactory okhttp3 = new OkHttp3ClientHttpRequestFactory(builder.build());
        //设置连接超时时间

        okhttp3.setConnectTimeout(3000);
        //设置读取超时时间
        okhttp3.setReadTimeout(3000);
        okhttp3.setWriteTimeout(3000);
        RestTemplate restTemplate = new RestTemplate(okhttp3);
        // 解决中文乱码
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        messageConverters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }


    public SSLSocketFactory createSSLSocketFactory() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                }
            }};

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private HostnameVerifier createTrustAllHostnameVerifier() {
        return (hostname, session) -> true;
    }

    private X509TrustManager creatTrustAllCerts() {
        return new X509TrustManager() {

            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
    }

    @Bean
    public RedisLock redisLock(RedissonClient redissonClient) {
        return new RedisLock(redissonClient);
    }

    @Bean
    public RedisDelayQueue redisDelayQueue(RedissonClient redissonClient) {
        return new RedisDelayQueue(redissonClient);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        // key采用String的序列化方式
        template.setKeySerializer(new StringRedisSerializer());
        // value序列化方式采用jackson
        template.setValueSerializer(new StringRedisSerializer());
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(new StringRedisSerializer());
        // hash的value序列化方式采用jackson
        template.setHashValueSerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }
}
