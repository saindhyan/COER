package coer.notification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class login extends AppCompatActivity {

    Button sendotp, googlesignin, forfaculty;
    EditText etphone;
    ProgressBar pbar, pbargm, pbarf;
    private static final String TAG = "my tag";
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 124;
    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etphone = findViewById(R.id.etPhone);
        sendotp = findViewById(R.id.sendotp);
        googlesignin = findViewById(R.id.googlesignin);
        pbar = findViewById(R.id.pbar);
        pbargm = findViewById(R.id.pbargm);
        forfaculty = findViewById(R.id.forfaculty);
        pbarf = findViewById(R.id.pbarf);


        forfaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbar.setVisibility(View.VISIBLE);
                forfaculty.setVisibility(View.INVISIBLE);
                Toast.makeText(login.this, "COMING SOON", Toast.LENGTH_SHORT).show();
                pbar.setVisibility(View.INVISIBLE);
                forfaculty.setVisibility(View.VISIBLE);

            }
        });


        mAuth = FirebaseAuth.getInstance();

        googlesignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbargm.setVisibility(View.VISIBLE);
                googlesignin.setVisibility(View.INVISIBLE);
                signIn();
            }
        });

        //used to keep login
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        if (user != null) {
            // User is signed in
            Intent i = new Intent(login.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        } else {
            // User is signed out
            Log.d(TAG, "onAuthStateChanged:signed_out");

            //Login with number
            sendotp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!etphone.getText().toString().trim().isEmpty()) {
                        if ((etphone.getText().toString().trim()).length() == 10) {
                            pbar.setVisibility(View.VISIBLE);
                            sendotp.setVisibility(View.INVISIBLE);

                            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                    "+91" + etphone.getText().toString(),
                                    60,
                                    TimeUnit.SECONDS,
                                    login.this,
                                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                        @Override
                                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                            pbar.setVisibility(View.GONE);
                                            sendotp.setVisibility(View.VISIBLE);

                                        }

                                        @Override
                                        public void onVerificationFailed(@NonNull FirebaseException e) {
                                            pbar.setVisibility(View.GONE);
                                            sendotp.setVisibility(View.VISIBLE);
                                            Toast.makeText(login.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                        }

                                        @Override
                                        public void onCodeSent(@NonNull String backendotp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                            pbar.setVisibility(View.GONE);
                                            sendotp.setVisibility(View.VISIBLE);

                                            Intent intent = new Intent(getApplicationContext(), manageotp.class);
                                            intent.putExtra("mobile", etphone.getText().toString());
                                            intent.putExtra("backendotp", backendotp);
                                            startActivity(intent);
                                        }
                                    }
                            );
                        } else {
                            Toast.makeText(login.this, "Please Enter Correct number", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(login.this, "Enter mobile number", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }

        //google sign in
        googlesignin();

    }

    private void googlesignin() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        pbargm.setVisibility(View.INVISIBLE);
        googlesignin.setVisibility(View.VISIBLE);

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
                pbargm.setVisibility(View.INVISIBLE);
                googlesignin.setVisibility(View.VISIBLE);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                pbargm.setVisibility(View.INVISIBLE);
                googlesignin.setVisibility(View.VISIBLE);
            }
        }
    }

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            boolean newuser = task.getResult().getAdditionalUserInfo().isNewUser();


                            if (newuser) {
                                // User is signed in
                                // Sign in success, update UI with the signed-in user's information
                                Intent intent = new Intent(getApplicationContext(), logindetailswithemail.class);
                                startActivity(intent);
                                finish();
                                pbargm.setVisibility(View.INVISIBLE);
                                googlesignin.setVisibility(View.VISIBLE);
                            } else {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                                pbargm.setVisibility(View.INVISIBLE);
                                googlesignin.setVisibility(View.VISIBLE);
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            pbargm.setVisibility(View.INVISIBLE);
                            googlesignin.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }
}
