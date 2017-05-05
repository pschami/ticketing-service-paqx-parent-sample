/**
 * Copyright © 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 */

package com.dell.cpsd.ticket.servicenow; 

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;

import com.dell.cpsd.hdp.capability.registry.client.binding.config.CapabilityRegistryBindingManagerConfig;
import com.dell.cpsd.hdp.capability.registry.client.lookup.config.CapabilityRegistryLookupManagerConfig;

/**
 * Ticketing Service application class
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * </p>
 *
 * @version 0.1
 * @since 0.1
 */
@SpringBootApplication
@EnableAsync
@Import({CapabilityRegistryBindingManagerConfig.class, CapabilityRegistryLookupManagerConfig.class})

public class Application extends AsyncConfigurerSupport
{
    public static void main(String[] args) throws Exception
    {
      
    	new SpringApplicationBuilder().sources(Application.class).bannerMode(Banner.Mode.OFF).run(args);    	
     	    		
    
    }

}

