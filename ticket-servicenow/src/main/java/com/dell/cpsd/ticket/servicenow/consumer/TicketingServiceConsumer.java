/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 */

package com.dell.cpsd.ticket.servicenow.consumer;

import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.dell.cpsd.common.rabbitmq.consumer.error.ErrorTransformer;
import com.dell.cpsd.common.rabbitmq.consumer.handler.DefaultMessageHandler;
import com.dell.cpsd.common.rabbitmq.message.HasMessageProperties;
import com.dell.cpsd.common.rabbitmq.validators.DefaultMessageValidator;
import com.dell.cpsd.ticket.servicenow.api.MessageProperties;
import com.dell.cpsd.ticket.servicenow.api.TicketServiceRequest;
import com.dell.cpsd.ticket.servicenow.api.TicketServiceResponse;
import com.dell.cpsd.ticket.servicenow.producer.TicketingServiceProducer;
import com.dell.cpsd.ticket.servicenow.services.TicketingServiceService;
/**
 * This class handles Ticketing Service messages.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * </p>
 *
 * @version 1.0
 * @since 1.0
 */
public class TicketingServiceConsumer extends DefaultMessageHandler<TicketServiceRequest>
{


	public TicketingServiceConsumer(TicketingServiceService service, TicketingServiceProducer producer, ErrorTransformer<HasMessageProperties<?>> errorTransformer) {
		 
		
		super(TicketServiceRequest.class, new DefaultMessageValidator<>(), "", errorTransformer);
		this.service = service;
		this.producer = producer;	

	}


	private static final Logger LOGGER = LoggerFactory.getLogger(TicketingServiceConsumer.class);

      /**
     * The service that actually handles the request
     */

   
   	private final TicketingServiceService service;
   	
   
    /**
     * Producer to rabbit mq with the response
     */
    private final TicketingServiceProducer producer;
	
    @Override
	protected void executeOperation(TicketServiceRequest request) throws Exception {
		LOGGER.info("Received message {}", request);
		
            
        String requestReplyTo = request.getMessageProperties().getReplyTo();
        String responseCode = service.createTicket(request);
        TicketServiceResponse response = new TicketServiceResponse();
		response.setResponseCode(responseCode);       
               
		//Don't set the replyTo..
        response.setMessageProperties(setMessageProperties());
                
        producer.sendResponse(response, requestReplyTo); 
  
		
	}
	
	 private MessageProperties setMessageProperties()
	    {
	        final MessageProperties messageProperties = new MessageProperties();
	        messageProperties.setTimestamp(new Date());
	        messageProperties.setCorrelationId((UUID.randomUUID().toString()));
	        return messageProperties;
	    }
	
}



