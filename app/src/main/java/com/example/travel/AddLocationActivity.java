package com.example.travel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AddLocationActivity extends AppCompatActivity {

    private Spinner spinnerRegions;
    final int CODE_GALLERY_REQUEST = 999;

    private ImageView imgAtdd;
    private String etMien;
    Bitmap mbitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        this.spinnerRegions = (Spinner) findViewById(R.id.spinner_regions);

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

        TextView btn_editPhoto = (TextView) findViewById(R.id.btn_select_dd);
        btn_editPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        AddLocationActivity.this,
                        new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                        CODE_GALLERY_REQUEST
                );
            }
        });

        TextView btn_add = (TextView) findViewById(R.id.btn_add_dd);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddLocation(view);
            }
        });

    }
    private void onItemSelectedHandler(AdapterView<?> adapterView, View view, int position, long id) {
        Adapter adapter = adapterView.getAdapter();
        Regison_Admin regisonAdmin = (Regison_Admin) adapter.getItem(position);

        etMien = String.valueOf(regisonAdmin.getId());

       // Toast.makeText(getApplicationContext(), "Selected Employee: " + regisonAdmin.getId() ,Toast.LENGTH_SHORT).show();
    }
    public static
    Regison_Admin[] getRegison_admins()  {
        Regison_Admin rg1 = new Regison_Admin("Miền Bắc", 1);
        Regison_Admin rg2 = new Regison_Admin("Miền Trung", 2);
        Regison_Admin rg3 = new Regison_Admin("Miền Nam", 3);

        return new Regison_Admin[] {rg1, rg2, rg3};
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
        imgAtdd = (ImageView) findViewById(R.id.img_dd);
        if(requestCode == CODE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null){
            Uri filPath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filPath);
                mbitmap = BitmapFactory.decodeStream(inputStream);
                imgAtdd.setImageBitmap(mbitmap );
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

    private void AddLocation(View view){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading....");
        progressDialog.show();

        String urlLt = "https://travelhc.000webhostapp.com/addLocation.php";

        String mName = ((EditText) findViewById(R.id.et_tendd)).getText().toString().trim();
        String mMota = ((EditText) findViewById(R.id.et_mota)).getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlLt, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals("failure")) {
                    Toast.makeText(AddLocationActivity.this, "Successfully Add Location", Toast.LENGTH_SHORT).show();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                } else {
                    Toast.makeText(AddLocationActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    progressDialog.hide();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(AddLocationActivity.this, volleyError.toString().trim(), Toast.LENGTH_SHORT).show();
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
                params.put("tendiadiem",mName);
                params.put("mota",mMota);
                params.put("mien_id",etMien);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(AddLocationActivity.this);
        requestQueue.add(stringRequest);
    }
}