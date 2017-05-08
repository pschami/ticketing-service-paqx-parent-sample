/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 */

package com.dell.cpsd.ticket.servicenow.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * Development config that instantiates un-authenticated
 * connection factory.
 * <b>This should only be used for development purpose.</b>
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * </p>
 *
 * @version 0.1
 * @since 0.1
 */
@Configuration
public class TicketingServiceDevelopmentConfig
{
    protected static final String PROFILE  = "development";
    private static final   String HOSTNAME = System.getProperty("container.id");
    /*
     * The properties configuration.
     */
    @Autowired
    private TicketingServicePropertiesConfig propertiesConfig;

}

