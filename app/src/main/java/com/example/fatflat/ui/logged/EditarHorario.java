package com.example.fatflat.ui.logged;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.example.fatflat.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;


public class EditarHorario extends AppCompatActivity {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    WeekView mWeekView;
    List<WeekViewEvent> weekEvents;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_horario);
        //Escondemos la Action Bar porque usamos la ToolBar
        getSupportActionBar().hide();
        if (mAuth.getCurrentUser() == null) signInAnonymously();
        final ImageView Atras = findViewById(R.id.DeleteAccount_Atras);
        // Get a reference for the week view in the layout.
        mWeekView = findViewById(R.id.weekView);
        weekEvents = initWeekEvents();

        // Set an action when any event is clicked.
        //mWeekView.setOnEventClickListener(mEventClickListener);

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(mMonthChangeListener);

        // Set long press listener for events.
        //mWeekView.setEventLongPressListener(mEventLongPressListener);

        Atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditarHorario.this, Profile.class);
                onNewIntent(intent);
                finish();
            }
        });
    }

    MonthLoader.MonthChangeListener mMonthChangeListener = new MonthLoader.MonthChangeListener() {
        @Override
        public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
            // Populate the week view with some events.
            List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
            int numDays = 28;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                numDays = YearMonth.of(newYear, newMonth).lengthOfMonth();
            }
            for (int i = 0; i < numDays; ++i) {
                WeekViewEvent weekEvent = weekEvents.get(i%7);
                if (!weekEvent.getStartTime().equals(weekEvent.getEndTime())) {
                    Calendar startTime = (Calendar) weekEvent.getStartTime().clone();
                    startTime.set(Calendar.MONTH, newMonth - 1);
                    startTime.set(Calendar.YEAR, newYear);
                    startTime.set(Calendar.DAY_OF_MONTH, i);
                    Calendar endTime = (Calendar) weekEvent.getEndTime().clone();
                    endTime.set(Calendar.MONTH, newMonth - 1);
                    endTime.set(Calendar.YEAR, newYear);
                    endTime.set(Calendar.DAY_OF_MONTH, i);
                    WeekViewEvent event = new WeekViewEvent(i, getEventTitle(), startTime, endTime);
                    events.add(event);
                }
            }
            return events;
        }
    };

    List<WeekViewEvent> initWeekEvents() {
        List<WeekViewEvent> weekEvents = new ArrayList<WeekViewEvent>();
        //Monday
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 8);
        startTime.set(Calendar.MINUTE, 0);
        Calendar endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 7);
        WeekViewEvent event = new WeekViewEvent(3, getEventTitle(), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color));
        weekEvents.add(event);
        //Tuesday
        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 7);
        startTime.set(Calendar.MINUTE, 0);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 6);
        event = new WeekViewEvent(3, getEventTitle(), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color));
        weekEvents.add(event);
        //Wednesday
        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 7);
        startTime.set(Calendar.MINUTE, 0);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 8);
        event = new WeekViewEvent(3, getEventTitle(), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color));
        weekEvents.add(event);
        //Thursday
        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 14);
        startTime.set(Calendar.MINUTE, 0);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 5);
        event = new WeekViewEvent(3, getEventTitle(), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color));
        weekEvents.add(event);
        //Friday
        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 8);
        startTime.set(Calendar.MINUTE, 0);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 8);
        event = new WeekViewEvent(3, getEventTitle(), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color));
        weekEvents.add(event);
        //Saturday
        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 9);
        startTime.set(Calendar.MINUTE, 0);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 4);
        event = new WeekViewEvent(3, getEventTitle(), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color));
        weekEvents.add(event);
        //Sunday
        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 7);
        startTime.set(Calendar.MINUTE, 0);
        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 0);
        event = new WeekViewEvent(3, getEventTitle(), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color));
        weekEvents.add(event);

        return weekEvents;
    }

    protected String getEventTitle() {
        return "Horario de visitas";
    }

    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(this, new  OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // do your stuff
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            private static final String TAG = "SignInStorage" ;
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e(TAG, "signInAnonymously:FAILURE", exception);
            }
        });
    }
}

