package com.example.uts.UnitTesting;

import android.content.Context;
import android.content.Intent;

import com.example.uts.ReservationActivity;

public class ActivityUtil {
    private Context context;

    public ActivityUtil(Context context) {
        this.context = context;
    }

    public void startMainReservation() {
        context.startActivity(new Intent(context, ReservationActivity.class));
    }
}
