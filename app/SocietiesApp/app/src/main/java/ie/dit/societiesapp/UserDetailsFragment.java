package ie.dit.societiesapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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

    private TextView IDView;
    private EditText editNameView, editMobileView, editPhoneView;
    private RadioButton editFulltimeView;

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

        IDView = (TextView) v.findViewById(R.id.textViewID);
        editNameView = (EditText) v.findViewById(R.id.editNameView);
        editMobileView = (EditText) v.findViewById(R.id.editMobileView);
        editPhoneView = (EditText) v.findViewById(R.id.editPhoneView);
        editFulltimeView = (RadioButton) v.findViewById(R.id.fullTimePartRadio);

        return v;
    }

    public void onResume()
    {
        super.onResume();

        // get user data
        SharedPreferences userData = getContext().getSharedPreferences("userData", 0);

        // get user id
        int userID = Integer.parseInt(userData.getString("member_id", "-1"));

        // open database
        SocDBOpenHelper db = new SocDBOpenHelper(getContext());

        Cursor cursor = db.getUserDetails(userID);
        cursor.moveToFirst();

        int name_column = cursor.getColumnIndex("name");
        int mobile_column = cursor.getColumnIndex("mobile");
        int phone_column = cursor.getColumnIndex("emergency_ph");
        int full_time_column = cursor.getColumnIndex("full_part_time");

        String userName = cursor.getString(name_column);
        String userMobile = cursor.getString(mobile_column);
        String userPhone = cursor.getString(phone_column);
        String fullPartTime = cursor.getString(full_time_column);

        IDView.setText(Integer.toString(userID));
        editNameView.setText(userName);
        editMobileView.setText(userMobile);
        editPhoneView.setText(userPhone);
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
        }
    }
}
