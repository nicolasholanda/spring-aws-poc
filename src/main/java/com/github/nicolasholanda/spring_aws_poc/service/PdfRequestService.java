package com.github.nicolasholanda.spring_aws_poc.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.net.URI;

@Service
public class PdfRequestService {
    @Value("${aws.sqs.queueUrl}")
    private String queueUrl;
    @Value("${aws.sqs.endpoint:http://localhost:4566}")
    private String sqsEndpoint;
    @Value("${aws.sqs.accessKey:test}")
    private String accessKey;
    @Value("${aws.sqs.secretKey:test}")
    private String secretKey;

    private SqsClient sqsClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        this.sqsClient = SqsClient.builder()
                .region(Region.US_EAST_1)
                .endpointOverride(URI.create(sqsEndpoint))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)
                ))
                .build();
    }

    public void sendPdfRequest(String text, String email) {
        try {
            String messageBody = objectMapper.writeValueAsString(new PdfRequestMessage(text, email));
            SendMessageRequest sendMsgRequest = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(messageBody)
                    .build();
            sqsClient.sendMessage(sendMsgRequest);
        } catch (Exception e) {
            throw new RuntimeException("Error while sending message to SQS", e);
        }
    }

    private record PdfRequestMessage(String text, String email) {}
}
