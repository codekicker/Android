package de.codekicker.app.android.business;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;

public interface IServerRequest {
	String downloadJSON(String url, String postParameters) throws URISyntaxException, ClientProtocolException, IOException;
	String downloadJSON(String url, String postParameters, String username, String password) throws URISyntaxException, ClientProtocolException, IOException;
	String send(String url, String postParameters) throws URISyntaxException, ClientProtocolException, IOException;
	String send(String url, String postParameters, String username, String password) throws URISyntaxException, ClientProtocolException, IOException;
}