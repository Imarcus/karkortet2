package com.karkortet.marcusisaksson.karkortet2.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.karkortet.marcusisaksson.karkortet2.R;
import com.karkortet.marcusisaksson.karkortet2.model.Restaurant;

import java.util.ArrayList;

/*
 * GUI element for browsing choosen restaurants
 */
public class SlideFragment extends Fragment {

    private Restaurant mRestaurant;

    public SlideFragment(Restaurant r){
        if(r.getRestaurantName().equals("Meny Food Court")){
            r.setRestaurantName("Meny L's Kitchen");
        }

        this.mRestaurant = r;
    }


    public static final SlideFragment newInstance(Restaurant r)
    {
        return new SlideFragment(r);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Display food
        String message = mRestaurant.getFoods().get(0);
        View v = inflater.inflate(R.layout.fragment_servings, container, false);
        TextView textView = (TextView)v.findViewById(R.id.TextViewRestaurantFood);
        textView.setText(message);

        //Display the restaurant name
        /*if(mRestaurant.getRestaurantName().equals("")){
            message = getArguments().getString(RESTAURANT_MESSAGE);
        } else if(getArguments().getString(RESTAURANT_MESSAGE).equals("")){
            message = getArguments().getString(DISH_MESSAGE);
        } else {
            message = getArguments().getString(DISH_MESSAGE) + " - "
                    + getArguments().getString(RESTAURANT_MESSAGE);
        }
        textView = (TextView)v.findViewById(R.id.TextViewRestaurantName);
        textView.setText(message);*/

        return v;

    }

}
