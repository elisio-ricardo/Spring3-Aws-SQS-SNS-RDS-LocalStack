package br.com.elisio.spring3awsLocalStack.config;


import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSAsyncClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.config.QueueMessageHandlerFactory;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.QueueMessageHandler;
import org.springframework.cloud.aws.messaging.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class LocalStackCloudConfig  {

    @Value("${spring.cloud.aws.endpoint}")
    private String host;

    @Value("${spring.cloud.aws.credentials.access-key}")
    protected String accessKeyId;

    @Value("${spring.cloud.aws.credentials.secret-key}")
    protected String secretAccessKey;

    @Value("${spring.cloud.aws.region.static}")
    protected String region;

    @Bean
    public AmazonSQSAsync amazonSQSAsync() {
        return AmazonSQSAsyncClientBuilder.standard()
                .withEndpointConfiguration(getEndpointConfiguration())
                .withCredentials(getCredentialsProvider())
                .build();
    }

    @Bean
    @Primary
    public AmazonSNS amazonSNSAsync() {
        return AmazonSNSAsyncClientBuilder.standard()
                .withEndpointConfiguration(getEndpointConfiguration())
                .withCredentials(getCredentialsProvider())
                .build();
    }

    @Bean
    private static AmazonS3 getLocalS3() {
        BasicSessionCredentials awsCreds = new BasicSessionCredentials("test", "test", "");
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:4566", "us-east-1"))
                .enablePathStyleAccess()
                .build();
    }


    @Bean
    public QueueMessagingTemplate queueMessagingTemplate() {
        return new QueueMessagingTemplate(amazonSQSAsync());
    }

    /** this bean is responsible for listening to the queue and must be defined. */
    @Bean
    public QueueMessageHandler queueMessageHandler() {
        var queueMessageHandlerFactory = new QueueMessageHandlerFactory();
        queueMessageHandlerFactory.setAmazonSqs(amazonSQSAsync());
        return queueMessageHandlerFactory.createQueueMessageHandler();
    }

    /** this bean is responsible for listening to the queue and must be defined. */
    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer() {
        var simpleListenerContainer = new SimpleMessageListenerContainer();
        simpleListenerContainer.setAmazonSqs(amazonSQSAsync());
        simpleListenerContainer.setMessageHandler(queueMessageHandler());
        return simpleListenerContainer;
    }

    protected AWSStaticCredentialsProvider getCredentialsProvider() {
        return new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKeyId, secretAccessKey));
    }

    @Bean
    public NotificationMessagingTemplate notificationMessagingTemplate(AmazonSNS amazonSNS) {
        return new NotificationMessagingTemplate(amazonSNS);
    }

    private EndpointConfiguration getEndpointConfiguration() {
        return new EndpointConfiguration(host, region);
    }
}