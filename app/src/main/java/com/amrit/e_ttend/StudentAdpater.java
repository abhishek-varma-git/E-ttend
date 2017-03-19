package com.amrit.e_ttend;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Amrit on 3/9/2017.
 */

public class StudentAdpater extends RecyclerView.Adapter<StudentAdpater.ViewHolder> {


    ArrayList<StudentListItem> arrayList = new ArrayList<>();
    Context ctx;
    static String ssub_name;
    static String sub_name;
    static long diff;


    public StudentAdpater(ArrayList<StudentListItem> arrayList, Context ctx) {
        this.arrayList = arrayList;
        this.ctx = ctx;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subject_student_list_item, parent, false);
        ViewHolder viewholder = new ViewHolder(v, ctx, arrayList);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.head.setText(arrayList.get(position).getHead());
        holder.desc1.setText(arrayList.get(position).getDesc1());
        holder.desc2.setText(arrayList.get(position).getDesc2());
        holder.desc3.setText(arrayList.get(position).getDesc3());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView head;
        public TextView desc1;
        public TextView desc2;
        public TextView desc3;
        ArrayList<StudentListItem> studentListItems = new ArrayList<>();
        Context ctx;


        public ViewHolder(View itemView, Context ctx, ArrayList<StudentListItem> studentListItems) {
            super(itemView);
            this.studentListItems = studentListItems;
            this.ctx = ctx;
            itemView.setOnClickListener(this);
            head = (TextView) itemView.findViewById(R.id.head);
            desc1 = (TextView) itemView.findViewById((R.id.desc1));
            desc2 = (TextView) itemView.findViewById((R.id.desc2));
            desc3 = (TextView) itemView.findViewById((R.id.desc3));
        }

        @Override
        public void onClick(View v) {
            final AlertDialog.Builder builder;
            int position = getAdapterPosition();
            final StudentListItem studentListItem = this.studentListItems.get(position);

            sub_name = studentListItem.getHead();
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
            long localtime = cal.getTimeInMillis();
            long databasetime=Contents.timearray[position];
            diff = localtime - databasetime;

            // v.setEnabled(false);
            // v.setClickable(false);

           if (diff < 120000)
            {
                Snackbar.make(v, "You recently marked your attendance!\n Wait for 2 minutes to mark again", Snackbar.LENGTH_LONG)
                        .setAction("Action",null).show();
            }
            else
            {
               /* Date currentLocalTime = cal.getTime();
                DateFormat date = new SimpleDateFormat("dd-MM-yyy HH:mm:ss z");
                date.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
                String localTime = date.format(currentLocalTime);*/
                builder = new AlertDialog.Builder(this.ctx);
                builder.setTitle("Alert!");
                builder.setMessage("Are you Sure you want to continue ?\n\nAs you won't be able to mark your attendace for this subject again for one hour");
                builder.setPositiveButton("PROCEED", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ctx, fingerprint.class);
                        ssub_name = studentListItem.getHead();
                        ctx.startActivity(intent);
                        ((Activity)ctx).finish();

                    }
                });
                builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((Activity)ctx).finish();
                        ctx.startActivity(new Intent(ctx,StudentArea.class));
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        }
    }


}





