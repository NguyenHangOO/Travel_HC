package com.example.travel;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QlCuisineActivity extends AppCompatActivity {

    private RecyclerView rcvCuisineql;
    private CuisineAdapterAdmin mCuisineAdapterAdmin;
    private List<CuisineAdmin> listCuisineAdmin = new ArrayList<>();

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ql_cuisine);

        Toolbar toolbarR = findViewById(R.id.tb_admin_cuisine);
        toolbarR.setTitle("Manage Cuisine");
        setSupportActionBar(toolbarR);

        getListAllCuisineAdmin();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void getListAllCuisineAdmin(){
        String urlAll = "https://travelhc.000webhostapp.com/cuisine_all.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlAll, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals("failure")) {
                    JSONArray obj = null;
                    try {
                        obj = new JSONArray(response);
                        for(int i=0; i < obj.length(); i++){
                            JSONObject item = obj.getJSONObject(i);
                            String imgurl = item.getString("img");
                            String ingten = item.getString("tenmon");
                            int imgid = item.getInt("id");
                            int diadiem_id = item.getInt("diadiem_id");
                            listCuisineAdmin.add(new CuisineAdmin(imgurl,ingten,imgid,diadiem_id));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    rcvCuisineql = findViewById(R.id.rev_allcuisine_ql);
                    mCuisineAdapterAdmin = new CuisineAdapterAdmin(QlCuisineActivity.this);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(QlCuisineActivity.this, 1);
                    rcvCuisineql.setLayoutManager(gridLayoutManager);
                    mCuisineAdapterAdmin.setData(listCuisineAdmin);
                    rcvCuisineql.setAdapter(mCuisineAdapterAdmin);
                } else {
                    Toast.makeText(QlCuisineActivity.this, "No data to view", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(QlCuisineActivity.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(QlCuisineActivity.this);
        requestQueue.add(stringRequest);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin,menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_searcha).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        menu.findItem(R.id.action_add).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(QlCuisineActivity.this, AddCuisineActivity.class);
                startActivity(intent);
                return false;
            }
        });

        menu.findItem(R.id.action_re).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mCuisineAdapterAdmin.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mCuisineAdapterAdmin.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public void onBackPressed() {
        if(!searchView.isIconified()){
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }
}