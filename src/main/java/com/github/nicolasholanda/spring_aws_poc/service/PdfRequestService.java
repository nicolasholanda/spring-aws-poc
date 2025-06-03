package com.github.nicolasholanda.spring_aws_poc.service;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.regions.Region;
import org.springframework.beans.factory.annotation.Value;
import jakarta.annotation.PostConstruct;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PdfRequestService {
    @Value("${aws.sqs.queueUrl}")
    private String queueUrl;

    private SqsClient sqsClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        this.sqsClient = SqsClient.builder()
                .region(Region.US_EAST_1)
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
