package com.simplifiers.pictassistant;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText editText_email,editText_enrollmentno,editText_password,editText_classname,editText_batchname;
    private Button button_proceed;
    private String email;


    private FirebaseFirestore db ;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private Intent nextactivity;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(mAuth.getCurrentUser() != null){
                    nextactivity = new Intent(MainActivity.this,Main2Activity.class);
                    startActivity(nextactivity);
                    finish();
                }
            }
        };


        editText_email = findViewById(R.id.editText_email_activity_main);
        editText_enrollmentno = findViewById(R.id.editText_enrollment_no_activity_main);
        editText_password = findViewById(R.id.editText_password_activity_main);
        editText_classname = findViewById(R.id.editText_classname_activity_main);
        editText_batchname = findViewById(R.id.editText_batchname_activity_main);
        button_proceed = findViewById(R.id.button_activity_main);



        button_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                if(TextUtils.isEmpty(editText_email.getText().toString())){
                    editText_email.setError("Enter Email");
                }
                if(TextUtils.isEmpty(editText_enrollmentno.getText().toString())){
                    editText_enrollmentno.setError("Enter EnrollmentNo");
                }
                if(TextUtils.isEmpty(editText_password.getText().toString())){
                    editText_password.setError("Enter Password");
                }
                if(TextUtils.isEmpty(editText_classname.getText().toString())){
                    editText_classname.setError("Enter Classname");
                }
                if(TextUtils.isEmpty(editText_batchname.getText().toString())){
                    editText_batchname.setError("Enter Batchname");
                }

                showDialog(view);
            }
        });

    }


    private void showDialog(final View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        builder.setTitle(getString(R.string.dialog_title));

        String  message  = "Email : "+ editText_email.getText().toString() +"\n";
                message += "Enrollment No: "+ editText_enrollmentno.getText().toString()+"\n";
                message += "Password: "+ editText_password.getText().toString()+"\n";
                message += "Class Name: "+ editText_classname.getText().toString()+"\n";
                message += "Batch Name: "+ editText_batchname.getText().toString()+"\n";

        builder.setMessage(message);

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        mAuth.createUserWithEmailAndPassword(editText_email.getText().toString(), editText_password.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()){
                                            signIn(view);
                                        }
                                        else{
                                            Snackbar.make(view,"Error ! Contact Admin",Snackbar.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                });

        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    void signIn(final View view){
        mAuth.signInWithEmailAndPassword(editText_email.getText().toString(), editText_password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            Map<String, Object> user = new HashMap<>();
                            email = editText_email.getText().toString();
                            user.put("email", editText_email.getText().toString());
                            user.put("enrollmentno", editText_enrollmentno.getText().toString());
                            user.put("password", editText_password.getText().toString());
                            user.put("classname", editText_classname.getText().toString());
                            user.put("batchname", editText_batchname.getText().toString());

                            db.collection("users")
                                    .document(email)
                                    .set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Snackbar.make(view,"DETAILS ADDED",Snackbar.LENGTH_SHORT).show();
                                            nextactivity = new Intent(MainActivity.this,Main2Activity.class);
                                            startActivity(nextactivity);
                                            finish();
                                        }
                                    });
                        }
                        else{
                            Snackbar.make(view,"Error ! Contact Admin",Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });

    }


}
