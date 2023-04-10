package coer.notification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class logindetailswithmobile extends AppCompatActivity {
    Button save;
    String UserID = "";
    FirebaseAuth mAuth;
    ProgressBar pbarmd;
    DatabaseReference dbreference;
    EditText email, city, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logindetailswithmobile);
        save = findViewById(R.id.btnsaveinfo);
        email = findViewById(R.id.email);
        city = findViewById(R.id.city);
        name = findViewById(R.id.name);
        pbarmd = findViewById(R.id.pbarmd);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserID = user.getUid();
        dbreference = FirebaseDatabase.getInstance().getReference().child("newuser");

        String[] users = {"M. Tech(CSE)", "M. Tech(ME)", "B Tech (CE)", "B Tech (IT)", "B Tech (CSE)", "B Tech(ME)", "B Tech[EN(EEE)]", "B Tech(ET)", "B Tech(ME)", "B Tech(PPE)", "Diploma(CE)", "Diploma(ME)", "Diploma(EE)", "B.COM.(CFA)", "BBA", "MBA", "PHD", "MCA", "BCA", "BSC", "BFA", "MFA"};
        Spinner spin = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, users);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbarmd.setVisibility(View.VISIBLE);
                save.setVisibility(View.INVISIBLE);
                String uemail = email.getText().toString();
                String uname = name.getText().toString();
                String ucity = city.getText().toString();

                if (TextUtils.isEmpty(uemail)) {
                    email.setError("Email cannot empty");
                    email.requestFocus();
                    pbarmd.setVisibility(View.INVISIBLE);
                    save.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(uname)) {
                    name.setError("name cannt be empty");
                    name.requestFocus();
                    pbarmd.setVisibility(View.INVISIBLE);
                    save.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(ucity)) {
                    city.setError("city cannt be empty");
                    city.requestFocus();
                    pbarmd.setVisibility(View.INVISIBLE);
                    save.setVisibility(View.VISIBLE);
                } else {
                    updateemail();
                    updatecity();
                    updatename();
                    updatecourse();
                    updatenumber();

                }


            }

            private void updateemail() {


                final Map<String, Object> map = new HashMap<>();
                map.put("uemail", email.getText().toString());
                dbreference.child(UserID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())
                            dbreference.child(UserID).updateChildren(map);
                        else
                            dbreference.child(UserID).setValue(map);
                        pbarmd.setVisibility(View.INVISIBLE);
                        save.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        pbarmd.setVisibility(View.INVISIBLE);
                        save.setVisibility(View.VISIBLE);
                    }
                });
            }
            private void updatenumber() {

String mnumwithoucode =user.getPhoneNumber();
                mnumwithoucode.replaceAll("\\s+","").replaceFirst("91","");
                final Map<String, Object> map = new HashMap<>();
                map.put("uphonenumber", mnumwithoucode);
                dbreference.child(UserID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())
                            dbreference.child(UserID).updateChildren(map);
                        else
                            dbreference.child(UserID).setValue(map);
                        pbarmd.setVisibility(View.INVISIBLE);
                        save.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        pbarmd.setVisibility(View.INVISIBLE);
                        save.setVisibility(View.VISIBLE);
                    }
                });

            }

            private void updatename() {


                final Map<String, Object> map = new HashMap<>();
                map.put("uname", name.getText().toString());
                dbreference.child(UserID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())
                            dbreference.child(UserID).updateChildren(map);
                        else
                            dbreference.child(UserID).setValue(map);
                        pbarmd.setVisibility(View.INVISIBLE);
                        save.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        pbarmd.setVisibility(View.INVISIBLE);
                        save.setVisibility(View.VISIBLE);
                    }
                });

            }

            private void updatecourse() {


                final Map<String, Object> map = new HashMap<>();
                map.put("ucourse", spin.getSelectedItem().toString());
                dbreference.child(UserID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())
                            dbreference.child(UserID).updateChildren(map);
                        else
                            dbreference.child(UserID).setValue(map);
                        pbarmd.setVisibility(View.INVISIBLE);
                        save.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        pbarmd.setVisibility(View.INVISIBLE);
                        save.setVisibility(View.VISIBLE);
                    }
                });

            }

            private void updatecity() {


                final Map<String, Object> map = new HashMap<>();
                map.put("ucity", city.getText().toString());
                dbreference.child(UserID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())
                            dbreference.child(UserID).updateChildren(map);
                        else
                            dbreference.child(UserID).setValue(map);
                        pbarmd.setVisibility(View.INVISIBLE);
                        save.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        pbarmd.setVisibility(View.INVISIBLE);
                        save.setVisibility(View.VISIBLE);
                    }
                });

            }

        });
    }
}