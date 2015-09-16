package com.karkortet.marcusisaksson.karkortet2.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.karkortet.marcusisaksson.karkortet2.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_CARD_NUMBER = "cardnumber";
    private static final String ARG_CARD_VALUE = "cardvalue";
    private static final String ARG_NAME = "name";

    // TODO: Rename and change types of parameters
    private String mCardNumber;
    private String mCardValue;
    private String mName;

    private OnFragmentInteractionListener mListener;


    public static CardFragment newInstance(String cardNumber, String cardValue, String name) {
        CardFragment fragment = new CardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CARD_NUMBER, cardNumber);
        args.putString(ARG_CARD_VALUE, cardValue);
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);

        return fragment;
    }

    public CardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCardNumber = getArguments().getString(ARG_CARD_NUMBER);
            mCardValue = getArguments().getString(ARG_CARD_VALUE);
            mName = getArguments().getString(ARG_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_card, container, false);
        TextView tv = (TextView)v.findViewById(R.id.cardValueText);
        tv.setText(mCardValue + " SEK");

        tv = (TextView)v.findViewById(R.id.nameText);
        tv.setText(mName);

        tv = (TextView)v.findViewById(R.id.cardNumberText);
        tv.setText(mCardNumber);
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
