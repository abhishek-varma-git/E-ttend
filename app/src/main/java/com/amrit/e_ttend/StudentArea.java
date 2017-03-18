package com.amrit.e_ttend;

import android.app.DownloadManager;
import android.app.LauncherActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


public class StudentArea extends AppCompatActivity {
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.studentmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.logout)
        {
            if(SharedPrefManager.getInstance(this).logout())
            {
                finish();
                startActivity(new Intent(this,StudentLogin.class));
            }
        }
        return super.onOptionsItemSelected(item);
    }

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layout;
    ArrayList<StudentListItem> arrayList = new ArrayList<>();
    String url_data = "http://irretrievable-meter.000webhostapp.com/retrivestudent.php";
    public static String susn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_area);
        Toolbar studenttoolbar=(Toolbar)findViewById(R.id.studenttoolbar);
        setSupportActionBar(studenttoolbar);
       if(!SharedPrefManager.getInstance(this).isLoggedIn())
        {
            finish();
            startActivity(new Intent(this,StudentLogin.class));
        }
        recyclerView = (RecyclerView) findViewById(R.id.recylerview);
        layout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layout);
        recyclerView.setHasFixedSize(true);
      /*  Bundle bundle;
        bundle = getIntent().getExtras();
        final String usn = bundle.getString("usn");*/
        susn=SharedPrefManager.getInstance(this).getusn();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Subject...");
        progressDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST,url_data,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        for (int count = 0; count < response.length(); count++) {
                            try {

                                JSONArray JsonArray = new JSONArray(response);
                                JSONObject jsonobject = JsonArray.getJSONObject(count);
                                String sessions = jsonobject.getString("sessions");
                                String attended = jsonobject.getString("attended");
                                float total = Float.parseFloat(sessions);
                                float total_attended = Float.parseFloat(attended);
                                float per = total_attended / total * 100;
                                DecimalFormat decimalFormat = new DecimalFormat("#.#");
                                float twoDigitsF = Float.valueOf(decimalFormat.format(per));
                                String percentage = Float.toString(twoDigitsF);
                                StudentListItem item = new StudentListItem(
                                        jsonobject.getString("sub_name"),
                                        jsonobject.getString("sessions"),
                                        jsonobject.getString("attended"),
                                        percentage + "%");
                                arrayList.add(item);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        adapter = new StudentAdpater(arrayList,StudentArea.this);
                        recyclerView.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params=new HashMap<String, String>();
                params.put("usn",susn);
                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestque(stringRequest);
    }

}




