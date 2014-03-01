package com.example.zapposalert;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmActivity extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent arg1) {
		// TODO Auto-generated method stub
		
	long interval = 10000;
		 Intent intent = new Intent(context, NotifierService.class);  // <3>
		    PendingIntent pendingIntent = PendingIntent.getService(context, -1, intent,
		        PendingIntent.FLAG_UPDATE_CURRENT); // <4>

		    // Setup alarm service to wake up and start service periodically
		    AlarmManager alarmManager = (AlarmManager) context
		            .getSystemService(Context.ALARM_SERVICE); // <5>
		        alarmManager.setInexactRepeating(AlarmManager.RTC, System
		            .currentTimeMillis(), interval, pendingIntent); // <6>

		        Log.d("BootReceiver", "onReceived");
		   
		
	}

}
