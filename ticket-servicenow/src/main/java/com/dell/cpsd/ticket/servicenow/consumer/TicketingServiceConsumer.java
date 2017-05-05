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
import com.dell.cpsd.ticket.servicenow.api.TicketDetails;
import com.dell.cpsd.ticket.servicenow.api.TicketServiceRequest;
import com.dell.cpsd.ticket.servicenow.api.TicketServiceResponse;
import com.dell.cpsd.ticket.servicenow.producer.TicketingServiceProducer;
import com.dell.cpsd.ticket.servicenow.services.TicketingIntegrationService;
/**
 * This class handles Ticketing Service messages.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * </p>
 *
 * @version 0.1
 * @since 0.1
 */
public class TicketingServiceConsumer extends DefaultMessageHandler<TicketServiceRequest>
{


	public TicketingServiceConsumer(TicketingIntegrationService service, TicketingServiceProducer producer, ErrorTransformer<HasMessageProperties<?>> errorTransformer) {
		 
		
		super(TicketServiceRequest.class, new DefaultMessageValidator<>(), "", errorTransformer);
		this.service = service;
		this.producer = producer;	

	}


	private static final Logger LOGGER = LoggerFactory.getLogger(TicketingServiceConsumer.class);

      /**
     * The service that actually handles the request
     */

   
   	private final TicketingIntegrationService service;
   	
   
    /**
     * Producer to rabbit mq with the response
     */
    private final TicketingServiceProducer producer;
	
    @Override
	protected void executeOperation(TicketServiceRequest request) throws Exception {
		LOGGER.info("Received message {}", request);
		
            
        String requestReplyTo = request.getMessageProperties().getReplyTo();
        
        String requestType = request.getRequestType();
        
        String responseCode = "FAILED"; 
        
        String incidentId = null;
        
        switch (requestType) {
		case "create":
			incidentId = service.createTicket(request);	
			if (incidentId != null)
			{
				responseCode = "SUCCESS"; 
			}			
			break;
		case "update":
			responseCode = service.updateTicket(request);
			incidentId = request.getTicketDetails().getIncidentId();
			break;
		case "approve":
			responseCode = service.approveTicket(request);
			incidentId = request.getTicketDetails().getIncidentId();
			break;
		case "close":
			responseCode = service.closeTicket(request);
			incidentId = request.getTicketDetails().getIncidentId();
			break;

		default:
			break;
		}      
             
        
       
        TicketServiceResponse response = new TicketServiceResponse();
		response.setResponseCode(responseCode); 
		response.setEventId(request.getEventId());
		TicketDetails ticketDetails = new TicketDetails();
		ticketDetails.setIncidentId(incidentId);
               
		//Don't set the replyTo..
        response.setMessageProperties(setMessageProperties());
        response.setTicketDetails(ticketDetails);
                
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



