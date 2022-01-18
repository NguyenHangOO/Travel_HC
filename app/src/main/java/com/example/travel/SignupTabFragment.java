package com.example.travel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class SignupTabFragment extends Fragment {
    EditText emaildk,passdk,cfpassdk,fullname;
    TextView signup;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup root = (ViewGroup)  inflater.inflate(R.layout.signup_tab_fragment,container,false);

        fullname = root.findViewById(R.id.fullname);
        emaildk = root.findViewById(R.id.emaildk);
        passdk = root.findViewById(R.id.passdk);
        cfpassdk = root.findViewById(R.id.cfpassdk);
        signup = root.findViewById(R.id.signup);

        fullname.setTranslationX(800);
        emaildk.setTranslationX(800);
        passdk.setTranslationX(800);
        cfpassdk.setTranslationX(800);
        signup.setTranslationX(800);

        fullname.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(100).start();
        emaildk.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        passdk.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        cfpassdk.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
        signup.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(900).start();
        return  root;
    }
}
