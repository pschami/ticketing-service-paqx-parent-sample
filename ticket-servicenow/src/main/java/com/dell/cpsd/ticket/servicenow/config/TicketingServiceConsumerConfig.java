/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 */

package com.dell.cpsd.ticket.servicenow.config;

import javax.annotation.Resource;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.dell.cpsd.common.rabbitmq.consumer.error.DefaultErrorTransformer;
import com.dell.cpsd.common.rabbitmq.consumer.error.ErrorTransformer;
import com.dell.cpsd.common.rabbitmq.consumer.handler.DefaultMessageListener;
import com.dell.cpsd.common.rabbitmq.context.builder.DefaultContainerErrorHandler;
import com.dell.cpsd.common.rabbitmq.message.HasMessageProperties;
import com.dell.cpsd.ticket.servicenow.api.TicketServiceErrorResponse;
import com.dell.cpsd.ticket.servicenow.consumer.TicketingServiceConsumer;
import com.dell.cpsd.ticket.servicenow.producer.TicketingServiceProducer;
import com.dell.cpsd.ticket.servicenow.services.TicketingIntegrationService;

/**
 * The Ticketing Service Consumer Spring Config class
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * </p>
 *
 * @version 0.1
 * @since 0.1
 */
@Configuration
@ComponentScan(basePackages = {TicketingServiceProductionConfig.CONFIG_PACKAGE})
public class TicketingServiceConsumerConfig
{
   
    /*
     * The RabbitMQ Connection Factory.
     */
    @Autowired
    @Qualifier("rabbitConnectionFactory")
    private ConnectionFactory             rabbitConnectionFactory;
    @Resource(name = "rabbitTemplate")
    private RabbitTemplate                rabbitTemplate;
  
    /*
     * The message converter to be used.
     */
    @Autowired
    private MessageConverter              messageConverter;
    
    
    @Autowired
    private TicketingIntegrationService ticketingService;
    
    @Autowired
    private TicketingServiceProducer ticketingServiceProducer;
  

    @Autowired
    private Queue ticketServiceRequestQueue;
    
    @Autowired
    private String replyTo;

    /**
     * This bean instantiates and returns the Simple message
     * listener container.
     *
     * @return Simple message listener container
     */
    @Bean
    SimpleMessageListenerContainer ticketingServiceListenerContainer()
    {
        final SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(rabbitConnectionFactory);
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
        container.setQueues(ticketServiceRequestQueue);
        container.setMessageListener(ticketingServiceListener());
        container.setErrorHandler(new DefaultContainerErrorHandler("ticketingServiceListenerContainer"));
        return container;
    }

    /**
     * This is the message listener for Ticketing Service listener container.
     *
     * @return Default message listener
     */
    @Bean
    TicketingServiceConsumer ticketingServiceMessageHandler()
    {
        return new TicketingServiceConsumer(ticketingService, ticketingServiceProducer, messageErrorTransformer());
    }


    @Bean
    DefaultMessageListener ticketingServiceListener()
    {
    	return new DefaultMessageListener(messageConverter, ticketingServiceMessageHandler());
		
    }
    
    ErrorTransformer<HasMessageProperties<?>> messageErrorTransformer()
    {
        return new DefaultErrorTransformer<>(
        		TicketingServiceRabbitConfig.EXCHANGE_TICKET_RESPONSE,
                replyTo,
                () -> new TicketServiceErrorResponse().withMessageProperties(), null);                         
       
       
    }
 
}

