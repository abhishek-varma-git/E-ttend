package com.amrit.e_ttend;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

public class StudentLogin extends AppCompatActivity implements View.OnClickListener{

    TextView register,switchuser;
    Button loginbutton;
    EditText Usn,Password;
    String usn,password;
    String login_url="http://irretrievable-meter.000webhostapp.com/loginlist.php";
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);
        if(SharedPrefManager.getInstance(this).isStudentLoggedIn())
        {
            finish();
            Intent i=new Intent(this,StudentArea.class);
            startActivity(i);
            return;
        }
        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(StudentLogin.this, StudentRegister.class));
            }
        });
        switchuser=(TextView)findViewById(R.id.switchuser);
        switchuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(StudentLogin.this, MainActivity.class));
            }
        });

        builder = new AlertDialog.Builder(StudentLogin.this);
        loginbutton = (Button) findViewById(R.id.login);
        Usn = (EditText) findViewById(R.id.susn);
        Password = (EditText) findViewById(R.id.spassword);
        loginbutton.setOnClickListener(this);
    }
        private void userLogin() {
        try {

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {}
           final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Signing In...");
        usn = Usn.getText().toString();
        password = Password.getText().toString();
        if (usn.equals("") || password.equals("")) {

            builder.setTitle("Something went Wrong");
            displayAlert("Enter a valid usn and password");
        } else {
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, login_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            try {
                                JSONArray JsonArray = new JSONArray(response);
                                JSONObject jsonObject = JsonArray.getJSONObject(0);
                                String code = jsonObject.getString("code");
                                if (code.equals("login_failed")) {
                                    builder.setTitle("Login Error");
                                    displayAlert(jsonObject.getString("message"));
                                } else {
                                    SharedPrefManager.getInstance(getApplicationContext())
                                            .StudentLogin(jsonObject.getString("usn"),
                                                    jsonObject.getString("name"),
                                                    jsonObject.getString("email"));

                                    Contents.usn=jsonObject.getString("usn");
                                    Intent intent = new Intent(StudentLogin.this, StudentArea.class);
                                    startActivity(intent);
                                    /* Bundle bundle = new Bundle();
                                    bundle.putString("usn", jsonObject.getString("usn"));
                                    intent.putExtras(bundle);*/
                                    finish();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(StudentLogin.this, "Error! Check for your Internet Connection" +
                            "        Unable to fetch data from Server", Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("usn", usn);
                    params.put("password", password);
                    return params;
                }
            };
            MySingleton.getInstance(StudentLogin.this).addToRequestque(stringRequest);
        }
    }
    public void displayAlert(String message)
    {
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Usn.setText("");
                Password.setText("");
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        if(v==loginbutton)
        {
            userLogin();

        }
    }
}

