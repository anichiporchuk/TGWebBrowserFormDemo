package com.cals.tgwebbrowser.helper;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;

public class ServerResponse {
HttpResponse response;
String cookie;

	public ServerResponse(HttpResponse r){
		response = r;
	}
	
	public ServerResponse(ArrayList<Object> resp){
		response = (HttpResponse) resp.get(0);
		cookie = (String) resp.get(1);
	}
	
	public String getCookie(){
		return cookie;
	}
	
	public int getServerAnswer() throws ParseException, IOException{
/*        String responseString = new String();
        HttpEntity responseEntity = response.getEntity();
        if(responseEntity!=null) {
        responseString = EntityUtils.toString(responseEntity);
        }
        int answer = Integer.parseInt(responseString);*/
		if (response != null)
			return response.getStatusLine().getStatusCode();
		else
			return -1;
	}
	
	
	
}
