package com.example.prans.booklist;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    private BookAdapter mAdapter;

    private static final String LOG_TAG = MainActivity.class.getName();
    private String GOOGLE_REQUEST_URL =
            "https://www.googleapis.com/books/v1/volumes?q=";

    private ListView bookListView;
    TextView mEmptyStateTextView;

    private static final int BOOK_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(LOG_TAG, "onCreate.....");
        mEmptyStateTextView = findViewById(R.id.empty_view);
        final EditText search_query = findViewById(R.id.etSearch);
        Button mSearch = findViewById(R.id.btnSearch);
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

                    ConnectivityManager connMgr = (ConnectivityManager)
                            getSystemService(Context.CONNECTIVITY_SERVICE);

                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                    if (networkInfo != null && networkInfo.isConnected()) {

                        LoaderManager loaderManager = getLoaderManager();
                        loaderManager.initLoader(BOOK_LOADER_ID, null, this);  // error here on 3rd argument

                    } else {
                        Toast.makeText(MainActivity.this, "No internet available", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
        bookListView = findViewById(R.id.list);
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

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {

        Log.d(LOG_TAG, "onCreateLoader.....");
        return new BookLoader(this, GOOGLE_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {

        Log.d(LOG_TAG, " onLoadFinished.....");
        mAdapter.clear();
        if (books != null && !books.isEmpty()) {
            mAdapter.setNotifyOnChange(true);
            mAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        Log.d(LOG_TAG, " onLoaderReset.....");

        mAdapter.clear();
        mAdapter.notifyDataSetChanged();
    }
}
