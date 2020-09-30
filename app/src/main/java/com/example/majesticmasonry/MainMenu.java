package com.example.majesticmasonry;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainMenu extends AppCompatActivity {
    FirebaseDatabase mDatabase;
    public FirebaseAuth mAuth;
    public FirebaseUser currentUser;
    public String password_entry, emailEntered, emailFinal,userEmail;
    public Button LogoutButton,AddProjectButton,ViewProjectButton;
    public TextView UserNameBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this); // Needed For Firebase to Run
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();


        //Define all Menu Buttons
        LogoutButton = (Button) findViewById(R.id.logoutbtn);
        AddProjectButton = (Button) findViewById(R.id.addproject);
        ViewProjectButton = (Button) findViewById(R.id.viewproj);
        UserNameBox = (TextView) findViewById(R.id.userNameBox);
        // End of Menu Buttons

        //setAddProjectButton to be invisible unless admin is authenticated
        AddProjectButton.setVisibility(AddProjectButton.INVISIBLE);
        //end invisibility

        //on Create Sign in
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            SignIn();
        } else {
            CheckForAdmin();
        }
        // End of Sign in



        //OnClick For Logout Button
        LogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                SignIn();
            }
        });
        //end of Onclick

        //Add Project OnClick
        AddProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addProjIntent = new Intent(MainMenu.this,AddProject.class);
                startActivity(addProjIntent);
            }
        });
        //end of OnClick

        //onClick for Project list
        ViewProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoToProjectList();
            }
        });
    }


    public void SignIn() {

        currentUser = mAuth.getCurrentUser();
        final AlertDialog.Builder loginBuilder = new AlertDialog.Builder(this);
        loginBuilder.setCancelable(false);
        loginBuilder.setTitle("Enter Employee Pin");

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.logindialog, null);
        final EditText emailBox = (EditText) dialogView.findViewById(R.id.userName);
        final EditText passBox = (EditText) dialogView.findViewById(R.id.password);
        loginBuilder.setView(dialogView);
        loginBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                password_entry = passBox.getText().toString();
                emailEntered = emailBox.getText().toString();
                emailFinal = emailEntered + "@nothing.com";
                if (password_entry.length() != 6) {
                    loginBuilder.create().cancel();
                    Toast.makeText(MainMenu.this, "Bad Login Try again", Toast.LENGTH_LONG).show();
                    SignIn();

                } else {
                    mAuth.signInWithEmailAndPassword(emailFinal, password_entry).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d("test", "login success");
                                currentUser = mAuth.getCurrentUser();
                                userEmail = currentUser.getEmail();
                                Log.d("test",userEmail);
                                CheckForAdmin();
                            } else {
                                Log.d("test", "login fail");
                                loginBuilder.create().cancel();
                                Toast.makeText(MainMenu.this, "Bad Login Try again", Toast.LENGTH_LONG).show();
                                SignIn();
                            }
                        }

                    });


                }
            }
        });
        loginBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        loginBuilder.show();

    }

public void CheckForAdmin(){
        userEmail = currentUser.getEmail();
        Log.d("test",userEmail);
if(userEmail.equals("admin@nothing.com")){
    // Set Username in upper Right
    final FirebaseUser currentUser = mAuth.getCurrentUser();
    String signInEmail = currentUser.getEmail();
    signInEmail = signInEmail.split("@")[0];
    String userEmailCap = signInEmail.substring(0, 1).toUpperCase() + signInEmail.substring(1);
    Log.d("test", userEmailCap);
    Log.d("test","admin detected");
    UserNameBox.setText("User:  "+ userEmailCap);
    //End Set Username
    adminActions();
}
else
{
    // Set Username in upper Right
    final FirebaseUser currentUser = mAuth.getCurrentUser();
    String signInEmail = currentUser.getEmail();
    signInEmail = signInEmail.split("@")[0];
    String userEmailCap = signInEmail.substring(0, 1).toUpperCase() + signInEmail.substring(1);
    Log.d("test", userEmailCap);
    UserNameBox.setText("User:  "+ userEmailCap);
    //End Set Username
    employeeActions();
    Log.d("test","normie");

}
    }


    public void adminActions(){
        AddProjectButton.setVisibility(AddProjectButton.VISIBLE);
    }

    public void employeeActions(){
        AddProjectButton.setVisibility(AddProjectButton.INVISIBLE);
    }

    public void GoToProjectList(){
        Intent projectDisplayIntent = new Intent(MainMenu.this,ProjectList.class);
        startActivity(projectDisplayIntent);
    }
}

