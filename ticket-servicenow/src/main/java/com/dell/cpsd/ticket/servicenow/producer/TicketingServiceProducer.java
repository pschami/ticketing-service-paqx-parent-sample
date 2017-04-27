package com.dell.cpsd.ticket.servicenow.producer;

import com.dell.cpsd.ticket.servicenow.api.TicketServiceErrorResponse;
import com.dell.cpsd.ticket.servicenow.api.TicketServiceResponse;
import com.dell.cpsd.ticket.servicenow.services.TicketingServiceException;

/**
 * Ticketing Service Producer Interface
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * </p>
 *
 * @version 1.0
 * @since 1.0
 */
public interface TicketingServiceProducer
{

	/**
	 * 
	 * @param response
	 * @param replyTo
	 * @throws TicketingServiceException
	 */
    void sendResponse(final TicketServiceResponse response, final String replyTo) throws TicketingServiceException;
    
    /**
     * 
     * @param response
     * @param replyTo
     * @throws TicketingServiceException
     */
    void sendResponse(final TicketServiceErrorResponse response, final String replyTo) throws TicketingServiceException;

   
    
}