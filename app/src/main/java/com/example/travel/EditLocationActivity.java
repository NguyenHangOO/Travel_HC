package com.example.travel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class EditLocationActivity extends AppCompatActivity {

    private Spinner spinnerRegions;
    private EditText etNameE,etMotaE;
    private ImageView imgEdit;
    private String MienId, id;
    final int CODE_GALLERY_REQUEST = 999;
    Bitmap mbitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_location);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null ){
            return;
        }
        LocationAdmin locationAdmin = (LocationAdmin) bundle.get("object_location_admin");
        etNameE = findViewById(R.id.et_tendd_e);
        etMotaE = findViewById(R.id.et_mota_e);
        imgEdit = findViewById(R.id.img_dd_e);

        etNameE.setText(locationAdmin.getTitle());
        etMotaE.setText(locationAdmin.getMotaItem());
        id = String.valueOf(locationAdmin.getId_item());

        ImageRequest imageRequest = new ImageRequest(locationAdmin.getResourceImage(), new Response.Listener<Bitmap>() {
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

        this.spinnerRegions = (Spinner) findViewById(R.id.spinner_regions_e);

        Regison_Admin[] regison_admins = getRegison_admins();

        ArrayAdapter<Regison_Admin> adapter = new ArrayAdapter<Regison_Admin>(this,
                android.R.layout.simple_spinner_item,
                regison_admins);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.spinnerRegions.setAdapter(adapter);

        this.spinnerRegions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onItemSelectedHandler(parent, view, position, id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        TextView btnDel = (TextView) findViewById(R.id.btn_del_dd);
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mAlert = new AlertDialog.Builder(EditLocationActivity.this);
                mAlert.setTitle("Notification");
                mAlert.setIcon(R.drawable.ic_help);
                mAlert.setMessage("Are you sure you want to delete location?");
                mAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        delLocation(view);
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

        TextView btn_editPhoto = (TextView) findViewById(R.id.btn_select_dd_e);
        btn_editPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        EditLocationActivity.this,
                        new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                        CODE_GALLERY_REQUEST
                );
            }
        });

        TextView btnUp = (TextView) findViewById(R.id.btn_edit_dd);
        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditLocation(view);
            }
        });


    }
    private void onItemSelectedHandler(AdapterView<?> adapterView, View view, int position, long id) {
        Adapter adapter = adapterView.getAdapter();
        Regison_Admin regisonAdmin = (Regison_Admin) adapter.getItem(position);
        MienId = String.valueOf(regisonAdmin.getId());
    }
    public static Regison_Admin[] getRegison_admins()  {
        Regison_Admin rg1 = new Regison_Admin("Miền Bắc", 1);
        Regison_Admin rg2 = new Regison_Admin("Miền Trung", 2);
        Regison_Admin rg3 = new Regison_Admin("Miền Nam", 3);

        return new Regison_Admin[] {rg1, rg2, rg3};
    }

    public void  delLocation(View view){
        String urldel = "https://travelhc.000webhostapp.com/dellocation.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urldel, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("success")) {
                    Toast.makeText(EditLocationActivity.this, "Delete location successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditLocationActivity.this, QlLocationActivity.class);
                    startActivity(intent);
                    finish();

                } else if (response.equals("failure")) {
                    Toast.makeText(EditLocationActivity.this, "Delete location failed", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditLocationActivity.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("id_diadiem", id);
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
        imgEdit = (ImageView) findViewById(R.id.img_dd_e);
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

        String encodedImage = android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return  encodedImage;
    }

    private void EditLocation(View view){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading....");
        progressDialog.show();

        String urluplt = "https://travelhc.000webhostapp.com/upLocation.php";

        String mNameE = ((EditText) findViewById(R.id.et_tendd_e)).getText().toString().trim();
        String mMotaE = ((EditText) findViewById(R.id.et_mota_e)).getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urluplt, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals("failure")) {
                    Toast.makeText(EditLocationActivity.this, "Successfully Edit Location", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditLocationActivity.this, QlLocationActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(EditLocationActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(EditLocationActivity.this, volleyError.toString().trim(), Toast.LENGTH_SHORT).show();
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
                params.put("tendiadiem",mNameE);
                params.put("mota",mMotaE);
                params.put("mien_id",MienId);
                params.put("id",id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(EditLocationActivity.this);
        requestQueue.add(stringRequest);
    }
}