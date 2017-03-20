package ie.dit.societiesapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
    }

    public void sendMessage(View view)
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void attemptRegister()
    {
        /*
        if (mAuthTask != null)
        {
            return;
        }
        */
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
            finish();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            /*
            showProgress(true);
            mAuthTask = new UserRegisterTask(email, password);
            mAuthTask.execute((Void) null);
            */

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
        private final String email;
        private final String password;

        UserRegisterTask(String userEmail, String userPassword) {
            email = userEmail;
            password = userPassword;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service
            URL url;
            HttpURLConnection client = null;

            try{
                url = new URL("http://padraig.red/cgi-bin/test.py");
                client = (HttpURLConnection)url.openConnection();
                client.setRequestMethod("POST");
                client.setRequestProperty("Email", email);
                client.setRequestProperty("Password", password);
                client.setDoOutput(true);
                OutputStream outputPost = new BufferedOutputStream(client.getOutputStream());
                // outputPost.write(client.getContent().toString().getBytes());
                outputPost.flush();
                outputPost.close();
            } catch(MalformedURLException error) {
                error.printStackTrace();
            } catch(SocketTimeoutException error) {
                error.printStackTrace();
            } catch (IOException error) {
                error.printStackTrace();
            } finally {
                if(client != null)
                    client.disconnect();
            }

            /*
            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }
            */
            /*
            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }
            */
            // TODO: register the new account here.
            return true;
        }
    }

}
