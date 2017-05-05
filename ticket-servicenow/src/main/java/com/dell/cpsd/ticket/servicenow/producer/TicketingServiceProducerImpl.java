/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 */

package com.dell.cpsd.ticket.servicenow.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.dell.cpsd.ticket.servicenow.api.TicketServiceErrorResponse;
import com.dell.cpsd.ticket.servicenow.api.TicketServiceResponse;
import com.dell.cpsd.ticket.servicenow.config.TicketingServiceRabbitConfig;
import com.dell.cpsd.ticket.servicenow.services.TicketingServiceException;

/**
 * Ticketing Service Producer implementation class.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * </p>
 *
 * @version 0.1
 * @since 0.1
 */
public class TicketingServiceProducerImpl implements TicketingServiceProducer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(TicketingServiceProducerImpl.class);
    private Exchange       ticketServiceResponseExchange;
    private RabbitTemplate rabbitTemplate;


    /**
     * @param rabbitTemplate                    Rabbit Template
     * @param ticketServiceResponseExchange Event Exchange
     */
    public TicketingServiceProducerImpl(final RabbitTemplate rabbitTemplate, final Exchange ticketServiceResponseExchange)
    {
        this.ticketServiceResponseExchange = ticketServiceResponseExchange;
        this.rabbitTemplate = rabbitTemplate;
 
    }

	@Override
	public void sendResponse(TicketServiceResponse response, String replyTo) throws TicketingServiceException {
		
		
		final String routingKey = TicketingServiceRabbitConfig.ROUTING_KEY_TICKET_RESPONSE.replace("{replyTo}", "." + replyTo);
		
        LOGGER.info("Ticket Service Response,sending to exchange [{}] with routing key [{}]",
        		ticketServiceResponseExchange.getName(), routingKey);
        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug(response.toString());
        }
        rabbitTemplate.convertAndSend(ticketServiceResponseExchange.getName(), routingKey, response);
		
	}
    
}
    

