package coer.notification;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class needhelp extends AppCompatActivity {

    Button btnblt;
    EditText coerid;
    DatabaseReference reference;
    String uuserid;
    ProgressBar pbarnh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.need_help);
        btnblt = findViewById(R.id.btndlt);
        coerid = findViewById(R.id.coerid);
        pbarnh = findViewById(R.id.pbarnh);


        reference = FirebaseDatabase.getInstance().getReference("alreadyincollege");


        btnblt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbarnh.setVisibility(View.VISIBLE);
                btnblt.setVisibility(View.INVISIBLE);
                deleteuser();
            }
        });
    }

    private void deleteuser() {
        uuserid = coerid.getText().toString();

        if (TextUtils.isEmpty(uuserid)) {
            coerid.setError("Coer id cannot empty");
            coerid.requestFocus();
            pbarnh.setVisibility(View.INVISIBLE);
            btnblt.setVisibility(View.VISIBLE);

        } else {


            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            ref.child("alreadyincollege").child(uuserid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        String userid = snapshot.child("ucoerid")
                                .getValue(String.class);
                        if (userid.equals(uuserid)) {
                            reference.child(uuserid).removeValue()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    pbarnh.setVisibility(View.INVISIBLE);
                                    btnblt.setVisibility(View.VISIBLE);
                                    Toast.makeText(needhelp.this, "DONE! YOU NEED TO SIGNUP AGAIN", Toast.LENGTH_SHORT).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pbarnh.setVisibility(View.INVISIBLE);
                                    btnblt.setVisibility(View.VISIBLE);
                                    Toast.makeText(needhelp.this, e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            pbarnh.setVisibility(View.INVISIBLE);
                            btnblt.setVisibility(View.VISIBLE);
                            Toast.makeText(needhelp.this, "YOU DON'T HAVE ANY ACCOUNT WITH THIS COER ID", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        pbarnh.setVisibility(View.INVISIBLE);
                        btnblt.setVisibility(View.VISIBLE);
                        Toast.makeText(needhelp.this, "YOU DON'T HAVE ANY ACCOUNT WITH THIS COER ID", Toast.LENGTH_SHORT).show();


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                    pbarnh.setVisibility(View.INVISIBLE);
                    btnblt.setVisibility(View.VISIBLE);

                }
            });


        }
    }
}