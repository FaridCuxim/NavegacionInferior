package com.example.navegacioninferior;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.navegacioninferior.Fragments.GiftsFragment;
import com.example.navegacioninferior.Fragments.ProfileFragment;
import com.example.navegacioninferior.Fragments.StoreFragment;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Adjuntar comportamiento de NavigationBottom - Ocultar/Mostrar al hacer scroll
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        //Cargar StoreFragment por defecto
        toolbar.setTitle("Shop");
        loadFragment(new StoreFragment());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
       @Override
       public boolean onNavigationItemSelected(@NonNull MenuItem item) {
           Fragment fragment;
           switch (item.getItemId()){
               case R.id.navigation_shop:
                   toolbar.setTitle("Shop");
                   fragment = new StoreFragment();
                   loadFragment(fragment);
                   return true;
               case R.id.navigation_gifts:
                   toolbar.setTitle("My Gifts");
                   fragment = new GiftsFragment();
                   loadFragment(fragment);
                   return true;
               case R.id.navigation_cart:
                   toolbar.setTitle("Cart");
                   fragment = new GiftsFragment();
                   loadFragment(fragment);
                   return true;
               case R.id.navigation_profile:
                   toolbar.setTitle("Profile");
                   fragment = new ProfileFragment();
                   loadFragment(fragment);
                   return true;
           }
           return false;
       }
   };

    private void loadFragment(Fragment fragment) {
        //Cargar Fragmento
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
