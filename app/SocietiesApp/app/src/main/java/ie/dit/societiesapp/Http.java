package ie.dit.societiesapp;

import android.util.Log;

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

		String check = "";

		FormBody.Builder formBody = new FormBody.Builder();
		for(NameValuePair pair : params)
		{
			formBody.add(pair.getName(), pair.getValue());
			/*
			if (pair.getName().equals(""))
			{
				check += "EMP";
			}
			else
			{
				check += pair.getName();
			}
			check += " ";
			if (pair.getValue().equals(""))
			{
				check += "EMP";
			}
			else
			{
				check += pair.getValue();
			}
			check += "\n";
			*/
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

