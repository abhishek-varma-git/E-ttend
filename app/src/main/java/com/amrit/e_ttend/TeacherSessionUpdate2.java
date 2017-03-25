package com.amrit.e_ttend;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Amrit on 3/21/2017.
 */

public class TeacherSessionUpdate2 extends Fragment {
    Button update;
    static EditText updatetext;
    TextView ClassestakenTextView,Text;
    AlertDialog.Builder builder;
    static String classestaken,textinput,emp_id,classid;
    String url_data="http://irretrievable-meter.000webhostapp.com/m2sessions.php";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view =inflater.inflate(R.layout.teachersessionupdate2,container,false);
        update=(Button)view.findViewById(R.id.updatebutton);
        classestaken = getArguments().getString("sessions");
        emp_id=getArguments().getString("empid");
        classid=getArguments().getString("classid");
        ClassestakenTextView=(TextView)view.findViewById(R.id.session);
        Text=(TextView)view.findViewById(R.id.text);
        ClassestakenTextView.setText(classestaken);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatetext=(EditText)view.findViewById(R.id.update);
                textinput=updatetext.getText().toString();
                if(textinput.equals(""))
                {
                    builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Error!");
                    builder.setMessage("Field Cannot be left Blank");
                    AlertDialog alertDialog=builder.create();
                    alertDialog.show();
                }
                else {

                    final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("Updating Attendance...");
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
                                            Toast.makeText(getActivity(),"Failed to Update! Click UPDATE Again!",Toast.LENGTH_LONG).show();
                                        }
                                        else {
                                            updatetext.setText("");
                                            Text.setText("Classes Taken Updated to:");
                                            ClassestakenTextView.setText(jsonObject.getString("sessions"));

                                            Toast.makeText(getActivity(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();

                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Error! Check for your Internet Connection" +
                                    "        Unable to fetch data from Server", Toast.LENGTH_SHORT).show();
                            error.printStackTrace();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("sessions",textinput);
                            params.put("emp_id",emp_id);
                            params.put("class_id",classid);
                            return params;
                        }
                    };
                    MySingleton.getInstance(getActivity()).addToRequestque(stringRequest);

                }
            }
        });
        return view;

    }
}
