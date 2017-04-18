package ie.dit.societiesapp;

import android.content.Context;
import android.content.SharedPreferences;
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
	
	public String post(String url, ArrayList<NameValuePair> params) throws IOException {
		FormBody.Builder formBody = new FormBody.Builder();

		// Add in the member_id and session_id if they exist

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

	// This adds the member_id and session_id fields to the request before it's sent
	public String post(String url, ArrayList<NameValuePair> params, Context context) throws IOException {
		SharedPreferences userData = context.getSharedPreferences("userData", 0);
		params.add(new NameValuePair("session_id", userData.getString("session_id", "")));
		params.add(new NameValuePair("member_id", userData.getString("member_id", "")));

		return (post(url, params));
	}
}

