package com.example.uts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.uts.databinding.MainMenuBinding;
import com.example.uts.preferences.UserPreferences;

public class MainActivity extends AppCompatActivity {

    MainMenuBinding binding;
    UserPreferences userPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.main_menu);

        userPreferences = new UserPreferences(this);

        TextView welcome = findViewById(R.id.welcome);
        welcome.setText("Welcome, "+ userPreferences.getUserLogin().getName());

        binding.setActivity(this);
    }

    public View.OnClickListener btnProfile = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            Intent mainActivity = new Intent(MainActivity.this, Profile.class);
            startActivity(mainActivity);
            finish();
        }
    };

    public View.OnClickListener btnReservation = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            Intent mainActivity = new Intent(MainActivity.this, ReservationActivity.class);
            startActivity(mainActivity);
        }
    };

    public View.OnClickListener btnLocation = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            Intent mainActivity = new Intent(MainActivity.this, Location.class);
            startActivity(mainActivity);
        }
    };

    public View.OnClickListener btnDetail = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            Intent mainActivity = new Intent(MainActivity.this, Detail.class);
            startActivity(mainActivity);
        }
    };

    public View.OnClickListener btnLogout = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            userPreferences.logout();
            Intent mainActivity = new Intent(MainActivity.this, Login.class);
            startActivity(mainActivity);
            finish();
        }
    };
}