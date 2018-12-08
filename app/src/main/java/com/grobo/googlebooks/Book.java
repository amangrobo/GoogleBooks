package com.grobo.googlebooks;

public class Book {

    private String mTitle;
    private String mAuthors;
    private String mDescription;

    public Book(String title, String authors, String description){
        mAuthors = authors;
        mDescription = description;
        mTitle = title;
    }

    public String getTitle(){
        return mTitle;
    }

    public String getAuthors() {
        return mAuthors;
    }

    public String getDescription() {
        return mDescription;
    }
}
