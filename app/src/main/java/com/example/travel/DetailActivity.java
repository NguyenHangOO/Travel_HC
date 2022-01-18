package com.example.travel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DetailActivity extends AppCompatActivity {

    private TextView tv_ten, tv_mota, btnFavorite, btnShare, btnMap;
    private LinearLayout btnBack;
    private ImageView imgLocation;
    private String id_location;

    private RecyclerView rcvCulinary;
    private CulinaryAdapter mCulinaryAdapter;
    private List<Culinary> listCulinary = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null ){
            return;
        }
        Location location = (Location) bundle.get("object_location");
        tv_ten = findViewById(R.id.ten_detail);
        tv_mota = findViewById(R.id.mota_detail);
        imgLocation = findViewById(R.id.img_detail);
        btnFavorite = findViewById(R.id.btnYeuThich);
        btnShare = findViewById(R.id.btnShare);
        btnMap = findViewById(R.id.btnMap);
        btnBack = findViewById(R.id.btnBack);
        ///Do du lieu
        tv_ten.setText(location.getTitle());
        tv_mota.setText(location.getMotaItem());
        id_location = String.valueOf(location.getId_item());
        //do hinh anh
        ImageRequest imageRequest = new ImageRequest(location.getResourceImage(), new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
               imgLocation.setImageBitmap(response);
            }
        }, 0, 0, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                imgLocation.setImageResource(R.drawable.dulich1);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(imageRequest);

        setFarovite();

        getListCulinary();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackHome(view);
            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sh = "https://travelhc.000webhostapp.com/DetailLocation.php?id_location=";
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String Body = location.getTitle();
                String Sub = location.getTitle() +" " + sh+location.getId_item();
                intent.putExtra(Intent.EXTRA_TEXT,Body);
                intent.putExtra(Intent.EXTRA_TEXT,Sub);
                startActivity(Intent.createChooser(intent,"Share using"));
            }
        });
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(DetailActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.custom_dialog,null);

                final EditText inputLocation = (EditText) mView.findViewById(R.id.input_location);
                final TextView btn_cancel = (TextView) mView.findViewById(R.id.btn_cancel);
                final TextView btn_yes = (TextView) mView.findViewById(R.id.btn_yes);

                alert.setView(mView);

                final AlertDialog alertDialog = alert.create();
                alertDialog.setCanceledOnTouchOutside(false);

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String sStartLocation = inputLocation.getText().toString().trim();
                        String sEndLocation = location.getTitle();
                        //alertDialog.dismiss();
                        if(sStartLocation.equals("") && sEndLocation.equals("")){
                            Toast.makeText(getApplicationContext(), "Enter both location", Toast.LENGTH_SHORT).show();
                        } else {
                            DislayTrack(sStartLocation,sEndLocation);
                        }
                    }

                    private void DislayTrack(String sStartLocation, String sEndLocation) {
                        try {
                            Uri uri = Uri.parse("https://www.google.co.in/maps/dir/" + sStartLocation + "/"
                                    + sEndLocation);
                            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                            intent.setPackage("com.google.android.apps.maps");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } catch (ActivityNotFoundException e){
                            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
                            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });
                alertDialog.show();
            }
        });

    }
    public void onBackHome(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }
    public void onFavorite(View view){
        String urlFavorite = "https://travelhc.000webhostapp.com/favorite.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlFavorite, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("success")) {
                    Toast.makeText(DetailActivity.this, "Liked the place successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);

                } else if (response.equals("failure")) {
                    Toast.makeText(DetailActivity.this, "Failed to like the place", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailActivity.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("id_diadiem", id_location);
                data.put("id_usser", String.valueOf(Static.id));
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    public void setFarovite(){
        String urlsetFavorite = "https://travelhc.000webhostapp.com/setfavorite.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlsetFavorite, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("farlilili", "onResponse: "+response);
                if (response.equals("success")) {
                    btnFavorite.setBackgroundResource(R.drawable.ic_favorite);
                    btnFavorite.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            delFarovite(view);
                        }
                    });
                } else if (response.equals("failure")) {
                    btnFavorite.setBackgroundResource(R.drawable.ic_favorite_no);
                    btnFavorite.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onFavorite(view);
                        }
                    });
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailActivity.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("id_diadiem", id_location);
                data.put("id_usser", String.valueOf(Static.id));
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    public void  delFarovite(View view){
        String urldelFavorite = "https://travelhc.000webhostapp.com/delfavorite.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urldelFavorite, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("success")) {
                    Toast.makeText(DetailActivity.this, "Canceled like a place successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);

                } else if (response.equals("failure")) {
                    Toast.makeText(DetailActivity.this, "Canceled liking the place failed", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailActivity.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("id_diadiem", id_location);
                data.put("id_usser", String.valueOf(Static.id));
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getListCulinary(){
        String urlCulinary = "https://travelhc.000webhostapp.com/list_culinary.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlCulinary, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals("failure")) {
                    JSONArray obj = null;
                    try {
                        obj = new JSONArray(response);
                        for(int i=0; i < obj.length(); i++){
                            JSONObject item = obj.getJSONObject(i);
                            String imgurl = item.getString("img");
                            String ingten = item.getString("tenmon");
                            listCulinary.add(new Culinary(imgurl,ingten));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    rcvCulinary = findViewById(R.id.rcv_Culinary);
                    mCulinaryAdapter = new CulinaryAdapter(DetailActivity.this);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(DetailActivity.this, 2);
                    rcvCulinary.setLayoutManager(gridLayoutManager);
                    mCulinaryAdapter.setData(listCulinary);
                    rcvCulinary.setAdapter(mCulinaryAdapter);
                } else {
                    //Toast.makeText(DetailActivity.this, "No data to view", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailActivity.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("diadiem_id", id_location);
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(DetailActivity.this);
        requestQueue.add(stringRequest);
    }

}