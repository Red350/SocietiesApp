package ie.dit.societiesapp;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class DBUpdater {
    private SocDBOpenHelper db;
    private Context context;
    private static final String baseURL = "http://www.padraig.red/cgi-bin/api/";
    private static final String getSocs = "get_socs.py";

    public DBUpdater(Context context) {
        db = new SocDBOpenHelper(context);
        this.context = context;
    }

    // Requests all society data from server and stores in local db
    public boolean updateSocieties() throws IOException, JSONException {
        Log.d("DBTEST", "Starting society update");
        Http conn = new Http();
        String url = baseURL + getSocs;
        ArrayList<NameValuePair> args = new ArrayList<NameValuePair>();
//        args.add(new NameValuePair("member_id", "5"));
//        args.add(new NameValuePair("session_id", "783ed58689a74fbd8b60139399a42862"));
        String s = conn.post(url, args, context);
        JSONResponse response = new JSONResponse(s, context);

        if(response.isValid()) {
            JSONArray societies = response.getSocieties();

            for(int i = 0; i < societies.length(); i++) {
                JSONObject soc = (JSONObject)societies.get(i);
                db.addSociety(
                        soc.getInt("society_id"),
                        soc.getString("name"),
                        soc.getString("email"),
                        soc.getString("description")
                );

                Cursor c = db.getSociety(i+1);
                c.moveToFirst();
                Log.d("DBTEST", "soc name: " + c.getString(1));
            }
            Log.d("DBTEST", "finished");



            return true;
        } else {
            Log.d("DBTEST", response.getMessage());
            return false;
        }
    }
}
