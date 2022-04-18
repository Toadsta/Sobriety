package com.orangeelephant.sobriety.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.orangeelephant.sobriety.R;
import com.orangeelephant.sobriety.counter.Counter;
import com.orangeelephant.sobriety.activities.adapters.CounterAdapter;

public class HomeScreenActivity extends AppCompatActivity implements CounterAdapter.OnItemClicked {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;
    private CounterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        setContentView(R.layout.activity_home_screen);
        onCreateRecycler();

        setSupportActionBar(findViewById(R.id.homeScreenToolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        setTimeMessageUpdateHandler();
        preferenceChangeListener =
                new SharedPreferences.OnSharedPreferenceChangeListener() {
                    @Override
                    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                        recreate();
                    }
                };
        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }


    @Override
    public void onResume() {
        adapter.onDataChanged();
        super.onResume();
    }

    public void onClickAddCounter (View v) {
        Intent intent = new Intent(HomeScreenActivity.this, AddCounterActivity.class);
        startActivity(intent);
    }

    public void onClickSettings (View v) {
        Intent intent = new Intent(HomeScreenActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(int position) {
        Counter openCounter = this.adapter.getmCounter()[position];

        Intent intent = new Intent(HomeScreenActivity.this, CounterFullViewActivity.class);
        intent.putExtra("openCounterId", openCounter.get_id());

        startActivity(intent);
    }

    public void onCreateRecycler() {
        RecyclerView countersView = (RecyclerView) findViewById(R.id.counterView);

        this.adapter = new CounterAdapter(this);
        countersView.setAdapter(adapter);
        countersView.setLayoutManager(new LinearLayoutManager(this));

        adapter.setOnClick(this);
    }

    public void onUpdateRecycler() {
        this.adapter.onDataChanged();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    // https://stackoverflow.com/questions/11434056/how-to-run-a-method-every-x-seconds
    private void setTimeMessageUpdateHandler() {
        final Handler handler = new Handler();
        final int delay = 1000; // 1000 milliseconds == 1 second

        handler.postDelayed(new Runnable() {
            public void run() {
                onUpdateRecycler();
                handler.postDelayed(this, delay);
            }
        }, delay);
    }
}