/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 */
package com.dell.cpsd.ticket.servicenow.producer;

import com.dell.cpsd.ticket.servicenow.api.TicketServiceResponse;
import com.dell.cpsd.ticket.servicenow.services.TicketingServiceException;

/**
 * Ticketing Service Producer Interface
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * </p>
 *
 * @version 0.1
 * @since 0.1
 */
public interface TicketingServiceProducer
{

	/**
	 * Sends a response to a ticket service request
	 * @param response
	 * @param replyTo
	 * @throws TicketingServiceException
	 */
    void sendResponse(final TicketServiceResponse response, final String replyTo) throws TicketingServiceException;
   
    
}
