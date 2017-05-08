/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 */

package com.dell.cpsd.ticket.servicenow.services;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.dell.cpsd.ticket.servicenow.api.TicketServiceRequest;
import com.google.gson.Gson;



/**
 * Ticketing Integration Service, integrates with Service Now
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * </p>
 *
 * @version 0.1
 * @since 0.1
 */

@Service
public class TicketingIntegrationService {
	
	private static final Logger LOG = LoggerFactory.getLogger(TicketingIntegrationService.class);

	private static final String CREATE_URL = "/api/now/table/incident";
	private static final String NOTE_ADD_URL = "/sys_journal_field.do?JSONv2&sysparm_action=insert";
	private String INCIDENT = "";

	static {
	    loadProperties();
	}
	static Properties prop;
	
	
	private static void loadProperties() {
	    prop = new Properties();
	  
	    InputStream in = null;
	    try {
	    	in = new FileInputStream("/opt/dell/cpsd/ticket-servicenow/conf/config.properties");
	    	// prop.store(in, null);
	        prop.load(in);
	        in.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	// service now api properties, username, password and the address of the service now instance
	private static final String USER = prop.getProperty("servicenow.username");
	private static final String PASS = prop.getProperty("servicenow.password");
	private static final String HOST = prop.getProperty("servicenow.instance");



	/*
	 * Credentials provider for authenticating with service now
	 */
	private CredentialsProvider credentialsProvider() {
		// sets up credentials object
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(new HttpHost(HOST)),
                new UsernamePasswordCredentials(USER, PASS));
        return credsProvider;
	}
	
	/*
	 * HTTP post request 
	 */

	private Map post(String url, String data) throws IOException, HttpException {
		Map<String,Object> map = null;
		CredentialsProvider credentials = credentialsProvider();
		CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCredentialsProvider(credentials)
                .build();
	
		try {
			HttpPost httpPost = new HttpPost(url);
	 		httpPost.setHeader("Accept", "application/json");
	 		httpPost.setHeader("Content-Type", "application/json");
	        HttpEntity entity = new ByteArrayEntity(data.getBytes("utf-8"));
	 		httpPost.setEntity(entity);
		        
		    System.out.println("Executing request " + httpPost.getRequestLine());
		    CloseableHttpResponse response = httpclient.execute(httpPost);
		    try {
		        LOG.debug("----------------------------------------");
		        LOG.debug((String)response.getStatusLine().getReasonPhrase());
		        String responseBody = EntityUtils.toString(response.getEntity());
		        LOG.debug(responseBody);
		        Gson gson = new Gson();
		        map = new HashMap<String,Object>();
				map = (Map<String,Object>) gson.fromJson(responseBody, map.getClass());
		        LOG.debug(responseBody);
		    } finally {
		        response.close();
		    }
		} finally {
		    httpclient.close();
		}
	
		return map;
	}

	/*
	 * HTTP put request 
	 */
	private Map put(String url, String data) throws IOException, HttpException {
		Map<String,Object> map = null;
		CredentialsProvider credentials = credentialsProvider();
		CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCredentialsProvider(credentials)
                .build();
	
		try {
			HttpPut httpPut = new HttpPut(url);
	 		httpPut.setHeader("Accept", "application/json");
	 		httpPut.setHeader("Content-Type", "application/json");
	        HttpEntity entity = new ByteArrayEntity(data.getBytes("utf-8"));
	 		httpPut.setEntity(entity);
		        
		    System.out.println("Executing request " + httpPut.getRequestLine());
		    CloseableHttpResponse response = httpclient.execute(httpPut);
		    try {
		        LOG.debug("----------------------------------------");
		        LOG.debug((String)response.getStatusLine().getReasonPhrase());
		        String responseBody = EntityUtils.toString(response.getEntity());
		        LOG.debug(responseBody);
		        Gson gson = new Gson();
		        map = new HashMap<String,Object>();
				map = (Map<String,Object>) gson.fromJson(responseBody, map.getClass());
		        LOG.debug(responseBody);
		    } finally {
		        response.close();
		    }
		} finally {
		    httpclient.close();
		}
	
		return map;
	}
	
	/*
	 * HTTP delete request 
	 */
	private void delete(String url) throws IOException, HttpException {
		CredentialsProvider credentials = credentialsProvider();
		CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCredentialsProvider(credentials)
                .build();
	
		try {
			HttpDelete httpDelete = new HttpDelete(url);
	 		httpDelete.setHeader("Accept", "application/json");
		        
		    System.out.println("Executing request " + httpDelete.getRequestLine());
		    CloseableHttpResponse response = httpclient.execute(httpDelete);
		    try {
		        LOG.debug("----------------------------------------");
		        LOG.debug((String)response.getStatusLine().getReasonPhrase());
		    } finally {
		        response.close();
		    }
		} finally {
		    httpclient.close();
		}
	}

	/**
	 * 
	 * Calls service now api to create a ticket
	 *
	 * @param message
	 * @return
	 * @throws IOException
	 * @throws HttpException
	 */
	public String createTicket(TicketServiceRequest message) throws IOException, HttpException {
		
		LOG.debug("Create a service now ticket");
		String incidentId = "";
		String incidentTitle = "";
		String incidentNote = "";
		
		//fall back to using canned messages for demo purposes, if no ticket service request message
		if (message == null) {
			incidentTitle = "Test title";	
			incidentNote = "test note";
		} else {
			 incidentTitle = message.getTicketDetails().getIncidentTitle().replace('"',' ');
			 incidentNote = message.getTicketDetails().getIncidentNote().replace('"',' ');
		}
		

		// TODO CLEAN UP - example test code to create ServiceNow object
 		// This must be valid json string with valid fields and values from table
	 	String data = "{\"short_description\":\"" + incidentTitle + "\"}";
	 	LOG.info("Received new incident: Title: '" + incidentTitle + "' Description: '" + incidentNote + "'");	
	 	LOG.debug("POST data: " + data);
	 	Map result = post("https://" + HOST+CREATE_URL, data);
	 	incidentId = (String)((Map)result.get("result")).get("sys_id");
	 	
	 	INCIDENT = incidentId;	 
	 	LOG.info("Created incident: " + incidentId);		
		return incidentId;
	}
	/**
	 * Calls service now api to update a ticket
	 * 
	 * @param message
	 * @return
	 * @throws IOException
	 * @throws HttpException
	 */
	
	public String updateTicket(TicketServiceRequest message) throws IOException, HttpException {
		
		LOG.debug("Update a service now ticket");
		String incidentId = "";
		String incidentTitle = "";
		String incidentNote = "";
		
		//fall back to using canned messages for demo purposes, if no ticket service request message
		if (message == null) {
			incidentTitle = "Test title";	
			incidentNote = "test note added by Symphony";
			incidentId = INCIDENT;
		} else {
			incidentTitle = message.getTicketDetails().getIncidentTitle().replace('"',' ');
			incidentNote = message.getTicketDetails().getIncidentNote().replace('"',' ');
			incidentId = message.getTicketDetails().getIncidentId().replace('"',' ');
		}

		String data = "{\"element\": \"work_notes\"," +
							"\"element_id\": \"" + incidentId + "\"," +
    						"\"name\": \"task\"," +
    						"\"value\": \"" + incidentNote + "\"," +
							"}";
		LOG.debug("POST data: " + data);
		LOG.info("Updating incident: Title: '" + incidentTitle + "' Description: '" + incidentNote + "'");
	 	Map result = post("https://" + HOST+NOTE_ADD_URL, data);	
	 	LOG.info("Updated incident: " + incidentId);
		
		return "SUCCESS";
	}
	
	/**
	 * Method Stub
	 * @param message
	 * @return
	 */
	
	public String approveTicket(TicketServiceRequest message) {
		
		LOG.debug("Approve a service now ticket");
		// TODO Auto-generated method stub
		return "SUCCESS";
	}
	/**
	 * Calls service now api to close a ticket
	 * 
	 * @param message
	 * @return
	 * @throws IOException
	 * @throws HttpException
	 */
	
	public String closeTicket(TicketServiceRequest message) throws IOException, HttpException {
		
		LOG.debug("Close a service now ticket");
		String incidentId = "";
		String incidentTitle = "";
		String incidentNote = "";
		
		//fall back to using canned messages for demo purposes, if no ticket service request message
		if (message == null) {
			incidentId = INCIDENT;
			incidentNote = "Incident close by Symphony";
		} else {
			incidentNote = message.getTicketDetails().getIncidentNote().replace('"',' ');
			incidentId = message.getTicketDetails().getIncidentId().replace('"',' ');
		}

		String data = "{\"close_code\": \"Solved (Permanently)\"," +
						"\"close_notes\": \"" + incidentNote + "\"," +
						"\"state\": \"7\"}";
		LOG.debug("PUT data: " + data);
		LOG.info("Closing incident: Title: '" + incidentTitle + "' Description: '" + incidentNote + "'");
	 	Map result = put("https://" + HOST+CREATE_URL+ "/" + incidentId, data);	
	 	LOG.info("Closed incident: " + incidentId);

		return "SUCCESS";
	}

	/**
	 * Run service independently for testing purposes
	 * @param args
	 * @throws IOException
	 * @throws HttpException
	 */
 	public static void main(String[] args) throws IOException, HttpException {
 		TicketingIntegrationService restAction = new TicketingIntegrationService();
 		restAction.createTicket(null);
 	}

}


