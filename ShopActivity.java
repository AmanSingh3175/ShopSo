package com.example.shopso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ShopActivity extends AppCompatActivity {

    TextView catShowAll,popularShowAll,newProductShowAll;

    Toolbar toolbar;
    FirebaseAuth auth;

    ViewFlipper imgBanner;
    ProgressDialog progressDialog;

    RecyclerView catRecycler;
    RecyclerView newProductRecycler;
    RecyclerView popularProductRecycler;

    CategoryAdapter categoryAdapter;
    List<CategoryModel> categoryModelList;

    NewProductsAdapter newProductsAdapter;
    List<NewProductsModel> newProductsModelList;

    PopularProductAdapter popularProductAdapter;
    List<PopularProductModel> popularProductModelList;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        auth = FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);


        imgBanner = findViewById(R.id.imgBanner);

        progressDialog = new ProgressDialog(this);
        catRecycler = findViewById(R.id.rec_category);
        newProductRecycler = findViewById(R.id.new_product_rec);
        popularProductRecycler = findViewById(R.id.popular_rec);

        catShowAll = findViewById(R.id.category_see_all);
        popularShowAll = findViewById(R.id.popular_see_all);
        newProductShowAll = findViewById(R.id.newProducts_see_all);

        catShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ShowAllActivity.class);
                startActivity(intent);
            }
        });

        popularShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ShowAllActivity.class);
                startActivity(intent);
            }
        });

        newProductShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ShowAllActivity.class);
                startActivity(intent);
            }
        });

        db = FirebaseFirestore.getInstance();

        int slides[]={
                R.drawable.banner1,
                R.drawable.banner5,
                R.drawable.banner4
        };
        for(int slide:slides){
            bannerFlip(slide);
        }

        progressDialog.setTitle("Welcome to SHOPSO");
        progressDialog.setMessage("Loading");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        catRecycler.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        categoryModelList = new ArrayList<>();
        categoryAdapter= new CategoryAdapter(getApplicationContext(),categoryModelList);
        catRecycler.setAdapter(categoryAdapter);

        db.collection("Category")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                CategoryModel categoryModel = document.toObject(CategoryModel.class);
                                categoryModelList.add(categoryModel);
                                categoryAdapter.notifyDataSetChanged();
                                progressDialog.dismiss();
                            }
                        }
                    }
                });

        // Fetching New Products from our firebase database

        newProductRecycler.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        newProductsModelList = new ArrayList<>();
        newProductsAdapter = new NewProductsAdapter(getApplicationContext(),newProductsModelList);
        newProductRecycler.setAdapter(newProductsAdapter);

        db.collection("NewProducts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                NewProductsModel newProductsModel = document.toObject(NewProductsModel.class);
                                newProductsModelList.add(newProductsModel);
                                newProductsAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });

        popularProductRecycler.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        popularProductModelList = new ArrayList<>();
        popularProductAdapter = new PopularProductAdapter(getApplicationContext(),popularProductModelList);
        popularProductRecycler.setAdapter(popularProductAdapter);

        db.collection("PopularProducts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                PopularProductModel popularProductModel = document.toObject(PopularProductModel.class);
                                popularProductModelList.add(popularProductModel);
                                popularProductAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

    public void bannerFlip(int image){
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(image);
        imgBanner.addView(imageView);
        imgBanner.setFlipInterval(4000);
        imgBanner.setAutoStart(true);
        imgBanner.setInAnimation(this,android.R.anim.fade_in);
        imgBanner.setOutAnimation(this,android.R.anim.fade_out);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id== R.id.home_logout){
            auth.signOut();
            startActivity(new Intent(ShopActivity.this,RegistrationActivity.class));
            finish();
        }
        return true;
    }

}