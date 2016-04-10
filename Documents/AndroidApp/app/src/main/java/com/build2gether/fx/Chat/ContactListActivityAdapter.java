package com.build2gether.fx.Chat;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.build2gether.fx.Firebase.FirebaseIO;
import com.build2gether.fx.Firebase.FirebaseUtil;
import com.build2gether.fx.OOP.User;
import com.build2gether.fx.R;
import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by adityamittal on 4/1/16.
 */
public class ContactListActivityAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private static ArrayList<String> contactList;

    public ContactListActivityAdapter(Activity context, ArrayList<String> contactList) {

        super(context, R.layout.activity_contact, contactList);
        this.context = context;
        this.contactList = contactList;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.activity_contact, null, true);
        TextView contact =  (TextView) rowView.findViewById(R.id.contactName);


        System.out.println("Size contact" + contactList.size());

        String[] idAndValue = contactList.get(position).split(",");
        System.out.println("Position: " + position);
        setContactNameByTextView(idAndValue[0],rowView);


        ImageView imgView = (ImageView) rowView.findViewById(R.id.fbPic);
        //profilePic.setProfileId(idAndValue[0]);

        Picasso.with(this.getContext()).load("https://graph.facebook.com/" + idAndValue[0] + "/picture?type=large").transform(new CircleTransform()).into(imgView);


        return rowView;
    }

    public static ArrayList<String> getContactArrayList() {
        return contactList;
    }

    private class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }

    private void setContactNameByTextView( String ID,final View view) {
        Firebase root = new Firebase(FirebaseUtil.USERS.toString()  + "/" + ID);
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    TextView contact = (TextView) view.findViewById(R.id.contactName);
                    contact.setText(postSnapshot.child("name").getValue().toString());
                }
            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });






    }

}


