/**
 * Copyright © 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.ticket.servicenow.capabilityintegration;

import static com.dell.cpsd.hdp.capability.registry.client.builder.AmqpProviderEndpointBuilder.AMQP_PROTOCOL;
import static org.springframework.util.Assert.notNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.dell.cpsd.hdp.capability.registry.api.Capability;
import com.dell.cpsd.hdp.capability.registry.api.Element;
import com.dell.cpsd.hdp.capability.registry.api.Identity;
import com.dell.cpsd.hdp.capability.registry.api.ProviderEndpoint;
import com.dell.cpsd.hdp.capability.registry.client.CapabilityRegistryException;
import com.dell.cpsd.hdp.capability.registry.client.ICapabilityRegistryBindingManager;
import com.dell.cpsd.hdp.capability.registry.client.ICapabilityRegistryLookupManager;
import com.dell.cpsd.hdp.capability.registry.client.binder.CapabilityBinder;
import com.dell.cpsd.hdp.capability.registry.client.binder.CapabilityBindingService;
import com.dell.cpsd.hdp.capability.registry.client.binder.CapabilityMatcher;
import com.dell.cpsd.hdp.capability.registry.client.binder.rpc.AmqpRpcCapabilityBindingService;
import com.dell.cpsd.hdp.capability.registry.client.binding.amqp.producer.IAmqpCapabilityRegistryControlProducer;
import com.dell.cpsd.hdp.capability.registry.client.builder.AmqpProviderEndpointBuilder;
import com.dell.cpsd.service.common.client.context.IConsumerContextConfig;
import com.dell.cpsd.service.common.client.exception.ServiceTimeoutException;
import com.dell.cpsd.ticket.servicenow.config.TicketingServiceRabbitConfig;

/**
 * A class to handle the integration between the Ticketing Service and the Capability Registry.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * </p>
 *
 * @version 1.0
 * @since 1.0
 */
@Component
public class CapabilityRegistryIntegration
{
    /**
     * The logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CapabilityRegistryIntegration.class);

    /**
     * The capability registry binding manager.
     */
    private final ICapabilityRegistryBindingManager capabilityRegistryBindingManager;
    
   
        
    /**
     * The consumer context for this data provider.
     */
    private final IConsumerContextConfig consumerContextConfig;
  

    private final IAmqpCapabilityRegistryControlProducer capabilityRegistryControlProducer;

	private final TopicExchange ticketServiceRequestExchange;
	private final TopicExchange ticketServiceResponseExchange;

    @Autowired
    public CapabilityRegistryIntegration(final ICapabilityRegistryBindingManager capabilityRegistryBindingManager,
    		final TopicExchange ticketServiceRequestExchange,
    		final TopicExchange ticketServiceResponseExchange,
    		final IConsumerContextConfig consumerContextConfig, 
            final IAmqpCapabilityRegistryControlProducer capabilityRegistryControlProducer)
    {
        notNull(capabilityRegistryBindingManager, "capabilityRegistryBindingManager cannot be null");
        notNull(consumerContextConfig, "consumerContextConfig cannot be null");
        notNull(capabilityRegistryControlProducer, "capabilityRegistryControlProducer cannot be null");
        notNull(ticketServiceRequestExchange, "ticketServiceRequestExchange cannot be null");
        notNull(ticketServiceResponseExchange, "ticketServiceResponseExchange cannot be null");

        this.capabilityRegistryBindingManager = capabilityRegistryBindingManager;
        this.consumerContextConfig = consumerContextConfig;
        this.capabilityRegistryControlProducer = capabilityRegistryControlProducer;
        this.ticketServiceRequestExchange = ticketServiceRequestExchange;
        this.ticketServiceResponseExchange = ticketServiceResponseExchange;
        
        
    }  
      
    

    public void start()
    {
        register();

        this.capabilityRegistryBindingManager.start();
    }

    /**
     * This is called when the application context is closed.
     *
     * @param event The context closed event.
     * @since 1.0
     */
    @EventListener
    public void event(ContextClosedEvent event) throws Exception
    {
        this.unregister();

        //        this.stop();
    }

    /**
     * Register with the capability registry
     */
    private void register()
    {
        // create the capability provider identity for this data provider
        final Identity identity = new Identity(consumerContextConfig.consumerName(), consumerContextConfig.consumerUuid());

        final List<Capability> capabilities = new ArrayList<>();
        capabilities.add(createLookupCapability());

        // register with the capability registry
        try
        {
            capabilityRegistryBindingManager.registerCapabilityProvider(identity, capabilities);
        }
        catch (CapabilityRegistryException exception)
        {
            LOGGER.error(exception.getMessage(), exception);

        }
        catch (Exception exception)
        {
            LOGGER.error(exception.getMessage(), exception);
        }

    }

    /**
     * This unregisters the capabilities.
     *
     * @since 1.0
     */
    private void unregister()
    {
        if ((this.consumerContextConfig != null) && (this.capabilityRegistryBindingManager != null))
        {
            final Identity identity = new Identity(this.consumerContextConfig.consumerName(), this.consumerContextConfig.consumerUuid());

            try
            {
                // TODO: amend client code which is currently failing a null assertion during this call
                // this.capabilityRegistryBindingManager.unregisterCapabilityProvider(identity);
                this.capabilityRegistryBindingManager.stop();
                String correlationId = UUID.randomUUID().toString();
                this.capabilityRegistryControlProducer.publishUnregisterCapabilityProvider(correlationId, identity);
            }
            catch (CapabilityRegistryException exception)
            {
                LOGGER.error(exception.getMessage(), exception);
            }
        }
    }

    private Capability createLookupCapability()
    {
    	 // create the Ticketing Service endpoint and capability
        AmqpProviderEndpointBuilder endpointBuilder = new AmqpProviderEndpointBuilder(AMQP_PROTOCOL);

        // TODO: Extract these properties/capabilities.
        endpointBuilder.requestExchange(ticketServiceRequestExchange.getName());
        endpointBuilder.requestRoutingKey(TicketingServiceRabbitConfig.ROUTING_KEY_TICKET_REQUEST);
        endpointBuilder.requestMessageType("com.dell.cpsd.ticket.request");
        
        // TODO: Extract these properties/capabilities.
        endpointBuilder.responseExchange(ticketServiceResponseExchange.getName());
        endpointBuilder.responseRoutingKey(TicketingServiceRabbitConfig.ROUTING_KEY_TICKET_RESPONSE);
        endpointBuilder.responseMessageType("com.dell.cpsd.ticket.response");

        final ProviderEndpoint providerEndpoint = endpointBuilder.build();
        final List<Element> elements = Collections.emptyList();

        return new Capability("raise-service-ticket", elements, providerEndpoint);
    }
}
