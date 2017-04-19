package ie.dit.societiesapp;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class DBUpdater {
    private SocDBOpenHelper db;
    private Context context;
    private static String URL;
    private static String getSocs;
    private static String getUserSocs;

    public DBUpdater(Context context) {
        db = new SocDBOpenHelper(context);
        this.context = context;
        URL = context.getString(R.string.base_url) + context.getString(R.string.script_bin);
        getSocs = context.getString(R.string.get_socs_script);
        getUserSocs = context.getString(R.string.get_user_socs_script);
    }

    public boolean updateAll() throws IOException, JSONException {
        if(updateSocieties() && updateMember()) {
            return true;
        } else {
            return false;
        }
    }

    // Requests all society data from server and stores in local db
    public boolean updateSocieties() throws IOException, JSONException {
        Http conn = new Http();
        String url = URL + getSocs;
        ArrayList<NameValuePair> args = new ArrayList<NameValuePair>();
        String s = conn.post(url, args, context);
        JSONResponse response = new JSONResponse(s, context);

        if(response.isValid()) {
            JSONArray societies = response.getArrayByKey("society_details");

            for(int i = 0; i < societies.length(); i++) {
                JSONObject soc = (JSONObject)societies.get(i);
                db.addSociety(
                        soc.getInt("society_id"),
                        soc.getString("name"),
                        soc.getString("email"),
                        soc.getString("description")
                );
            }
            Log.d("SOCDEBUG", "Societies updated");
            return true;
        } else {
            Log.d("SOCDEBUG", "Attempted to update societies: " + response.getMessage());
            return false;
        }
    }

    // Updates which societies the user is a member of
    public boolean updateMember() throws IOException, JSONException {
        Http conn = new Http();
        String url = URL + getUserSocs;
        ArrayList<NameValuePair> args = new ArrayList<NameValuePair>();
        String s = conn.post(url, args, context);
        JSONResponse response = new JSONResponse(s, context);

        if(response.isValid()) {
            JSONArray society_ids = response.getArrayByKey("society_id");

            db.clearMember();
            for(int i = 0; i < society_ids.length(); i++) {
                db.setMember(society_ids.getInt(i), 1);
            }
            Log.d("SOCDEBUG", "Memberships updated");
            return true;
        } else {
            Log.d("SOCDEBUG", "Attempted to update memberships: " + response.getMessage());
            return false;
        }
    }

    //TODO add updateCommittee and updateChair methods
}
