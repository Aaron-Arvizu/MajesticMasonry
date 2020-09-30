package com.example.majesticmasonry;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProjectDisplay extends AppCompatActivity {
    public String userEmail;
    public FirebaseUser currentUser;
    public FirebaseAuth mAuth;
    public String pulledAdd,pulledZip,pulledName,finalAdd,projectName;
    public Button DailyLogButton,ViewLogButton;
    public TextView finalAddBox,ProjectNameBox;
    public DatabaseReference baseRef,zipRef,addRef,nameRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_display);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        CheckForAdmin();
        GetNameAndAdd();

        //Define Buttons and Boxes
        DailyLogButton = (Button) findViewById(R.id.dailyLogButton);
        finalAddBox = (TextView) findViewById(R.id.addAndZipBox);
        ProjectNameBox = (TextView) findViewById(R.id.projNameBox);
        ViewLogButton = (Button) findViewById(R.id.viewLogBtn);
        //End Defs

        DailyLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent DailyLogIntent = new Intent (ProjectDisplay.this,DailyLogActivity.class);
                startActivity(DailyLogIntent);
            }
        });

        ViewLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoToViewLog();
            }
        });

    }

    public void CheckForAdmin(){
        userEmail = currentUser.getEmail();
        Log.d("test",userEmail);
        if(userEmail.equals("admin@nothing.com")){
            adminActions();
            Log.d("test","admin");
        }
        else
        {
            employeeActions();
            Log.d("test","normie");

        }
    }
    public void adminActions(){

    }

    public void employeeActions(){

    }

    public void GetNameAndAdd(){

        baseRef = FirebaseDatabase.getInstance().getReference().child("Projects").child("Majestic Masonry");


        //GET PROJECT NAME
        nameRef = baseRef.child("ProjectName");
        nameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pulledName = dataSnapshot.getValue(String.class);
                Log.d("test",pulledName);
                ProjectNameBox.setText(pulledName);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        // END OF NAME


        zipRef = baseRef.child("ZipCode");
        addRef = baseRef.child("Address");
        zipRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pulledZip = dataSnapshot.getValue(String.class);
                addRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        pulledAdd = dataSnapshot.getValue(String.class);
                        finalAdd = pulledAdd +" "+ pulledZip;
                        finalAddBox.setText(finalAdd);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void GoToViewLog(){
        Intent viewLogIntent = new Intent(ProjectDisplay.this,ShowLogActivity.class);
        startActivity(viewLogIntent);
    }
    public void onBackPressed() {
            Intent backToListIntent = new Intent(ProjectDisplay.this,ProjectList.class);
            startActivity(backToListIntent);

    }
}
