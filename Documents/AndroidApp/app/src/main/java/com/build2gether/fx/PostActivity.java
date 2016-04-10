package com.build2gether.fx;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import com.cloudinary.*;


import com.build2gether.fx.Firebase.FirebaseIO;
import com.cloudinary.utils.ObjectUtils;
import com.facebook.Profile;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PostActivity extends AppCompatActivity {

    private ImageView img;
    private String mCurrentPhotoPath;//get path
    static final int REQUEST_TAKE_PHOTO = 1;
    public static final int CAMERA_REQUEST = 1;
    private static File photoFile;

    private Bitmap photo;
    private Cloudinary cloudinary = new Cloudinary("cloudinary://919184818441873:b4PtPkGreVNh8LxhbmErYKvFc7g@dav4zrplc");

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        img = (ImageView) findViewById(R.id.pic);
        final Button post = (Button) findViewById(R.id.post);
        final TextView tv = (TextView) findViewById(R.id.titleInput);
        final TextView dv = (TextView) findViewById(R.id.descriptionInput);
        final Switch exSw = (Switch) findViewById(R.id.exchange);
        final Switch fSw = (Switch) findViewById(R.id.favor);
        post.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String title = tv.getText().toString();
                String description = dv.getText().toString();
                boolean exchange = exSw.isChecked();
                boolean favor = fSw.isChecked();

                if(savedInstanceState != null) {
                    Log.d("what", "on create not null");
                    //photo = savedInstanceState.getParcelable("BitmapImage");
                    if(photoFile != null) {
                        //img.setImageURI(Uri.fromFile(photoFile));
                        Picasso.with(getApplicationContext()).load(photoFile).fit().centerCrop().into(img);
                    }
                } else {
                    Log.d("what", "on create null");
                    img = (ImageView) findViewById(R.id.pic);
                }
                //Picasso.with(getApplicationContext()).load(photoFile).config(Bitmap.Config.RGB_565).resize(300, 500).rotate(90).into(img);
                FirebaseIO fbIO = new FirebaseIO();

                FileInputStream is = null;
                try {
                    is = new FileInputStream(photoFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String URL = null;
                try {
                    String id = cloudinary.randomPublicId();
                    URL = cloudinary.url().generate(id + ".jpg");
                    upload(is, id, URL);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                fbIO.saveInventory(title, description, favor, exchange, URL);
                cancel();
             }
        });

//        final Button cancelPost = (Button) findViewById(R.id.cancelPost);
//        cancelPost.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                cancel();
//            }
//        });

        final Button uploadImg = (Button) findViewById(R.id.uploadImg);

        uploadImg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                Log.d("what", "on upload before startActivity");
                //startActivityForResult(cameraIntent, CAMERA_REQUEST);
//                Log.d("what", "on upload after startActivity");
//                // Ensure that there's a camera activity to handle the intent
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should g
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File

                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(photoFile));
                        //cameraIntent.putExtra("return-data", true);
                        Log.d("what", "before startactivity for result");
                        startActivityForResult(cameraIntent, REQUEST_TAKE_PHOTO);

                        if(photoFile != null) {
                            Picasso.with(getApplicationContext()).load(photoFile).fit().centerCrop().into(img);
                        }
                        Log.d("what", "after startactivity for result");
                        //startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }
                }
                Log.d("what", "on after upload");
            }
        });
    }

    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent data){
        Log.d("what", "on activity result");
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Log.d("what", "on activity result in if");
            //photo = (Bitmap) data.getExtras().get("data");
            if(photoFile != null) {
                Log.d("what", "on activity result photoFile not null");
                Picasso.with(getApplicationContext()).load(photoFile).fit().centerCrop().into(img);
            }
            //img.setImageBitmap(photo);
        }
        Log.d("what", "on activity result the end");
    }


    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        boolean gotImage = savedInstanceState.getBoolean("gotImage");
        if(gotImage) {
            Log.d("what", "on restore");
            Picasso.with(getApplicationContext()).load(photoFile).fit().centerCrop().into(img);
        }
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("what", "on after restore");

        // Restore state members from saved instance

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.d("what", "on save");
//        if(photoFile != null) {
//            Log.d("what", "on save and not null");
//            img.setImageURI(Uri.fromFile(photoFile));
//            //img.buildDrawingCache();
//            //photo = img.getDrawingCache();
//            //savedInstanceState.putParcelable("BitmapImage", photo);
//        }
        //savedInstanceState.putString("ImageFile", String..readFileToString(photoFile, "UTF-8")));
        savedInstanceState.putBoolean("gotImage", true);
        Log.d("what", "on save after");
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestart() {
        Log.d("what", "on restart");
        super.onRestart();
        Log.d("what", "on restart after");
    }

   // public InputStream getPhotoInput()

    private void cancel() {
        Log.d("what", "on cancel");
        startActivity(new Intent(this, MainPageActivity.class));
    }

    private void upload(final InputStream is, final String id, final String URL) throws IOException {
;        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    cloudinary.uploader().upload(is, ObjectUtils.asMap("public_id", id));
                } catch (IOException  e) {
                    e.printStackTrace();
                }
            }

        };
        Log.d("what", "on uploadddd thread");
        new Thread (runnable).start();
    }


    private File createImageFile() throws IOException {
        Log.d("what", "on create image file");
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }
}
