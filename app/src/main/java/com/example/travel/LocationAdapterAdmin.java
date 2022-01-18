package com.example.travel;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

public class LocationAdapterAdmin extends RecyclerView.Adapter<LocationAdapterAdmin.LocationAdminViewHolder> implements Filterable {

    private Context mContext;
    private List<LocationAdmin> mListLocationAdmin;
    private List<LocationAdmin> mListLocationAdminOld;

    public LocationAdapterAdmin(Context mContext) {
        this.mContext = mContext;
    }

    public void setData (List<LocationAdmin> list){
        this.mListLocationAdmin = list;
        this.mListLocationAdminOld = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LocationAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dd, parent,false);
        return new LocationAdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationAdminViewHolder holder, int position) {
        LocationAdmin locationAdmin = mListLocationAdmin.get(position);
        if(locationAdmin == null){
            return;
        }
        //holder.imgLocation.setImageResource(location.getResourceImage());
        ImageRequest imageRequest = new ImageRequest(locationAdmin.getResourceImage(), new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                holder.imgLocationdd.setImageBitmap(response);
            }
        }, 0, 0, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                holder.imgLocationdd.setImageResource(R.drawable.dulich1);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(imageRequest);
        holder.tvTitledd.setText(locationAdmin.getTitle());

        holder.layoutItemdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickGoToDetail(locationAdmin);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mListLocationAdmin != null){
            return mListLocationAdmin.size();
        }
        return 0;
    }


    public  class LocationAdminViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgLocationdd;
        private TextView tvTitledd;
        private RelativeLayout layoutItemdd;

        public LocationAdminViewHolder(@NonNull View itemView) {
            super(itemView);

            imgLocationdd = itemView.findViewById(R.id.img_location_dd);
            tvTitledd = itemView.findViewById(R.id.tv_title_dd);
            layoutItemdd = itemView.findViewById(R.id.layout_item_dd);
        }
    }
    private void onClickGoToDetail(LocationAdmin locationAdmin){
        Intent intent = new Intent(mContext, EditLocationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_location_admin",locationAdmin);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }
    public void release(){
        mContext = null;
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
               String strSearch = charSequence.toString();
               if(strSearch.isEmpty()){
                   mListLocationAdmin = mListLocationAdminOld;
               } else {
                   List<LocationAdmin> list = new ArrayList<>();
                   for(LocationAdmin locationAdmin : mListLocationAdminOld){
                       if(locationAdmin.getTitle().toLowerCase().contains(strSearch.toLowerCase())){
                           list.add(locationAdmin);
                       }

                   }
                   mListLocationAdmin = list;
               }
               FilterResults filterResults = new FilterResults();
               filterResults.values = mListLocationAdmin;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mListLocationAdmin = (List<LocationAdmin>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
