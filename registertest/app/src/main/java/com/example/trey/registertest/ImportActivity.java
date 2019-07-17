package com.example.trey.registertest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import androidx.annotation.RequiresApi;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ImportActivity extends AppCompatActivity {

    private ImageView imageView;
    BottomNavigationView bottomNavigationView;

    private static final String TAG = "CapturePicture";
    private String pictureFilePath;
    private int CAMERA_PERMISSION_CODE = 1;
    String useridnum;

    public static final int IMAGE_GALLERY_REQUEST = 20;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);

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
                        Intent intent = new Intent(ImportActivity.this, HomeActivity.class);
                        startActivity(intent);
                        break;

                    case (R.id.homeBudget):
                        Intent intent1 = new Intent(ImportActivity.this, BudgetActivity.class);
                        startActivity(intent1);
                        break;

                    case (R.id.homeAdd):
                        Intent intent2 = new Intent(ImportActivity.this, AddActivity.class);
                        startActivity(intent2);
                        break;

                    case (R.id.homeSettings):
                        Intent intent3 = new Intent(ImportActivity.this, SettingsActivity.class);
                        startActivity(intent3);
                        break;

                }

                return false;
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.globalPreferenceName, MODE_PRIVATE);
        String userid = sharedPreferences.getString("userid", "No Value");
        useridnum = userid;

        importPhoto();
    }

    private void importPhoto() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirectoryPath = pictureDirectory.getPath();

        Uri data = Uri.parse(pictureDirectoryPath);
        photoPickerIntent.setDataAndType(data, "image/*");
        startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_GALLERY_REQUEST && resultCode == RESULT_OK) {

            Uri imageUri = data.getData();
            InputStream inputStream;
            pictureFilePath = getPath(this.getApplicationContext( ), imageUri );
            Log.e("Picture Path", pictureFilePath);
//            Log.e("Ia", String.valueOf(imageUri));
            try {
                inputStream = getContentResolver().openInputStream(imageUri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                bitmap = correctOrientation(bitmap, pictureFilePath);

                imageView.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Unable to open image", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Toast.makeText(ImportActivity.this, "Please wait a couple seconds", Toast.LENGTH_LONG).show();
            detectText();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getPath( Context context, Uri uri ) {
        String result = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver( ).query( uri, proj, null, null, null );
        if(cursor != null){
            if ( cursor.moveToFirst( ) ) {
                int column_index = cursor.getColumnIndexOrThrow( proj[0] );
                result = cursor.getString( column_index );
            }
            cursor.close( );
        }
        if(result == null) {
            result = "Not found";
        }
        return result;
    }

    public Bitmap correctOrientation(Bitmap bitmap, String imagePath) throws IOException {

        ExifInterface ei = new ExifInterface(imagePath);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

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
                    //Log.e("Taggun", responseString);
                    JSONObject jsonObject = new JSONObject(responseString);
                    Double totalAmount1 = jsonObject.getJSONObject("totalAmount").getDouble("data");
                    String totalAmount = Double.toString(totalAmount1);
                    String merchantName = jsonObject.getJSONObject("merchantName").getString("data");
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

                    Intent intent = new Intent(ImportActivity.this, PreviewScreenActivity.class);
                    intent.putExtra("price1", totalAmount);
                    intent.putExtra("storeName", merchantName);
                    intent.putExtra("date1", correctDisplay);
                    startActivity(intent);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute() {
            // TODO: check this.exception
            // TODO: do something with the feed
        }

    }

}
