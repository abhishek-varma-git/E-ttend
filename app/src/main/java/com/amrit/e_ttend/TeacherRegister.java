package com.amrit.e_ttend;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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

public class TeacherRegister extends AppCompatActivity {

    EditText sempid, sname, semail, sphone, spassword, sconfirmpassword;
    Button sregister;
    String empid, name, email, password, confirmpassword, phone_number;
    AlertDialog.Builder builder;
    String reg_url = "http://irretrievable-meter.000webhostapp.com/teacherinsert.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_register);

        sregister = (Button)findViewById(R.id.tregister);
        sempid = (EditText)findViewById(R.id.tempid);
        sname = (EditText)findViewById(R.id.tname);
        semail = (EditText)findViewById(R.id.temail);
        sphone = (EditText)findViewById(R.id.tphone);
        spassword = (EditText)findViewById(R.id.tpassword);
        sconfirmpassword = (EditText)findViewById(R.id.tpasswordconfirm);
        builder = new AlertDialog.Builder(TeacherRegister.this);
        final ProgressDialog progressDialog=new ProgressDialog(this);
        sregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {}
                empid = sempid.getText().toString();
                name = sname.getText().toString();
                email = semail.getText().toString();
                phone_number = sphone.getText().toString();
                password = spassword.getText().toString();
                confirmpassword = sconfirmpassword.getText().toString();
                if (empid.equals("") || name.equals("") || email.equals("") || phone_number.equals("") || password.equals("") || confirmpassword.equals("")) {
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
                                Toast.makeText(TeacherRegister.this,"Error! Check for your Internet Connection" +
                                        "        Unable to fetch data from Server",Toast.LENGTH_SHORT).show();
                                error.printStackTrace();
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params=new HashMap<String, String>();
                                params.put("emp_id",empid);
                                params.put("emp_name",name);
                                params.put("email",email);
                                params.put("emp_pass",password);
                                params.put("phone_num",phone_number);
                                return params;
                            }
                        };
                        MySingleton.getInstance(TeacherRegister.this).addToRequestque(stringRequest);
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
                    sempid.setText("");
                    sname.setText("");
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
