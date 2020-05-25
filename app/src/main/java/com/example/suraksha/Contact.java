package com.example.suraksha;

public class Contact {
    String mynum="", gar1="", gar2="", gar3="";

    public Contact(String num, String num1, String num2, String num3)
    {
        mynum = num;
        gar1 = num1;
        gar2 = num2;
        gar3 = num3;
    }

    public String getMynum() {
        return mynum;
    }

    public void setMynum(String mynum) {
        this.mynum = mynum;
    }

    public String getGar1() {
        return gar1;
    }

    public void setGar1(String gar1) {
        this.gar1 = gar1;
    }

    public String getGar2() {
        return gar2;
    }

    public void setGar2(String gar2) {
        this.gar2 = gar2;
    }

    public String getGar3() {
        return gar3;
    }

    public void setGar3(String gar3) {
        this.gar3 = gar3;
    }
}
