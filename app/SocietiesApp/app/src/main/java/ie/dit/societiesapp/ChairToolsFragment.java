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

import java.util.ArrayList;

import layout.QRGenFragment;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChairToolsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChairToolsFragment#newInstance} factory method to
        * create an instance of this fragment.
        */
public class ChairToolsFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;

    private int society_id;

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
        if (getArguments() != null)
        {
            society_id = getArguments().getInt("society_id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Create a listener for the search button
        View v = inflater.inflate(R.layout.fragment_chairtools, container, false);

        Button addButton = (Button) v.findViewById(R.id.chair_add_button);
        addButton.setOnClickListener(this);

        Button deleteButton = (Button) v.findViewById(R.id.chair_delete_button);
        deleteButton.setOnClickListener(this);

        // Auto complete text view and adapter for society names R.id.testytest
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.select_dialog_singlechoice, societies);
//        AutoCompleteTextView acTextView = (AutoCompleteTextView) v.findViewById(R.id.soc_search_field);
//        acTextView.setThreshold(1);
//        acTextView.setAdapter(adapter);

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

    /**
     * This interface must be implemented by activities that contain this
            * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
             * >Communicating with Other Fragments</a> for more information.
            */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void addCommittee() {
        AutoCompleteTextView addTextView = (AutoCompleteTextView) getView().findViewById(R.id.chair_add_field);
        int memberId;
        try {
            memberId = Integer.parseInt(addTextView.getText().toString());
            addCommitteeTask addTask = new addCommitteeTask(memberId);
            addTask.execute();
        }
        catch(NumberFormatException e) {
            e.printStackTrace();
            Log.d("CHAIRTOOLSDEBUG", "Add committee: not a number");
        }

    }

    private void deleteCommittee() {
        AutoCompleteTextView addTextView = (AutoCompleteTextView) getView().findViewById(R.id.chair_delete_field);
        int memberId;
        try {
            memberId = Integer.parseInt(addTextView.getText().toString());
            deleteCommitteeTask deleteTask = new deleteCommitteeTask(memberId);
            deleteTask.execute();
        }
        catch(NumberFormatException e) {
            e.printStackTrace();
            Log.d("CHAIRTOOLSDEBUG", "Add committee: not a number");
        }
    }


    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.chair_add_button:
                addCommittee();
                break;
            case R.id.chair_delete_button:
                deleteCommittee();
                break;
        }
    }

    public class addCommitteeTask extends AsyncTask<Void, Void, Boolean> {

        private int committee_id;

        private addCommitteeTask(int committee_id) {
            this.committee_id = committee_id;
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            Http conn = new Http();
            JSONResponse response;

            ArrayList<NameValuePair> args = new ArrayList<NameValuePair>();
            args.add(new NameValuePair("committee_id", Integer.toString(committee_id)));
            args.add(new NameValuePair("society_id", Integer.toString(society_id)));

            String url = getString(R.string.base_url) + getString(R.string.script_bin) + getString(R.string.add_committee_script);

            try
            {
                String s = conn.post(url, args, getActivity().getApplicationContext());
                response = new JSONResponse(s, getActivity().getApplicationContext());

                // Check if committee member was added successfully
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
                Log.d("CHAIRTOOLSDEBUG", "Added successfully");
            } else {
                Log.d("CHAIRTOOLSDEBUG", "Failed to add");
            }
        }
    }

    public class deleteCommitteeTask extends AsyncTask<Void, Void, Boolean> {

        private int committee_id;

        private deleteCommitteeTask(int committee_id) {
            this.committee_id = committee_id;
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            Http conn = new Http();
            JSONResponse response;

            ArrayList<NameValuePair> args = new ArrayList<NameValuePair>();
            args.add(new NameValuePair("committee_id", Integer.toString(committee_id)));
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
                Log.d("CHAIRDEBUG", "Deleted successfully");
            } else {
                Log.d("CHAIRDEBUG", "Failed to delete");
            }
        }
    }
}
