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
	
	public static void main(String[] args)
	{
		Http conn = new Http();
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new NameValuePair("email", "test@test.com"));
		params.add(new NameValuePair("password", "pass"));
		String url = "http://www.padraig.red/cgi-bin/api/login.py";
//		params.add(new NameValuePair("member_id", "6"));
//		params.add(new NameValuePair("session_id", "cfd59024462a4f66a2d0014837ce4f32"));
//		params.add(new NameValuePair("society_id", "1"));
//		params.add(new NameValuePair("society_id", "2"));
//		String url = "http://www.padraig.red/cgi-bin/api/get_socs.py";
		try
		{
			String s = conn.post(url, params);
			System.out.println(s);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		// get socs
		
	}
}


