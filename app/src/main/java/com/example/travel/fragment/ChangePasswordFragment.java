package com.example.travel.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.travel.MainActivity;
import com.example.travel.R;
import com.example.travel.Static;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordFragment extends Fragment {

    private String Password, cfPass;
    private int idUsser;
    private  View mViewChange;
    private TextView btnChange_Pass,tvStatus;
    private String URL = "https://travelhc.000webhostapp.com/change_pass.php";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewChange =  inflater.inflate(R.layout.fragment_change_password,container,false);
        Password = cfPass = "";
        btnChange_Pass = mViewChange.findViewById(R.id.btn_change_update);
        btnChange_Pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Password =  ((EditText) mViewChange.findViewById(R.id.change_pass)).getText().toString().trim();
                cfPass =  ((EditText) mViewChange.findViewById(R.id.change_cfPass)).getText().toString().trim();
                tvStatus =  ((TextView) mViewChange.findViewById(R.id.tvStatus));
                idUsser = Static.id;
                if(!Password.equals(cfPass)){
                   tvStatus.setText("Password Mismatch");
                }
                else if(!Password.equals("") && !cfPass.equals("") ) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals("success")) {
                                tvStatus.setText("Successfully Change Password");
                                ((EditText) mViewChange.findViewById(R.id.change_pass)).setText("");
                                ((EditText) mViewChange.findViewById(R.id.change_cfPass)).setText("");
                                btnChange_Pass.setClickable(false);

                            } else if (response.equals("failure")) {
                                tvStatus.setText("Something went wrong!");
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            tvStatus.setText(error.toString().trim());
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> data = new HashMap<>();
                            data.put("password", Password);
                            data.put("id", String.valueOf(idUsser));
                            return data;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                    requestQueue.add(stringRequest);
                }
            }
        });
        return  mViewChange;
    }
}
