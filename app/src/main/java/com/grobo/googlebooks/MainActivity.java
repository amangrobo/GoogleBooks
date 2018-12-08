package com.grobo.googlebooks;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListItemAdapter mAdapter;
    private TextView emptyView;
    private ProgressBar progressBar;
    String constructedUrl;
    EditText searchQueryInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        final ListView displayListView = (ListView) findViewById(R.id.display_list_view);
        emptyView = (TextView) findViewById(R.id.empty_view);
        displayListView.setEmptyView(emptyView);

        mAdapter = new ListItemAdapter(this, new ArrayList<Book>());
        displayListView.setAdapter(mAdapter);

    }


    public void searchBooks(View view){
        progressBar.setVisibility(View.VISIBLE);

        searchQueryInput = (EditText) findViewById(R.id.search_query);

        constructedUrl = "https://www.googleapis.com/books/v1/volumes?q=" + searchQueryInput.getText().toString() + "&maxResults=20";

        BookAsyncTask task = new BookAsyncTask();
        task.execute(constructedUrl);
    }

    private class BookAsyncTask extends AsyncTask<String, Void, List<Book>> {

        @Override
        protected List<Book> doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            List<Book> bookList = QueryUtils.doSearch(urls[0]);
            return  bookList;
        }

        @Override
        protected void onPostExecute(List<Book> data) {

            mAdapter.clear();

            progressBar = (ProgressBar) findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.GONE);
            emptyView.setText(R.string.no_data);

            if (data != null && !data.isEmpty()){
                mAdapter.addAll(data);
            }

        }

    }


}
