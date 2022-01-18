package com.example.travel;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> implements Filterable {

    private Context mContext;
    private List<Location> mListLocation;
    private List<Location> mListLocationOld;

    public LocationAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData (List<Location> list){
        this.mListLocation = list;
        this.mListLocationOld = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_diadiem, parent,false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        Location location = mListLocation.get(position);
        if(location == null){
            return;
        }
        //holder.imgLocation.setImageResource(location.getResourceImage());
        ImageRequest imageRequest = new ImageRequest(location.getResourceImage(), new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                holder.imgLocation.setImageBitmap(response);
            }
        }, 0, 0, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                holder.imgLocation.setImageResource(R.drawable.dulich1);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(imageRequest);
        holder.tvTitle.setText(location.getTitle());

        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickGoToDetail(location);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mListLocation != null){
            return mListLocation.size();
        }
        return 0;
    }


    public  class LocationViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgLocation;
        private TextView tvTitle;
        private LinearLayout layoutItem;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);

            imgLocation = itemView.findViewById(R.id.img_location);
            tvTitle = itemView.findViewById(R.id.tv_title);
            layoutItem = itemView.findViewById(R.id.layout_item);
        }
    }
    private void onClickGoToDetail(Location location){
        upLuotxem(location);
        Intent intent = new Intent(mContext, DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_location",location);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }
    public void release(){
        mContext = null;
    }

    public void upLuotxem(Location location){
        String urlluotxem = "https://travelhc.000webhostapp.com/luotxem.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlluotxem, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("success")) {

                } else if (response.equals("failure")) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> data = new HashMap<>();
                data.put("id_diadiem", String.valueOf(location.getId_item()));
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }
    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
               String strSearch = charSequence.toString();
               if(strSearch.isEmpty()){
                   mListLocation = mListLocationOld;
               } else {
                   List<Location> list = new ArrayList<>();
                   for(Location location : mListLocationOld){
                       if(location.getTitle().toLowerCase().contains(strSearch.toLowerCase())){
                           list.add(location);
                       }

                   }
                   mListLocation = list;
               }
               FilterResults filterResults = new FilterResults();
               filterResults.values = mListLocation;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mListLocation = (List<Location>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
