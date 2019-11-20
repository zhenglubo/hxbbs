package com.lixin.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.Objects;

/**
 * @description: elasticsearch客户端配置
 * @author: zhenglubo
 * @create: 2019-10-17 16:06
 **/

@Configuration
@Component
@Slf4j
public class ElasticSearchClientConfiguration {

    @Value(value = "${spring.data.elasticsearch.repositories.enabled}")
    private boolean enabled;
    @Value(value = "${spring.data.elasticsearch.cluster.nodes}")
    private String[] ipAddress;
    @Value(value = "${spring.data.elasticsearch.cluster.name}")
    private String clusterName;
    private static final String HTTP_SCHEME = "http";
    private static final int ADDRESS_LENGTH=2;

    @Bean
    public RestClientBuilder restClientBuilder() {
        HttpHost[] hosts = Arrays.stream(ipAddress)
                .map(this::makeHttpHost)
                .filter(Objects::nonNull)
                .toArray(HttpHost[]::new);
        log.debug("hosts:{}", Arrays.toString(hosts));
        return RestClient.builder(hosts);
    }


    @Bean(name = "highLevelClient")
    public RestHighLevelClient highLevelClient(@Autowired RestClientBuilder restClientBuilder) {
        return new RestHighLevelClient(restClientBuilder);
    }


    private HttpHost makeHttpHost(String s) {
        assert StringUtils.isNotEmpty(s);
        String[] address = s.split(":");
        if (address.length == ADDRESS_LENGTH) {
            String ip = address[0];
            int port = Integer.parseInt(address[1]);
            return new HttpHost(ip, port, HTTP_SCHEME);
        } else {
            return null;
        }
    }
}
