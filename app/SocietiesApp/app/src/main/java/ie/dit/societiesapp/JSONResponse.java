package ie.dit.societiesapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONResponse {
    public static final String PREFS_NAME = "userData";
    private JSONObject json;
    private SharedPreferences userData;

    private int return_code;
    private String return_msg;

    public JSONResponse(String s, Context context) throws JSONException {
        json = new JSONObject(s);
        userData =  context.getSharedPreferences(PREFS_NAME, 0);


        this.return_code = json.getInt("return_code");
        this.return_msg = json.getString("return_msg");

        // Send user to login screen if session id is invalid
        if (return_code == 1) {
            Intent myIntent = new Intent(context, LoginActivity.class);
            context.startActivity(myIntent);
        }
    }

    // Returns true if the request was valid
    public boolean isValid() {
        return return_code == 0;
    }

    // Stores the session and member ids in shared preferences
    public boolean storeLogin() throws JSONException {
        if(json.has("session_id") && json.has("")) {
            SharedPreferences.Editor userDataEditor = userData.edit();
            String session_id = json.getString("session_id");
            String member_id = json.getString("member_id");
            userDataEditor.putString("session_id", session_id);
            userDataEditor.putString("member_id", member_id);
            userDataEditor.commit();
            Log.d("JSONDEBUG", member_id + " " + session_id);
            return true;
        } else {
            return false;
        }
    }


    public String getMessage() {
        return "Error code " + return_code + ": " + return_msg;
    }



}
