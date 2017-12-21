package com.example.prans.booklist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by prans on 10-10-2017.
 */

public class BookAdapter extends ArrayAdapter<Book> {
    public BookAdapter(@NonNull Context context, List<Book> books) {
        super(context, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Book newBook = getItem(position);
        TextView bookNameView = listItemView.findViewById(R.id.tvBookName);
        bookNameView.setText(newBook.getBookName());

        TextView authorNameView = listItemView.findViewById(R.id.tvAuthorName);
        authorNameView.setText(newBook.getAuthorName());

        ImageView book_image = listItemView.findViewById(R.id.book_image);
        Picasso.with(getContext()).load(newBook.getImageResourceUrl()).into(book_image);
        return listItemView;
    }
}
