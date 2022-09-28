package com.unicorn.vamped;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/*======================================================EVENT CODES======================================================
    _________________________________________________________________________________________________________________________
    ****EVENTS
    _________________________________________________________________________________________________________________________
        101 - PERIOD
        -----INTENSITY---
             201 - SPOTTING
             202 - LIGHT
             203 - MEDIUM
             204 - HARD
        102 - INTERCOURSE
        103 - CONTRACEPTIVE PILL
        104 - CONTRACEPTIVE INJECTION
        105 - NEW PATCH
        106 - NEW IUD
        107 - NEW RING
    _________________________________________________________________________________________________________________________
    ****SYMPTOMS
    _________________________________________________________________________________________________________________________
        108 - CRAMPS
        109 - HEADACHE/MIGRAINE
        110 - BACKACHE
        111 - NAUSEA
        112 - DIARRHEA
        113 - THRUSH/CANDIDIASIS
        114 - TENDER BREAST
        115 - DISCHARGE
        116 - BLOATING
        117 - CRAVINGS
        118 - PIMPLES
    _________________________________________________________________________________________________________________________
    ****MOODS
    _________________________________________________________________________________________________________________________
        119 - TIRED
        120 - ENERGETIC
        121 - SAD
        122 - HAPPY
        123 - ANGRY
        124 - EDGY/IRRITABLE
        125 - FRISKY
    _________________________________________________________________________________________________________________________
     */

public class EventActivity extends AppCompatActivity implements ListedListener, FlowListener {

    com.unicorn.vamped.NoScrollExListView expandList, expandListFlow;
    TextView tvTitle;
    int eventdate, eventtype, intensity;
    RadioButton rbPerYes, rbPerNo;
    String flow, selectedDate;
    ExpandListAdapter listAdapter;
    ExpandabListAdapter expandabListAdapter;
    List<String> listDataHeader, listDataHeader2, temp;
    List<Integer> symptom;
    HashMap<String, List<String>> listDataChild, listDataChild2;
    Calendar calendar = Calendar.getInstance();
    DbHandler dbHandler = new DbHandler(EventActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        initViews();
        prepareListData();

        SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatter2 = new SimpleDateFormat("dd MMM yyyy");
        SimpleDateFormat formatter3 = new SimpleDateFormat("yyyyMMdd");

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        selectedDate = getIntent().getStringExtra("selectedDate");
        try {
            //-----CONVERT STRING DATE SELECTED BY USER TO DATE
            Date date1 = formatter1.parse(selectedDate);

            //-----CONVERT DATE TO READABLE FORMAT & SET TEXT IN TEXTVIEW
            String dateVisual = formatter2.format(date1);
            tvTitle.setText("TRACK " + dateVisual + " SYMPTOMS");

            //-----CONVERT DATE TO STRING TO UPLOAD AS EVENTDATE KEY
            String myEventdate = formatter3.format(date1);
            eventdate = Integer.parseInt(myEventdate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        //----------------------------ADAPTER FOR CHECKBOX EXPAND LIST VIEW-------------------------
        listAdapter = new ExpandListAdapter(this, listDataHeader, listDataChild);
        listAdapter.setmListener(this);
        expandList.setAdapter(listAdapter);

        //--------------------------ADAPTER FOR RADIO BUTTON EXPAND LIST VIEW-----------------------
        expandabListAdapter = new ExpandabListAdapter(this, listDataHeader2, listDataChild2);
        expandabListAdapter.setfListener(this);
        expandListFlow.setAdapter(expandabListAdapter);

        //--------------------------LOGGING LISTS TO SAVE DAY & CYCLE DATA--------------------------
        symptom = new ArrayList<>();

        rbPerYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isPeriod();
            }
        });
        rbPerNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isPeriod();
            }
        });
    }

    private boolean isPeriod() {
        if (rbPerNo.isChecked()) {
            expandListFlow.setEnabled(false);
            expandListFlow.setClickable(false);
            expandListFlow.clearChoices();
            flow = null;
            eventtype = 100;
            expandListFlow.collapseGroup(0);
            return false;
            //System.out.println(flow);
        } else if (rbPerYes.isChecked()) {
            eventtype = 101;
            expandListFlow.setEnabled(true);
            expandListFlow.setClickable(true);
            expandListFlow.expandGroup(0);
            return true;
        }
        return false;
    }

    private void initViews() {
        expandList = findViewById(R.id.expList);
        tvTitle = findViewById(R.id.tv_seldate);
        expandListFlow = findViewById(R.id.expListFlow);
        rbPerYes = findViewById(R.id.rb_peryes);
        rbPerNo = findViewById(R.id.rb_perno);

        expandListFlow.setEnabled(false);
        expandListFlow.setClickable(false);
    }

    //CREATE LISTS FROM STRING ARRAYS AND USE THEM FOR CHECKBOX AND RADIO GROUP EXPANDABLE LIST VIEWS
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        listDataHeader2 = new ArrayList<String>();
        listDataChild2 = new HashMap<String, List<String>>();

        //Adding child data
        listDataHeader.add(getResources().getString(R.string.str_mood));
        listDataHeader.add(getResources().getString(R.string.str_pain));
        listDataHeader.add(getResources().getString(R.string.str_contra));
        listDataHeader2.add(getResources().getString(R.string.str_flow));

        //Setting string arrays as list
        List<String> moodList = Arrays.asList(getResources().getStringArray(R.array.mood_array));
        List<String> painList = Arrays.asList(getResources().getStringArray(R.array.pain_array));
        List<String> contraList = Arrays.asList(getResources().getStringArray(R.array.contra_array));
        List<String> flowList = Arrays.asList(getResources().getStringArray(R.array.flow_array));

        //add lists to
        listDataChild.put(listDataHeader.get(0), moodList);
        listDataChild.put(listDataHeader.get(1), painList);
        listDataChild.put(listDataHeader.get(2), contraList);
        listDataChild2.put(listDataHeader2.get(0), flowList);
    }

    @Override
    public void onListChanged(final ArrayList<String> chosenChildren) {
        temp = chosenChildren;
        createSymptomCode(temp);
    }

    @Override
    public void onFlowChanged(final String chosenFlow) {
        flow = chosenFlow;
        if (isPeriod()) {
            updateDataTable(chosenFlow);
        } else {
            updateDataTable(null);
        }
    }

    public void createSymptomCode(List<String> temp) {
        List<String> sympList = Arrays.asList(getResources().getStringArray(R.array.symp_array));
        ArrayList<Integer> codeList = new ArrayList<>();
        int code = 102;
        for (int i = 0; i < sympList.size(); i++) {
            codeList.add(code);
            code++;
        }
        for (int j = 0; j < sympList.size(); j++) {
            if (temp.contains(sympList.get(j))) {
                if (!symptom.contains(sympList.get(j))) {
                    symptom.add(codeList.get(j));
                }
            }
        }
    }

    public void updateDataTable(String flow) {
        if (flow == null) {
            System.out.println("NOPE");
            intensity = 0;
        } else if (flow.equals("Spotting")) {
            intensity = 201;
        } else if (flow.equals("Light")) {
            intensity = 202;
        } else if (flow.equals("Medium")) {
            intensity = 203;
        } else if (flow.equals("Heavy")) {
            intensity = 204;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHandler.trackPeriod(eventdate, eventtype, intensity);
        for (int i = 0; i<symptom.size(); i++){
            dbHandler.trackSymptoms(eventdate, symptom.get(i));
        }
    }
}