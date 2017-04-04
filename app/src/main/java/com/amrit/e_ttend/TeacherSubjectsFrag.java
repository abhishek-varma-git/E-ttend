package com.amrit.e_ttend;

import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by Amrit on 3/21/2017.
 */

public class TeacherSubjectsFrag extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layout;
    ArrayList<TeacherListItems> arrayList = new ArrayList<>();
    String emp_id;static int count;
    String url_data = "http://irretrievable-meter.000webhostapp.com/teacherretrieve.php";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.teachersubjectsfrag,container,false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recylerview);
        layout = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layout);
        recyclerView.setHasFixedSize(true);
        emp_id=SharedPrefManager.getInstance(getActivity()).getteacherempid();
        if(!SharedPrefManager.getInstance(getActivity()).isTeacherLoggedIn())
        {
            getActivity().finish();
            startActivity(new Intent(getActivity(),TeacherLogin.class));
        }

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Fetching Subject...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        StringRequest stringRequest=new StringRequest(Request.Method.POST,url_data,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Contents.teachersubjects=new String[response.length()];
                        Contents.classid=new String[response.length()];
                        for (count = 0; count < response.length(); count++) {
                            try {
                                JSONArray JsonArray = new JSONArray(response);
                                JSONObject jsonobject = JsonArray.getJSONObject(count);
                                Contents.teachersubjects[count]=jsonobject.getString("sub_name");
                                Contents.classid[count]=jsonobject.getString("class_id");
                                TeacherListItems item = new TeacherListItems(
                                        jsonobject.getString("sub_name"),
                                        jsonobject.getString("class_id"),
                                        jsonobject.getString("sessions")
                                );

                                arrayList.add(item);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        adapter = new TeacherAdapter(arrayList,getActivity());
                        recyclerView.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Error! Check for your Internet Connection" +
                        "        Unable to fetch data from Server", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params=new HashMap<String, String>();
                params.put("emp_id",emp_id);
                return params;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestque(stringRequest);
        return view;
    }

}
