package ie.dit.societiesapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONResponse {
    public static final String PREFS_NAME = "userData";
    private JSONObject json;
    private SharedPreferences userData;
    private String rawResponse;

    private int return_code;
    private String return_msg;

    public JSONResponse(String s, Context context) throws JSONException {
        rawResponse = s;
        Log.d("JSONDEBUG", "Received: " + rawResponse);
        json = new JSONObject(rawResponse);
        userData =  context.getSharedPreferences(PREFS_NAME, 0);

        this.return_code = json.getInt("return_code");
        this.return_msg = json.getString("return_msg");

        // Send user to login screen if session id is invalid
        if (return_code == 1) {
            Intent myIntent = new Intent(context, LoginActivity.class);
            context.startActivity(myIntent);
        }
    }

    public JSONArray getArrayByKey(String key) throws JSONException {
        return json.getJSONArray(key);
    }

    public String getString(String key) throws JSONException
    {
        return json.getString(key);
    }

    public int getInt(String key) throws JSONException
    {
        return json.getInt(key);
    }

    // Returns true if the request was valid
    public boolean isValid() {
        return return_code == 0;
    }

    // Stores the session and member ids in shared preferences
    public boolean storeLogin(String email) throws JSONException {
        if(json.has("session_id") && json.has("member_id")) {
            SharedPreferences.Editor userDataEditor = userData.edit();
            String session_id = json.getString("session_id");
            String member_id = json.getString("member_id");
            userDataEditor.putString("session_id", session_id);
            userDataEditor.putString("member_id", member_id);
            userDataEditor.putString("email", email);
            userDataEditor.commit();
            Log.d("JSONDEBUG", "Storing: " + member_id + " " + session_id);
            return true;
        } else {
            return false;
        }
    }


    public String getMessage()
    {
        return return_msg;
    }

    public JSONArray getSocieties() throws JSONException {
        JSONArray result = json.getJSONArray("society_details");

        return result;
    }

    public String getRawResponse() { return rawResponse; }

}
