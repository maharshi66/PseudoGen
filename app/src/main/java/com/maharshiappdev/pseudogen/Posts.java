package com.maharshiappdev.pseudogen;

public class Posts {
    private int id;
    private String title;
    private String description;
    private String pseudocode;
    private String input;
    private String output;
    private String time;
    private String space;

    public Posts() {}

    public Posts(int id, String title, String description,
                 String pseudocode, String input,
                 String output, String time,
                 String space)
    {
        this.id = id;
        this.title = title;
        this.description = description;
        this.pseudocode = pseudocode;
        this.input = input;
        this.output = output;
        this.time = time;
        this.space = space;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getPseudocode() {
        return pseudocode;
    }

    public void setPseudocode(String pseudocode) {
        this.pseudocode = pseudocode;
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
