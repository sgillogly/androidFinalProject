package com.example.finalproject;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class SearchFragment extends Fragment implements View.OnClickListener{

    String accessToken, fullUrl, where = "";
    String type = "", name = "", gender = "", age = "", size = "";
    RadioGroup groupType, groupGender, groupSize, groupAge;
    RadioButton buttonCat, buttonDog, buttonMale, buttonFemale, buttonSmall, buttonMedium, buttonLarge, buttonXlarge, buttonBaby, buttonYoung, buttonAdult, buttonSenior;
    Button searchButton;
    PetDatabaseHelper petDatabaseHelper;
    SQLiteDatabase sqLiteDatabase;
    View view;

    public SearchFragment(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);

        groupType = view.findViewById(R.id.radioGroupType);
        groupGender = view.findViewById(R.id.radioGroupGender);
        groupAge = view.findViewById(R.id.radioGroupAge);
        groupSize = view.findViewById(R.id.radioGroupSize);
        searchButton = view.findViewById(R.id.buttonSearch);
        buttonCat = view.findViewById(R.id.radioButtonCat);
        buttonDog = view.findViewById(R.id.radioButtonDog);
        buttonMale = view.findViewById(R.id.radioButtonMale);
        buttonFemale = view.findViewById(R.id.radioButtonFemale);
        buttonSmall = view.findViewById(R.id.radioButtonSmall);
        buttonMedium = view.findViewById(R.id.radioButtonMedium);
        buttonLarge = view.findViewById(R.id.radioButtonLarge);
        buttonXlarge = view.findViewById(R.id.radioButtonXL);
        buttonBaby = view.findViewById(R.id.radioButtonBaby);
        buttonYoung = view.findViewById(R.id.radioButtonYoung);
        buttonAdult = view.findViewById(R.id.radioButtonAdult);
        buttonSenior = view.findViewById(R.id.radioButtonSenior);
        searchButton.setOnClickListener(this);

        return view;
    }

    public void onClick(View v){
        fullUrl = "https://api.petfinder.com/v2/animals?";
        boolean added = false;
        if(buttonCat.isChecked()){
            fullUrl += "type=" + buttonCat.getText();
            where += "type = '"+buttonCat.getText()+"' ";
            added = true;
        }
        if(buttonDog.isChecked()){
            fullUrl += "type=" + buttonDog.getText();
            where += "type = '"+buttonDog.getText()+"' ";
            added = true;
        }
        if(buttonMale.isChecked()){
            fullUrl += "&gender=" + buttonMale.getText();
            if(added){
                where += "AND gender = '"+buttonMale.getText()+"' ";
            } else{
                where += "gender = '"+buttonMale.getText()+"' ";
                added = true;
            }
        }
        if(buttonFemale.isChecked()){
            fullUrl += "&gender=" + buttonFemale.getText();
            if(added){
                where += "AND gender = '"+buttonFemale.getText()+"' ";
            } else{
                where += "gender = '"+buttonFemale.getText()+"' ";
                added = true;
            }
        }
        if(buttonSmall.isChecked()){
            fullUrl += "&size=" + buttonSmall.getText();
            if(added){
                where += "AND size = '"+buttonSmall.getText()+"' ";
            } else{
                where += "size = '"+buttonSmall.getText()+"' ";
                added = true;
            }
        }
        if(buttonMedium.isChecked()){
            fullUrl += "&size=" + buttonMedium.getText();
            if(added){
                where += "AND size = '"+buttonMedium.getText()+"' ";
            } else{
                where += "size = '"+buttonMedium.getText()+"' ";
                added = true;
            }
        }
        if(buttonLarge.isChecked()){
            fullUrl += "&size=" + buttonLarge.getText();
            if(added){
                where += "AND size = '"+buttonLarge.getText()+"' ";
            } else{
                where += "size = '"+buttonLarge.getText()+"' ";
                added = true;
            }
        }
        if(buttonXlarge.isChecked()){
            fullUrl += "&size=" + buttonXlarge.getText();
            if(added){
                where += "AND size = '"+buttonXlarge.getText()+"' ";
            } else{
                where += "size = '"+buttonXlarge.getText()+"' ";
                added = true;
            }
        }
        if(buttonBaby.isChecked()){
            fullUrl += "&age=" + buttonBaby.getText();
            if(added){
                where += "AND age = '"+buttonBaby.getText()+"' ";
            } else{
                where += "age = '"+buttonBaby.getText()+"' ";
                added = true;
            }
        }
        if(buttonYoung.isChecked()){
            fullUrl += "&age=" + buttonYoung.getText();
            if(added){
                where += "AND age = '"+buttonYoung.getText()+"' ";
            } else{
                where += "age = '"+buttonYoung.getText()+"' ";
                added = true;
            }
        }
        if(buttonAdult.isChecked()){
            fullUrl += "&age=" + buttonAdult.getText();
            if(added){
                where += "AND age = '"+buttonAdult.getText()+"' ";
            } else{
                where += "age = '"+buttonAdult.getText()+"' ";
                added = true;
            }
        }
        if(buttonSenior.isChecked()){
            fullUrl += "&age=" + buttonSenior.getText();
            if(added){
                where += "AND age = '"+buttonSenior.getText()+"' ";
            } else{
                where += "age = '"+buttonSenior.getText()+"' ";
                added = true;
            }
        }

        GetAsyncTask getAsyncTask = new GetAsyncTask();
        getAsyncTask.execute();

    }

    private class GetAsyncTask extends AsyncTask {

        int favorite = 0;

        @Override
        protected Object doInBackground(Object[] objects) {
            HttpsURLConnection httpsURLConnection = null;
            try {
                URL url = new URL(fullUrl);
                httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setRequestMethod("GET");
                httpsURLConnection.setReadTimeout(20000);
                httpsURLConnection.setConnectTimeout(20000);
                httpsURLConnection.setRequestProperty("Authorization", "Bearer " + accessToken);

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
                String retval = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    retval += line;
                }
                JSONObject jsonObject = new JSONObject(retval);
                JSONArray jsonArray = jsonObject.getJSONArray("animals");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject2 = (JSONObject) jsonArray.get(i);
                    type = jsonObject2.getString("type");
                    String startName = jsonObject2.getString("name");
                    gender = jsonObject2.getString("gender");
                    age = jsonObject2.getString("age");
                    size = jsonObject2.getString("size");
                    if(size.equals("Extra Large")){
                        size = "Xlarge";
                    }
                    petDatabaseHelper = new PetDatabaseHelper(getContext());
                    sqLiteDatabase = petDatabaseHelper.getWritableDatabase();
                    String[] splitName = startName.split("\\s+");
                    name = splitName[0].replace("'", "");
                    //better error check for names
                    Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM pets where name='"+name+"'", null);
                    if(!(c.moveToFirst())){
                        ContentValues newValues = new ContentValues();
                        newValues.put("name", name);
                        newValues.put("type", type);
                        newValues.put("age", age);
                        newValues.put("gender", gender);
                        newValues.put("size", size);
                        newValues.put("favorite", favorite);
                        sqLiteDatabase.insert("pets", null, newValues);
                    }
                }
                bufferedReader.close();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                httpsURLConnection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            MainActivity mainActivity = (MainActivity) getActivity();
            if(mainActivity != null){
                petDatabaseHelper = new PetDatabaseHelper(mainActivity);
                sqLiteDatabase = petDatabaseHelper.getWritableDatabase();
                Cursor c;
                if(where.equals("where ")){
                    c = sqLiteDatabase.query("pets", new String[]{"_id", "name", "type", "gender", "age", "size", "favorite"}, null, null, null, null, null);
                } else{
                    c = sqLiteDatabase.query("pets", new String[]{"_id", "name", "type", "gender", "age", "size", "favorite"}, where, null, null, null, null);
                    if(c.getCount() == 0){
                        c = sqLiteDatabase.query("pets", new String[]{"_id", "name", "type", "gender", "age", "size", "favorite"}, null, null, null, null, null);
                    }
                }
                RecyclerFragment recyclerFragment = new RecyclerFragment(c);
                mainActivity.updateFragment(recyclerFragment);
            }
        }
    }
}