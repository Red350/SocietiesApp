package layout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;


import ie.dit.societiesapp.KeyboardHider;
import java.util.ArrayList;

import ie.dit.societiesapp.Http;
import ie.dit.societiesapp.JSONResponse;
import ie.dit.societiesapp.NameValuePair;

import ie.dit.societiesapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QRGenFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QRGenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QRGenFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String id_param = "param1";

    private int society_id;
    private View sceneView;
    private View progressView;
    private WebView browser;
    private String returnedToken;

    private OnFragmentInteractionListener mListener;

    public QRGenFragment() {
        // Required empty public constructor
    }

    public static QRGenFragment newInstance(int param1) {
        QRGenFragment fragment = new QRGenFragment();
        Bundle args = new Bundle();
        args.putInt(id_param, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            sceneView.setVisibility(show ? View.GONE : View.VISIBLE);
            sceneView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    sceneView.setVisibility(show ? View.GONE : View.VISIBLE);
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
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            sceneView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            society_id = getArguments().getInt(id_param);
            GenerateToken token = new GenerateToken(society_id);
            token.execute();
        }

        // Hide the keyboard when this fragment loads
        KeyboardHider.hideKeyboard(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_qrgen, container, false);
        sceneView = v.findViewById(R.id.gen_layout);
        progressView = v.findViewById(R.id.gen_progress);
        //showProgress(false);
        browser = (WebView) v.findViewById(R.id.webview);
        // Inflate the layout for this fragment
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class GenerateToken extends AsyncTask<Void, Void, Boolean> {

        private int society_id;

        private GenerateToken(int society_id)
        {
            this.society_id = society_id;
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            Http conn = new Http();
            JSONResponse response;

            ArrayList<NameValuePair> args = new ArrayList<NameValuePair>();
            args.add(new NameValuePair("society_id", Integer.toString(society_id)));

            String url = getString(R.string.base_url) + getString(R.string.img_directory);

            try
            {
                String s = conn.post(url, args, getActivity().getApplicationContext());
                response = new JSONResponse(s, getActivity().getApplicationContext());

                // Check if committee member was added successfully
                if(response.isValid())
                {
                    returnedToken = response.getString("token");
                    return true;
                }
                else
                {
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
        protected void onPostExecute(final Boolean success)
        {
            if (success)
            {
                String url = getString(R.string.base_url) + getString(R.string.img_directory)
                        + returnedToken + getString(R.string.qr_extension);
                Log.d("url", url);
                //browser.loadUrl("file:///android_asset/dinner_menu.png");
            }
            else
            {
                Log.d("fail", "fail");
            }
        }
    }
}
