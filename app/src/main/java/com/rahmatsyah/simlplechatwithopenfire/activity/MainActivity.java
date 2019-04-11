package com.rahmatsyah.simlplechatwithopenfire.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.rahmatsyah.simlplechatwithopenfire.R;
import com.rahmatsyah.simlplechatwithopenfire.fragment.GlobalFragment;
import com.rahmatsyah.simlplechatwithopenfire.fragment.OneToOneFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrame, new GlobalFragment()).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.mainBottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (menuItem.getItemId()){
            case R.id.menuGlobalChat:
                fragmentTransaction.replace(R.id.mainFrame, new GlobalFragment()).commit();
                break;
            case R.id.menuPerson:
                fragmentTransaction.replace(R.id.mainFrame, new OneToOneFragment()).commit();
                break;
        }
        return true;
    }
}
