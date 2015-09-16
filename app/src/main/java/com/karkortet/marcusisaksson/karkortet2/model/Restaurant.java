package com.karkortet.marcusisaksson.karkortet2.model;

import java.util.ArrayList;

public class Restaurant {

    private String mRestaurantName;
    private ArrayList<String> mDishes = new ArrayList<>();
    private ArrayList<String> mFoods = new ArrayList<>();

    public Restaurant(String restaurantName, ArrayList<String> foods, ArrayList<String> dishes){
        this.mRestaurantName = restaurantName;
        if(dishes != null){
            mDishes.addAll(dishes);
        } else {
            mDishes = new ArrayList<>();
        }
        if(foods != null){
            mFoods.addAll(dishes);
        } else {
            mFoods = new ArrayList<>();
        }

    }

    public String getRestaurantName() {
        return mRestaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.mRestaurantName = restaurantName;
    }

    public ArrayList<String> getDishes() {
        return mDishes;
    }

    public void setDishes(ArrayList<String> mDishes) {
        this.mDishes = mDishes;
    }

    public void addDish(String dish, String food){
        mDishes.add(dish);
        mFoods.add(food);
    }

    public ArrayList<String> getFoods() {
        return mFoods;
    }

    public void setFoods(ArrayList<String> mFoods) {
        this.mFoods = mFoods;
    }

}
