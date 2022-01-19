package com.example.travel;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddCuisineActivity extends AppCompatActivity {

    private Spinner spinnerLocation;
    private List<LocationAdmin> locationAdmins = new ArrayList<>();
    private String etLocation;

    private ImageView imgAtcs;
    final int CODE_GALLERY_REQUEST = 999;
    Bitmap mbitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cuisine);

        getLocation_admins();

        TextView btn_editPhoto = (TextView) findViewById(R.id.btn_select_cs);
        btn_editPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        AddCuisineActivity.this,
                        new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                        CODE_GALLERY_REQUEST
                );
            }
        });

        TextView btn_add = (TextView) findViewById(R.id.btn_add_cs);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddCuisine(view);
            }
        });

    }
    private void onItemSelectedHandler(AdapterView<?> adapterView, View view, int position, long id) {
        Adapter adapter = adapterView.getAdapter();
        LocationAdmin locationAdmin = (LocationAdmin) adapter.getItem(position);

        etLocation = String.valueOf(locationAdmin.getId_item());
    }
    public void getLocation_admins()  {

        String urlAll = "https://travelhc.000webhostapp.com/location_all.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlAll, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals("failure")) {
                    JSONArray obj = null;
                    try {
                        obj = new JSONArray(response);
                        for(int i=0; i < obj.length(); i++){
                            JSONObject item = obj.getJSONObject(i);
                            String imgurl = item.getString("img");
                            String ingten = item.getString("tendiadiem");
                            int imgid = item.getInt("id");
                            int mien_id = item.getInt("mien_id");
                            String motaItem = item.getString("mota");
                            locationAdmins.add(new LocationAdmin(imgurl,ingten,imgid,motaItem,mien_id));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    spinnerLocation = (Spinner) findViewById(R.id.spinner_location);

                    ArrayAdapter<LocationAdmin> adapter = new ArrayAdapter<LocationAdmin>(getApplicationContext(),
                            android.R.layout.simple_spinner_item,
                            locationAdmins);

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spinnerLocation.setAdapter(adapter);

                    spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            onItemSelectedHandler(parent, view, position, id);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else {
                    Toast.makeText(AddCuisineActivity.this, "No data to view", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddCuisineActivity.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(AddCuisineActivity.this);
        requestQueue.add(stringRequest);
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        imgAtcs = (ImageView) findViewById(R.id.img_cs);
        if(requestCode == CODE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null){
            Uri filPath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filPath);
                mbitmap = BitmapFactory.decodeStream(inputStream);
                imgAtcs.setImageBitmap(mbitmap );
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

        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return  encodedImage;
    }

    private void AddCuisine(View view){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading....");
        progressDialog.show();

        String urlcs = "https://travelhc.000webhostapp.com/addCuisine.php";

        String mName = ((EditText) findViewById(R.id.et_tencs)).getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlcs, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals("failure")) {
                    Toast.makeText(AddCuisineActivity.this, "Successfully Add Cuisine", Toast.LENGTH_SHORT).show();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                } else {
                    Toast.makeText(AddCuisineActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    progressDialog.hide();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(AddCuisineActivity.this, volleyError.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                if(mbitmap != null){
                    String imageData = imageToString(mbitmap);
                    params.put("img",imageData);
                } else {
                    params.put("img","");
                }
                params.put("tenmon",mName);
                params.put("diadiem_id",etLocation);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(AddCuisineActivity.this);
        requestQueue.add(stringRequest);
    }

    private void getListAllLocationAdmin(){
        String urlAll = "https://travelhc.000webhostapp.com/location_all.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlAll, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals("failure")) {
                    JSONArray obj = null;
                    try {
                        obj = new JSONArray(response);
                        for(int i=0; i < obj.length(); i++){
                            JSONObject item = obj.getJSONObject(i);
                            String imgurl = item.getString("img");
                            String ingten = item.getString("tendiadiem");
                            int imgid = item.getInt("id");
                            int mien_id = item.getInt("mien_id");
                            String motaItem = item.getString("mota");
                            //locationAdmins  = new LocationAdmin(imgurl,ingten,imgid,motaItem,mien_id);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(AddCuisineActivity.this, "No data to view", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddCuisineActivity.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(AddCuisineActivity.this);
        requestQueue.add(stringRequest);
    }
}