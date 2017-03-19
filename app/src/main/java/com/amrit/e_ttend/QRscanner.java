package com.amrit.e_ttend;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class QRscanner extends AppCompatActivity {

    String url="http://irretrievable-meter.000webhostapp.com/timemark.php";
    AlertDialog.Builder builder;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner);
        final Activity activity =this;
        builder = new AlertDialog.Builder(this);
        progressDialog=new ProgressDialog(this);
        IntentIntegrator intentIntegrator=new IntentIntegrator(activity);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        intentIntegrator.setPrompt("scan QR code to Mark Your Attendance ");
        intentIntegrator.setCameraId(0);
        intentIntegrator.setBeepEnabled(false);
        intentIntegrator.setBarcodeImageEnabled(false);
        intentIntegrator.initiateScan();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,StudentArea.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null)
        {
            if(result.getContents()==null)
            {
                Toast.makeText(this,"You cancelled Scanning, Press back",Toast.LENGTH_LONG).show();
                //startActivity(new Intent(this,StudentArea.class));
            }
            else
            {
                final String usn= StudentArea.susn;
                final String ssub_name=StudentAdpater.ssub_name;
                final String qrcode=result.getContents();
                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
                final long temp=cal.getTimeInMillis();
                final String temp1=Long.toString(temp);
                //Toast.makeText(this,temp1,Toast.LENGTH_LONG).show();
                progressDialog.setMessage("Please Wait....Server is working on marking your attendance");
                progressDialog.show();
                StringRequest stringRequest=new StringRequest(Request.Method.POST,url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
                                try {
                                    JSONArray JsonArray=new JSONArray(response);
                                    JSONObject jsonObject=JsonArray.getJSONObject(0);
                                    String code =jsonObject.getString("code");
                                    if(code.equals("failed"))
                                    {
                                        builder.setTitle("Failed!");
                                        displayAlert(jsonObject.getString("message"));

                                    }
                                    else
                                    {
                                        if(code.equals("success"))
                                        {
                                            builder.setTitle("Success!");
                                            displayAlert(jsonObject.getString("message"));
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(QRscanner.this,"Error! Check for your Internet Connection" +
                                "        Unable to fetch data from Server",Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params=new HashMap<String, String>();
                        params.put("usn",usn);
                        params.put("sub_name",ssub_name);
                        params.put("qr_code",qrcode);
                        params.put("time",temp1);
                        return params;
                    }
                };
                MySingleton.getInstance(QRscanner.this).addToRequestque(stringRequest);
            }
        }
        else {

            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    public void displayAlert(String message)
    {
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               Intent intent=new Intent(getApplicationContext(),StudentArea.class);
               startActivity(intent);
                finish();
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

}
