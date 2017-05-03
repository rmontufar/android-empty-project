package com.webtrack.examples.locationapiexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.util.Log;

/**
 * Created by renato on 2/05/17.
 */
public class BatteryLevelReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        //boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
        //        status == BatteryManager.BATTERY_STATUS_FULL;

        //int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        Log.d("BatteryLevel", "BATERIA BAJA ");
        //boolean usbCharge = chargePlug == BATTERY_PLUGGED_USB;
        //boolean acCharge = chargePlug == BATTERY_PLUGGED_AC;
    }
}