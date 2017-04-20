package layout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        JSONResponse response;
        JSONObject json;
        Http conn = new Http();
        ArrayList<NameValuePair> args = new ArrayList<NameValuePair>();
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        Log.d("QR", result.getContents());
        statusField = (TextView)getView().findViewById(R.id.qrValidView);
        statusField.setText("");
        //Places result into a JSON object to be parsed
        try
        {
            json = new JSONObject(result.getContents());
            if (json.has("society_id") && json.has("token"))
            {
                Log.d("Has", result.getContents());
                args.add(new NameValuePair("token", json.getString("token")));
                args.add(new NameValuePair("society_id", json.getString("society_id")));
                args.add(new NameValuePair("session_id", json.getString("session_id")));
                String url = getString(R.string.base_url) + getString(R.string.script_bin) + json.getString("token");
                String s = conn.post(url ,args , getActivity().getApplicationContext());
                response = new JSONResponse(s, getActivity().getApplicationContext());
                // Check to see if login succeeded
                if(response.isValid())
                {
                    statusField.setTextColor(Color.parseColor("#0096D7"));
                    statusField.setText(getString(R.string.reg_success));
                }
                else
                {
                    statusField.setTextColor(Color.parseColor("#CC0000"));
                    statusField.setText(getString(R.string.reg_unsuccess));
                }
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
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
}
