package com.android.wikimedia.model;

public class Pages
{
    private String index;

    private String title;

    private String ns;

    private Thumbnail thumbnail;

    private Terms terms;

    private String pageid;

    public String getIndex ()
    {
        return index;
    }

    public void setIndex (String index)
    {
        this.index = index;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public String getNs ()
    {
        return ns;
    }

    public void setNs (String ns)
    {
        this.ns = ns;
    }

    public Thumbnail getThumbnail ()
    {
        return thumbnail;
    }

    public void setThumbnail (Thumbnail thumbnail)
    {
        this.thumbnail = thumbnail;
    }

    public Terms getTerms ()
    {
        return terms;
    }

    public void setTerms (Terms terms)
    {
        this.terms = terms;
    }

    public String getPageid ()
    {
        return pageid;
    }

    public void setPageid (String pageid)
    {
        this.pageid = pageid;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [index = "+index+", title = "+title+", ns = "+ns+", thumbnail = "+thumbnail+", terms = "+terms+", pageid = "+pageid+"]";
    }
}