package com.amrit.e_ttend;

import android.app.Activity;
import android.app.LauncherActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by Amrit on 3/9/2017.
 */

public class StudentAdpater extends RecyclerView.Adapter<StudentAdpater.ViewHolder> {


    ArrayList<StudentListItem> arrayList = new ArrayList<>();
    Context ctx;
    static String ssub_name;
    static String sub_name;
    static String usn = StudentNav.susn;
    static long time;
    static long diff;
    static long databasetime;
    static ProgressDialog progressDialog;
    static int flag=0,flag1=0;



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

        String time_url = "http://irretrievable-meter.000webhostapp.com/timeretrieve.php";
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
            progressDialog = new ProgressDialog(ctx);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, time_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();

                            try {
                                JSONArray JsonArray = new JSONArray(response);
                                JSONObject jsonObject = JsonArray.getJSONObject(0);
                                databasetime=jsonObject.getLong("time");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    progressDialog.dismiss();
                    flag=1;
                    Toast.makeText(ctx, "Error! Check for your Internet Connection" + "        Unable to fetch data from Server", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("usn", usn);
                    params.put("sub_name", sub_name);
                    return params;
                }
            };
            MySingleton.getInstance(ctx).addToRequestque(stringRequest);

            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
            long localtime = cal.getTimeInMillis();
            //Toast.makeText(ctx,time,Toast.LENGTH_LONG).show();

            diff = localtime - databasetime;
            if(databasetime==0)
            {
                progressDialog.dismiss();
                Toast.makeText(ctx,"Click again to confirm",Toast.LENGTH_SHORT).show();
            }
            // v.setEnabled(false);
            // v.setClickable(false);

           else if (diff < 30000 && flag==0 )
            {
                    progressDialog.dismiss();
                    Toast.makeText(ctx, "Wait for 2 minute to mark your attendance   " , Toast.LENGTH_SHORT).show();
            }
            else
            {
               /* Date currentLocalTime = cal.getTime();
                DateFormat date = new SimpleDateFormat("dd-MM-yyy HH:mm:ss z");
                date.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
                String localTime = date.format(currentLocalTime);*/
                flag=0;
                Toast.makeText(ctx, ""+databasetime+"  diff  "+diff , Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
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
                        ctx.startActivity(new Intent(ctx,StudentNav.class));
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                databasetime=0;
            }
        }
    }


}





