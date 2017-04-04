package com.amrit.e_ttend;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.List;
import java.util.Map;

/**
 * Created by Amrit on 3/21/2017.
 */

public class TeacherSessionUpdate1 extends Fragment {

    Button session;
    EditText Classid;
    static AlertDialog.Builder builder;
    static String sessions,emp_id,classid;
    static String url_data="http://irretrievable-meter.000webhostapp.com/manualsessions.php";
    List<String> classidlist = new ArrayList<String>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view =inflater.inflate(R.layout.teachersessionupdate1,container,false);
        session=(Button)view.findViewById(R.id.session1);
        emp_id=SharedPrefManager.getInstance(getActivity()).getteacherempid();
        for (int i=0;i<Contents.classid.length;i++) {
            if (Contents.classid[i] != null)
                classidlist.add(Contents.classid[i]);
        }
        final Spinner spin = (Spinner)view.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, classidlist);
        spin.setAdapter(adapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                classid=parent.getItemAtPosition(position).toString();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }  });
        session.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               /* Toast.makeText(getActivity(),classid,Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(),emp_id,Toast.LENGTH_SHORT).show();*/
                    final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("Fetching attendance...");
                    progressDialog.show();
                    progressDialog.setCancelable(false);
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
                                            Toast.makeText(getActivity(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                                        }
                                        else {
                                            sessions=jsonObject.getString("sessions");
                                            TeacherSessionUpdate2 fragment = new TeacherSessionUpdate2();
                                              Bundle args = new Bundle();
                                                args.putString("sessions",sessions);
                                                args.putString("classid",classid);
                                                args.putString("empid",emp_id);
                                                fragment.setArguments(args);
                                            getFragmentManager().beginTransaction().replace(R.id.teachersessionupdate1, fragment).commit();
                                            //Toast.makeText(getActivity(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                           // Toast.makeText(getActivity(),classid,Toast.LENGTH_SHORT).show();
                            Toast.makeText(getActivity(), "Error! Check for your Internet Connection" +
                                    "        Unable to fetch data from Server", Toast.LENGTH_SHORT).show();
                            error.printStackTrace();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params=new HashMap<String, String>();
                            params.put("emp_id",emp_id);
                            params.put("class_id",classid);
                            return params;
                        }
                    };
                    MySingleton.getInstance(getActivity()).addToRequestque(stringRequest);
            }
        });
        return view;
    }

}
