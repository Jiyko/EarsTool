package com.radicalninja.logger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.anysoftkeyboard.ui.settings.setup.FinishInstallScreen;

/**
 * Created by gwicks on 20/10/2016.
 */

public class MyBroadCastReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Intent myIntent = new Intent(context, FinishInstallScreen.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(myIntent);
    }

}