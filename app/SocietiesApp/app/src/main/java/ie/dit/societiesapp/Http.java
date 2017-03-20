package ie.dit.societiesapp;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Http
{
	OkHttpClient client;
	
	public Http()
	{
		client = new OkHttpClient();
	}
	
	String post(String url, ArrayList<NameValuePair> params) throws IOException {

		FormBody.Builder formBody = new FormBody.Builder();
		for(NameValuePair pair : params)
		{
			formBody.add(pair.getName(), pair.getValue());
		}
		RequestBody requestBody = formBody.build();
		
		Request request = new Request.Builder()
		        .url(url)
		        .post(requestBody)
		        .build();
		
		Response response = client.newCall(request).execute();
		return response.body().string();
	}
}

