package com.amrit.e_ttend;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Amrit on 3/20/2017.
 */

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.ViewHolder> {

    static String emp_id;
    ArrayList<TeacherListItems> arrayList = new ArrayList<>();
    Context ctx;


    public TeacherAdapter(ArrayList<TeacherListItems> arrayList, Context ctx) {
        this.arrayList = arrayList;
        this.ctx = ctx;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_teacher_list_items, parent, false);
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
        ArrayList<TeacherListItems> teacherListItems = new ArrayList<>();
        Context ctx;


        public ViewHolder(View itemView, Context ctx, ArrayList<TeacherListItems> teacherListItems) {
            super(itemView);
            this.teacherListItems = teacherListItems;
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
            final TeacherListItems teacherListItems = this.teacherListItems.get(position);
            emp_id = teacherListItems.getHead();

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
                        emp_id = teacherListItems.getHead();
                        ctx.startActivity(intent);
                        ((Activity) ctx).finish();

                    }
                });
                builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((Activity) ctx).finish();
                        ctx.startActivity(new Intent(ctx, StudentNav.class));
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        }
    }
}
