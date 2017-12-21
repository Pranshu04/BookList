package com.example.prans.booklist;

/**
 * Created by prans on 10-10-2017.
 */

public class Book {

    private String mBookName;
    private String mAuthorName;
    private String mImageResourceUrl;
    private String mUrl;

    public Book(String mBookName, String mAuthorName, String mImageResourceUrl, String mUrl) {
        this.mBookName = mBookName;
        this.mAuthorName = mAuthorName;
        this.mImageResourceUrl = mImageResourceUrl;
        this.mUrl = mUrl;
    }

    public String getImageResourceUrl() {
        return mImageResourceUrl;
    }

    public String getUrl() {
        return mUrl;
    }


    public String getBookName() {
        return mBookName;
    }

    public String getAuthorName() {
        return mAuthorName;
    }

}
