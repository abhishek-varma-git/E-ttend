package com.amrit.e_ttend;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class StudentRegister extends AppCompatActivity {

    EditText susn, sname, sclass, semail, sphone, spassword, sconfirmpassword;
    Button sregister;
    String usn, name, class_id, email, password, confirmpassword, phone_number;
    AlertDialog.Builder builder;
    String reg_url = "http://irretrievable-meter.000webhostapp.com/registerstudent.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);
        sregister = (Button)findViewById(R.id.sregister);
        susn = (EditText)findViewById(R.id.susn);
        sname = (EditText)findViewById(R.id.sname);
        sclass = (EditText)findViewById(R.id.sclass);
        semail = (EditText)findViewById(R.id.semail);
        sphone = (EditText)findViewById(R.id.sphone);
        spassword = (EditText)findViewById(R.id.spassword);
        sconfirmpassword = (EditText)findViewById(R.id.sconfirmpassword);
        builder = new AlertDialog.Builder(StudentRegister.this);
        final ProgressDialog progressDialog=new ProgressDialog(this);
        sregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {}
                usn = susn.getText().toString();
                name = sname.getText().toString();
                class_id = sclass.getText().toString();
                email = semail.getText().toString();
                phone_number = sphone.getText().toString();
                password = spassword.getText().toString();
                confirmpassword = sconfirmpassword.getText().toString();
                if (usn.equals("") || name.equals("") || class_id.equals("") || email.equals("") || phone_number.equals("") || password.equals("") || confirmpassword.equals("")) {
                    builder.setTitle("Something Went wrong.....");
                    builder.setMessage("Please fill all the field");
                    displayAlert("input_error");

                }
                else
                {
                    if(!(password.equals(confirmpassword)))
                    {
                        builder.setTitle("Something Went wrong.....");
                        builder.setMessage("Your Passwords do not Match!");
                        displayAlert("input_error");
                    }
                    else
                    {
                        progressDialog.setMessage("Registering...");
                        progressDialog.show();
                        StringRequest stringRequest=new StringRequest(Request.Method.POST, reg_url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        progressDialog.dismiss();

                                        try {
                                            JSONArray JsonArray=new JSONArray(response);
                                            JSONObject jsonObject=JsonArray.getJSONObject(0);
                                            String code=jsonObject.getString("code");
                                            String message=jsonObject.getString("message");
                                            builder.setTitle("Server Response... ");
                                            builder.setMessage(message);
                                            displayAlert(code);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                progressDialog.dismiss();
                                Toast.makeText(StudentRegister.this,"Error! Check for your Internet Connection" +
                                        "        Unable to fetch data from Server",Toast.LENGTH_LONG).show();
                                error.printStackTrace();
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params=new HashMap<String, String>();
                                params.put("usn",usn);
                                params.put("name",name);
                                params.put("class_id",class_id);
                                params.put("email",email);
                                params.put("password",password);
                                params.put("phone",phone_number);
                                return params;
                            }
                        };
                        MySingleton.getInstance(StudentRegister.this).addToRequestque(stringRequest);
                    }
                }
            }
        });
    }

    public void displayAlert(final String code) {
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(code.equals("input_error"))
                {
                    spassword.setText("");
                    sconfirmpassword.setText("");

                }
                else if (code.equals("reg_success"))
                {
                    finish();

                }
                else if(code.equals("reg_failed"))
                {
                    susn.setText("");
                    sname.setText("");
                    sclass.setText("");
                    semail.setText("");
                    sphone.setText("");
                    spassword.setText("");
                }
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();

    }
}


