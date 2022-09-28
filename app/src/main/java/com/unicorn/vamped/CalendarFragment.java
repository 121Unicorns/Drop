package com.unicorn.vamped;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.google.android.material.snackbar.Snackbar;
import com.unicorn.vampedcalendar.VampedCalendar;
import com.unicorn.vampedcalendar.listener.OnDateClickedListener;
import com.unicorn.vampedcalendar.model.Event;
import com.unicorn.vampedcalendar.model.EventDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarFragment extends Fragment {
    VampedCalendar eventCalendar;
    String date, day, month;
    Date selectDate;
    List<Event> events = new ArrayList<>();
    Calendar calendar = Calendar.getInstance();
    private DbHandler dbHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        eventCalendar = view.findViewById(R.id.calendar);
        customizeCalendar();

        //-------------------------------CALENDAR TO CREATE EVENTS----------------------------------
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        eventCalendar.setOnDateClickedListener(new OnDateClickedListener() {
            @Override
            public void onDateClicked(Event event) {
                try {
                    selectDate = getDateFromEventDate(event.getEventDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (new Date().before(selectDate)) {
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Sorry, you cannot track symptoms in advance!", Snackbar.LENGTH_LONG).show();
                } else {
                    if (event.getEventDate().getDay() < 10) {
                        day = "0" + event.getEventDate().getDay();
                    } else {
                        day = String.valueOf(event.getEventDate().getDay());
                    }
                    if (event.getEventDate().getMonth() < 10) {
                        month = "0" + event.getEventDate().getMonth();
                    } else {
                        month = String.valueOf(event.getEventDate().getMonth());
                    }
                    date = day + "/" + month + "/" + event.getEventDate().getYear();
                    Intent eventIntent = new Intent(getContext(), EventActivity.class);
                    eventIntent.putExtra("selectedDate", date);
                    startActivity(eventIntent);
                }
            }
        });
        return view;
    }

    private void customizeCalendar() {
        //eventCalendar.setHeaderBackgroundColor(getResources().getColor(R.color.colorPrimary));
        //eventCalendar.setBackground(ContextCompat.getDrawable(getContext(), R.color.transparentColor));
        eventCalendar.setDefaultEventDayShape(VampedCalendar.EventShape.CIRCLE);
        eventCalendar.setSaturdayLabelColor(getResources().getColor(R.color.colorPrimary));
        eventCalendar.setNoEventDayTextColor(getResources().getColor(R.color.colorGrey));

        //---------------------GET CURRENT DATE AND SET TRANSPARENT CIRCLE--------------------------
        EventDate eventDate = getCurrentDate();
        events.add(new Event(eventDate, ContextCompat.getDrawable(getContext(), R.drawable.ic_circle), getResources().getColor(R.color.colorPrimary)));
        eventCalendar.setEvents(events);
    }

    private Date getDateFromEventDate(EventDate eventDate) throws ParseException {
        String strDate;
        Date date;
        if (eventDate.getDay() < 10) {
            day = "0" + eventDate.getDay();
        } else {
            day = String.valueOf(eventDate.getDay());
        }
        if (eventDate.getMonth() < 10) {
            month = "0" + eventDate.getMonth();
        } else {
            month = String.valueOf(eventDate.getMonth());
        }
        strDate = day + "/" + month + "/" + eventDate.getYear();
        date = new SimpleDateFormat("dd/MM/yyyy").parse(strDate);
        return date;
    }


    /*@Override
    public void onStart() {
        super.onStart();
        DatabaseHandler db = new DatabaseHandler(this);
        ArrayList<HashMap<String, String>> myList = db.getAllDayCycles();
        int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        ArrayList<String> daysList = new ArrayList<>();
        String thisDay, thisMonth;

        thisMonth = (String.format("%02d", (calendar.get(Calendar.MONTH) + 1)));

        for (int i = 1; i <= days; i++) {
            thisDay = (String.format("%02d", i));
            daysList.add(thisDay + "" + thisMonth + calendar.get(Calendar.YEAR));
        }

        for (int x = 0; x < myList.size(); x++) {
            for (int k = 0; k < daysList.size(); k++) {
                if (myList.get(x).get("date").equals(daysList.get(k))) {
                    try {
                        Date foundDate = formatter2.parse(daysList.get(k));
                        calendar.setTime(foundDate);
                        int foundYear = Integer.parseInt(DateFormat.format("yyyy", foundDate).toString());
                        int foundMonth = Integer.parseInt(DateFormat.format("MM", foundDate).toString());
                        int foundDay = Integer.parseInt(DateFormat.format("dd", foundDate).toString());
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
                        EventDate eventDate = new EventDate(foundYear, foundMonth, foundDay);
                        events.add(new Event(eventDate, getResources().getColor(R.color.colorPrimary), EventCalendar.EventShape.CIRCLE));
                        eventCalendar.setEvents(events);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }*/

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        setRetainInstance(true);
    }

    private EventDate getCurrentDate() {
        Calendar mycalendar = Calendar.getInstance();
        mycalendar.set(Calendar.DAY_OF_MONTH, 1);
        Date toDate = new Date();

        int foundYear = Integer.parseInt(DateFormat.format("yyyy", toDate).toString());
        int foundMonth = Integer.parseInt(DateFormat.format("MM", toDate).toString());
        int foundDay = Integer.parseInt(DateFormat.format("dd", toDate).toString());
        mycalendar.add(Calendar.DAY_OF_MONTH, 1);
        return new EventDate(foundYear, foundMonth, foundDay);
    }
}
