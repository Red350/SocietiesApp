package layout;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ie.dit.societiesapp.ChairToolsFragment;
import ie.dit.societiesapp.KeyboardHider;
import ie.dit.societiesapp.R;
import ie.dit.societiesapp.SocDBOpenHelper;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SocietyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SocietyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SocietyFragment extends Fragment implements View.OnClickListener {

    private int society_id;

    private TextView societyEmailView, societyDescriptionView, societyStatusView;

    private OnFragmentInteractionListener mListener;

    public SocietyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment SocietyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SocietyFragment newInstance(int society_id)
    {
        SocietyFragment fragment = new SocietyFragment();
        Bundle args = new Bundle();
        args.putInt("society_id", society_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            society_id = getArguments().getInt("society_id");
        }

        // Hide the keyboard when this fragment loads
        KeyboardHider.hideKeyboard(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        // Create a listener for the search button
        SocDBOpenHelper db = new SocDBOpenHelper(getContext());

        View v = inflater.inflate(R.layout.fragment_society, container, false);

        Button qrGenButton = (Button) v.findViewById(R.id.qr_gen_button);
        qrGenButton.setOnClickListener(this);

        Button chairToolsButton = (Button) v.findViewById(R.id.chair_tools_button);
        chairToolsButton.setOnClickListener(this);

        societyEmailView = (TextView) v.findViewById(R.id.society_email);
        societyDescriptionView = (TextView) v.findViewById(R.id.society_description);
        societyStatusView = (TextView) v.findViewById(R.id.society_message);

        societyEmailView.setTextIsSelectable(true);

        Cursor cursor = db.getSociety(society_id);
        cursor.moveToFirst();

        int id_column = cursor.getColumnIndex("society_id");
        int name_column = cursor.getColumnIndex("name");
        int email_column = cursor.getColumnIndex("email");
        int desc_column = cursor.getColumnIndex("description");
        int member_column = cursor.getColumnIndex("is_member");
        int com_column = cursor.getColumnIndex("is_committee");
        int chair_column = cursor.getColumnIndex("is_chair");

        String societyEmail = cursor.getString(email_column);
        String societyDescription = cursor.getString(desc_column);
        String status;

        societyEmailView.setText(societyEmail);
        societyDescriptionView.setText(societyDescription);

        // Set the user's status
        if(cursor.getInt(member_column) == 1) {
            status = "You are a member";
        } else {
            status = "Not a member";
        }
        societyStatusView.setText(status);

        getActivity().setTitle(cursor.getString(name_column));

        if(cursor.getInt(com_column) != 1) qrGenButton.setVisibility(View.GONE);
        if(cursor.getInt(chair_column) != 1) chairToolsButton.setVisibility(View.GONE);

        return v;
    }

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
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.qr_gen_button:
            {
                QRGenFragment qrGenFragment= new QRGenFragment().newInstance(society_id);
                this.getFragmentManager().beginTransaction().replace(
                        R.id.relative_layout_for_fragment,
                        qrGenFragment,
                        qrGenFragment.getTag())
                        .addToBackStack(qrGenFragment.getTag())
                        .commit();
                break;
            }
            case R.id.chair_tools_button:
            {
                ChairToolsFragment chairToolsFragment = new ChairToolsFragment().newInstance(society_id);
                this.getFragmentManager().beginTransaction().replace(
                        R.id.relative_layout_for_fragment,
                        chairToolsFragment,
                        chairToolsFragment.getTag())
                        .addToBackStack(chairToolsFragment.getTag())
                        .commit();
                break;
            }
        }
    }
}
