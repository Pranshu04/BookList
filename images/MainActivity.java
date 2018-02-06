package com.example.prans.jsonparsing;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    public static TextView data;
    private StudentAdapter studentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        studentAdapter = new StudentAdapter(this, new ArrayList<Student>());
        listView.setAdapter(studentAdapter);

        FetchData fetchData = new FetchData();
        fetchData.execute();
    }

    public class FetchData extends AsyncTask<String, Void, List<Student>> {

        String result = "";
        String rank, population, country, flag;

        @Override
        protected List<Student> doInBackground(String... strings) {

            List<Student> students = new ArrayList<>();
            HttpURLConnection httpURLConnection = null;
            InputStream inputStream = null;
            try {
         
                URL url = new URL("http://www.androidbegin.com/tutorial/jsonparsetutorial.txt");
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while (line != null) {
                    line = bufferedReader.readLine();
                    result = result + line;
                }
                JSONObject object = new JSONObject(result);
                JSONArray worldPopulation = object.getJSONArray("worldpopulation");
                 for (int i = 0; i < worldPopulation.length(); i++) {
                    JSONObject jsonObject = (JSONObject) worldPopulation.get(i);
                    rank = jsonObject.getString("rank");
                    population = jsonObject.getString("country");
                    country = jsonObject.getString("population");
                    flag = jsonObject.getString("flag");
                    students.add(new Student(rank, country, population, flag));
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return students;
        }

        @Override
        protected void onPostExecute(List<Student> students) {
            studentAdapter.clear();
            if (students != null && !students.isEmpty()) {
                studentAdapter.addAll(students);
            }
        }
    }

}
