package com.amrit.e_ttend;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Amrit on 3/8/2017.
 */

public class SharedPrefManager {
    private static SharedPrefManager mInstance;

    private static Context mctx;
    private static final String shared_pref_name="rtretert";
    private static String keyusn=null;
    private static String keyname="sdfgsdgs";
    private static String keyemail="dfgdfg";
    private SharedPrefManager(Context context)
    {
        mctx=context;

    }

    public static synchronized SharedPrefManager getInstance(Context context)
    {
        if(mInstance==null)
        {
            mInstance=new SharedPrefManager(context);
        }
        return mInstance;
    }
    public boolean userLogin(String usn,String name,String email)
    {

        SharedPreferences sharedPreferences=mctx.getSharedPreferences(shared_pref_name,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(keyusn,usn);
        editor.putString(keyname,name);
        editor.putString(keyemail,email);
        editor.apply();
        return true;
    }
    public boolean isLoggedIn()
    {
        SharedPreferences sharedPreferences=mctx.getSharedPreferences(shared_pref_name,Context.MODE_PRIVATE);
        if(sharedPreferences.getString(keyusn,null)!=null)
        {
            return true;
        }
        return false;
    }
    public  boolean logout()
    {
        SharedPreferences sharedPreferences=mctx.getSharedPreferences(shared_pref_name,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }
    public String getusn()
    {
        SharedPreferences sharedPreferences=mctx.getSharedPreferences(shared_pref_name,Context.MODE_PRIVATE);
        return sharedPreferences.getString(keyusn,null);
    }
    public String getname()
    {
        SharedPreferences sharedPreferences=mctx.getSharedPreferences(shared_pref_name,Context.MODE_PRIVATE);
        return sharedPreferences.getString(keyname,null);
    }
    public String getemail()
    {
        SharedPreferences sharedPreferences=mctx.getSharedPreferences(shared_pref_name,Context.MODE_PRIVATE);
        return sharedPreferences.getString(keyemail,null);
    }
}
