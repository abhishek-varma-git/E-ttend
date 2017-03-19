package com.amrit.e_ttend;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

/**
 * Created by Amrit on 3/8/2017.
 */

public class SharedPrefManager {
    private static SharedPrefManager mInstance;

    private static Context mctx;
    private static final String shared_pref_name="rtretert";
    private static String keyusn="fgfgsg";
    private static String keyname="fdsfdfdf";
    private static String keyemail="dsfsdfdfsdf";
    private static String keyempid="sdfdfsfsdf";
    private static String keytname="dfsdfsf";
    private static String keytemail="Sfsdfsffdsf";
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
    public boolean StudentLogin(String usn,String name,String email)
    {

        SharedPreferences sharedPreferences=mctx.getSharedPreferences(shared_pref_name,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(keyusn,usn);
        editor.putString(keyname,name);
        editor.putString(keyemail,email);
        editor.apply();
        return true;
    }
    public boolean TeacherLogin(String empid,String name,String email)
    {

        SharedPreferences sharedPreferences=mctx.getSharedPreferences(shared_pref_name,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(keyempid,empid);
        editor.putString(keytname,name);
        editor.putString(keytemail,email);
        editor.apply();
        return true;
    }
    public boolean isStudentLoggedIn()
    {
        SharedPreferences sharedPreferences=mctx.getSharedPreferences(shared_pref_name,Context.MODE_PRIVATE);
        if(sharedPreferences.getString(keyusn,null)!=null)
        {
            return true;
        }
        return false;
    }
    public boolean isTeacherLoggedIn()
    {
        SharedPreferences sharedPreferences=mctx.getSharedPreferences(shared_pref_name,Context.MODE_PRIVATE);
        if(sharedPreferences.getString(keyempid,null)!=null)
        {
            return true;
        }
        return false;
    }
    public  boolean studentlogout()
    {
        SharedPreferences sharedPreferences=mctx.getSharedPreferences(shared_pref_name,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.remove(keyusn);
        editor.apply();
        return true;
    }
    public  boolean Teacherlogout()
    {
        SharedPreferences sharedPreferences=mctx.getSharedPreferences(shared_pref_name,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.remove(keyempid);
        editor.apply();
        return true;
    }
    public String getstudentusn()
    {
        SharedPreferences sharedPreferences=mctx.getSharedPreferences(shared_pref_name,Context.MODE_PRIVATE);
        return sharedPreferences.getString(keyusn,null);
    }
    public String getstudentname()
    {
        SharedPreferences sharedPreferences=mctx.getSharedPreferences(shared_pref_name,Context.MODE_PRIVATE);
        return sharedPreferences.getString(keyname,null);
    }
    public String getstudentemail()
    {
        SharedPreferences sharedPreferences=mctx.getSharedPreferences(shared_pref_name,Context.MODE_PRIVATE);
        return sharedPreferences.getString(keyemail,null);
    }
    public String getteacherempid()
    {
        SharedPreferences sharedPreferences=mctx.getSharedPreferences(shared_pref_name,Context.MODE_PRIVATE);
        return sharedPreferences.getString(keyempid,null);
    }
    public String getteachernamae()
    {
        SharedPreferences sharedPreferences=mctx.getSharedPreferences(shared_pref_name,Context.MODE_PRIVATE);
        return sharedPreferences.getString(keytname,null);
    }
    public String getteacheremail()
    {
        SharedPreferences sharedPreferences=mctx.getSharedPreferences(shared_pref_name,Context.MODE_PRIVATE);
        return sharedPreferences.getString(keytemail,null);
    }
}
