/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 */

package com.dell.cpsd.ticket.servicenow.config;

import com.dell.cpsd.common.rabbitmq.config.RabbitMQPropertiesConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/**
 *  Ticketing Service properties config.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * </p>
 *
 * @version 1.0
 * @since 1.0
 */
@Configuration
@PropertySources({@PropertySource(value = "classpath:META-INF/spring/ticket-servicenow/rabbitmq.properties"),
        @PropertySource(value = "file:/opt/dell/cpsd/registration-services/ticket-servicenow/conf/rabbitmq-config.properties", ignoreResourceNotFound = true)})
@Qualifier("rabbitPropertiesConfig")
public class TicketingServicePropertiesConfig extends RabbitMQPropertiesConfig
{
    /**
     * Ticketing Service Properties Config constructor
     */
    public TicketingServicePropertiesConfig()
    {
        super();
    }
}

