package coer.notification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class loginwithcoerid extends AppCompatActivity {

    Button signupwithcoerid;
    EditText coerid, dob;
    private DatabaseReference reference;
    Button login;
    String ucoerid, udob;
    private FirebaseAuth auth;
    ProgressBar pbarcidl, pbarcids,pbarh;
    TextView help;


    @Override
    protected void onStop() {
        super.onStop();
        pbarcids.setVisibility(View.INVISIBLE);
       // signupwithcoerid.setVisibility(View.VISIBLE);
        pbarcidl.setVisibility(View.INVISIBLE);
        login.setVisibility(View.VISIBLE);
        pbarh.setVisibility(View.INVISIBLE);
//        help.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginwithcoerid);
       // signupwithcoerid = findViewById(R.id.signupbtn);
        coerid = findViewById(R.id.coerid);
        dob = findViewById(R.id.dob);
        pbarcidl = findViewById(R.id.pbarcidl);
        pbarcids = findViewById(R.id.pbarcids);
        pbarh = findViewById(R.id.pbarh);
    //    help = findViewById(R.id.help);

        login = findViewById(R.id.login);

        auth = FirebaseAuth.getInstance();

        reference = FirebaseDatabase.getInstance().getReference("alreadyincollege");

//        signupwithcoerid.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pbarcids.setVisibility(View.VISIBLE);
//                signupwithcoerid.setVisibility(View.INVISIBLE);
//                Intent intent = new Intent(getApplicationContext(), signupwithcoerid.class);
//                startActivity(intent);
//            }
//        });
//        help.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pbarh.setVisibility(View.VISIBLE);
//                help.setVisibility(View.INVISIBLE);
//                Intent intent = new Intent(getApplicationContext(), needhelp.class);
//                startActivity(intent);
//            }
//        });

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

                    if (clean.length() < 8) {
                        clean = clean + ddmmyyyy.substring(clean.length());
                    } else {
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day = Integer.parseInt(clean.substring(0, 2));
                        int mon = Integer.parseInt(clean.substring(2, 4));
                        int year = Integer.parseInt(clean.substring(4, 8));

                        if (mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon - 1);

                        year = (year < 1900) ? 1900 : (year > 2100) ? 2100 : year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum(Calendar.DATE) : day;
                        clean = String.format("%02d%02d%02d", day, mon, year);
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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbarcidl.setVisibility(View.VISIBLE);
                login.setVisibility(View.INVISIBLE);
                ucoerid = coerid.getText().toString();
                udob = dob.getText().toString();

                if (TextUtils.isEmpty(ucoerid)) {
                    coerid.setError("coerid cannt be empty");
                    coerid.requestFocus();
                    pbarcidl.setVisibility(View.INVISIBLE);
                    login.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(udob)) {
                    dob.setError("Date of birth cannt be empty");
                    dob.requestFocus();
                    pbarcidl.setVisibility(View.INVISIBLE);
                    login.setVisibility(View.VISIBLE);
                } else {
                    reference.child(ucoerid)
                            .addListenerForSingleValueEvent(listener);

                }
            }
        });

    }

    ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()) {
                String dob = snapshot.child("udob")
                        .getValue(String.class);

                String email = snapshot.child("uemail").getValue(String.class);
                if (dob.equals(udob)) {

                    auth.signInWithEmailAndPassword(email, dob)
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pbarcidl.setVisibility(View.INVISIBLE);
                                    login.setVisibility(View.VISIBLE);
                                    Toast.makeText(loginwithcoerid.this, e.toString(), Toast.LENGTH_SHORT).show();

                                }
                            })
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Intent intent = new Intent(loginwithcoerid.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });

                } else {
                    Toast.makeText(getApplicationContext(), "INCORRECT DATE OF BIRTH ", Toast.LENGTH_SHORT).show();
                    pbarcidl.setVisibility(View.INVISIBLE);
                    login.setVisibility(View.VISIBLE);
                }
            } else {
                Toast.makeText(getApplicationContext(), "INCORRECT COER ID", Toast.LENGTH_SHORT).show();
                pbarcidl.setVisibility(View.INVISIBLE);
                login.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            pbarcidl.setVisibility(View.INVISIBLE);
            login.setVisibility(View.VISIBLE);
            Toast.makeText(loginwithcoerid.this, error.toString(), Toast.LENGTH_SHORT).show();
        }
    };
}