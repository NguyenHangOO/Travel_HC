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

public class CuisineAdapterAdmin extends RecyclerView.Adapter<CuisineAdapterAdmin.CuisineAdminViewHolder> implements Filterable {

    private Context mContext;
    private List<CuisineAdmin> mListCuisineAdmin;
    private List<CuisineAdmin> mListCuisineAdminOld;

    public CuisineAdapterAdmin(Context mContext) {
        this.mContext = mContext;
    }

    public void setData (List<CuisineAdmin> list){
        this.mListCuisineAdmin = list;
        this.mListCuisineAdminOld = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CuisineAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dd, parent,false);
        return new CuisineAdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CuisineAdminViewHolder holder, int position) {
        CuisineAdmin cuisineAdmin = mListCuisineAdmin.get(position);
        if(cuisineAdmin == null){
            return;
        }
        //holder.imgLocation.setImageResource(location.getResourceImage());
        ImageRequest imageRequest = new ImageRequest(cuisineAdmin.getResourceImage(), new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                holder.imgCuisine.setImageBitmap(response);
            }
        }, 0, 0, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                holder.imgCuisine.setImageResource(R.drawable.dulich1);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(imageRequest);
        holder.tvTitleCuisine.setText(cuisineAdmin.getTitle());

        holder.layoutItemCuisine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickGoToDetail(cuisineAdmin);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mListCuisineAdmin != null){
            return mListCuisineAdmin.size();
        }
        return 0;
    }


    public  class CuisineAdminViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgCuisine;
        private TextView tvTitleCuisine;
        private RelativeLayout layoutItemCuisine;

        public CuisineAdminViewHolder(@NonNull View itemView) {
            super(itemView);

            imgCuisine = itemView.findViewById(R.id.img_location_dd);
            tvTitleCuisine = itemView.findViewById(R.id.tv_title_dd);
            layoutItemCuisine = itemView.findViewById(R.id.layout_item_dd);
        }
    }
    private void onClickGoToDetail(CuisineAdmin cuisineAdmin){
        Intent intent = new Intent(mContext, EditCuisineActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_cuisine_admin",cuisineAdmin);
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
                   mListCuisineAdmin = mListCuisineAdminOld;
               } else {
                   List<CuisineAdmin> list = new ArrayList<>();
                   for(CuisineAdmin cuisineAdmin : mListCuisineAdminOld){
                       if(cuisineAdmin.getTitle().toLowerCase().contains(strSearch.toLowerCase())){
                           list.add(cuisineAdmin);
                       }

                   }
                   mListCuisineAdmin = list;
               }
               FilterResults filterResults = new FilterResults();
               filterResults.values = mListCuisineAdmin;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mListCuisineAdmin = (List<CuisineAdmin>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
