/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 */

package com.dell.cpsd.ticket.servicenow.config;

import com.dell.cpsd.ticket.servicenow.producer.TicketingServiceProducer;
import com.dell.cpsd.ticket.servicenow.producer.TicketingServiceProducerImpl;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * This the Ticketing Service producer spring config required
 * for instantiating the producer instance.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * </p>
 *
 * @version 0.1
 * @since 0.1
 */
@Configuration
@ComponentScan(TicketingServiceProductionConfig.CONFIG_PACKAGE)
public class TicketingServiceProducerConfig
{
    /**
     * The Spring RabbitMQ template.
     */
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Exchange ticketServiceResponseExchange;

    @Autowired
    private String hostName;

    @Bean
    TicketingServiceProducer  ticketingServiceProducer()
    {
        return new TicketingServiceProducerImpl(rabbitTemplate, ticketServiceResponseExchange);
    }
  
}

