package com.example.travel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.travel.fragment.ProfileFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    ImageView fb,google;
    float v=0;

    private  String email1, password, fullname1, emaildk1, passworddk1, cfpass1, strEMAIL, strPASS;
    private TextView tvStatus;
    private TextView btnRegister, btnLogin;
    private String URL = "https://travelhc.000webhostapp.com/login.php";
    private String URL2 = "https://travelhc.000webhostapp.com/register.php";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(checkLogin()>0){
            onLogin(strEMAIL,strPASS);
        } else {
            tabLayout = findViewById(R.id.tab_layout);
            viewPager = findViewById(R.id.view_pager);
            fb = findViewById(R.id.fab_facebook);
            google = findViewById(R.id.fab_google);

            tabLayout.addTab(tabLayout.newTab().setText("Login"));
            tabLayout.addTab(tabLayout.newTab().setText("Signup"));
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

            final LoginAdapter adapter = new LoginAdapter(getSupportFragmentManager(),this,tabLayout.getTabCount()) ;
            viewPager.setAdapter(adapter);

            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                    Log.i("TAG", "onTabSelected: " + tab.getPosition());
                }
                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    Log.i("TAG", "onTabUnselected: " + tab.getPosition());
                }
                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                    Log.i("TAG", "onTabReselected: " + tab.getPosition());
                }
            });

            fb.setTranslationY(300);
            google.setTranslationY(300);
            tabLayout.setTranslationY(300);

            fb.setAlpha(v);
            google.setAlpha(v);
            tabLayout.setAlpha(v);

            fb.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
            google.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();
            tabLayout.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800).start();

            email1 = password = "";
            emaildk1 = passworddk1 = cfpass1 = fullname1 = "";
        }

    }
    public void login(View view) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("....");
        progressDialog.show();
        email1 = ((EditText)findViewById(R.id.email)).getText().toString().trim();
        password = ((EditText)findViewById(R.id.pass)).getText().toString().trim();
        CheckBox remberchk = ((CheckBox)findViewById(R.id.remember));
        if(!email1.equals("") && !password.equals("")){
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("res_hhhh", response);
                    if (!response.equals("failure")) {
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);
                            Static.id = obj.getInt("id");
                            Static.fullname = obj.getString("fullname");
                            Static.email = obj.getString("email");
                            Static.avartar = obj.getString("avatar");
                            Static.level = obj.getInt("level");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        rememberUser(email1,password,remberchk.isChecked());
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid Login Id/Password", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(LoginActivity.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    data.put("email", email1);
                    data.put("password", password);
                    return data;
                }

            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }else{
            Toast.makeText(this, "Fields can not be empty!", Toast.LENGTH_SHORT).show();
        }
    }
    public void register(View view){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("....");
        progressDialog.show();
        fullname1 = ((EditText)findViewById(R.id.fullname)).getText().toString().trim();
        emaildk1 = ((EditText)findViewById(R.id.emaildk)).getText().toString().trim();
        passworddk1 = ((EditText)findViewById(R.id.passdk)).getText().toString().trim();
        cfpass1 = ((EditText)findViewById(R.id.cfpassdk)).getText().toString().trim();
        tvStatus = ((TextView)findViewById(R.id.tvStatus));
        btnRegister = findViewById(R.id.signup);
        if(!passworddk1.equals(cfpass1)){
            Toast.makeText(this, "Password Mismatch", Toast.LENGTH_SHORT).show();
        }
        else if(!fullname1.equals("") && !emaildk1.equals("") && !passworddk1.equals("")){
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL2, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("success")) {
                        tvStatus.setText("Successfully registered");
                        btnRegister.setClickable(false);

                    } else if (response.equals("failure")) {
                        tvStatus.setText("Something went wrong!");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    data.put("fullname", fullname1);
                    data.put("email", emaildk1);
                    data.put("password", passworddk1);
                    return data;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }

    public void rememberUser(String email, String password, boolean status){
        SharedPreferences sharedpreferences = getSharedPreferences("UserFile.txt",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        if(!status){
            editor.clear();
        }else {
            editor.putString("EMAIL",email);
            editor.putString("PASSWORD",password);
            editor.putBoolean("REMEMBER", status);
        }
        editor.commit();
    }
    public int checkLogin(){
        SharedPreferences sharedPreferences = getSharedPreferences("UserFile.txt",MODE_PRIVATE);
        boolean chk = sharedPreferences.getBoolean("REMEMBER",false);
        if(chk){
            strEMAIL = sharedPreferences.getString("EMAIL","");
            strPASS = sharedPreferences.getString("PASSWORD","");
            return 1;
        }
        return -1;
    }
    public void onLogin(String logemail, String logpass){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("res_hhhh", response);
                if (!response.equals("failure")) {
                    JSONObject obj = null;
                    try {
                        obj = new JSONObject(response);
                        Static.id = obj.getInt("id");
                        Static.fullname = obj.getString("fullname");
                        Static.email = obj.getString("email");
                        Static.avartar = obj.getString("avatar");
                        Static.level = obj.getInt("level");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    SharedPreferences sharedpreferences = getSharedPreferences("UserFile.txt",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.clear();
                    editor.commit();
                    Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("email", logemail);
                data.put("password", logpass);
                return data;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

}