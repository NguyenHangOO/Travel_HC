package com.example.travel.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.travel.Location;
import com.example.travel.LocationAdapter;
import com.example.travel.LoginActivity;
import com.example.travel.MainActivity;
import com.example.travel.Photo;
import com.example.travel.PhotoAdapter;
import com.example.travel.R;
import com.example.travel.Regions;
import com.example.travel.Static;
import com.example.travel.ZoomOutPageTransformer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator3;

public class HomeFragment extends Fragment {

    private ViewPager2 mViewPager2;
    private CircleIndicator3 mCircleIndicator3;
    private List<Photo> mListPhoto;
    private View mView;

    private ImageView btn_mienbac, btn_mientrung,btn_miennam;

    private RecyclerView rcvLocation;
    private LocationAdapter mLocationAdapter;
    private List<Location> listLocation6 = new ArrayList<>();

    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if(mViewPager2.getCurrentItem() == mListPhoto.size() -1){
                mViewPager2.setCurrentItem(0);
            }else{
                mViewPager2.setCurrentItem(mViewPager2.getCurrentItem() + 1);
            }

        }
    };

    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home,container,false);
        mViewPager2 = mView.findViewById(R.id.view_pager_2);
        mCircleIndicator3 = mView.findViewById(R.id.cd3);
        mListPhoto = getListPhoto();
        PhotoAdapter adapter = new PhotoAdapter(mListPhoto);
        mViewPager2.setAdapter(adapter);

        mCircleIndicator3.setViewPager(mViewPager2);

        mViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mHandler.removeCallbacks(mRunnable);
                mHandler.postDelayed(mRunnable, 5000);
            }
        });
        mViewPager2.setPageTransformer(new ZoomOutPageTransformer());

        getListLocation();

        btn_mienbac = mView.findViewById(R.id.btn_mienbac);
        btn_mientrung = mView.findViewById(R.id.btn_mientrung);
        btn_miennam = mView.findViewById(R.id.btn_miennam);

        btn_mienbac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Regions.class);
                i.putExtra("id_mien",1);
                startActivity(i);
            }
        });

        btn_mientrung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Regions.class);
                i.putExtra("id_mien",2);
                startActivity(i);
            }
        });

        btn_miennam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Regions.class);
                i.putExtra("id_mien",3);
                startActivity(i);
            }
        });

        return mView;

    }

    private List<Photo> getListPhoto(){
        List<Photo> list = new ArrayList<>();
        list.add(new Photo(R.drawable.dulich1));
        list.add(new Photo(R.drawable.dulich2));
        list.add(new Photo(R.drawable.dulich3));
        list.add(new Photo(R.drawable.dulich4));
        list.add(new Photo(R.drawable.dulich5));
        return  list;
    }

    @Override
    public void onPause() {
        mHandler.removeCallbacks(mRunnable);
        super.onPause();
    }
    @Override
    public void onResume() {
        super.onResume();
        mHandler.postDelayed(mRunnable, 5000);
    }
    private void getListLocation(){
//        List<Location> listLocation6 = new ArrayList<>();
        String urlHight = "https://travelhc.000webhostapp.com/location_hight.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlHight, new Response.Listener<String>() {
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
                            listLocation6.add(new Location(imgurl,ingten,imgid,motaItem));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    rcvLocation = mView.findViewById(R.id.rev_highlights);
                    mLocationAdapter = new LocationAdapter(getActivity());
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
                    rcvLocation.setLayoutManager(gridLayoutManager);
                    mLocationAdapter.setData(listLocation6);
                    rcvLocation.setAdapter(mLocationAdapter);
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
                return super.getParams();
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mLocationAdapter != null){
            mLocationAdapter.release();
        }
    }
}
