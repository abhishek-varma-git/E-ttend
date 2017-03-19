package com.amrit.e_ttend;

//import android.app.Fragment;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StudentNav extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layout;
    ArrayList<StudentListItem> arrayList = new ArrayList<>();
    String url_data = "http://irretrievable-meter.000webhostapp.com/retrivestudent.php";
    TextView name,email;
    public static String susn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        //View view=navigationView.inflateHeaderView(R.layout.nav_header_student_nav);

        if(!SharedPrefManager.getInstance(this).isLoggedIn())
        {
            finish();
            startActivity(new Intent(this,StudentLogin.class));
        }
        recyclerView = (RecyclerView) findViewById(R.id.recylerview);
        layout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layout);
        recyclerView.setHasFixedSize(true);
        name=(TextView)header.findViewById(R.id.Name);
        email=(TextView)header.findViewById(R.id.Email);
        name.setText(SharedPrefManager.getInstance(this).getname());
        email.setText(SharedPrefManager.getInstance(this).getemail());
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
                        Contents.timearray=new long[response.length()];
                        for (int count = 0; count < response.length(); count++) {
                            try {

                                JSONArray JsonArray = new JSONArray(response);
                                JSONObject jsonobject = JsonArray.getJSONObject(count);
                                String sessions = jsonobject.getString("sessions");
                                String attended = jsonobject.getString("attended");
                                Contents.timearray[count]=jsonobject.getLong("time");
                                //Toast.makeText(getApplicationContext(),Contents.timearray[count],Toast.LENGTH_SHORT).show();
                                float total = Float.parseFloat(sessions);
                                float total_attended = Float.parseFloat(attended);
                                float per = total_attended / total * 100;
                                DecimalFormat decimalFormat = new DecimalFormat("#.#");
                                float twoDigitsF = Float.valueOf(decimalFormat.format(per));
                                String percentage = Float.toString(twoDigitsF);
                                StudentListItem item = new StudentListItem(
                                        //jsonobject.getString("time"),
                                        jsonobject.getString("sub_name"),
                                        jsonobject.getString("sessions"),
                                        jsonobject.getString("attended"),
                                        percentage + "%");
                                arrayList.add(item);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        adapter = new StudentAdpater(arrayList,StudentNav.this);
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
            super.onBackPressed();
        }
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.student_nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;zfdfd
        }

        return super.onOptionsItemSelected(item);
    made some changes
    }*/
   static long temp;
    private void displayselectedscreen(int id)
    {
        Fragment fragment=null;
        switch(id)
        {
            case R.id.About:
                finish();
                startActivity(new Intent(this,TeacherArea.class));
                break;
            case R.id.logout:
                fragment=new StudentLogout();
                break;
            case R.id.studentarea:
                finish();
                startActivity(new Intent(this,StudentNav.class));

                //fragment=new StudentSubjects();
                break;
        }
        if (fragment!=null)
        {
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.contentmain,fragment);
            fragmentTransaction.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        displayselectedscreen(id);

        return true;
    }
}
