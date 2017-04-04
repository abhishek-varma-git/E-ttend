package com.amrit.e_ttend;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
 * Created by Amrit on 3/22/2017.
 */

public class TeacherAttendanceUpdate2 extends Fragment {

    Button update;
    static EditText updatetext;
    TextView ClassesAttendedTextView,Text;
    AlertDialog.Builder builder;
    static String classesAttended,textinput,usn,sub_name;
    String url_data="http://irretrievable-meter.000webhostapp.com/m2attendance.php";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view =inflater.inflate(R.layout.teacherattendanceupdate2,container,false);

        update=(Button)view.findViewById(R.id.updatebutton);
        classesAttended = getArguments().getString("attended");
        usn=getArguments().getString("usn");
        sub_name=getArguments().getString("sub_name");
        ClassesAttendedTextView=(TextView)view.findViewById(R.id.attended);
        Text=(TextView)view.findViewById(R.id.text);
        ClassesAttendedTextView.setText(classesAttended);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatetext=(EditText)view.findViewById(R.id.updateedittext);
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
                                            Toast.makeText(getActivity(),"Failed to Update! Click UPDATE Again!",Toast.LENGTH_LONG).show();
                                        }
                                        else {
                                            updatetext.setText("");
                                            Text.setText("Classes Attended Updated to:");
                                            ClassesAttendedTextView.setText(jsonObject.getString("attended"));
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
                            params.put("attended",textinput);
                            params.put("usn",usn);
                            params.put("sub_name",sub_name);
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
