/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 */

package com.dell.cpsd.ticket.servicenow.services;



/**
 * Ticketing Service custom exception class.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * </p>
 *
 * @version 0.1
 * @since 0.1
 */
public class TicketingServiceException extends Exception
{
    /*
     * The serial version identifier.
     */
    private static final long serialVersionUID = 13264591L;

    /**
     * TicketingServiceException constructor.
     *
     * @param message The exception message.
     * @since 0.1
     */
    public TicketingServiceException(final String message)
    {
        super(message);
    }

    /**
     * TicketingServiceException constructor.
     *
     * @param cause The cause of the exception.
     * @since 0.1
     */
    public TicketingServiceException(final Throwable cause)
    {
        super(cause);
    }

    /**
     * TicketingServiceException constructor.
     *
     * @param message The exception message.
     * @param cause   The cause of the exception.
     * @since 0.1
     */
    public TicketingServiceException(final String message, final Throwable cause)
    {
        super(message, cause);
    }
}

