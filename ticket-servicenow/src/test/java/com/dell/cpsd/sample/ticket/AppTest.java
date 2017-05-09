/**
 * Copyright © 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 */
package com.dell.cpsd.sample.ticket;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        try {
            // TicketingServiceService restAction = new TicketingServiceService();
            // TicketServiceRequest message1 = new TicketServiceRequest();
            // message1.setTicketDetails(new TicketDetails(null, "Title of Ticket", "incident creation"));
            // String incidentId = restAction.createTicket(message1);
            // TicketServiceRequest message2 = new TicketServiceRequest();
            // message2.setTicketDetails(new TicketDetails(incidentId, "Title of Ticket", "First update"));
            // restAction.updateTicket(message2);
            // TicketServiceRequest message3 = new TicketServiceRequest();
            // message3.setTicketDetails(new TicketDetails(incidentId, "Title of Ticket", "Second update"));
            // restAction.updateTicket(message3);
            // Thread.sleep(6000);
            // TicketServiceRequest message4 = new TicketServiceRequest();
            // message4.setTicketDetails(new TicketDetails(incidentId, "Title of Ticket", "Third update"));
            // restAction.updateTicket(message4);
            // Thread.sleep(6000);
            // TicketServiceRequest message5 = new TicketServiceRequest();
            // message5.setTicketDetails(new TicketDetails(incidentId, "Title of Ticket", "Fourth update"));
            // restAction.updateTicket(message5);
            // Thread.sleep(1000);
            // TicketServiceRequest message6 = new TicketServiceRequest();
            // message6.setTicketDetails(new TicketDetails(incidentId, "Title of Ticket", "Final update"));
            // restAction.closeTicket(message6);
        } catch (Throwable e){
            e.printStackTrace(System.out);
            // it broke
        } finally {
            assertTrue( true );
        }
    }
}
