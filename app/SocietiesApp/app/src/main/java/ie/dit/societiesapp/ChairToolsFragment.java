package ie.dit.societiesapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import layout.QRGenFragment;


public class ChairToolsFragment extends Fragment implements View.OnClickListener
{
    //Creation of views and listeners
    private OnFragmentInteractionListener mListener;

    private int society_id;

    AutoCompleteTextView addTextView;
    AutoCompleteTextView deleteTextView;

    private Button addButton;
    private Button deleteButton;

    private View addProgress;
    private View deleteProgress;

    public ChairToolsFragment() {
        // Required empty public constructor
    }

    public static ChairToolsFragment newInstance(int society_id) {
        ChairToolsFragment fragment = new ChairToolsFragment();
        Bundle args = new Bundle();
        args.putInt("society_id", society_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Retrieves stored ID
        if (getArguments() != null)
        {
            society_id = getArguments().getInt("society_id");
        }

        // Hide the keyboard when this fragment loads
        KeyboardHider.hideKeyboard(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Create a listener for the search button
        View v = inflater.inflate(R.layout.fragment_chairtools, container, false);

        addTextView = (AutoCompleteTextView) v.findViewById(R.id.chair_add_field);
        deleteTextView = (AutoCompleteTextView) v.findViewById(R.id.chair_delete_field);

        addButton = (Button) v.findViewById(R.id.chair_add_button);
        addButton.setOnClickListener(this);

        deleteButton = (Button) v.findViewById(R.id.chair_delete_button);
        deleteButton.setOnClickListener(this);

        addProgress = v.findViewById(R.id.chair_add_progress);
        deleteProgress = v.findViewById(R.id.chair_delete_progress);

        // Auto complete text view and searchAdapter for society names R.id.testytest
        //ArrayAdapter<String> searchAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.select_dialog_singlechoice, societies);
        //AutoCompleteTextView acTextView = (AutoCompleteTextView) v.findViewById(R.id.soc_search_field);
        //acTextView.setThreshold(1);
        //acTextView.setAdapter(searchAdapter);

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

    public interface OnFragmentInteractionListener
    {
        void onFragmentInteraction(Uri uri);
    }

    private void addCommittee()
    {
        String committee_email;
        committee_email = addTextView.getText().toString();
        addButton.setVisibility(View.GONE);
        addProgress.setVisibility(View.VISIBLE);
        addCommitteeTask addTask = new addCommitteeTask(committee_email);
        addTask.execute();
    }

    private void deleteCommittee()
    {
        //Send query with email to delete committee member
        String committee_email;
        committee_email = deleteTextView.getText().toString();
        deleteButton.setVisibility(View.GONE);
        deleteProgress.setVisibility(View.VISIBLE);
        deleteCommitteeTask deleteTask = new deleteCommitteeTask(committee_email);
        deleteTask.execute();

    }

    /*
        Listener for button clicks
    */
    public void onClick(View v)
    {
        switch(v.getId()) {
            case R.id.chair_add_button:
                addCommittee();
                break;
            case R.id.chair_delete_button:
                deleteCommittee();
                break;
        }
    }

    /*
        AsyncTask task with will query the database and add a member to the committee
        if successful
    */
    public class addCommitteeTask extends AsyncTask<Void, Void, Boolean> {

        private String committee_email;

        private addCommitteeTask(String committee_email) {
            this.committee_email = committee_email;
        }

        @Override
        protected void onPreExecute()
        {
            // Disable views during execution
            addButton.setEnabled(false);
            deleteButton.setEnabled(false);
            addTextView.setEnabled(false);
            addTextView.setEnabled(false);
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            Http conn = new Http();
            JSONResponse response;

            ArrayList<NameValuePair> args = new ArrayList<NameValuePair>();
            args.add(new NameValuePair("committee_email", committee_email));
            args.add(new NameValuePair("society_id", Integer.toString(society_id)));

            String url = getString(R.string.base_url) + getString(R.string.script_bin) + getString(R.string.add_committee_script);

            //Creates connection to database and retrieves JSON response
            try
            {
                String s = conn.post(url, args, getActivity().getApplicationContext());
                response = new JSONResponse(s, getActivity().getApplicationContext());

                // Check if committee member was added successfully
                if(response.isValid())
                {
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

        /*
            Checks the query result once finished to see if it true. The user will receive a
            Toast for a true or false query
        */
        @Override
        protected void onPostExecute(final Boolean success)
        {
            // Buttons re-enabled and visible
            addProgress.setVisibility(View.GONE);
            addButton.setVisibility(View.VISIBLE);
            addButton.setEnabled(true);
            deleteButton.setEnabled(true);
            addTextView.setEnabled(true);
            deleteTextView.setEnabled(true);
            if (success)
            {
                addTextView.setText("");
                Toast.makeText(getActivity(), "Added committee member",
                        Toast.LENGTH_LONG).show();
                Log.d("CHAIRTOOLSDEBUG", "Added successfully");
            }
            else
            {
                Toast.makeText(getActivity(), "Failed to add committee member",
                        Toast.LENGTH_LONG).show();
                Log.d("CHAIRTOOLSDEBUG", "Failed to add");
            }
        }
    }

    /*
        AsyncTask task with will query the database and add a member to the committee
        if successful
    */
    public class deleteCommitteeTask extends AsyncTask<Void, Void, Boolean> {

        private String committee_email;

        private deleteCommitteeTask(String committee_email) {
            this.committee_email = committee_email;
        }

        @Override
        protected void onPreExecute() {
            // Disable views during execution
            addButton.setEnabled(false);
            deleteButton.setEnabled(false);
            addTextView.setEnabled(false);
            addTextView.setEnabled(false);
        }

        //AsyncTask
        @Override
        protected Boolean doInBackground(Void... params)
        {
            Http conn = new Http();
            JSONResponse response;

            ArrayList<NameValuePair> args = new ArrayList<NameValuePair>();
            args.add(new NameValuePair("committee_email", committee_email));
            args.add(new NameValuePair("society_id", Integer.toString(society_id)));

            String url = getString(R.string.base_url) + getString(R.string.script_bin) + getString(R.string.delete_committee_script);

            try
            {
                String s = conn.post(url, args, getActivity().getApplicationContext());
                response = new JSONResponse(s, getActivity().getApplicationContext());

                // Check if committee member was deleted successfully
                if(response.isValid())
                {
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

        /*
            Checks the query result once finished to see if it true. The user will receive a
            Toast for a true or false query
        */
        @Override
        protected void onPostExecute(final Boolean success) {
            // Buttons re-enabled and visible
            deleteProgress.setVisibility(View.GONE);
            deleteButton.setVisibility(View.VISIBLE);
            addButton.setEnabled(true);
            deleteButton.setEnabled(true);
            addTextView.setEnabled(true);
            deleteTextView.setEnabled(true);
            if (success)
            {
                deleteTextView.setText("");
                Toast.makeText(getActivity(), "Deleted committee member",
                        Toast.LENGTH_LONG).show();
                Log.d("CHAIRDEBUG", "Deleted successfully");
            } else {
                Toast.makeText(getActivity(), "Failed to delete committee member",
                        Toast.LENGTH_LONG).show();
                Log.d("CHAIRDEBUG", "Failed to delete");
            }
        }
    }
}
