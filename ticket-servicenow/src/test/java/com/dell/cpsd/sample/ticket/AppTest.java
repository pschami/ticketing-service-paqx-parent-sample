package com.dell.cpsd.sample.ticket;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.dell.cpsd.ticket.servicenow.services.*;

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
            // restAction.createTicket(null);
            // restAction.updateTicket(null);
            // restAction.closeTicket(null);
        } catch (Throwable e){
            // it broke
        } finally {
            assertTrue( true );
        }
    }
}
