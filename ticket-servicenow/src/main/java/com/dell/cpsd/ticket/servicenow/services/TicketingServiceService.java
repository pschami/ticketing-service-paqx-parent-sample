/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 */

package com.dell.cpsd.ticket.servicenow.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.dell.cpsd.ticket.servicenow.api.TicketServiceRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;

import java.util.Properties;
import java.io.InputStream;
import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.OutputStream;

import java.io.IOException;
import java.io.FileNotFoundException;

@Service
public class TicketingServiceService {
	
	private static final Logger LOG = LoggerFactory.getLogger(TicketingServiceService.class);

	private static final String CREATE_URL = "/api/now/table/incident";
	private static final String NOTE_ADD_URL = "/sys_journal_field.do?JSONv2&sysparm_action=insert";
	private String INCIDENT = "";

	static {
	    loadProperties();
	}
	static Properties prop;

	private static void loadProperties() {
	    prop = new Properties();
	    // prop.setProperty("servicenow.username", "admin");
	    // prop.setProperty("servicenow.password", "password");
	    // prop.setProperty("servicenow.instance", "instance.service-now.com");
	    InputStream in = null;
	    try {
	    	in = new FileInputStream("config.properties");
	    	// prop.store(in, null);
	        prop.load(in);
	        in.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	private static final String USER = prop.getProperty("servicenow.username");
	private static final String PASS = prop.getProperty("servicenow.password");
	private static final String HOST = prop.getProperty("servicenow.instance");



	private CredentialsProvider credentialsProvider() {
		// sets up credentials object
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(new HttpHost(HOST)),
                new UsernamePasswordCredentials(USER, PASS));
        return credsProvider;
	}

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




	public String createTicket(TicketServiceRequest message) throws IOException, HttpException {
		// TODO Auto-generated method stub
		LOG.debug("Create a service now ticket");
		String incidentId = "";
		String incidentTitle = "";
		String incidentNote = "";
		if (message == null) {
			incidentTitle = "Test title";	
			incidentNote = "test note";
		} else {
			// incidentTitle = message.incidentTitle;
			// incidentNote = message.incidentNote;
		}
		

		// CLEAN UP - example test code to create ServiceNow object
 		// This must be valid json string with valid fields and values from table
	 	String postData = "{\"short_description\":\"" + incidentTitle + "\"}";
	 	Map result = post("https://" + HOST+CREATE_URL, postData);
	 	incidentId = (String)((Map)result.get("result")).get("sys_id");
	 	LOG.debug("Created incident: " + incidentId);
 	
		INCIDENT = incidentId;
		return incidentId;
	}
	
	public String updateTicket(TicketServiceRequest message) throws IOException, HttpException {
		// TODO Auto-generated method stub
		LOG.debug("Update a service now ticket");
		String incidentId = "";
		String incidentTitle = "";
		String incidentNote = "";
		
		if (message == null) {
			incidentTitle = "Test title";	
			incidentNote = "test note added by Symphony";
			incidentId = INCIDENT;
		} else {
			// incidentTitle = message.incidentTitle;
			// incidentNote = message.incidentNote;
			// incidentId = message.incidentId;
		}

		String data = "{\"element\": \"work_notes\"," +
							"\"element_id\": \"" + incidentId + "\"," +
    						"\"name\": \"task\"," +
    						"\"value\": \"" + incidentNote + "\"," +
							"}";
	 	Map result = post("https://" + HOST+NOTE_ADD_URL, data);
	 	LOG.debug("Updated incident: " + incidentId);
		
		return "SUCCESS";
	}
	
	public String approveTicket(TicketServiceRequest message) {
		// TODO Auto-generated method stub
		LOG.debug("Create a service now ticket");
		
		return "SUCCESS";
	}
	
	public String closeTicket(TicketServiceRequest message) throws IOException, HttpException {
		// TODO Auto-generated method stub
		LOG.debug("Create a service now ticket");
		String incidentId = "";
		String incidentTitle = "";
		String incidentNote = "";
		
		if (message == null) {
			incidentId = INCIDENT;
			incidentNote = "Incident close by Symphony";
		} else {
			// incidentId = message.incidentId;
			// incidentNote = message.incidentNote;
		}

		String data = "{\"close_code\": \"Solved (Permanently)\"," +
						"\"close_notes\": \"" + incidentNote + "\"," +
						"\"state\": \"7\"}";

	 	Map result = put("https://" + HOST+CREATE_URL+ "/" + incidentId, data);
	 	LOG.debug("Closed incident: " + incidentId);

		return "SUCCESS";
	}


 	public static void main(String[] args) throws IOException, HttpException {
 		TicketingServiceService restAction = new TicketingServiceService();
 		restAction.createTicket(null);
 	}

}


