package coer.notification.ui.profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import coer.notification.MainActivity;
import coer.notification.R;
import coer.notification.login;
import coer.notification.logincategory;

import static android.app.Activity.RESULT_OK;


public class profile_fragment extends Fragment {


    ImageView uimage, cover;
    TextView uname, unumber, uemail, ucity, course,coerid;
    private Uri filePath;
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference dbreference;
    Button logout;
    FirebaseAuth mAuth;
    ConstraintLayout layouthrader;

    String ugender = "";
    String UserID = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_fragment, container, false);
        uimage = view.findViewById(R.id.uimage);
        uname = view.findViewById(R.id.uname);
        unumber = view.findViewById(R.id.unumber);
        cover = view.findViewById(R.id.header_cover_image);
        uemail = view.findViewById(R.id.uemail);
        ucity = view.findViewById(R.id.city);
        course = view.findViewById(R.id.course);
        coerid = view.findViewById(R.id.coerid);
        layouthrader = getActivity().findViewById(R.id.layoutheader);
        // timer = findViewById(R.id.timer);
        logout = view.findViewById(R.id.btnlogout);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getProviderId();
        UserID = user.getUid();




        UserID = user.getUid();
        FirebaseDatabase.getInstance().getReference().child("newuser").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(UserID)) {
                    dbreference = FirebaseDatabase.getInstance().getReference().child("newuser");


                } else {
                    dbreference = FirebaseDatabase.getInstance().getReference().child("coeruserdetails");
                    uimage.setEnabled(false);
                    uname.setEnabled(false);
                    unumber.setEnabled(false);
                    ucity.setEnabled(false);
                    uemail.setEnabled(false);
                    coerid.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 mAuth.signOut();
                startActivity(new Intent(getActivity(), logincategory.class));
                getActivity().finish();

            }
        });
//dialogbox
        unumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final DialogPlus dialogPlus = DialogPlus.newDialog(uimage.getContext())
                        .setContentHolder(new ViewHolder(R.layout.dialogcontent))
                        .setExpanded(true, 400)
                        .create();

                dialogPlus.show();

                View myview = dialogPlus.getHolderView();
                EditText number = myview.findViewById(R.id.enterdetails);
                Button btnsaveinfo = myview.findViewById(R.id.btnsubmit);


                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                UserID = user.getUid();
                dbreference.child(UserID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild("phonenumber")) {
                            number.setText(snapshot.child("uphonenumber").getValue().toString());

                        } else {

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {


                    }
                });
                dialogPlus.show();
                btnsaveinfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mnumber = number.getText().toString();
                        if (mnumber.startsWith("+91")) {
                            number.setError("Remove +91");
                            number.requestFocus();
                        } else {
                            updatenumber();
                            dialogPlus.dismiss();
                            unumber.setEnabled(false);
                            new CountDownTimer(86400000, 1000) {

                                public void onTick(long millisUntilFinished) {
                                    //  timer.setText("seconds remaining: " + millisUntilFinished / 1000);
                                    //here you can have your logic to set text to edittext
                                }

                                public void onFinish() {
                                    //    mTextField.setText("done!");
                                    unumber.setEnabled(true);
                                }

                            }.start();
                        }
                    }

                    private void updatenumber() {


                        final Map<String, Object> map = new HashMap<>();
                        map.put("uphonenumber", number.getText().toString());
                        dbreference.child(UserID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists())
                                    dbreference.child(UserID).updateChildren(map);
                                else
                                    dbreference.child(UserID).setValue(map);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });

                        Toast.makeText(getContext(), "Updated Successfully", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
        uname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(uimage.getContext())
                        .setContentHolder(new ViewHolder(R.layout.dialogcontent2))
                        .setExpanded(true, 400)
                        .create();

                dialogPlus.show();

                View myview = dialogPlus.getHolderView();
                EditText name = myview.findViewById(R.id.enternamedetails);
                Button btnnamesaveinfo = myview.findViewById(R.id.btnnamesubmit);

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                UserID = user.getUid();
                dbreference.child(UserID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild("uname")) {
                            name.setText(snapshot.child("uname").getValue().toString());

                        } else {

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {


                    }
                });
                dialogPlus.show();
                btnnamesaveinfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updatename();
                        dialogPlus.dismiss();
                        uname.setEnabled(false);
                        new CountDownTimer(86400000, 1000) {

                            public void onTick(long millisUntilFinished) {
                                //  timer.setText("seconds remaining: " + millisUntilFinished / 1000);
                                //here you can have your logic to set text to edittext
                            }

                            public void onFinish() {
                                //    mTextField.setText("done!");
                                uname.setEnabled(true);
                            }

                        }.start();
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
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });

                        Toast.makeText(getContext(), "Updated Successfully", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


        uemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(uimage.getContext())
                        .setContentHolder(new ViewHolder(R.layout.dialogcontentemail))
                        .setExpanded(true, 400)
                        .create();

                dialogPlus.show();

                View myview = dialogPlus.getHolderView();
                EditText email = myview.findViewById(R.id.enteremail);
                Button btnsaveinfo = myview.findViewById(R.id.btnemailsubmit);


                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                UserID = user.getUid();
                dbreference.child(UserID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild("uemail")) {
                            email.setText(snapshot.child("uemail").getValue().toString());

                        } else {

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {


                    }
                });
                dialogPlus.show();
                btnsaveinfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateemail();
                        dialogPlus.dismiss();
                        uemail.setEnabled(false);
                        new CountDownTimer(86400000, 1000) {

                            public void onTick(long millisUntilFinished) {
                                //  timer.setText("seconds remaining: " + millisUntilFinished / 1000);
                                //here you can have your logic to set text to edittext
                            }

                            public void onFinish() {
                                //    mTextField.setText("done!");
                                uemail.setEnabled(true);
                            }

                        }.start();
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
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });

                        Toast.makeText(getContext(), "Updated Successfully", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        ucity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(uimage.getContext())
                        .setContentHolder(new ViewHolder(R.layout.dialogcontentcity))
                        .setExpanded(true, 400)
                        .create();

                dialogPlus.show();

                View myview = dialogPlus.getHolderView();
                EditText city = myview.findViewById(R.id.enterdetails);
                Button btnsubmit = myview.findViewById(R.id.btnsubmit);


                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                UserID = user.getUid();
                dbreference.child(UserID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild("ucity")) {
                            city.setText(snapshot.child("ucity").getValue().toString());

                        } else {

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {


                    }
                });
                dialogPlus.show();
                btnsubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updatecity();
                        dialogPlus.dismiss();
                        ucity.setEnabled(false);
                        new CountDownTimer(86400000, 1000) {

                            public void onTick(long millisUntilFinished) {
                                //  timer.setText("seconds remaining: " + millisUntilFinished / 1000);
                                //here you can have your logic to set text to edittext
                            }

                            public void onFinish() {
                                //    mTextField.setText("done!");
                                ucity.setEnabled(true);
                            }

                        }.start();
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
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });

                        Toast.makeText(getContext(), "Updated Successfully", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


       /* course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(uimage.getContext())
                        .setContentHolder(new ViewHolder(R.layout.dialogcontentcourse))
                        .setExpanded(true, 400)
                        .create();

                dialogPlus.show();
                View myview = dialogPlus.getHolderView();
                RadioGroup gender = myview.findViewById(R.id.gendergp);
                RadioButton male = myview.findViewById(R.id.male);
                RadioButton female = myview.findViewById(R.id.female);
                Button btnsaveinfo = myview.findViewById(R.id.btnsubmit);


                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                UserID = user.getUid();
                dbreference.child(UserID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild("ugender")) {
                            if (ugender == ("Male")) {
                                gender.check(R.id.male);
                            } else if (ugender == "Female") {
                                gender.check(R.id.female);
                            }
                        } else {

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {


                    }
                });
                dialogPlus.show();
                btnsaveinfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (male.isChecked()) {
                            ugender = "Male";
                        }
                        if (female.isChecked()) {
                            ugender = "Female";
                        }
                        updategender();
                        dialogPlus.dismiss();
                        gendertv.setEnabled(false);
                        new CountDownTimer(86400000, 1000) {

                            public void onTick(long millisUntilFinished) {
                                //  timer.setText("seconds remaining: " + millisUntilFinished / 1000);
                                //here you can have your logic to set text to edittext
                            }

                            public void onFinish() {
                                //    mTextField.setText("done!");
                                gendertv.setEnabled(true);
                            }

                        }.start();
                    }

                    private void updategender() {
                        final Map<String, Object> map = new HashMap<>();
                        map.put("ugender", ugender.toString());
                        dbreference.child(UserID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists())
                                    dbreference.child(UserID).updateChildren(map);
                                else
                                    dbreference.child(UserID).setValue(map);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });

                        Toast.makeText(getContext(), "Updated Successfully", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        */


        uimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();

            }

            private void chooseImage() {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Profile Picture"), 101);

            }

        });
        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosecoverImage();
            }

            private void choosecoverImage() {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Profile Picture"), 201);

            }

        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filePath);
                uimage.setImageBitmap(bitmap);
                uploadImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
        }
        if (requestCode == 201 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filePath);
                cover.setImageBitmap(bitmap);
                uploadcoverImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {

        if (filePath != null) {
            final ProgressDialog pd = new ProgressDialog(getContext());
            pd.setTitle("File Uploader");
            pd.show();

            StorageReference ref = storageReference.child("profileimages/" + "img" + System.currentTimeMillis());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final Map<String, Object> map = new HashMap<>();
                                    map.put("uimage", uri.toString());
                                    Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                                    dbreference.child(UserID).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                dbreference.child(UserID).updateChildren(map);
                                                uimage.setEnabled(false);
                                                new CountDownTimer(86400000, 1000) {

                                                    public void onTick(long millisUntilFinished) {
                                                        //  timer.setText("seconds remaining: " + millisUntilFinished / 1000);
                                                        //here you can have your logic to set text to edittext
                                                    }

                                                    public void onFinish() {
                                                        //    mTextField.setText("done!");
                                                        uimage.setEnabled(true);
                                                    }

                                                }.start();
                                            } else
                                                dbreference.child(UserID).setValue(map);
                                            uimage.setEnabled(false);
                                            new CountDownTimer(86400000, 1000) {

                                                public void onTick(long millisUntilFinished) {
                                                    //  timer.setText("seconds remaining: " + millisUntilFinished / 1000);
                                                    //here you can have your logic to set text to edittext
                                                }

                                                public void onFinish() {
                                                    //    mTextField.setText("done!");
                                                    uimage.setEnabled(true);
                                                }

                                            }.start();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    pd.dismiss();
                                    Toast.makeText(getContext(), "Updated Successfully", Toast.LENGTH_LONG).show();
                                }

                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(getContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            pd.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }

    }

    private void uploadcoverImage() {

        if (filePath != null) {
            final ProgressDialog pd = new ProgressDialog(getContext());
            pd.setTitle("File Uploader");
            pd.show();


            StorageReference ref = storageReference.child("ucoverimages/" + "img" + System.currentTimeMillis());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final Map<String, Object> map = new HashMap<>();
                                    map.put("ucoverimage", uri.toString());
                                    Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                                    dbreference.child(UserID).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                dbreference.child(UserID).updateChildren(map);
                                                cover.setEnabled(false);
                                                new CountDownTimer(86400000, 1000) {

                                                    public void onTick(long millisUntilFinished) {
                                                        //  timer.setText("seconds remaining: " + millisUntilFinished / 1000);
                                                        //here you can have your logic to set text to edittext
                                                    }

                                                    public void onFinish() {
                                                        //    mTextField.setText("done!");
                                                        cover.setEnabled(true);
                                                    }

                                                }.start();
                                            } else
                                                dbreference.child(UserID).setValue(map);
                                            cover.setEnabled(false);
                                            new CountDownTimer(86400000, 1000) {

                                                public void onTick(long millisUntilFinished) {
                                                    //  timer.setText("seconds remaining: " + millisUntilFinished / 1000);
                                                    //here you can have your logic to set text to edittext
                                                }

                                                public void onFinish() {
                                                    //    mTextField.setText("done!");
                                                    cover.setEnabled(true);
                                                }

                                            }.start();

                                        }


                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    pd.dismiss();
                                    Toast.makeText(getContext(), "Updated Successfully", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(getContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            pd.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        layouthrader.setVisibility(View.GONE);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserID = user.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("coeruserdetails");
        ref.child(UserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.hasChild("uname")) {
                        uname.setText(snapshot.child("uname").getValue().toString());
                    }
                    if (snapshot.hasChild("uphonenumber")) {
                        unumber.setText(snapshot.child("uphonenumber").getValue().toString());
                    }
                    if (snapshot.hasChild("uemail")) {
                        uemail.setText(snapshot.child("uemail").getValue().toString());
                    }
                    if (snapshot.hasChild("ucity")) {
                        ucity.setText(snapshot.child("ucity").getValue().toString());
                    } if (snapshot.hasChild("ucoerid")) {
                        coerid.setText(snapshot.child("ucoerid").getValue().toString());
                    }
                    if (snapshot.hasChild("ucourse")) {
                        course.setText(snapshot.child("ucourse").getValue().toString());
                    }
                    if (snapshot.hasChild("uimage")) {
                        Glide.with(getContext()).load(snapshot.child("uimage").getValue().toString()).into(uimage);
                    }
                    if (snapshot.hasChild("ucoverimage")) {
                        Glide.with(getContext()).load(snapshot.child("ucoverimage").getValue().toString()).into(cover);
                    } else {
                    }
                } else {
                    updateprofiledetails();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void updateprofiledetails() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserID = user.getUid();
        dbreference.child(UserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.hasChild("uname")) {
                        uname.setText(snapshot.child("uname").getValue().toString());
                    }
                    if (snapshot.hasChild("uphonenumber")) {
                        unumber.setText(snapshot.child("uphonenumber").getValue().toString());
                    }
                    if (snapshot.hasChild("uemail")) {
                        uemail.setText(snapshot.child("uemail").getValue().toString());
                    }
                    if (snapshot.hasChild("ucity")) {
                        ucity.setText(snapshot.child("ucity").getValue().toString());
                    }
                    if (snapshot.hasChild("ucourse")) {
                        course.setText(snapshot.child("ucourse").getValue().toString());
                    }
                    if (snapshot.hasChild("uimage")) {
                        Glide.with(getContext()).load(snapshot.child("uimage").getValue().toString()).into(uimage);
                    }
                    if (snapshot.hasChild("ucoverimage")) {
                        Glide.with(getContext()).load(snapshot.child("ucoverimage").getValue().toString()).into(cover);
                    }

                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        layouthrader.setVisibility(View.VISIBLE);
    }
}