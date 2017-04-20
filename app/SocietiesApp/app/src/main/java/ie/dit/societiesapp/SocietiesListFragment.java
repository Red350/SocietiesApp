package ie.dit.societiesapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SocietiesListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SocietiesListFragment#newInstance} factory method to
        * create an instance of this fragment.
        */
public class SocietiesListFragment extends Fragment implements View.OnClickListener {

    private ArrayList<String> societies = new ArrayList<String>();

    private OnFragmentInteractionListener mListener;

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
        // Create a listener for the search button
        View v = inflater.inflate(R.layout.fragment_societies_list, container, false);

        // Set up the society page view button
        Button button = (Button) v.findViewById(R.id.soc_search_button);
        button.setOnClickListener(this);

        // Auto complete text view and adapter for society names R.id.testytest
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.select_dialog_singlechoice, societies);
        AutoCompleteTextView acTextView = (AutoCompleteTextView) v.findViewById(R.id.soc_search_field);
        acTextView.setThreshold(1);
        acTextView.setAdapter(adapter);

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
        Log.d("LISTDEBUG", Integer.toString(id));

        QRGenFragment qrGenFragment= new QRGenFragment().newInstance(id);
        this.getFragmentManager().beginTransaction().replace(
                R.id.relative_layout_for_fragment,
                qrGenFragment,
                qrGenFragment.getTag())
                .commit();

    }


    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.soc_search_button:
                loadSocFragment();
                break;
        }
    }
}
