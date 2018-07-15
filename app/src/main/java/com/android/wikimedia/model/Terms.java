package com.android.wikimedia.model;

public class Terms
{

    private String[] description;

    public String[] getDescription ()
    {
        return description;
    }

    public void setDescription (String[] description)
    {
        this.description = description;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [description = "+description+"]";

    }
}