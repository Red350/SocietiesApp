package layout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import ie.dit.societiesapp.Http;
import ie.dit.societiesapp.JSONResponse;
import ie.dit.societiesapp.NameValuePair;
import ie.dit.societiesapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QRScanFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QRScanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QRScanFragment extends Fragment
{

    private OnFragmentInteractionListener mListener;
    private TextView statusField;
    public QRScanFragment()
    {
        // Required empty public constructor
    }

    //Creates an instance
    public static QRScanFragment newInstance()
    {
        QRScanFragment fragment = new QRScanFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Lauches scanner
        IntentIntegrator.forSupportFragment(this).initiateScan();
    }

    /*
        Method that gains the result of the QR code and parses it.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        JSONObject json;
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        Log.d("QR", result.getContents());
        //Places result into a JSON object to be parsed
        try
        {
            json = new JSONObject(result.getContents());
            if (json.has("society_id") && json.has("token"))
            {
                JoinSocTask joinSocTask = new JoinSocTask(json.getString("token"), json.getString("society_id"));
                joinSocTask.execute((Void) null);
            }
            else
            {
                statusField = (TextView)  getView().findViewById(R.id.qrValidView);
                statusField.setTextColor(Color.parseColor("#CC0000"));
                statusField.setText("Invalid QR");
            }//end else
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        Log.d("qr", result.getContents());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_qrscan, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
        {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    //Activity listening to fragments
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class JoinSocTask extends AsyncTask<Void, Void, Boolean> {

        private final String token;
        private final String society_id;

        JoinSocTask(String token, String society_id) {
            this.token = token;
            this.society_id = society_id;
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            Http conn = new Http();
            JSONResponse response;

            ArrayList<NameValuePair> args = new ArrayList<NameValuePair>();
            args.add(new NameValuePair("token", token));
            args.add(new NameValuePair("society_id", society_id));

            String url = getString(R.string.base_url) + getString(R.string.script_bin) + getString(R.string.join_soc_script);

            // Send login request to the server and parse the JSON response
            try
            {
                String s = conn.post(url, args, getActivity().getApplicationContext());
                response = new JSONResponse(s, getActivity().getApplicationContext());

                // Check to see if login succeeded
                if(response.isValid())
                {
                    return true;
                } else {
                    Log.d("QRDEBUG", "Failed to join soc: " + response.getMessage());
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
            //Allows Login
            if (success)
            {
                Log.d("QRDEBUG", "Society Successfully joined");
                statusField.setTextColor(Color.parseColor("#0061AA"));
                statusField.setText("Joined");
            }
            else
            {
                // Show an error message to the user I suppose
                statusField.setTextColor(Color.parseColor("#CC0000"));
                statusField.setText("Failed to join");
            }
        }
    }
}
