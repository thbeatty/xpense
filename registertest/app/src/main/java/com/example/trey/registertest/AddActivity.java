package com.example.trey.registertest;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.text.TextRecognizer;

import static com.example.trey.registertest.LoginActivity.globalPreferenceName;


public class AddActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    String useridnum;
    Button manualEntryButton;
    Button scanButton;
    Button importButton;
    private int STORAGE_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        manualEntryButton = findViewById(R.id.manualEntryButton);
        scanButton = findViewById(R.id.scanButton);
        importButton = findViewById(R.id.importButton);
        bottomNavigationView = findViewById(R.id.navBar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case (R.id.homeNav):
                        Intent intent0 = new Intent(AddActivity.this, HomeActivity.class);
                        startActivity(intent0);
                        break;

                    case (R.id.homeBudget):
                        Intent intent1 = new Intent(AddActivity.this, BudgetActivity.class);
                        startActivity(intent1);
                        break;

                    case (R.id.homeAdd):
                        Intent intent2 = new Intent(AddActivity.this, AddActivity.class);
                        startActivity(intent2);
                        break;

                    case (R.id.homeSettings):
                        Intent intent3 = new Intent(AddActivity.this, SettingsActivity.class);
                        startActivity(intent3);
                        break;


                }


                return false;
            }
        });

        manualEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddActivity.this, ManualEntryActivity.class);
                startActivity(intent);
            }
        });
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(AddActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(AddActivity.this, ScanReceipt.class);
                    startActivity(intent);
                } else {
                    requestStoragePermission();
                }
            }
        });
        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(AddActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(AddActivity.this, ImportActivity.class);
                    startActivity(intent);
                } else {
                    requestStoragePermission();
                }
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences(globalPreferenceName, MODE_PRIVATE);
        String userid = sharedPreferences.getString("userid", "No Value");
        useridnum = userid;

        requestStoragePermission();

    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle("Storage access needed")
                    .setMessage("This permission is needed for the scanning function")
                    .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(AddActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                //Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }

    }
}