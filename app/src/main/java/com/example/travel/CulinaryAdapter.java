package com.example.travel;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.util.List;

public class CulinaryAdapter extends RecyclerView.Adapter<CulinaryAdapter.CulinaryViewHolder>{

    private Context mContext;
    private List<Culinary> mListCulinary;

    public CulinaryAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData (List<Culinary> list){
        this.mListCulinary = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CulinaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_diadiem, parent,false);
        return new CulinaryAdapter.CulinaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CulinaryViewHolder holder, int position) {
        Culinary culinary = mListCulinary.get(position);
        if(culinary == null){
            return;
        }
        //holder.imgLocation.setImageResource(location.getResourceImage());
        ImageRequest imageRequest = new ImageRequest(culinary.getResourceImage(), new Response.Listener<Bitmap>() {
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
        holder.tvTitle.setText(culinary.getTitle());
    }

    @Override
    public int getItemCount() {
        if(mListCulinary != null){
            return mListCulinary.size();
        }
        return 0;
    }

    public class CulinaryViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgLocation;
        private TextView tvTitle;

        public CulinaryViewHolder(@NonNull View itemView) {
            super(itemView);

            imgLocation = itemView.findViewById(R.id.img_location);
            tvTitle = itemView.findViewById(R.id.tv_title);
        }
    }
    public void release(){
        mContext = null;
    }
}
