package com.example.walkmap;

import java.io.Serializable;

public class managerListViewItem implements Serializable {

    private String textid;

    public String getTextid() {
        return textid;
    }

    public void setTextid(String textid) {
        this.textid = textid;
    }

    private String textname ;
    private String textage ;
    private String textcareer ;
    private String textsex ;

    public String getTextname() {
        return textname;
    }

    public void setTextname(String textname) {
        this.textname = textname;
    }

    public String getTextage() {
        return textage;
    }

    public void setTextage(String textage) {
        this.textage = textage;
    }

    public String getTextcareer() {
        return textcareer;
    }

    public void setTextcareer(String textcareer) {
        this.textcareer = textcareer;
    }

    public String getTextsex() {
        return textsex;
    }

    public void setTextsex(String textsex) {
        this.textsex = textsex;
    }
}

