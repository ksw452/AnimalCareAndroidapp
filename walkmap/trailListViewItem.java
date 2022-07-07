package com.example.walkmap;

import java.io.Serializable;

public class trailListViewItem implements Serializable {

        private String textStr ;
        private String textStr1 ;
        private String textdate;


    public String getTextdate() {
        return textdate;
    }

    public void setTextdate(String textdate) {
        this.textdate = textdate;
    }

    private String pointtext;
        private String mydbnum;

    public String getMydbnum() {
        return mydbnum;
    }

    public void setMydbnum(String mydbnum) {
        this.mydbnum = mydbnum;
    }

    public String getPointtext() {
        return pointtext;
    }

    public void setPointtext(String pointtext) {
        this.pointtext = pointtext;
    }


        public void setText(String text) {
            textStr = text ;
        }

    public void setText1(String text1) {
        textStr1 = text1 ;
    }


        public String getText() {
            return this.textStr ;
        }
    public String getText1() {
        return this.textStr1 ;
    }
}
