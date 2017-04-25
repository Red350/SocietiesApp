package ie.dit.societiesapp;

import android.content.Context;
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
import android.widget.RadioGroup;

import java.util.ArrayList;

import layout.SocietyFragment;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserDetailsFragment#newInstance} factory method to
        * create an instance of this fragment.
        */
public class UserDetailsFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private ArrayList<String> currentSelected;
    private ArrayList<String> allSocieties = new ArrayList<String>();
    private ArrayList<String> memberSocieties = new ArrayList<String>();
    private ArrayList<String> committeeSocieties = new ArrayList<String>();
    private ArrayList<String> chairSocieties = new ArrayList<String>();

    private OnFragmentInteractionListener mListener;

    private SwipeRefreshLayout swipeLayout;
    private AutoCompleteTextView searchBox;
    private Button loadSocietyButton;

    ArrayAdapter<String> searchAdapter;

    public UserDetailsFragment() {
        // Required empty public constructor
    }

    public static UserDetailsFragment newInstance() {
        UserDetailsFragment fragment = new UserDetailsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get a list of the society names from the local db
        SocDBOpenHelper db = new SocDBOpenHelper(getActivity().getApplicationContext());
        allSocieties = db.getSocietyNames();

        // Create the array lists for each society subgroup
        for(String soc : allSocieties) {
            int society_id = db.getSocietyIdByName(soc);
            if(db.checkMember(society_id)) {
                memberSocieties.add(soc);
            }
            if(db.checkCommittee(society_id)) {
                committeeSocieties.add(soc);
            }
            if(db.checkChair(society_id)) {
                chairSocieties.add(soc);
            }
        }

        // Default selected group is all
        currentSelected = allSocieties;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user_details, container, false);

        getActivity().setTitle("User Details");

        // Set up swipe to refresh
        swipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.societies_swipe);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // Set up the society page view loadSocietyButton
        loadSocietyButton = (Button) v.findViewById(R.id.soc_search_button);
        loadSocietyButton.setOnClickListener(this);

        // Auto complete text view and searchAdapter for society names
        searchAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.select_dialog_singlechoice, currentSelected);
        searchBox = (AutoCompleteTextView) v.findViewById(R.id.soc_search_field);
        searchBox.setThreshold(1);
        searchBox.setAdapter(searchAdapter);

        return v;
    }

    // Updates the search list based on whatever currentSelected points to.
    // Also clears the search box.
    private void updateSearchList() {
        searchAdapter.clear();
        searchAdapter.addAll(currentSelected);
        searchAdapter.notifyDataSetChanged();

        // Clear the text view and remove focus
        searchBox.setText("");
        searchBox.clearFocus();
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
            case R.id.radio_search:
                Log.d("RADIODEBUG", "Radio clicked");
        }
    }

    public void onRefresh() {
        UpdateSocietiesTask updateTask = new UpdateSocietiesTask();
        updateTask.execute();
    }

    public class UpdateSocietiesTask extends AsyncTask<Void, Void, Boolean> {

        public UpdateSocietiesTask() {
            searchBox.setFocusable(false);
            loadSocietyButton.setEnabled(false);
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {

            SocDBUpdater db = new SocDBUpdater(getActivity().getApplicationContext());
            try {
                Log.d("SOCDEBUG", "Attempting to update local database");
                db.updateAllSocietyData();
                return true;
            } catch(Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            swipeLayout.setRefreshing(false);
            searchBox.setFocusableInTouchMode(true);
            searchBox.setFocusable(true);
            loadSocietyButton.setEnabled(true);
        }
    }
}
