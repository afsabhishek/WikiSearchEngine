package com.android.wikimedia.util;

import android.content.DialogInterface;

/**
 * Created by Abhishek.Kumar on 2/23/2017.
 */



public interface AlertMessage {

  public abstract void OkMethod(DialogInterface dialog, int id);

  public abstract void CancelMethod(DialogInterface dialog, int id);

}

