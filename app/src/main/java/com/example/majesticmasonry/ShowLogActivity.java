package com.example.majesticmasonry;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ShowLogActivity extends AppCompatActivity {
public ImageView LogImage;
public StorageReference logRef;
public Uri logImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_log);

        //Definitions
        LogImage = (ImageView) findViewById(R.id.showLog);
        //End Def

        getImage();

    }

    public void getImage(){
        logRef = FirebaseStorage.getInstance().getReference().child("Majestic Masonry").child("DailyLog");
        logRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                logImage = uri;
                Log.d("test",logImage.toString());
                Picasso.get().load(logImage).into(LogImage);
            }
        });
    }
}
