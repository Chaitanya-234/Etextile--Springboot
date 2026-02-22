package com.etextile.TextileECommerce.Service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;

@Configuration
public class AmazonsesService {
    @Bean
    public AmazonSimpleEmailService getAmazonSimpleEmailService() {
        return AmazonSimpleEmailServiceClientBuilder.standard()
                .withCredentials(getAwsCredentialProvider())
                .withRegion(Regions.US_EAST_1)
                .build();
    }

    private AWSCredentialsProvider getAwsCredentialProvider() {
        AWSCredentials awsCredentials = new BasicAWSCredentials("???",
                "??");
        return new AWSStaticCredentialsProvider(awsCredentials);
    }

}
