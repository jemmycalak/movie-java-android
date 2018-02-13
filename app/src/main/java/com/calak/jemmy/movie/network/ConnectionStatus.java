package com.calak.jemmy.movie.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.calak.jemmy.movie.model.BaseActivity;

import java.io.IOException;

public class ConnectionStatus extends BroadcastReceiver {

    public static final String NETWORK_AVAILABLE_ACTION = "com.calak.jemmy.movie.network.ConnectionStatus";
    public static final String IS_NETWORK_AVAILABLE = "isNetworkAvailable";

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent networkIntent = new Intent(NETWORK_AVAILABLE_ACTION);
        networkIntent.putExtra(IS_NETWORK_AVAILABLE, isConnected(context));
        LocalBroadcastManager.getInstance(context).sendBroadcast(networkIntent);
    }

    public Boolean isConnected(Context context){
        try{
            if(context!=null){
                ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnected();
            }
            return false;
        }catch(Exception e){
            Log.d(ConnectionStatus.class.getName(), e.getMessage());
            return false;
        }
    }
}
