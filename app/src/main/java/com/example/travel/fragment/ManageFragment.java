package com.example.travel.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.travel.LoginActivity;
import com.example.travel.MainActivity;
import com.example.travel.QlCuisineActivity;
import com.example.travel.QlLocationActivity;
import com.example.travel.R;

public class ManageFragment extends Fragment {

    private View mViewM;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewM =  inflater.inflate(R.layout.fragment_manage,container,false);

        ImageView btnQl = (ImageView) mViewM.findViewById(R.id.btn_dd);
        btnQl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), QlLocationActivity.class);
                startActivity(intent);
            }
        });
        ImageView btnCuisine = (ImageView) mViewM.findViewById(R.id.btn_at);
        btnCuisine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), QlCuisineActivity.class);
                startActivity(intent);
            }
        });
        return mViewM;
    }
}
