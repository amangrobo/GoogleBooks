package com.grobo.googlebooks;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListItemAdapter extends ArrayAdapter<Book> {

    public ListItemAdapter(Activity context, ArrayList<Book> bookList) {
        super(context, 0, bookList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Book currentBook = getItem(position);

        TextView titleView = (TextView) listItemView.findViewById(R.id.title_view);
        titleView.setText(currentBook.getTitle());

        TextView descriptionView = (TextView) listItemView.findViewById(R.id.description_view);
        descriptionView.setText(currentBook.getDescription());

        TextView authorsView = (TextView) listItemView.findViewById(R.id.authors_view);
        authorsView.setText("By: " + currentBook.getAuthors());

        return listItemView;
    }
}
