package com.example.trey.registertest;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import androidx.annotation.RequiresApi;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ScanReceipt extends AppCompatActivity {

    private ImageView imageView;

    BottomNavigationView bottomNavigationView;

    private static final String TAG = "CapturePicture";
    static final int REQUEST_PICTURE_CAPTURE = 1;
    private String pictureFilePath;
    private int CAMERA_PERMISSION_CODE = 1;
    String useridnum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_receipt);

        imageView = findViewById(R.id.imageView);

        bottomNavigationView = findViewById(R.id.navBar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case (R.id.homeNav):

                        break;

                    case (R.id.homeBudget):
                        Intent intent1 = new Intent(ScanReceipt.this, BudgetActivity.class);
                        startActivity(intent1);
                        break;

                    case (R.id.homeAdd):
                        Intent intent2 = new Intent(ScanReceipt.this, AddActivity.class);
                        startActivity(intent2);
                        break;

                    case (R.id.homeSettings):
                        Intent intent3 = new Intent(ScanReceipt.this, SettingsActivity.class);
                        startActivity(intent3);
                        break;


                }


                return false;
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.globalPreferenceName, MODE_PRIVATE);
        String userid = sharedPreferences.getString("userid", "No Value");
        useridnum = userid;

        takePicture();
    }

    private void takePicture() {
        if (ContextCompat.checkSelfPermission(ScanReceipt.this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(cameraIntent, REQUEST_PICTURE_CAPTURE);

                File pictureFile = null;
                try {
                    pictureFile = getPictureFile();
                } catch (IOException ex) {
                    Toast.makeText(ScanReceipt.this, "Photo file can't be created, please try again", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pictureFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(ScanReceipt.this,
                            "com.example.trey.registertest.android.fileprovider",
                            pictureFile);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(cameraIntent, REQUEST_PICTURE_CAPTURE);
                }

            }
        } else {
            requestCameraPermission();
        }
    }

    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {

            new AlertDialog.Builder(this)
                    .setTitle("Camera access needed")
                    .setMessage("This permission is needed for the scanning function")
                    .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(ScanReceipt.this,
                                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
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
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private File getPictureFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String pictureFile = "ZOFTINO_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(pictureFile,  ".jpg", storageDir);
        pictureFilePath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PICTURE_CAPTURE && resultCode == RESULT_OK) {
            File imgFile = new File(pictureFilePath);
            if (imgFile.exists()) {
                Uri fileUri = Uri.fromFile(imgFile);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), fileUri);
                    bitmap = correctOrientation(bitmap, pictureFilePath);
                    imageView.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
//                    Toast.makeText(ScanReceipt.this, "Please wait a couple seconds", Toast.LENGTH_LONG).show();
                    detectText();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public Bitmap correctOrientation(Bitmap bitmap,
                                     String imagePath) throws IOException {
        ExifInterface ei = new ExifInterface(imagePath);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap rotatedBitmap = null;
        switch(orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = rotateImage(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateImage(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateImage(bitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = bitmap;
        }
        return rotatedBitmap;
    }
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private final OkHttpClient client = new OkHttpClient();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void detectText() throws Exception {
        new DetectTextTask().execute();
        Toast.makeText(ScanReceipt.this, "Please wait a couple seconds", Toast.LENGTH_LONG).show();
    }

    class DetectTextTask extends AsyncTask<Void, Void, Void> {

        private Exception exception;

        @Override
        protected Void doInBackground(Void... voids) {

            try  {
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("title", "Square Logo")
                        .addFormDataPart("file",  pictureFilePath,
                                RequestBody.create(MEDIA_TYPE_PNG, new File(pictureFilePath)))
                        .build();

                Request request = new Request.Builder()
                        .header("apikey", "cf0591e0519011e98bfadfb7eb1aa8b5")
                        .url("https://api.taggun.io/api/receipt/v1/simple/file")
                        .post(requestBody)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                        String responseString = response.body().string();
                        Log.e("Taggun", responseString);
                        JSONObject jsonObject = new JSONObject(responseString);
                        Intent intent = new Intent(ScanReceipt.this, PreviewScreenActivity.class);


                        Double totalAmount1 = jsonObject.getJSONObject("totalAmount").getDouble("data");
                        String totalAmount = Double.toString(totalAmount1);

                        String merchantName = jsonObject.getJSONObject("merchantName").getString("data");

                        if (jsonObject.getJSONObject("date").getString("data") != null) {
                            String date1 = jsonObject.getJSONObject("date").getString("data");
                            date1 = date1.split("T")[0];

                            String initialMonthFinal = "";
                            String initialDayFinal = "";
                            String s = date1;
                            String[] parts = s.split("-");
                            String initialMonth = parts[1];
                            String initialDay = parts[2];

                            if (initialMonth.contains("0")){
                                String[] monthParts = initialMonth.split("0");
                                initialMonthFinal = monthParts[1];
                            } else {
                                initialMonthFinal = initialMonth;
                            }

                            if (initialDay.contains("0")){
                                String[] dayParts = initialDay.split("0");
                                initialDayFinal = dayParts[2];
                            } else {
                                initialDayFinal = initialDay;
                            }

                            String correctDisplay = initialMonthFinal + "/" + initialDayFinal + "/" + parts[0];

                            intent.putExtra("date1", correctDisplay);

                        } else {
                            String date1 = "01-01-2019";
                            intent.putExtra("date1", date1);
                        }

                        intent.putExtra("price1", totalAmount);
                        intent.putExtra("storeName", merchantName);

                        startActivity(intent);

                } catch (IOException e) {
                    Log.e("HELP!!!!!!!!!!", String.valueOf(e));
                    e.printStackTrace();
                    //startActivity(new Intent(ScanReceipt.this, AddActivity.class));
                }
            }

            catch (Exception e) {
                Log.e("PLEASE HELP!!!!!!!!!!", String.valueOf(e));
                e.printStackTrace();
                //startActivity(new Intent(ScanReceipt.this, AddActivity.class));
            }
            return null;
        }

        protected void onPostExecute() {
            // TODO: check this.exception
            // TODO: do something with the feed
        }

    }

}
