package ie.dit.societiesapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import layout.QRGenFragment;
import layout.SocietyFragment;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SocietiesListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SocietiesListFragment#newInstance} factory method to
        * create an instance of this fragment.
        */
public class SocietiesListFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private ArrayList<String> societies = new ArrayList<String>();

    private OnFragmentInteractionListener mListener;

    private SwipeRefreshLayout swipeLayout;
    private AutoCompleteTextView acTextView;
    private Button button;

    public SocietiesListFragment() {
        // Required empty public constructor
    }

    public static SocietiesListFragment newInstance() {
        SocietiesListFragment fragment = new SocietiesListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get a list of the society names from the local db
        SocDBOpenHelper db = new SocDBOpenHelper(getActivity().getApplicationContext());
        societies = db.getSocietyNames();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_societies_list, container, false);

        // Set up swipe to refresh
        swipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.societies_swipe);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // Set up the society page view button
        button = (Button) v.findViewById(R.id.soc_search_button);
        button.setOnClickListener(this);

        // Auto complete text view and adapter for society names R.id.testytest
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.select_dialog_singlechoice, societies);
        acTextView = (AutoCompleteTextView) v.findViewById(R.id.soc_search_field);
        acTextView.setThreshold(1);
        acTextView.setAdapter(adapter);

        getActivity().setTitle("Search");

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

    public void loadSocFragment() {
        AutoCompleteTextView searchView = (AutoCompleteTextView) getView().findViewById(R.id.soc_search_field);
        String s = searchView.getText().toString();
        SocDBOpenHelper db = new SocDBOpenHelper(getActivity().getApplicationContext());
        int id = db.getSocietyIdByName(s);

        if(id != -1) {
            SocietyFragment societyFragment = SocietyFragment.newInstance(id);
            this.getFragmentManager().beginTransaction().replace(
                    R.id.relative_layout_for_fragment,
                    societyFragment,
                    societyFragment.getTag())
                    .addToBackStack(societyFragment.getTag())
                    .commit();
        }

    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.soc_search_button:
                Log.d("SOCDEBUG", "Button clicked");
                loadSocFragment();
                break;
        }
    }

    public void onRefresh() {
        UpdateSocietiesTask updateTask = new UpdateSocietiesTask();
        updateTask.execute();
    }

    public class UpdateSocietiesTask extends AsyncTask<Void, Void, Boolean> {

        public UpdateSocietiesTask() {
            acTextView.setFocusable(false);
            button.setEnabled(false);
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {

            SocDBUpdater db = new SocDBUpdater(getActivity().getApplicationContext());
            try {
                Log.d("SOCDEBUG", "Attempting to update local database");
                if(db.updateAll()) {
                    Log.d("SOCDEBUG", "Successfully updated local database after login");
                    return true;
                } else {
                    Log.d("SOCDEBUG", "Failed to update local database after login");
                    return false;
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            swipeLayout.setRefreshing(false);
            acTextView.setFocusableInTouchMode(true);
            acTextView.setFocusable(true);
            button.setEnabled(true);
        }
    }
}
