package coer.notification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class signupwithcoerid extends AppCompatActivity {

    EditText dob ,coerid,email ,phone,name;
    Spinner spiner;
    Button signup;
    String udob,ucoerid,uemail,uphone,uname,uspiner,usertype="studant";
    private DatabaseReference reference;
    private FirebaseAuth auth ;
    ProgressBar pbarcidd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signupwithcoerid);


reference= FirebaseDatabase.getInstance().getReference("alreadyincollege");

        String[] users = { "M. Tech(CSE)", "M. Tech(ME)", "B Tech (CE)", "B Tech (IT)", "B Tech (CSE)","B Tech(ME)","B Tech[EN(EEE)]","B Tech(ET)","B Tech(ME)","B Tech(PPE)" ,"Diploma(CE)","Diploma(ME)","Diploma(EE)","B.COM.(CFA)","BBA","MBA","PHD","MCA","BCA","BSC","BFA","MFA"};
        Spinner spin = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, users);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);

        auth= FirebaseAuth.getInstance();


        dob = findViewById(R.id.dob);
        coerid = findViewById(R.id.coerid);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        name = findViewById(R.id.name);
        spiner = findViewById(R.id.spinner1);
        signup = findViewById(R.id.signup);
        pbarcidd = findViewById(R.id.pbarcidd);


        dob.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + ddmmyyyy.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day  = Integer.parseInt(clean.substring(0,2));
                        int mon  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        if(mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon-1);

                        year = (year<1900)?1900:(year>2100)?2100:year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d%02d",day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    dob.setText(current);
                    dob.setSelection(sel < current.length() ? sel : current.length());



                }
            }


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbarcidd.setVisibility(View.VISIBLE);
                signup.setVisibility(View.INVISIBLE);
                udob= dob.getText().toString();
                ucoerid = coerid.getText().toString();
                uemail=email.getText().toString();
                uphone= phone.getText().toString();
                uname = name.getText().toString();
                uspiner= spiner.getSelectedItem().toString();


                if(TextUtils.isEmpty(uphone)){
                    phone.setError("Phone number cannot empty");
                    phone.requestFocus();
                    pbarcidd.setVisibility(View.INVISIBLE);
                    signup.setVisibility(View.VISIBLE);
                }else if(TextUtils.isEmpty(uname)){
                    name.setError("name cannt be empty");
                    name.requestFocus();
                    pbarcidd.setVisibility(View.INVISIBLE);
                    signup.setVisibility(View.VISIBLE);
                }else if(TextUtils.isEmpty(uemail)){
                    email.setError("email cannt be empty");
                    email.requestFocus();
                    pbarcidd.setVisibility(View.INVISIBLE);
                    signup.setVisibility(View.VISIBLE);
                }else if(TextUtils.isEmpty(ucoerid)){
                    coerid.setError("coerid cannt be empty");
                    coerid.requestFocus();
                    pbarcidd.setVisibility(View.INVISIBLE);
                    signup.setVisibility(View.VISIBLE);
                }else if(TextUtils.isEmpty(udob)){
                    dob.setError("Date of birth cannt be empty");
                    dob.requestFocus();
                    pbarcidd.setVisibility(View.INVISIBLE);
                    signup.setVisibility(View.VISIBLE);
                }else {

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    ref.child("alreadyincollege").child(ucoerid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                pbarcidd.setVisibility(View.INVISIBLE);
                                signup.setVisibility(View.VISIBLE);
                                        Toast.makeText(getApplicationContext(),"user already exists",Toast.LENGTH_SHORT).show();
                                        Intent intent= new Intent(signupwithcoerid.this,loginwithcoerid.class);
                                        startActivity(intent);
                                        finish();
                            } else {


                        auth.createUserWithEmailAndPassword(uemail,udob)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {

                                        studants s = new studants(auth.getCurrentUser().getUid(),udob,ucoerid,uemail,uphone,uname,uspiner,usertype);
                                        reference.child(ucoerid).setValue(s)
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        pbarcidd.setVisibility(View.INVISIBLE);
                                                        signup.setVisibility(View.VISIBLE);
                                                        Toast.makeText(signupwithcoerid.this, e.toString(),Toast.LENGTH_SHORT ).show();
                                                    }
                                                })
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        pbarcidd.setVisibility(View.INVISIBLE);
                                                        signup.setVisibility(View.VISIBLE);
                                                        sendotp();


                                                    }
                                                });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pbarcidd.setVisibility(View.INVISIBLE);
                                signup.setVisibility(View.VISIBLE);
                                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                            }
                        });

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            pbarcidd.setVisibility(View.INVISIBLE);
                            signup.setVisibility(View.VISIBLE);

                        }
                    });


            }
}
            private void sendotp() {
                if (!phone.getText().toString().trim().isEmpty()) {
                    if ((phone.getText().toString().trim()).length() == 10) {
                        pbarcidd.setVisibility(View.VISIBLE);

                        signup.setVisibility(View.INVISIBLE);

                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                "+91" + phone.getText().toString(),
                                60,
                                TimeUnit.SECONDS,
                                signupwithcoerid.this,
                                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                    @Override
                                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                        signup.setVisibility(View.VISIBLE);
                                        pbarcidd.setVisibility(View.INVISIBLE);


                                    }

                                    @Override
                                    public void onVerificationFailed(@NonNull FirebaseException e) {
                                        signup.setVisibility(View.VISIBLE);
                                        pbarcidd.setVisibility(View.INVISIBLE);

                                        Toast.makeText(signupwithcoerid.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }

                                    @Override
                                    public void onCodeSent(@NonNull String backendotp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                        signup.setVisibility(View.VISIBLE);
                                        pbarcidd.setVisibility(View.INVISIBLE);


                                        Intent intent = new Intent(getApplicationContext(), verifyotp.class);
                                        intent.putExtra("mobile", phone.getText().toString());
                                        intent.putExtra("backendotp", backendotp);
                                        startActivity(intent);
                                    }
                                }
                        );
                    } else {
                        signup.setVisibility(View.VISIBLE);
                        pbarcidd.setVisibility(View.INVISIBLE);
                        Toast.makeText(signupwithcoerid.this, "Please Enter Correct number", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    signup.setVisibility(View.VISIBLE);
                    pbarcidd.setVisibility(View.INVISIBLE);
                    Toast.makeText(signupwithcoerid.this, "Enter mobile number", Toast.LENGTH_SHORT).show();

                }
            }
        });


    }
}