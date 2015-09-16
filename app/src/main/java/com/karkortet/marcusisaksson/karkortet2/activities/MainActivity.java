package com.karkortet.marcusisaksson.karkortet2.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.karkortet.marcusisaksson.karkortet2.R;
import com.karkortet.marcusisaksson.karkortet2.fragments.CardFragment;
import com.karkortet.marcusisaksson.karkortet2.fragments.LoginFragment;
import com.karkortet.marcusisaksson.karkortet2.fragments.RestaurantsFragment;
import com.karkortet.marcusisaksson.karkortet2.fragments.SlideFragment;
import com.karkortet.marcusisaksson.karkortet2.model.Restaurant;
import com.karkortet.marcusisaksson.karkortet2.utils.Constants;
import com.karkortet.marcusisaksson.karkortet2.utils.SlidePageAdapter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;


public class MainActivity extends FragmentActivity implements LoginFragment.OnFragmentInteractionListener,
        CardFragment.OnFragmentInteractionListener, RestaurantsFragment.OnFragmentInteractionListener {

    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 2;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    private String mCardNumber;
    private String mCardValue;
    private String mName;

    private SlidePageAdapter mServingPageAdapter;


    private final static String CARD_PREFS_NAME = "CARD_PREFS";

    private ArrayList<Restaurant> mRestaurants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Restore preferences
        SharedPreferences settings = getSharedPreferences(CARD_PREFS_NAME, 0);
        mCardNumber = settings.getString("cardNumber", "");
        mCardValue = settings.getString("cardValue", "");
        mName = settings.getString("name", "");


        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        if(!mCardNumber.equals("")){
            new Chalmrest().execute(mCardNumber);
            new ChalmFood().execute();

        }
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    public void goToCardFragment(){
        mPager.setCurrentItem(1);
    }

    /*public void setCardFragmentText(){
        TextView tv = (TextView)findViewById(R.id.cardValueText);
        tv.setText(mCardValue + " SEK");

        tv = (TextView)findViewById(R.id.nameText);
        tv.setText(mName);

        tv = (TextView)findViewById(R.id.cardNumberText);
        tv.setText(mCardNumber);
    }*/

    public void onCardNumberButtonClicked(View v){
        TextView tv = (TextView) findViewById(R.id.cardNumberLoginText);
        mCardNumber = tv.getText().toString();
        new Chalmrest().execute(mCardNumber);
        new ChalmFood().execute();
    }

    public void setPreferences(){
        SharedPreferences settings = getSharedPreferences(CARD_PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("cardNumber", mCardNumber);
        editor.putString("cardValue", mCardValue);
        editor.putString("name", mName);
        editor.commit();

    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void onListItemClick(ListView listView, View view, int position,
                                long id) {



        CheckBox checkBox = (CheckBox) listView.getChildAt(position)
                .findViewById(R.id.CheckBoxRestaurant);
        CheckedTextView checkedTV = (CheckedTextView) listView.getChildAt(
                position).findViewById(R.id.CheckedTextViewRestaurant);
        SharedPreferences sharedPref = getSharedPreferences(
                Constants.RESTAURANT_PREFS, 0);
        SharedPreferences.Editor editor = sharedPref.edit();

        // Set restaurant to chosen or not chosen depending on previous state
        // and save chosen state.
        if (checkBox.isChecked() == false) {
            editor.putBoolean((String) Constants.regularToUriRestNames
                    .get(checkedTV.getText()), true);
            checkBox.setChecked(true);

        } else {
            editor.putBoolean((String) Constants.regularToUriRestNames
                    .get(checkedTV.getText()), false);
            checkBox.setChecked(false);
        }
        editor.commit();
        Boolean b = sharedPref.getBoolean(Constants.regularToUriRestNames
                .get(checkedTV.getText()), false);
        b.toString().toUpperCase();
        /*Toast.makeText(getApplicationContext(),
                checkedTV.getText() + "is saved" + b,
                Toast.LENGTH_LONG).show();*/

    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0) {
                return LoginFragment.newInstance(null,null);
            } else if (position == 1) {
                CardFragment cardFragment = CardFragment.newInstance(mCardNumber, mCardValue, mName);
                return cardFragment;
            } else {
                return null;
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();

        if(mRestaurants.size() == 0){
            fList.add(SlideFragment.newInstance(new Restaurant("Nothing found :(", null, null)));
        } else {
            for (Restaurant restaurant : mRestaurants) {
                fList.add(new SlideFragment(restaurant));
            }
        }
        return fList;
    }

    private class Chalmrest extends AsyncTask<String, Void, String[]> {

        private ProgressDialog mProgressDialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            mProgressDialog.setTitle(R.string.loading);
            //TODO: remove hard coded string
            mProgressDialog.setMessage("Please wait chalmrest");
            mProgressDialog.show();

            super.onPreExecute();
        }

        @Override
        protected String[] doInBackground(String... strings) {
            String[] results = getCardInformation(strings[0]);

            return results;
        }

        // Credit to Bankdroid for the code for fetching the card number and
        // name from the web.
        protected String[] getCardInformation(String cardNr) {

            if (!cardNr.equals("")) {
                try {

                    HttpClient httpclient = new DefaultHttpClient();
                    HttpGet httpget = new HttpGet("http://kortladdning.chalmerskonferens.se/bgw.aspx?type=getCardAndArticles&card="
                            + cardNr);
                    HttpResponse response = httpclient.execute(httpget);
                    HttpEntity entity = response.getEntity();
                    if (entity == null) {
                        throw new ClientProtocolException("Could not connect");
                    }
                    String s1 = EntityUtils.toString(entity);
                    Pattern pattern = Pattern
                            .compile("<ExtendedInfo Name=\"Kortvarde\" Type=\"System.Double\" >(.*?)</ExtendedInfo>");
                    Matcher matcher = pattern.matcher(s1);

                    if (!matcher.find()) {
                        throw new IllegalArgumentException(
                                "Card number not valid");
                    }
                    String value = matcher.group(1);

                    StringBuilder sb = new StringBuilder();
                    int last = 0;
                    Matcher match = Pattern.compile("_x([0-9A-Fa-f]{4})_")
                            .matcher(value);
                    while (match.find()) {
                        sb.append(value.substring(last, match.start()));
                        int i = Integer.parseInt(match.group(1), 16);
                        sb.append((char) i);
                        last = match.end();
                    }
                    sb.append(value.substring(last));
                    value = sb.toString().replace(',', '.');

                    matcher = Pattern.compile(
                            "<CardInfo id=\"" + cardNr + "\" Name=\"(.*?)\"")
                            .matcher(s1);
                    if (!matcher.find()) {
                        throw new IllegalArgumentException(
                                "Card number not valid");
                    }

                    String name = matcher.group(1);

                    String[] results = new String[2];

                    results[0] = name;
                    results[1] = BigDecimal.valueOf(Double.parseDouble(value)).setScale(2, RoundingMode.CEILING).toString();

                    return results;

                    // Print out error message on screen, nothing happens, the
                    // user may try again.
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //errorTextView.setText("Invalid card number");
                        }
                    });
                    cancel(true);
                    return null;

                    // Print out error message on screen, nothing happens, the
                    // user may try again.
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //errorTextView.setText("Could not connect");
                        }
                    });
                    cancel(true);
                    return null;

                    // Print out error message on screen, nothing happens, the
                    // user may try again.
                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //errorTextView.setText("Could not connect");
                        }
                    });
                    cancel(true);
                    return null;
                }
            } else { //If no card number was entered.
                String[] results = new String[2];

                results[0] = "";
                results[1] = "";
                return results;
            }

        }

        @Override
        protected void onPostExecute(String[] results) {

            if (results == null) {
                return; // Do nothing
            }

            mName = results[0];
            mCardValue = results[1].replace('.', ',');

            setPreferences();


            mProgressDialog.dismiss();

            goToCardFragment();

        }
    }

    /*
	 * A class for fetching menus of the choosen restaurants
	 * Uses xml-files from: http://www.chalmerskonferens.se/rss-2/
	 *
	 */
    private class ChalmFood extends
            AsyncTask<String, Void, ArrayList<Restaurant>> {

        private ProgressDialog mProgressDialog = new ProgressDialog(MainActivity.this);

        protected void onPreExecute() {
            mProgressDialog.setTitle(R.string.loading);
            //TODO: remove hard coded string
            mProgressDialog.setMessage("Please wait chalmfood");
            mProgressDialog.show();

            super.onPreExecute();
        }

        @Override
        protected ArrayList<Restaurant> doInBackground(String... strings) {
            try {
                // Create xml document from uri
                DocumentBuilderFactory factory = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();

                //If device language is not Swedish, choose English
                String language = getResources().getConfiguration().locale
                        .getLanguage();
                if (!language.equals("sv")) {
                    language = "en";
                }

                //Get choosen restaurants from the restaurant gui list
                SharedPreferences sharedPref = getSharedPreferences(Constants.RESTAURANT_PREFS, 0);
                ArrayList<String> choosenRestaurants = new ArrayList<String>();
                for(String restaurant : Constants.allRestaurantsArray){
                    if(sharedPref.getBoolean(restaurant, false)){
                        choosenRestaurants.add(restaurant);
                    }
                }

                if(choosenRestaurants.size() == 0){
                    return null;
                }

                // Build expression for finding xml elements
                XPathFactory xPathfactory = XPathFactory.newInstance();
                XPath xpath = xPathfactory.newXPath();
                XPathExpression expr;

                //Variables used in the loop
                NodeList nodes;
                ArrayList<Restaurant> result = new ArrayList<Restaurant>();
                NodeList channelNodes;
                Node currentNode;
                NodeList childNodes;
                ArrayList<String> currentFoods;
                ArrayList<String> currentDishes;
                String currentRestName = "No restaurant name";
                String currentDishName = "No dish name found";
                String currentFood = "No dish found";
                String location = "";


                //Loop through all chosen restaurants and add their dishes as slide fragments
                for (String restaurant : choosenRestaurants) {
                    currentDishes = new ArrayList<>();
                    currentFoods = new ArrayList<>();

                    if(Constants.johannebergRestaurantsArray.contains(restaurant)){
                        location = "johanneberg";
                    } else if(Constants.lindholmenRestaurantsArray.contains(restaurant)){
                        location = "lindholmen";
                    }



                    Document doc = builder
                            .parse("http://cm.lskitchen.se/" + location + "/" + restaurant + "/"
                                    + language
                                    + "/"
                                    + "today" + ".rss");

                    expr = xpath.compile("//channel");

                    // Fetch the channel nodes from the document
                    nodes = (NodeList) expr.evaluate(doc,
                            XPathConstants.NODESET);
                    channelNodes = nodes.item(0).getChildNodes();

                    for(int k = 0; k < channelNodes.getLength(); k++){
                        if(channelNodes.item(k).getNodeName().equals("title")){
                            currentRestName = channelNodes.item(k).getFirstChild().getNodeValue();
                        }
                    }

                    expr = xpath.compile("//item");

                    // Fetch the item nodes from the document
                    nodes = (NodeList) expr.evaluate(doc,
                            XPathConstants.NODESET);
//                    for (int i = 0; i < nodes.getLength(); i++) {
//                        currentNode = nodes.item(i);
//                        childNodes = currentNode.getChildNodes();
//                        for (int j = 0; j < childNodes.getLength(); j++) {
//                            if (childNodes.item(j).getNodeName()
//                                    .equals("title")) {
//                                currentDishes.add(childNodes.item(j).getFirstChild().getNodeValue());
//                                /*
//                                currentDishName = childNodes.item(j)
//                                        .getFirstChild().getNodeValue();
//                                */
//                            } else if (childNodes.item(j).getNodeName()
//                                    .equals("description")) {
//                                currentFoods.add(childNodes.item(j).getFirstChild().getNodeValue().split("@")[0]);
//                                /*
//                                currentFood = childNodes.item(j)
//                                        .getFirstChild().getNodeValue();
//                                */
//                            }
//                        }
//                        /*
//                        result.add(new Restaurant(currentRestName, currentFood
//                                .split("@")[0], currentDishName)); //Removing the '@' sign from the price
//                        */
//                    }
                    ArrayList<String> a = new ArrayList<String>();
                    a.add("testing a");

                    ArrayList<String> b = new ArrayList<String>();
                    b.add("testing b");
                    result.add(new Restaurant("Testing", a, b));
                    //result.add(new Restaurant(currentRestName, currentDishes, currentFoods));
                }

                return result;

            } catch (Exception e) { //What do?
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(ArrayList<Restaurant> result) {
            if(result == null){
                mRestaurants  = new ArrayList<Restaurant>();
            } else {
                mRestaurants = result;
            }
            List<Fragment> fragments = getFragments();
            mServingPageAdapter = new SlidePageAdapter(getSupportFragmentManager(),
                    fragments);
            //View viewPager = findViewById(R.id.viewpager);
            ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
            pager.setAdapter(mServingPageAdapter);
            mProgressDialog.dismiss();
        }

    }
}
