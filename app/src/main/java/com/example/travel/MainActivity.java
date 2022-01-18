package com.example.travel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowInsets;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.travel.fragment.ChangePasswordFragment;
import com.example.travel.fragment.FavoriteFragment;
import com.example.travel.fragment.HomeFragment;
import com.example.travel.fragment.ManageFragment;
import com.example.travel.fragment.ProfileFragment;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator3;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private  static  final int FRAGMENT_HOME = 0;
    private  static  final int FRAGMENT_FAVORITE = 1;
    private  static  final int FRAGMENT_MANAGE = 2;
    private  static  final int FRAGMENT_PROFILE = 3;
    private  static  final int FRAGMENT_CHANGE_PASSWORD = 4;

    private  int mCurrentFragment = FRAGMENT_HOME;
    private DrawerLayout drawerLayout;
    private String url = Static.avartar;

    public ImageView imgAtr;
    final int CODE_GALLERY_REQUEST = 999;
    String urlUpImg="https://travelhc.000webhostapp.com/upload.php";
    Bitmap mbitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        replaceFragment( new HomeFragment());
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
        setTitleToolbar();
        View header = navigationView.getHeaderView(0);
        if(Static.id!= -1){
            ((TextView) header.findViewById(R.id.profile_name)).setText(Static.fullname);
            ((TextView) header.findViewById(R.id.profile_email)).setText(Static.email);
            ImageView imgAvatr = header.findViewById(R.id.profile_image);
            ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    imgAvatr.setImageBitmap(response);
                }
            }, 0, 0, null, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    imgAvatr.setImageResource(R.drawable.user);
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(imageRequest);
        }
        if(Static.level ==1){
            navigationView.getMenu().findItem(R.id.nav_manage).setVisible(true);
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.nav_home){
            if(mCurrentFragment != FRAGMENT_HOME){
                replaceFragment( new HomeFragment());
                mCurrentFragment = FRAGMENT_HOME;
            }
        } else if(id==R.id.nav_favorite){
            if(mCurrentFragment != FRAGMENT_FAVORITE){
                replaceFragment( new FavoriteFragment());
                mCurrentFragment = FRAGMENT_FAVORITE;
            }
        } else if(id==R.id.nav_manage){
            if(mCurrentFragment != FRAGMENT_MANAGE){
                replaceFragment( new ManageFragment());
                mCurrentFragment = FRAGMENT_MANAGE;
            }
        } else if(id==R.id.nav_my_profile){
            if(mCurrentFragment != FRAGMENT_PROFILE){
                replaceFragment( new ProfileFragment());
                mCurrentFragment = FRAGMENT_PROFILE;
            }
        } else if(id==R.id.nav_change_password){
            if(mCurrentFragment != FRAGMENT_CHANGE_PASSWORD){
                replaceFragment( new ChangePasswordFragment());
                mCurrentFragment = FRAGMENT_CHANGE_PASSWORD;
            }
        }else if(id==R.id.nav_logout) {
            logout();
        }

        setTitleToolbar();
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();
    }

    private void  setTitleToolbar(){
        String title = "";
       switch (mCurrentFragment){
           case FRAGMENT_HOME:
               title = getString(R.string.nav_travel);
               break;
           case FRAGMENT_FAVORITE:
               title = getString(R.string.nav_favorite);
               break;
           case FRAGMENT_MANAGE:
               title = getString(R.string.nav_manage);
               break;
           case FRAGMENT_PROFILE:
               title = getString(R.string.nav_my_profile);
               break;
           case FRAGMENT_CHANGE_PASSWORD:
               title = getString(R.string.nav_change_password);
               break;

       }
       if(getSupportActionBar() != null){
           getSupportActionBar().setTitle(title);
       }
    }
    public void logout(){
        Intent intent = new Intent(this, LoginActivity.class);
        AlertDialog.Builder mAlert = new AlertDialog.Builder(this);
        mAlert.setTitle("Notification");
        mAlert.setIcon(R.drawable.ic_help);
        mAlert.setMessage("Are you sure you want to sign out?");
        mAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Static.id = Static.level = -1;
                Static.avartar = Static.email = Static.fullname = "";
                SharedPreferences sharedpreferences = getSharedPreferences("UserFile.txt",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.commit();
                startActivity(intent);
                finish();
            }
        });
        mAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        mAlert.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == CODE_GALLERY_REQUEST){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"Select Image"),CODE_GALLERY_REQUEST);
            }else {
                Toast.makeText(getApplicationContext(), "You don't have premission to access gallery!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        imgAtr = (ImageView) findViewById(R.id.pfil_image);
        if(requestCode == CODE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null){
            Uri filPath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filPath);
                mbitmap = BitmapFactory.decodeStream(inputStream);
                imgAtr.setImageBitmap(mbitmap );
            } catch (FileNotFoundException e){
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private  String imageToString (Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream );
        byte[] imageBytes = outputStream.toByteArray();

        String encodedImage = android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return  encodedImage;
    }
    public void updateProtile(View view){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading....");
        progressDialog.show();

        String mfullname = ((EditText) findViewById(R.id.pfil_fullname)).getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlUpImg, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals("failure")) {
                    JSONObject obj = null;
                    try {
                        obj = new JSONObject(response);
                        Static.fullname = obj.getString("fullname");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(MainActivity.this, "Successfully Update Profile", Toast.LENGTH_SHORT).show();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity.this, volleyError.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                if(mbitmap != null){
                    String imageData = imageToString(mbitmap);
                    params.put("image",imageData);
                } else {
                    params.put("image","");
                }
                params.put("id", String.valueOf(Static.id));
                params.put("fullname",mfullname);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    public void searchAll(MenuItem item) {
        Intent intent = new Intent(MainActivity.this, SearchAllActivity.class);
        startActivity(intent);
    }
}