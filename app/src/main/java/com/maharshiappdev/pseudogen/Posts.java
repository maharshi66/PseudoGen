package com.maharshiappdev.pseudogen;

public class Posts {
    private String title;
    private String description;
    private String pseudocode;
    private String input;
    private String output;

    public Posts() {}

    public Posts(String title, String description,
                 String pseudocode, String input,
                 String output)
    {
        this.title = title;
        this.description = description;
        this.pseudocode = pseudocode;
        this.input = input;
        this.output = output;
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

}
