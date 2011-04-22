package de.codekicker.app.android.business;

import java.io.IOException;

public interface IServerRequest {
	String downloadJSON(String url, String postParameters) throws IOException;
	String downloadJSON(String url, String postParameters, String username, String password) throws IOException;
	String send(String url, String postParameters) throws IOException;
	String send(String url, String postParameters, String username, String password) throws IOException;
}