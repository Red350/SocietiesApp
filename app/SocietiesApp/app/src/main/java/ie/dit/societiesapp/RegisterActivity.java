package ie.dit.societiesapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity
{
    private EditText nameView;
    private EditText idView;
    private EditText emailView;
    private EditText password1View;
    private EditText password2View;
    private EditText dobView;
    private EditText mobileView;
    private EditText emergancyView;

    private View progressView;
    private View registerFormView;

    private RegisterActivity.UserRegisterTask mAuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameView = (EditText) findViewById(R.id.nameRegText);
        idView = (EditText) findViewById(R.id.idRegText);
        emailView = (EditText) findViewById(R.id.emailRegText);
        password1View = (EditText) findViewById(R.id.password1RegText);
        password2View = (EditText) findViewById(R.id.password2RegText);
        mobileView = (EditText) findViewById(R.id.mobileRegText);
        dobView = (EditText) findViewById(R.id.dobRegText);
        emergancyView = (EditText) findViewById(R.id.emergancyRegText);

        Button registerButton = (Button) findViewById(R.id.registerButton);
        //Register call
        registerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                attemptRegister();
            }
        });

        registerFormView = findViewById(R.id.register_form);
        progressView = findViewById(R.id.register_progress);
    }

    public void sendMessage(View view)
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void attemptRegister()
    {
        if (mAuthTask != null)
        {
            return;
        }//end if

        nameView.setError(null);
        idView.setError(null);
        emailView.setError(null);
        password1View.setError(null);
        password2View.setError(null);
        mobileView.setError(null);
        dobView.setError(null);
        emergancyView.setError(null);

        // Store values at the time of the login attempt.
        String name = nameView.getText().toString();
        String id = idView.getText().toString();
        String email = emailView.getText().toString();
        String password1 = password1View.getText().toString();
        String password2= password2View.getText().toString();
        String mobile = mobileView.getText().toString();
        String DOB = dobView.getText().toString();
        String emergancy= emergancyView.getText().toString();

        //System.out.println(name + id + email + password1 + password2 + mobile + DOB + emergancy);

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(email))
        {
            emailView.setError(getString(R.string.error_field_required));
            focusView = emailView;
            cancel = true;
        }//end if
        else
        {
            cancel = false;
        }//end else

        // Check for a valid password
        if (TextUtils.isEmpty(password1))
        {
            password1View.setError(getString(R.string.error_field_required));
            focusView =  password1View;
            cancel = true;
        }
        else if (TextUtils.isEmpty(password2))
        {
            password2View.setError(getString(R.string.error_field_required));
            focusView =  password2View;
            cancel = true;
        }
        else if (!isPasswordValid(password1))
        {
            password1View.setError(getString(R.string.error_invalid_email));
            focusView = password1View;
            cancel = true;
        }//end if
        else if (!doPasswordsMatch(password1, password2))
        {
            password2View.setError(getString(R.string.error_incorrect_password));
            focusView = password2View;
            cancel = true;
        }//end if
        else
        {
            cancel = false;
        }//end else

        if (cancel)
        {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
        else
        {
            showProgress(true);
            mAuthTask = new UserRegisterTask(name, id, email, password1, DOB, mobile, emergancy);
            mAuthTask.execute((Void) null);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show)
    {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
        {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            registerFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            registerFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    registerFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        }
        else
        {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            registerFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /*
        Checks if an email is valid by having an '@'
    */
    private boolean isEmailValid(String email)
    {
        return email.contains("@");
    }

    /*
        Checks if a password contains enough characters
    */
    private boolean isPasswordValid(String password)
    {
        return (password.length() > 4);
    }

    /*
        Checks if the two passwords match
    */
    private boolean doPasswordsMatch(String password, String copyPassword)
    {
        return password.equals(copyPassword);
    }

    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean>
    {
        private String name = "";
        private String id = "";
        private String email = "";
        private String password = "";
        private String dob = "";
        private String number = "";
        private String emergancy = "";
        private String fullTime = "";

        UserRegisterTask(String name, String id, String email, String password,
                         String dob, String number, String emergancy)
        {
            this.name = name;
            this.id = id;
            this.email = email;
            this.password = password;
            this.dob = dob;
            this.number = number;
            this.emergancy = emergancy;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            Http conn = new Http();

            ArrayList<NameValuePair> args = new ArrayList<NameValuePair>();
            args.add(new NameValuePair("student_num", id));
            args.add(new NameValuePair("password", password));
            args.add(new NameValuePair("name", name));
            args.add(new NameValuePair("email", email));
            args.add(new NameValuePair("dob", dob));
            args.add(new NameValuePair("mobile", number));
            args.add(new NameValuePair("emergency_ph", emergancy));
            //Delete me Soon
            args.add(new NameValuePair("date_joined", ""));
            args.add(new NameValuePair("full_part_time", fullTime));

            String url = "http://www.padraig.red/cgi-bin/api/login.py";

            try
            {
                String s = conn.post(url, args);
                Log.d("Look Here", s);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            return true;
        }
        @Override
        protected void onPostExecute(final Boolean success)
        {
            mAuthTask = null;
            showProgress(false);

            if (success)
            {
                finish();
                // if login is successful transition to main activity
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }


        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

}
