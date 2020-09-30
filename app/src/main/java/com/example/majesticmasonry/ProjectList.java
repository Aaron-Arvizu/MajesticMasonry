package com.example.majesticmasonry;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProjectList extends AppCompatActivity {
public Button Project1Button, Project2Button,Project3Button,Project4Button;
public DatabaseReference projNameRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);


        //Define Buttons
        Project1Button = (Button)findViewById(R.id.proj1box);
        Project2Button = (Button)findViewById(R.id.proj2box);
        Project3Button = (Button)findViewById(R.id.proj3box);
        Project4Button = (Button)findViewById(R.id.proj4box);
        //End of Button Definitions

        //Get Names for List
        projNameRef = FirebaseDatabase.getInstance().getReference().child("Projects").child("Project Names");
        projNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("Project 1"))
                {
                    Project1Button.setText("Project 1");
                }

                if(dataSnapshot.hasChild("Project 2"))
                {
                    Project2Button.setText("Project 2");
                }

                if(dataSnapshot.hasChild("Majestic Masonry"))
                {
                    Project3Button.setText("Majestic Masonry");
                }

                if(dataSnapshot.hasChild("Project 4"))
                {
                    Project4Button.setText("Project 4");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Project1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Project2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Project3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Proj3Intent = new Intent(ProjectList.this,ProjectDisplay.class);
                startActivity(Proj3Intent);
            }
        });

        Project4Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    public void onBackPressed() {
        Intent backToListIntent = new Intent(ProjectList.this,MainMenu.class);
        startActivity(backToListIntent);

    }
}
