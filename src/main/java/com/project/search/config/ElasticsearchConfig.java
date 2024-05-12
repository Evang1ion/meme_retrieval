package com.project.search.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.ssl.SSLContextBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;

@Configuration
public class ElasticsearchConfig {

    @Value("${elasticsearch.host}")
    private String elasticsearchHost;

    @Value("${elasticsearch.port}")
    private int elasticsearchPort;

    @Value("${elasticsearch.scheme}")
    private String elasticsearchScheme; //指定协议

    @Value("${elasticsearch.username}")
    private String username;

    @Value("${elasticsearch.password}")
    private String password;

    @Bean
    public ElasticsearchClient elasticsearchClient() throws Exception {
        //Forbidden ssl
        final SSLContext sslContext = SSLContextBuilder.create()
                .loadTrustMaterial((chain, authType) -> true)
                .build();

        RestClientBuilder builder = RestClient.builder(
                new HttpHost(elasticsearchHost, elasticsearchPort, elasticsearchScheme))
                .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                        .setSSLContext(sslContext)
                        .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE) 
                        .setDefaultCredentialsProvider(new BasicCredentialsProvider() {{
                            //set user name and password
                            setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
                        }}));

        RestClient restClient = builder.build();

        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new co.elastic.clients.json.jackson.JacksonJsonpMapper());

        return new ElasticsearchClient(transport);
    }
}
