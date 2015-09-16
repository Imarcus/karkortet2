package com.karkortet.marcusisaksson.karkortet2.fragments;

import android.app.Activity;
import android.support.v4.app.ListFragment;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.karkortet.marcusisaksson.karkortet2.R;
import com.karkortet.marcusisaksson.karkortet2.activities.MainActivity;
import com.karkortet.marcusisaksson.karkortet2.utils.Constants;
import com.karkortet.marcusisaksson.karkortet2.utils.RestaurantListAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RestaurantsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RestaurantsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RestaurantsFragment extends ListFragment {

    private static final String ARG_RESTAURANT_LIST = "paramRestaurantList";

    private ArrayList<String> mRestaurants;

    private OnFragmentInteractionListener mListener;
    public static RestaurantsFragment newInstance(ArrayList<String> param1) {
        RestaurantsFragment fragment = new RestaurantsFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_RESTAURANT_LIST, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public RestaurantsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRestaurants = getArguments().getStringArrayList(ARG_RESTAURANT_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_restaurants, container, false);

        // Set up restaurant gui list
        ArrayList<String> restaurants = new ArrayList<String>();
        for (int i = 0; i < Constants.allRestaurants.length; ++i) {
            restaurants.add(Constants.allRestaurants[i]);
        }
        RestaurantListAdapter adapter = new RestaurantListAdapter(inflater.getContext(),
                R.layout.restaurant_list_view_element, mRestaurants);
        setListAdapter(adapter);

        return v;
    }

    public void onListItemClick(ListView listView, View view, int position,
                                long id) {
        MainActivity act = (MainActivity) this.getActivity();
        act.onListItemClick(listView,view,position,id);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
