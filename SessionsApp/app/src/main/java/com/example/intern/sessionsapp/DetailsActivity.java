package com.example.intern.sessionsapp;

import android.content.Intent;
import android.os.Build;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {


    //Views
    @BindView(R.id.subject)
    TextView subject;
    @BindView(R.id.location)
    TextView location;
    @BindView(R.id.type)
    TextView type;
    @BindView(R.id.start)
    TextView start;
    @BindView(R.id.end)
    TextView end;
    @BindView(R.id.account)
    TextView account;
    @BindView(R.id.opportunity)
    TextView oppurtunity;
    @BindView(R.id.lead)
    TextView lead;
    @BindView(R.id.primaryContact)
    TextView primaryContact;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.invitees_list)
    LinearLayout inviteesLayout;

    //Used for Calender
    String startDate;
    String endDate;
    String subjectStr;
    String locationStr;
    String descriptionStr;
    ArrayList<String> inviteeEmails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_details);

        //Initializing Views
        ButterKnife.bind(this);

        Intent intent = getIntent();
        populateUI(intent);

    }


    public void populateUI(Intent intent) {

        subjectStr = intent.getStringExtra("Subject");
        setTitle(subjectStr);
        subject.setText(subjectStr);

        locationStr = intent.getStringExtra("Location");
        location.setText(locationStr);

        type.setText(intent.getStringExtra("Type"));

        startDate = intent.getStringExtra("Start");
        start.setText(startDate);

        endDate = intent.getStringExtra("End");
        end.setText(endDate);

        account.setText(intent.getStringExtra("Account"));
        oppurtunity.setText(intent.getStringExtra("Opportunity"));
        lead.setText(intent.getStringExtra("Lead"));
        primaryContact.setText(intent.getStringExtra("Primary Contact"));

        descriptionStr = intent.getStringExtra("Description");
        description.setText(descriptionStr);


        //Invitees Logic (List of Invitees)
        ArrayList<String> inviteeNames = (ArrayList<String>) intent.getSerializableExtra("names");
        ArrayList<String> inviteeJobTitles = (ArrayList<String>) intent.getSerializableExtra("titles");
        inviteeEmails = (ArrayList<String>) intent.getSerializableExtra("emails");


        int numInvitees = inviteeNames.size();

        LayoutInflater inflater = LayoutInflater.from(this);

        for (int i = 0; i < numInvitees; i++) {


            View newInvitee = inflater.inflate(R.layout.invitee_option,
                    inviteesLayout, false);


            TextView invitee = (TextView) newInvitee.findViewById(R.id.invitee);
            TextView title = (TextView) newInvitee.findViewById(R.id.jobTitle);
            TextView initials = (TextView) newInvitee.findViewById(R.id.initials);

            invitee.setText(inviteeNames.get(i));
            title.setText(inviteeJobTitles.get(i));

            //Intials logic (Circular Icon)
            String name = inviteeNames.get(i);
            String initialsStr = name.substring(0, 1).toUpperCase()
                    + name.substring(name.indexOf(' ') + 1, name.indexOf(' ') + 2).toUpperCase();
            initials.setText(initialsStr);

            //Email Intent logic
            final String email = inviteeEmails.get(i);
            newInvitee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    String aEmailList[] = new String[]{email};
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, aEmailList);
                    emailIntent.setType("text/plain");
                    startActivity(Intent.createChooser(emailIntent, "Send Mail Using :"));

                }
            });

            //Adding View to Layout
            inviteesLayout.addView(newInvitee);

        }

    }


    /* Method that add Session to Calender
     *
     * */
    public void addSessionToCalender() {

        Calendar beginTime = Calendar.getInstance();
        Calendar endTime = Calendar.getInstance();

        try {
            beginTime.setTime(new SimpleDateFormat("EEEE, MM/dd/yyyy, hh:mm a").parse(startDate));
            endTime.setTime(new SimpleDateFormat("EEEE, MM/dd/yyyy, hh:mm a").parse(endDate));
        } catch (Exception e) {

            e.printStackTrace();

        }

        String emails = "";

        if (!inviteeEmails.isEmpty()) {
            emails = inviteeEmails.get(0);

            for (int i = 1; i < inviteeEmails.size(); i++) {

                emails = emails + "," + inviteeEmails.get(i);

            }

        }

        if (Build.VERSION.SDK_INT >= 14) {
            Intent intent = new Intent(Intent.ACTION_INSERT)
                    .setData(CalendarContract.Events.CONTENT_URI)
                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                    .putExtra(CalendarContract.Events.TITLE, subjectStr)
                    .putExtra(CalendarContract.Events.DESCRIPTION, descriptionStr)
                    .putExtra(CalendarContract.Events.EVENT_LOCATION, locationStr)
                    .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
                    .putExtra(Intent.EXTRA_EMAIL, emails);
            startActivity(intent);
        } else {
            Calendar cal = Calendar.getInstance();
            Intent intent = new Intent(Intent.ACTION_EDIT);
            intent.setType("vnd.android.cursor.item/event");
            intent.putExtra("beginTime", beginTime.getTimeInMillis());
            intent.putExtra("allDay", true);
            intent.putExtra("rrule", "FREQ=YEARLY");
            intent.putExtra("endTime", endTime.getTimeInMillis());
            intent.putExtra("title", subjectStr);
            startActivity(intent);
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.menu, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.addToCalender) {

            //Adding session to calender
            addSessionToCalender();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}




