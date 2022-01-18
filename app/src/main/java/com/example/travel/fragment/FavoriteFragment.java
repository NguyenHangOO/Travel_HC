package com.example.travel.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.travel.Location;
import com.example.travel.LocationAdapter;
import com.example.travel.R;
import com.example.travel.Static;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavoriteFragment extends Fragment {

    private View mViewFavorite;

    private RecyclerView nrcvLocation;
    private LocationAdapter nLocationAdapter;
    private List<Location> listLocation5 = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewFavorite =  inflater.inflate(R.layout.fragment_favorite,container,false);
        getListFaLocation();

        return mViewFavorite;
    }

    private void getListFaLocation(){
        String urlFa = "https://travelhc.000webhostapp.com/getfavorite.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlFa, new Response.Listener<String>() {
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
                            String motaItem = item.getString("mota");
                            listLocation5.add(new Location(imgurl,ingten,imgid,motaItem));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    nrcvLocation = mViewFavorite.findViewById(R.id.rev_favorite);
                    nLocationAdapter = new LocationAdapter(getActivity());
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
                    nrcvLocation.setLayoutManager(gridLayoutManager);
                    nLocationAdapter.setData(listLocation5);
                    nrcvLocation.setAdapter(nLocationAdapter);
                } else {
                    Toast.makeText(getActivity(), "No data to view", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("id_user", String.valueOf(Static.id));
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
}
