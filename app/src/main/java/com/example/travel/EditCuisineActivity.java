package com.example.travel;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class EditCuisineActivity extends AppCompatActivity {

    private EditText etNameE;
    private ImageView imgEdit;
    private String id;
    final int CODE_GALLERY_REQUEST = 999;
    Bitmap mbitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_cuisine);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null ){
            return;
        }
        CuisineAdmin cuisineAdmin = (CuisineAdmin) bundle.get("object_cuisine_admin");
        etNameE = findViewById(R.id.et_tenmon_e);
        imgEdit = findViewById(R.id.img_cs_e);

        etNameE.setText(cuisineAdmin.getTitle());
        id = String.valueOf(cuisineAdmin.getId_item());

        ImageRequest imageRequest = new ImageRequest(cuisineAdmin.getResourceImage(), new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                imgEdit.setImageBitmap(response);
            }
        }, 0, 0, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                imgEdit.setImageResource(R.drawable.dulich1);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(imageRequest);

        TextView btnDel = (TextView) findViewById(R.id.btn_del_cs);
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mAlert = new AlertDialog.Builder(EditCuisineActivity.this);
                mAlert.setTitle("Notification");
                mAlert.setIcon(R.drawable.ic_help);
                mAlert.setMessage("Are you sure you want to delete cuisine?");
                mAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        delCuisine(view);
                    }
                });
                mAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                mAlert.show();
            }
        });

        TextView btn_editPhoto = (TextView) findViewById(R.id.btn_select_cs_e);
        btn_editPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        EditCuisineActivity.this,
                        new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                        CODE_GALLERY_REQUEST
                );
            }
        });

        TextView btnUp = (TextView) findViewById(R.id.btn_edit_cs);
        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditCuisine(view);
            }
        });

    }

    public void  delCuisine(View view){
        String urldel = "https://travelhc.000webhostapp.com/delcuisine.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urldel, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("success")) {
                    Toast.makeText(EditCuisineActivity.this, "Delete cuisine successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditCuisineActivity.this, QlCuisineActivity.class);
                    startActivity(intent);
                    finish();

                } else if (response.equals("failure")) {
                    Toast.makeText(EditCuisineActivity.this, "Delete cuisine failed", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditCuisineActivity.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("id_cuisine", id);
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

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
        imgEdit = (ImageView) findViewById(R.id.img_cs_e);
        if(requestCode == CODE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null){
            Uri filPath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filPath);
                mbitmap = BitmapFactory.decodeStream(inputStream);
                imgEdit.setImageBitmap(mbitmap );
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

    private void EditCuisine(View view){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading....");
        progressDialog.show();

        String urlupcs = "https://travelhc.000webhostapp.com/upCuisine.php";

        String mNameE = ((EditText) findViewById(R.id.et_tenmon_e)).getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlupcs, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals("failure")) {
                    Toast.makeText(EditCuisineActivity.this, "Successfully Edit Location", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditCuisineActivity.this, QlCuisineActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(EditCuisineActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    progressDialog.hide();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(EditCuisineActivity.this, volleyError.toString().trim(), Toast.LENGTH_SHORT).show();
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
                params.put("tenmon",mNameE);
                params.put("id",id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(EditCuisineActivity.this);
        requestQueue.add(stringRequest);
    }
}