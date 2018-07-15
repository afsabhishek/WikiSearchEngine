package com.android.wikimedia.network;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.android.wikimedia.Constants;
import com.android.wikimedia.FacadeApplication;

import com.android.wikimedia.R;
import com.android.wikimedia.model.ResponseDto;
import com.android.wikimedia.util.AlertMessage;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.wikimedia.Constants;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.android.wikimedia.Constants.CONTENT_TYPE;
import static com.android.wikimedia.Constants.ERROR_DES;
import static com.android.wikimedia.Constants.UTF;
import static com.android.volley.VolleyLog.TAG;

public class NetworkAPI {
    static int statusCode = 0;
    static FetchSearchListInterface mFetchSearchListInterface;

    public interface FetchSearchListInterface {
        void fetchSearchImp(ResponseDto results);
    }
    static ProgressDialog mProgressDialog;
    private static Gson gson = new Gson();

    public static void showProgressDialog(Context context, boolean isShow) {
        try {
            if (mProgressDialog == null && isShow) {
                mProgressDialog = new ProgressDialog(context);
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.setMessage("Processing...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();
            } else {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                }
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    public static String getString(Context context, int resId) {
        return context.getResources().getString(resId);
    }

    public static void getConfirmDialog(Context mContext, int titleID, String msg,
                                        int positiveBtnCaptionID, int negativeBtnCaptionID, boolean isCancelable,
                                        final AlertMessage target) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        String title = getString(mContext, titleID);
        int imageResource;
        if (title.contains(mContext.getResources().getString(R.string.success)))
            imageResource = R.drawable.ok;
        else
            imageResource = R.drawable.alert_error;
        Drawable image = mContext.getResources().getDrawable(imageResource);

        if (negativeBtnCaptionID == -1) {
            builder.setTitle(title).setMessage(msg).setIcon(image).setCancelable(false).setPositiveButton(
                    getString(mContext, positiveBtnCaptionID), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            target.OkMethod(dialog, id);
                        }
                    });
        } else {
            builder.setTitle(getString(mContext, titleID)).setMessage(msg).setIcon(image)
                    .setCancelable(false).setPositiveButton(getString(mContext, positiveBtnCaptionID),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            target.OkMethod(dialog, id);
                        }
                    })
                    .setNegativeButton(getString(mContext, negativeBtnCaptionID),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    target.CancelMethod(dialog, id);
                                }
                            });
        }

        AlertDialog alert = builder.create();
        alert.setCancelable(isCancelable);
        alert.show();

        if (isCancelable) {
            alert.setOnCancelListener(new DialogInterface.OnCancelListener() {

                @Override
                public void onCancel(DialogInterface arg0) {
                    target.CancelMethod(null, 0);
                }
            });
        }
    }
    public static void showDialog(final Context context, int id, String message) {
        getConfirmDialog(context, id, message, R.string.ok, -1, false, new AlertMessage() {
            @Override
            public void OkMethod(DialogInterface dialog, int id) {
                    dialog.dismiss();

            }

            @Override
            public void CancelMethod(DialogInterface dialog, int id) {
                // Not Applicable
            }
        });
    }

    private static void playerUnavailable(final Context context) {
        getConfirmDialog(context, R.string.error_title, getString(context,R.string.error_message), R.string.ok, -1,
                false, new AlertMessage() {
                    @Override
                    public void OkMethod(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }

                    @Override
                    public void CancelMethod(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
    }

    public static void showErrorMessage(Context context, VolleyError error) {
        try {
            String errorMsg;
            if (error != null) {
                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    String res =
                            new String(response.data, HttpHeaderParser.parseCharset(response.headers, UTF));
                    JSONObject obj = new JSONObject(res);
                    Log.v("VMA", "Obj " + obj.toString());
                    if (obj.has("errorMessage")) {
                        errorMsg = obj.getString("errorMessage");
                        showDialog(context, R.string.error_title, errorMsg);
                    } else if (obj.has("error")) {
                        errorMsg = obj.getString("error");
                        showDialog(context, R.string.error_title, errorMsg);
                    } else if (obj.has(ERROR_DES)) {
                        errorMsg = obj.getString(ERROR_DES);
                        showDialog(context, R.string.error_title, errorMsg);
                    }
                } else {
                    playerUnavailable(context);
                }
            } else {
                playerUnavailable(context);
            }
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
    }

    public static void getSearchResult(final Context context, String inputQuery){

        String finalUrl = Constants.WIKI_BASE_URL+inputQuery+Constants.WIKI_END_POINT;
        Log.v("VMA","FINAL URL "+finalUrl);
        mFetchSearchListInterface = (FetchSearchListInterface) context;
        showProgressDialog(context,true);
        try {
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, finalUrl, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                ResponseDto responseDto = gson.fromJson(response.toString(),ResponseDto.class);
                                showProgressDialog(context,false);
                                mFetchSearchListInterface.fetchSearchImp(responseDto);

                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    showProgressDialog(context, false);

                    NetworkResponse response = error.networkResponse;
                    if (response != null) {
                        showErrorMessage(context, error);
                    } else {
                        playerUnavailable(context);
                    }
                }
            }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put(CONTENT_TYPE, "application/json");
                    return headers;
                }

                @Override
                protected com.android.volley.Response<JSONObject> parseNetworkResponse(
                        NetworkResponse response) {
                    statusCode = response.statusCode;
                    return super.parseNetworkResponse(response);
                }
            };
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(300000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            FacadeApplication.getInstance().addToRequestQueue(jsonObjReq, TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
