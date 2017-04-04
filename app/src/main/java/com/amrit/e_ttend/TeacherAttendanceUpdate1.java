package com.amrit.e_ttend;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import java.util.List;
import java.util.Map;


/**
 * Created by Amrit on 3/22/2017.
 */

public class TeacherAttendanceUpdate1 extends Fragment{

    TextView sub;EditText Usn;
    String usn;
    List<String> subjects = new ArrayList<String>();
    Button next;AlertDialog.Builder builder;
    static String sub_name,attended;
    String url_data="http://irretrievable-meter.000webhostapp.com/manualattend.php";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.teacherattendanceupdate1, container, false);
        sub = (TextView) view.findViewById(R.id.subjectspinner);
        for (int i = 0; i < Contents.teachersubjects.length; i++) {
            if (Contents.teachersubjects[i] != null)
                subjects.add(Contents.teachersubjects[i]);
        }
        final Spinner spin = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, subjects);
        spin.setAdapter(adapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sub_name = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        next = (Button) view.findViewById(R.id.nextbutton);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {}
                Usn = (EditText) view.findViewById(R.id.usn);
                usn = Usn.getText().toString();
                if (usn.equals("")) {
                    builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Error!");
                    builder.setMessage("Field Cannot be left Blank");
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {

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
                                            Toast.makeText(getActivity(), "Entered Wrong USN! ", Toast.LENGTH_SHORT).show();
                                            Usn.setText("");
                                        } else {

                                            attended=jsonObject.getString("attended");
                                            TeacherAttendanceUpdate2 fragment = new TeacherAttendanceUpdate2();
                                            Bundle args = new Bundle();
                                            args.putString("attended",attended);
                                            args.putString("usn",usn);
                                            args.putString("sub_name",sub_name);
                                            fragment.setArguments(args);
                                            getFragmentManager().beginTransaction().replace(R.id.teacherattendanceupdate1, fragment).commit();
                                            // ClassestakenEditText.setText(jsonObject.getString("sessions"));
                                            //Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            /*Toast.makeText(getActivity(),usn, Toast.LENGTH_LONG).show();
                            Toast.makeText(getActivity(),sub_name, Toast.LENGTH_LONG).show();*/
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Error! Check for your Internet Connection" +
                                    "        Unable to fetch data from Server", Toast.LENGTH_SHORT).show();
                            error.printStackTrace();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("usn",usn);
                            params.put("sub_name",sub_name);

                            return params;
                        }
                    };
                    MySingleton.getInstance(getActivity()).addToRequestque(stringRequest);
                }
            }
        });

        //adapter=ArrayAdapter.createFromResource(getActivity(),)
        return view;
    }
}
