package ie.dit.societiesapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class StartupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);



        // Check if already logged in
        CheckAlreadyLoggedInTask checkAlreadyLoggedInTask = new CheckAlreadyLoggedInTask();
        checkAlreadyLoggedInTask.execute();
    }

    public class CheckAlreadyLoggedInTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params)
        {
            Http conn = new Http();
            JSONResponse response;

            ArrayList<NameValuePair> args = new ArrayList<NameValuePair>();

            String url = getString(R.string.base_url) + getString(R.string.script_bin) + getString(R.string.check_login_script);

            try
            {
                String s = conn.post(url, args, getApplicationContext());
                response = new JSONResponse(s, getApplicationContext());

                // Check to see if currently logged in
                if(response.isValid())
                {
                    return true;
                } else {
                    return false;
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success)
            {
                finish();
                // If already logged in, transition to home activity
                Intent intent = new Intent(StartupActivity.this, HomeActivity.class);
                startActivity(intent);
            } else {
                finish();
                // If not logged in, transition to login activity
                Intent intent = new Intent(StartupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }
    }
}
