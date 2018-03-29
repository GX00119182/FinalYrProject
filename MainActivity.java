package com.example.gavin_000.fareevasionappnew;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.gavin_000.fareevasionappnew.models.Offender;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //form input fields
    TextView tvOpenCamera;
    private TextView offenderDetails;
    private EditText editTextFName;
    private EditText editTextLName;
    private EditText editTextAddress;
    private EditText editTextDOB;
    private EditText editTextPhone;
    private EditText editTextEmail;
    private EditText editTextStopName;
    private Button subButton;
    private String offenderId;

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    private final int CAMERA_RESULT = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        tvOpenCamera = (TextView) findViewById(R.id.tvOpenCamera);
        tvOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakenPictureIntent();
                } else {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                        Toast.makeText(getApplicationContext(), "Permission needed", Toast.LENGTH_LONG).show();
                    }
                    requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_RESULT);
                }
            }

        });

        editTextFName = (EditText) findViewById(R.id.editFirstName);
        editTextLName = (EditText) findViewById(R.id.editLastName);
        editTextAddress = (EditText) findViewById(R.id.editAddress);
        editTextDOB = (EditText) findViewById(R.id.editDateOfBirth);
        editTextPhone = (EditText) findViewById(R.id.editPhone);
        editTextEmail = (EditText) findViewById(R.id.editEmail);
        editTextStopName = (EditText) findViewById(R.id.editStopName);
        subButton = (Button) findViewById(R.id.subButton);

        mFirebaseInstance = FirebaseDatabase.getInstance();

        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("offenders");

        // store app title to 'app_title' node
        mFirebaseInstance.getReference("app_title").setValue("Realtime Database");


        subButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String firstName = editTextFName.getText().toString();
                String lastName = editTextLName.getText().toString();
                String addressIn = editTextAddress.getText().toString();
                String dobIn = editTextDOB.getText().toString();
                String phoneIn = editTextPhone.getText().toString();
                String emailIn = editTextEmail.getText().toString();
                String stopIn = editTextStopName.getText().toString();

                // Check for already existed userId
                if (TextUtils.isEmpty(offenderId)) {
                    createOffender(firstName,lastName,addressIn,dobIn,phoneIn,emailIn,stopIn);
                } else {
                    Toast.makeText(getApplicationContext(), "User Already Exists", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void createOffender(String fname, String lname,String address,String dob,String phone,String email, String stopName)
    {
        if (TextUtils.isEmpty(offenderId)) {
            offenderId = mFirebaseDatabase.push().getKey();
        }

        Offender offender = new Offender(fname, lname, address,dob,phone,email,stopName);

        mFirebaseDatabase.child(offenderId).setValue(offender);

        addOffenderChangeListener();
    }

    /**
     * User data change listener
     */
    private void addOffenderChangeListener()
    {
        // User data change listener
        mFirebaseDatabase.child(offenderId).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Offender offender = dataSnapshot.getValue(Offender.class);

                // Check for null
                if (offender == null)
                {
                    Toast.makeText(getApplicationContext(), "Offender data is empty", Toast.LENGTH_LONG).show();
                    return;
                }

                Toast.makeText(getApplicationContext(), "Offender data was changed", Toast.LENGTH_LONG).show();
                // clear edit text
                editTextFName.setText("");
                editTextLName.setText("");
                editTextAddress.setText("");
                editTextDOB.setText("");
                editTextPhone.setText("");
                editTextEmail.setText("");
                editTextStopName.setText("");
            }

            @Override
            public void onCancelled(DatabaseError error)
            {
                // Failed to read value
                Toast.makeText(getApplicationContext(), "Offender data input failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void dispatchTakenPictureIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(intent, CAMERA_RESULT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode == RESULT_OK)
        {
            if(requestCode == CAMERA_RESULT)
            {
                Bundle extras = data.getExtras();
                Bitmap bitmap =  (Bitmap) extras.get("data");
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if(requestCode == CAMERA_RESULT)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
            {
                dispatchTakenPictureIntent();
            }
            else{
                Toast.makeText(getApplicationContext(),"Permission needed", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            super.onRequestPermissionsResult(requestCode, permissions,grantResults);
        }
    }






}
