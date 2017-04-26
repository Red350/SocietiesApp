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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserDetailsFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private OnFragmentInteractionListener mListener;

    private SwipeRefreshLayout swipeLayout;

    private Button updateButton;

    private EditText editNameView, editMobileView, editEmergencyPhoneView;

    private View userUpdateProgress;

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

        editNameView = (EditText) v.findViewById(R.id.editNameView);
        editMobileView = (EditText) v.findViewById(R.id.editMobileView);
        editEmergencyPhoneView = (EditText) v.findViewById(R.id.editEmergencyView);

        updateButton = (Button) v.findViewById(R.id.updateButton);
        updateButton.setOnClickListener(this);

        userUpdateProgress = v.findViewById(R.id.user_update_progress);

        return v;
    }

    public void onResume()
    {
        super.onResume();

        setTextFields();
    }

    private void setTextFields() {
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
        //int full_time_column = cursor.getColumnIndex("full_part_time");

        String userName = emptyString(cursor.getString(name_column));
        String userMobile = emptyString(cursor.getString(mobile_column));
        String emergencyPhone = emptyString(cursor.getString(phone_column));

        editNameView.setText(userName);
        editMobileView.setText(userMobile);
        editEmergencyPhoneView.setText(emergencyPhone);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private String emptyString(String value)
    {
        if (value.equals("-"))
        {
            return "";
        }
        else
        {
            return value;
        }
    }

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
        void onFragmentInteraction(Uri uri);
    }


    public void onClick(View v) {
        Log.d("DETAILSDEBUG", "Screen pressed");
        switch(v.getId()) {
            // Send the user's details to the server
            case R.id.updateButton:
                Log.d("DETAILSDEBUG", "Button clicked");
                String name = editNameView.getText().toString();
                String mobile = editMobileView.getText().toString();
                String emergencyPhone = editEmergencyPhoneView.getText().toString();

                UpdateUserDetailsTask updateUserDetailsTask = new UpdateUserDetailsTask(name, mobile, emergencyPhone);
                updateUserDetailsTask.execute();
                break;
        }
    }

    // Update the local database when the screen is pulled down
    public void onRefresh() {
        GetUserDetailsTask getUserDetailsTask = new GetUserDetailsTask();
        getUserDetailsTask.execute();
    }

    // Disable view elements during an update
    private void disableView() {
        updateButton.setEnabled(false);

        editNameView.setEnabled(false);
        editMobileView.setEnabled(false);
        editEmergencyPhoneView.setEnabled(false);
    }

    // Re-enable view updates
    private void enableView() {
        updateButton.setEnabled(true);

        editNameView.setEnabled(true);
        editMobileView.setEnabled(true);
        editEmergencyPhoneView.setEnabled(true);
    }

    // Send the updated user details to the server
    public class UpdateUserDetailsTask extends AsyncTask<Void, Void, Boolean> {

        private String name, mobile, emergencyPhone;

        public UpdateUserDetailsTask(String name, String mobile, String emergencyPhone)
        {
            this.name = name;
            this.mobile = mobile;
            this.emergencyPhone = emergencyPhone;
        }

        @Override
        protected void onPreExecute() {
            // Make the view unclickable
            disableView();
            swipeLayout.setEnabled(false);
            updateButton.setVisibility(View.GONE);
            userUpdateProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            Http conn = new Http();
            JSONResponse response;

            ArrayList<NameValuePair> args = new ArrayList<NameValuePair>();
            args.add(new NameValuePair("name", name));
            args.add(new NameValuePair("mobile", mobile));
            args.add(new NameValuePair("emergency_ph", emergencyPhone));

            String url = getString(R.string.base_url) + getString(R.string.script_bin) + getString(R.string.update_user_details_script);

            try
            {
                String s = conn.post(url, args, getActivity().getApplicationContext());
                response = new JSONResponse(s, getActivity().getApplicationContext());

                // Check if the details updated successfully
                if(response.isValid())
                {
                    // Only update the local database if the server updated
                    SocDBOpenHelper db = new SocDBOpenHelper(getActivity().getApplicationContext());
                    if (db.partialUpdateUserDetails(name, mobile, emergencyPhone)) {
                        return true;
                    } else {
                        Log.d("DETAILSDEBUG", "Failed to update local database");
                        return false;
                    }
                } else {
                    Log.d("DETAILSDEBUG", response.getMessage());
                    return false;
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
                Log.d("DETAILSDEBUG", e.toString());
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success)
        {
            enableView();
            swipeLayout.setEnabled(true);
            updateButton.setVisibility(View.VISIBLE);
            userUpdateProgress.setVisibility(View.GONE);
            if(success) {
                Toast.makeText(getActivity(), "Details updated",
                        Toast.LENGTH_LONG).show();
                Log.d("DETAILSDEBUG", "Details updated");
            } else {
                Toast.makeText(getActivity(), "Failed to update details",
                        Toast.LENGTH_LONG).show();
                Log.d("DETAILSDEBUG", "Failed to update details");
            }
        }
    }

    public class GetUserDetailsTask extends AsyncTask<Void, Void, Boolean> {

        public GetUserDetailsTask()
        {
            disableView();
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {

            SocDBUpdater db = new SocDBUpdater(getActivity().getApplicationContext());
            try {
                Log.d("DETAILSDEBUG", "Attempting to update local database");
                db.updateUserDetails();
                return true;
            } catch(Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success)
        {
            swipeLayout.setRefreshing(false);
            enableView();
            // Refresh the text fields with the new values
            setTextFields();
        }
    }
}
