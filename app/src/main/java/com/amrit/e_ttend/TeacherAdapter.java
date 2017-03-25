package com.amrit.e_ttend;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Amrit on 3/20/2017.
 */

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.ViewHolder> {

    static String emp_id;
    ArrayList<TeacherListItems> arrayList = new ArrayList<>();
    Context ctx;
    static AlertDialog.Builder builder;

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
    }

    @Override
    public int getItemCount() {
        return arrayList.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView head;
        public TextView desc1;
        public TextView desc2;
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
        }

        @Override
        public void onClick(final View v) {
            final AlertDialog.Builder builder;
            int position = getAdapterPosition();
            final TeacherListItems teacherListItems = this.teacherListItems.get(position);
            final String sub_name = teacherListItems.getHead();
            emp_id=SharedPrefManager.getInstance(ctx).getteacherempid();
            final String class_id=teacherListItems.getDesc1();
            final String url_data="http://irretrievable-meter.000webhostapp.com/teacherattendance.php";

            {
               /* Date currentLocalTime = cal.getTime();
                DateFormat date = new SimpleDateFormat("dd-MM-yyy HH:mm:ss z");
                date.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
                String localTime = date.format(currentLocalTime);*/
                builder = new AlertDialog.Builder(this.ctx);
                builder.setTitle("Alert!");
                builder.setMessage("Are you Sure?\nHave you taken class for this subject?");
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        final ProgressDialog progressDialog = new ProgressDialog(ctx);
                        progressDialog.setMessage("Updating...");
                        progressDialog.show();
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_data,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        progressDialog.dismiss();
                                        try {
                                            JSONArray JsonArray = new JSONArray(response);
                                            JSONObject jsonObject = JsonArray.getJSONObject(0);
                                            String code = jsonObject.getString("code");
                                            if (code.equals("failed")) {
                                                Toast.makeText(ctx,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                                            }
                                            else {
                                                ctx.startActivity(new Intent(ctx,TeacherArea.class));
                                                Toast.makeText(ctx,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Toast.makeText(ctx, "Error! Check for your Internet Connection" +
                                        "        Unable to fetch data from Server", Toast.LENGTH_SHORT).show();
                                error.printStackTrace();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("sub_name", sub_name);
                                params.put("class_id", class_id);
                                params.put("emp_id", emp_id);
                                return params;
                            }
                        };
                        MySingleton.getInstance(ctx).addToRequestque(stringRequest);

                    }
                });
                builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        }
    }

}
