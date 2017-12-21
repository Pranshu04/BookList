package com.example.prans.booklist;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BookAdapter mAdapter;

    private static final String LOG_TAG = MainActivity.class.getName();
    private String GOOGLE_REQUEST_URL =
            "https://www.googleapis.com/books/v1/volumes?q=";

    private ListView bookListView;
    TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        final EditText search_query = (EditText) findViewById(R.id.etSearch);
        Button mSearch = (Button) findViewById(R.id.btnSearch);
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String etQuery = search_query.getText().toString();
                if (etQuery.length() == 0) {
                    etQuery.equalsIgnoreCase("");
                    search_query.setError("Fields cannot be empty");


                } else {

                    etQuery = etQuery.replaceAll(" ", "+");
                    GOOGLE_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=" + etQuery;
                    BookAsyncTask task = new BookAsyncTask();
                    task.execute(GOOGLE_REQUEST_URL);
                }

            }
        });
        bookListView = (ListView) findViewById(R.id.list);
        mAdapter = new BookAdapter(this, new ArrayList<Book>());
        bookListView.setAdapter(mAdapter);

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book current_book = mAdapter.getItem(position);
                Uri bookUri = Uri.parse(current_book.getUrl());
                Intent book_intent = new Intent(Intent.ACTION_VIEW, bookUri);
                startActivity(book_intent);
            }
        });

    }


    private class BookAsyncTask extends AsyncTask<String, Void, List<Book>> {
        @Override
        protected List<Book> doInBackground(String... urls) {
            Log.d(LOG_TAG, "doInBackground process started.....");
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            List<Book> result = QueryUtils.fetchBooksData(urls[0]);
            return result;
        }

        @Override
        protected void onPostExecute(List<Book> data) {
            Log.d(LOG_TAG, "onPostExecute process started.....");

            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            mEmptyStateTextView.setText(R.string.no_books);
            mAdapter.clear();
            if (data != null && !data.isEmpty()) {
                mAdapter.addAll(data);
                Log.d(LOG_TAG, "Books added.....");
            }
        }
    }
}
