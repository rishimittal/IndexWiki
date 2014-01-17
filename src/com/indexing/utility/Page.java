package com.indexing.utility;

/**
 * Created by rishimittal on 11/1/14.
 */
public class Page {

    private String title;
    private String id;
    private String rev_con_username;
    private String rev_minor;
    private String rev_comment;
    private String rev_text;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRev_con_username() {
        return rev_con_username;
    }

    public void setRev_con_username(String rev_con_username) {
        this.rev_con_username = rev_con_username;
    }

    public String getRev_minor() {
        return rev_minor;
    }

    public void setRev_minor(String rev_minor) {
        this.rev_minor = rev_minor;
    }

    public String getRev_comment() {
        return rev_comment;
    }

    public void setRev_comment(String rev_comment) {
        this.rev_comment = rev_comment;
    }

    public String getRev_text() {
        return rev_text;
    }

    public void setRev_text(String rev_text) {
        this.rev_text = rev_text;
    }
}
