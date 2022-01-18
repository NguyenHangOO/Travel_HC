package com.example.travel.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.travel.R;
import com.example.travel.Static;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private static final int RESULT_OK = -1;
    private View mViewPfil;
    private EditText etFullname, etEmail;
    private ImageView imgAvatr;
    private TextView btnChoose;
    final int CODE_GALLERY_REQUEST = 999;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewPfil = inflater.inflate(R.layout.fragment_profile,container,false);
        etFullname = mViewPfil.findViewById(R.id.pfil_fullname);
        etEmail =  mViewPfil.findViewById(R.id.pfil_email);
        btnChoose = mViewPfil.findViewById(R.id.btn_select);
        if(Static.id!= -1){
            etFullname.setText(Static.fullname);
            etEmail.setText(Static.email);
            imgAvatr = mViewPfil.findViewById(R.id.pfil_image);
            ImageRequest imageRequest = new ImageRequest(Static.avartar, new Response.Listener<Bitmap>() {
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
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(imageRequest);
        }
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        getActivity(),
                        new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                        CODE_GALLERY_REQUEST
                );
            }
        });
        return  mViewPfil;
    }

 }
