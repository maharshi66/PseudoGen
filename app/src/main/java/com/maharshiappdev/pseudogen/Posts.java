package com.maharshiappdev.pseudogen;

public class Posts {
    private String title;
    private String description;
    private String input;
    private String output;
    private String time;
    private String space;

    public static final String TABLE_NAME = "Posts";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DES = "description";
    public static final String COLUMN_INPUT = "input";
    public static final String COLUMN_OUTPUT = "output";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_SPACE = "space";

    public Posts() {}
    public Posts(String title, String description, String input, String output, String time, String space)
    {
        this.title = title;
        this.description = description;
        this.input = input;
        this.output = output;
        this.time = time;
        this.space = space;
    }

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_TITLE + " VARCHAR,"
                    + COLUMN_DES + " VARCHAR,"
                    + COLUMN_INPUT + " VARCHAR,"
                    + COLUMN_OUTPUT + " VARCHAR,"
                    + COLUMN_TIME + " VARCHAR,"
                    + COLUMN_SPACE + " VARCHAR"
                    + ")";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space;
    }
}
