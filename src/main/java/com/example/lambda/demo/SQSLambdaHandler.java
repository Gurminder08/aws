package com.example.lambda.demo;

import java.util.List;
import java.util.Map;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.lambda.AbstractAWSLambda;
import com.amazonaws.services.lambda.model.CreateEventSourceMappingRequest;
import com.amazonaws.services.lambda.model.CreateEventSourceMappingResult;
import com.amazonaws.services.lambda.model.CreateEventSourceMappingRequest;
import com.amazonaws.services.lambda.model.CreateEventSourceMappingResult;


public class SQSLambdaHandler extends  AbstractAWSLambda implements RequestHandler<Object, String> {

	private static final String awsAccessKeyId = "********";
	private static final String awsSecretAccessKey = "*******";
	private static final String regionName = "ca-central-1";
	private static AWSCredentials credentials = new BasicAWSCredentials(awsAccessKeyId, awsSecretAccessKey);
	private static AmazonSQS client = AmazonSQSClientBuilder.standard()
			.withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(regionName).build();

	@Override
	public String handleRequest(Object input, Context context) {

		context.getLogger().log("Input: " + input);
		//String message = input.getRecords().get(0).getSNS().getMessage();
		
		workSQS();
		// TODO: implement your handler
		return "Hello from Lambda!";
		
		
	}

	@Override
	public CreateEventSourceMappingResult createEventSourceMapping(CreateEventSourceMappingRequest request) {
		// TODO Auto-generated method stub
		
		return super.createEventSourceMapping(request);
	}

	public void workSQS() {

		
		
		
		CreateEventSourceMappingRequest request = new CreateEventSourceMappingRequest();
		request.setEventSourceArn("https://sqs.ca-central-1.amazonaws.com/*****/demoQueue");
		CreateEventSourceMappingResult result = createEventSourceMapping(request);
		
		// Receive messages.

		System.out.println("Receiving messages from MyQueue.\n");
		final ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(
				"https://sqs.ca-central-1.amazonaws.com/*****/demoQueue");
		final List<Message> messages = client.receiveMessage(receiveMessageRequest.withMessageAttributeNames("All"))
				.getMessages();

		for (final Message message : messages) {

			System.out.println("Message");
			System.out.println("  MessageId:     " + message.getMessageId());
			System.out.println("  ReceiptHandle: " + message.getReceiptHandle());
			System.out.println("  MD5OfBody:     " + message.getMD5OfBody());
			System.out.println("  Body:          " + message.getBody());

			for (final Map.Entry<String, MessageAttributeValue> entry : message.getMessageAttributes().entrySet()) {

				System.out.println("Attribute");
				System.out.println("  Name:  " + entry.getKey());
				System.out.println("  Value: " + entry.getValue().getStringValue());

			}

		}

		System.out.println();

		// Delete the message.

		// logger.log("Deleting a message.\n");
		// final String messageReceiptHandle = messages.get(0).getReceiptHandle(); //
		// identify different messages
		// client.deleteMessage(new
		// DeleteMessageRequest("https://sqs.ca-central-1.amazonaws.com/002741887385/demoQueue",
		// messageReceiptHandle));

	}

}
