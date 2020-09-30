package com.example.majesticmasonry;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddProject extends AppCompatActivity {

    public EditText ProjectNameBox,AddressBox,ZipCodeBox;
    public DatabaseReference baseRef = FirebaseDatabase.getInstance().getReference().child("Projects");
    public DatabaseReference pNameRef,pAddRef,pZipRef,pNameListRef;
    public String pName,pAdd,pZip;
    public Button CompleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);


        //Define button and editTexts
        ProjectNameBox = (EditText) findViewById(R.id.projectName);
        CompleteButton = (Button) findViewById(R.id.completeButton);
        AddressBox = (EditText) findViewById(R.id.addressBox);
        ZipCodeBox = (EditText) findViewById(R.id.zipCodeBox);

        //End Definitions


        //completeOnclick
        CompleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInfo();
            }
        });

    }


    public void getInfo(){
        //Get & save name (Check if Null)
        if(ProjectNameBox.getText().toString().equals("")){
            //If is Null
            Toast.makeText(this,"Please enter a project name",Toast.LENGTH_LONG).show();
        }
        else
            {
                //If name isn't Null
            pName = ProjectNameBox.getText().toString();
            pNameRef = baseRef.child(pName).child("ProjectName");

            pNameListRef = baseRef.child("Project Names").child(pName);
            pNameListRef.setValue(pName);
            pNameRef.setValue(pName);
        }
        // End Name

        //Get & save address (Check if null)
        if(AddressBox.getText().toString().equals("")){
            //If is Null
            Toast.makeText(this,"Please enter an address",Toast.LENGTH_LONG).show();

        }
        else
        {
            //If Add isn't Null
            pAdd = AddressBox.getText().toString();
            pAddRef = baseRef.child(pName).child("Address");
            pAddRef.setValue(pAdd);
        }

        if(ZipCodeBox.getText().toString().equals("")){
            //If is Null
            Toast.makeText(this,"Please enter a Zip Code",Toast.LENGTH_LONG).show();

        }
        else
        {
            //If Add isn't Null
            pZip = ZipCodeBox.getText().toString();
            pZipRef = baseRef.child(pName).child("ZipCode");
            pZipRef.setValue(pZip);
        }

        Intent completeintent = new Intent (AddProject.this,MainMenu.class);
        startActivity(completeintent);
    }

}
