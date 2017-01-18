package com.kilogramm.mattermost.rxtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.kilogramm.mattermost.R;
import com.kilogramm.mattermost.view.BaseActivity;

/**
 * Created by kepar on 18.01.17.
 */

public class StartScreenActivity extends BaseActivity<StartScreenPresenter> {

    BroadcastReceiver mBroadcastReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        tryToStart();


    }

    private void tryToStart(){
        if(getPresenter().isNetworkAvailable()){
            startActivity(new Intent(this, MainRxActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
        } else {
            Toast.makeText(this,
                    "Network error. Please check your Internet connetion",
                    Toast.LENGTH_SHORT).show();

            mBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getExtras() != null) {
                        final ConnectivityManager connectivityManager = (ConnectivityManager)context
                                .getSystemService(Context.CONNECTIVITY_SERVICE);
                        final NetworkInfo ni = connectivityManager.getActiveNetworkInfo();

                        if (ni != null && ni.isConnectedOrConnecting()) {
                            tryToStart();
                        } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY,
                                Boolean.FALSE)) {

                        }
                    }
                }
            };

            registerReceiver(mBroadcastReceiver,
                    new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        }
    }

    @Override
    protected void onDestroy() {
        if(mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
        }
        super.onDestroy();
    }
}
