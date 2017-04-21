package layout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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

    private static final String id_param = "id";

    private int id;

    private TextView societyInfo;

    private OnFragmentInteractionListener mListener;

    public SocietyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment SocietyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SocietyFragment newInstance(int param1) {
        SocietyFragment fragment = new SocietyFragment();
        Bundle args = new Bundle();
        args.putInt(id_param, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            id = getArguments().getInt(id_param);
        }
        Log.d("BLAHDEBUG", "Created");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        // Create a listener for the search button
        View v = inflater.inflate(R.layout.fragment_society, container, false);

        Button button = (Button) v.findViewById(R.id.qr_gen_button);
        button.setOnClickListener(this);

        //SocDBOpenHelper conn = new SocDBOpenHelper();
        Log.d("BLAHDEBUG", "View Created");
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
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
        Log.d("BLAHDEBUG", "Detached");
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void onClick(View v) {
        Log.d("BLAHDEBUG", "Click happened");
        switch(v.getId()) {
            case R.id.qr_gen_button:
            {
                QRGenFragment qrGenFragment= new QRGenFragment().newInstance(id);
                this.getFragmentManager().beginTransaction().replace(
                        R.id.relative_layout_for_fragment,
                        qrGenFragment,
                        qrGenFragment.getTag())
                        .commit();
            }
        }
    }
}
