package com.example.myapp;

public class Book {
    private String bookName;
    private  String authorName;
    private String bookDesc;
    private  String readingStatus;
    private  String ignoreCaseBookName;

    public Book() {
    }

    public Book(String bookName, String authorName, String bookDesc, String readingStatus) {
        this.bookName = bookName;
        this.authorName = authorName;
        this.bookDesc = bookDesc;
        this.readingStatus = readingStatus;
        this.ignoreCaseBookName=bookName.toLowerCase();
    }

    public String getBookName() {
        return bookName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getBookDesc() {
        return bookDesc;
    }

    public String getReadingStatus() {
        return readingStatus;
    }

    public String getIgnoreCaseBookName() {
        return ignoreCaseBookName;
    }
}
