package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    String accessToken;
    public Fragment currentFragment;
    SQLiteDatabase sqLiteDatabase;
    PetDatabaseHelper petDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StartAsyncTask startAsyncTask = new StartAsyncTask();
        startAsyncTask.execute();
    }

    private class StartAsyncTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            HttpsURLConnection httpsURLConnection = null;
            try {
                URL url = new URL("https://api.petfinder.com/v2/oauth2/token");
                httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setRequestMethod("POST");
                httpsURLConnection.setReadTimeout(20000);
                httpsURLConnection.setConnectTimeout(20000);
                httpsURLConnection.setDoOutput(true);
                httpsURLConnection.setDoInput(true);
                OutputStream outputStream = httpsURLConnection.getOutputStream();
                String payload = "grant_type=client_credentials";
                payload += "&client_id=wpnCvdgsBfHIUG3yg4z64Tn8MEnrwTDxLMphonFMfKvuWbUezB";
                payload += "&client_secret=rcvulb1DGmQHfk0JxO4fzUZXkgHtJxqVVKeNnN1k";
                byte[] rawData = payload.getBytes("UTF-8");
                outputStream.write(rawData);
                outputStream.flush();
                outputStream.close();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
                String retval = "";
                String line = "";
                while((line = bufferedReader.readLine()) != null){
                    retval += line;
                }
                bufferedReader.close();
                JSONObject jsonObject = new JSONObject(retval);
                accessToken = jsonObject.getString("access_token");
            } catch (IOException | JSONException e){
                e.printStackTrace();
            } finally{
                httpsURLConnection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            SearchFragment searchFragment = new SearchFragment(accessToken);
            updateFragment(searchFragment);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SearchFragment searchFragment = new SearchFragment(accessToken);
        updateFragment(searchFragment);
    }

    public void updateFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        currentFragment = fragment;
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.menuSearch:
                SearchFragment searchFragment = new SearchFragment(accessToken);
                updateFragment(searchFragment);
                return true;
            case R.id.menuFav:
                petDatabaseHelper = new PetDatabaseHelper(getApplicationContext());
                sqLiteDatabase = petDatabaseHelper.getWritableDatabase();
                Cursor cursor = sqLiteDatabase.query("pets", new String[]{"_id", "name", "type", "gender", "age", "size", "favorite"}, null, null, null, null, "favorite DESC");
                RecyclerFragment recyclerFragment = new RecyclerFragment(cursor);
                updateFragment(recyclerFragment);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}