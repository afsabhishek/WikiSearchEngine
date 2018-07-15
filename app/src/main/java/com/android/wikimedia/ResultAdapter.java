package com.android.wikimedia;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.wikimedia.model.Pages;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class ResultAdapter extends ArrayAdapter<Pages> {

private ArrayList<Pages> dataSet;
        Context mContext;
        String title;


    private static class ViewHolder {
    TextView description,
             title;

    ImageView thumbnail;
}

    public ResultAdapter(ArrayList<Pages> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext=context;

    }


    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Pages dataModel = getItem(position);
        final ViewHolder viewHolder;
        final View result;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.description = (TextView) convertView.findViewById(R.id.description);

            viewHolder.description.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadUrl(dataModel.getTitle());
                }
            });
            viewHolder.thumbnail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        loadUrl(dataModel.getTitle());
                    }
                });
            viewHolder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadUrl(dataModel.getTitle());
                }
            });

            if(dataModel != null) {
                if(dataModel.getThumbnail() != null && dataModel.getThumbnail().getSource() != null) {
                    Glide.with(mContext).load(dataModel.getThumbnail().getSource())
                            .thumbnail(0.5f)
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(viewHolder.thumbnail);
                }
                if(dataModel.getTitle() != null) {
                    viewHolder.title.setText(dataModel.getTitle());
                    title = dataModel.getTitle();
                }
                if(dataModel.getTerms() != null && dataModel.getTerms().getDescription() != null)
                    viewHolder.description.setText(dataModel.getTerms().getDescription()[0]);
            }

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }


        lastPosition = position;


        return convertView;
    }

    private void loadUrl(String title) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setTitle(title);
        alert.setCancelable(true);
        WebView wv = new WebView(mContext);

        wv.loadUrl(Constants.WIKI_PAGE_URL+title);

        wv.setWebViewClient(new WebViewClient() {
            ProgressDialog prDialog;
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                prDialog = ProgressDialog.show(mContext, null, "Loading, please wait...");
                super.onPageStarted(view, url, favicon);
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);
                return true;
            }

             @Override
             public void onPageFinished(WebView view, String url) {
                 prDialog.dismiss();
                 super.onPageFinished(view, url);

             }
        });
        wv.getSettings().setJavaScriptEnabled(true);

        alert.setView(wv);
        alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alert.show();
    }



}
