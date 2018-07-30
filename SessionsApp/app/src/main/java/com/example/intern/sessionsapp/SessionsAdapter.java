package com.example.intern.sessionsapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SessionsAdapter extends RecyclerView.Adapter<SessionsAdapter.ViewHolder> {


    //Viewtype flags to indicate which layout to inflate i.e expanding or normal
    public static int upcompingType = 1;
    public static int normalType = 2;


    //Data store
    List<Session> mSessionsList;


    //Handling Clicks
    public interface ListItemClickListener {

        void onListItemClick(Session session);

    }

    private ListItemClickListener mOnclickListener;


    public SessionsAdapter(ListItemClickListener listener) {

        mOnclickListener = listener;


    }


    public abstract class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void bindType(Session session);

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            mOnclickListener.onListItemClick(mSessionsList.get(position));

        }

    }


    //ViewHolder Class for normal
    public class SessionsAdapterViewHolder extends ViewHolder {

        //Views
        public @BindView(R.id.subject)
        TextView subjectTextView;
        public @BindView(R.id.owner)
        TextView ownerTextView;
        public @BindView(R.id.account)
        TextView accountTextView;
        public @BindView(R.id.initials)
        TextView initialsTextView;
        public @BindView(R.id.date)
        TextView dateTextView;
        public @BindView(R.id.time)
        TextView timeTextView;
        public @BindView(R.id.period)
        TextView periodTextView;

        public Session session;

        public SessionsAdapterViewHolder(View view) {
            super(view);


            ButterKnife.bind(this, view);

            view.setOnClickListener(this);

        }

        @Override
        public void bindType(Session session) {

            this.session = session;

            subjectTextView.setText(session.getSubject());
            ownerTextView.setText(session.getOwner());
            accountTextView.setText(session.getAccountName());

            //Intials logic (Circular Icon)
            String name = session.getOwner();
            String initials = name.substring(0, 1).toUpperCase()
                    + name.substring(name.indexOf(' ') + 1, name.indexOf(' ') + 2).toUpperCase();
            initialsTextView.setText(initials);

            //Date Logic i,e converting to appropiate date format using DateHelper
            dateTextView.setText(DateHelper.getDateString(session.getStartDate(), "MMM dd, yyyy"));
            timeTextView.setText(DateHelper.getDateString(session.getStartDate(), "hh:mm"));
            periodTextView.setText(DateHelper.getDateString(session.getStartDate(), "a").toUpperCase());


        }


    }

    //ViewHolder Class for expanding layout
    public class SessionsAdapterExpandedViewHolder extends ViewHolder {

        //Views
        public @BindView(R.id.subject)
        TextView subjectTextView;
        public @BindView(R.id.owner)
        TextView ownerTextView;
        public @BindView(R.id.account)
        TextView accountTextView;
        public @BindView(R.id.initials)
        TextView initialsTextView;
        public @BindView(R.id.date)
        TextView dateTextView;
        public @BindView(R.id.time)
        TextView timeTextView;
        public @BindView(R.id.period)
        TextView periodTextView;

        public @BindView(R.id.location)
        TextView locationTextView;
        public @BindView(R.id.duration)
        TextView durationTextView;

        public Session session;

        public SessionsAdapterExpandedViewHolder(View view) {
            super(view);


            ButterKnife.bind(this, view);

            view.setOnClickListener(this);

        }

        @Override
        public void bindType(Session session) {

            this.session = session;

            subjectTextView.setText(session.getSubject());
            ownerTextView.setText(session.getOwner());
            accountTextView.setText(session.getAccountName());
            locationTextView.setText(session.getLocation());

            //Duration logic
            String duration = DateHelper.getDateString(session.getStartDate(), "hh:mm")
                    + " - "
                    + DateHelper.getDateString(session.getEndDate(), "hh:mm")
                    + DateHelper.getDateString(session.getStartDate(), "a").toUpperCase();

            durationTextView.setText(duration);


            //Intials logic (Circular Icon)
            String name = session.getOwner();
            String initials = name.substring(0, 1).toUpperCase()
                    + name.substring(name.indexOf(' ') + 1, name.indexOf(' ') + 2).toUpperCase();
            initialsTextView.setText(initials);

            //Date Logic i,e converting to appropiate date format using DateHelper
            dateTextView.setText(DateHelper.getDateString(session.getStartDate(), "MMM dd, yyyy"));
            timeTextView.setText(DateHelper.getDateString(session.getStartDate(), "hh:mm"));
            periodTextView.setText(DateHelper.getDateString(session.getStartDate(), "a").toUpperCase());


        }


    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.session_list_item;
        int layoutIdForListItemExpanded = R.layout.session_list_item_expanded;

        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;


        View view = null;

        //Handling different viewTypes
        if (viewType == normalType) {
            view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
            return new SessionsAdapterViewHolder(view);
        } else if (viewType == upcompingType) {

            view = inflater.inflate(layoutIdForListItemExpanded, parent, shouldAttachToParentImmediately);
            return new SessionsAdapterExpandedViewHolder(view);
        }

        return null;// will never reach

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.bindType(mSessionsList.get(position));


    }


    @Override
    public int getItemViewType(int position) {

        if (mSessionsList.get(position).isSessionUpcoming())
            return upcompingType;
        else
            return normalType;

    }

    @Override
    public int getItemCount() {

        if (mSessionsList == null)
            return 0;
        else
            return mSessionsList.size();
    }


    public void setSessionsData(List<Session> sessionsList) {
        mSessionsList = sessionsList;
        notifyDataSetChanged();
    }


}
