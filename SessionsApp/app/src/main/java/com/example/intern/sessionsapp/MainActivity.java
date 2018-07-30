package com.example.intern.sessionsapp;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.intern.sessionsapp.Session.JSONDateformat;


public class MainActivity extends AppCompatActivity implements SessionsAdapter.ListItemClickListener {

    @BindView(R.id.recycler_view_sessions)
    RecyclerView mRecyclerView;


    private SessionsAdapter mSessionsAdapter;
    private List<Session> sessionList;
    private List<Invitee> inviteesList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        initRecyclerView();
        //Init Sessions and Invitees List
        initLists();

        //Loading data to adapter
        mSessionsAdapter.setSessionsData(sessionList);


    }


    public void initRecyclerView() {

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        //mRecyclerView.setHasFixedSize(true);
        mSessionsAdapter = new SessionsAdapter(this);
        mRecyclerView.setAdapter(mSessionsAdapter);


    }


    /*Method to initialize Lists for sessions and invitees
     *
     * */
    public void initLists() {

        //Initialize Sessions and Invitees List
        String inviteesJSONString = loadJSONFromAsset("Invitees.json");
        String sessionJSONString = loadJSONFromAsset("session.json");
        inviteesList = parseJSONInvitees(inviteesJSONString);
        sessionList = parseJSONSessions(sessionJSONString);

        //Sorting List in descending order
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {


            sessionList.sort(new Comparator<Session>() {

                @Override
                public int compare(Session o1, Session o2) {


                    //Converting Start date string to object
                    Date o1Date = null;
                    Date o2Date = null;
                    try {
                        SimpleDateFormat format = new SimpleDateFormat(DateHelper.JSONformat);
                        o1Date = format.parse(o1.getStartDate());
                        o2Date = format.parse(o2.getStartDate());
                    } catch (Exception e) {

                        e.printStackTrace();
                    }

                    if (o1Date.before(o2Date))

                        return 1;

                    else if (o1Date.after(o2Date))
                        return -1;
                    else
                        return 0;


                }


            });
        }


    }

    /*Method that loads json file and returns json file
     *
     * */
    public String loadJSONFromAsset(String fileName) {

        String json = null;
        try {

            InputStream is = getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    /*Method that loads sessions file and return the list of all sessions
     *
     * */
    public List<Session> parseJSONSessions(String json) {

        List<Session> sessionsList = new ArrayList<>();


        JSONObject jsonObject = null;
        JSONArray jsonArray = null;


        try {
            jsonObject = new JSONObject(json);
            jsonArray = jsonObject.getJSONArray("Sessions");

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject sessionJSONObject = jsonArray.getJSONObject(i);

                //Converting Json object to POJO (
                Gson gson = new Gson();
                Session session = gson.fromJson(sessionJSONObject.toString(), Session.class);

                sessionsList.add(session);


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return sessionsList;


    }


    /*Method that loads invitee file and return the list of all sessions
     *
     * */
    public List<Invitee> parseJSONInvitees(String json) {

        List<Invitee> inviteesList = new ArrayList<>();


        JSONObject jsonObject = null;
        JSONArray jsonArray = null;


        try {
            jsonObject = new JSONObject(json);
            jsonArray = jsonObject.getJSONArray("invitees");

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject inviteeJSONObject = jsonArray.getJSONObject(i);

                //Converting Json object to POJO (
                Gson gson = new Gson();
                Invitee invitee = gson.fromJson(inviteeJSONObject.toString(), Invitee.class);

                inviteesList.add(invitee);


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return inviteesList;


    }


    @Override
    public void onListItemClick(Session session) {

        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);

        //Creating Bundle
        Bundle bundle = new Bundle();
        bundle.putString("Subject", session.getSubject());
        bundle.putString("Location", session.getLocation());
        bundle.putString("Type", session.getActivityType());
        bundle.putString("Start", DateHelper.getDateString(session.getStartDate(), "EEEE, MM/dd/yyyy, hh:mm a"));
        bundle.putString("End", DateHelper.getDateString(session.getEndDate(), "EEEE, MM/dd/yyyy, hh:mm a"));
        bundle.putString("Account", session.getAccountName());
        bundle.putString("Opportunity", session.getOpportunityName());
        bundle.putString("Lead", session.getLeadName());
        bundle.putString("Primary Contact", session.getPrimaryContactName());
        bundle.putString("Description", session.getDescription());

        ArrayList<String> inviteeNames = new ArrayList<>();
        ArrayList<String> inviteeJobTitles = new ArrayList<String>();
        ArrayList<String> inviteeEmails = new ArrayList<String>();

        int numInvitees = session.getInvitees().size();

        for (int i = 0; i < numInvitees; i++) {

            Invitee invitee = session.getInvitees().get(i);

            inviteeNames.add(invitee.getName());
            inviteeJobTitles.add(invitee.getJobTitle());
            inviteeEmails.add(invitee.getEmail());

        }

        bundle.putSerializable("names", inviteeNames);
        bundle.putSerializable("titles", inviteeJobTitles);
        bundle.putSerializable("emails", inviteeEmails);

        intent.putExtras(bundle);
        startActivity(intent);

    }
}

