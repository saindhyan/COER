package coer.notification.ui.post;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

import coer.notification.R;
import coer.notification.ui.model.postmodel;

import static android.app.Activity.RESULT_OK;

public class addpostfragment extends Fragment {

    ConstraintLayout layoutheader;
    EditText postcaption;
    Button postbtn;
    ImageView addimage, postimg, fimage, close,removeimg;
    Uri uri;
    FirebaseAuth auth;
    FirebaseDatabase database;
    TextView fname, about;
    FirebaseStorage storage;
    ProgressDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        dialog = new ProgressDialog(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_addpostfragment, container, false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Uploading");
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);


        fname = view.findViewById(R.id.fname);
        about = view.findViewById(R.id.about);
        fimage = view.findViewById(R.id.fimage);
        close = view.findViewById(R.id.close);
        removeimg = view.findViewById(R.id.removeimg);

        database.getReference().child("faculty").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.hasChild("fname")) {
                        fname.setText(snapshot.child("fname").getValue().toString());
                    }
                    if (snapshot.hasChild("about")) {
                        about.setText(snapshot.child("about").getValue().toString());
                    }
                    if (snapshot.hasChild("fname")) {
                        Glide.with(getContext()).load(snapshot.child("fimage").getValue().toString()).into(fimage);
                    }
                } else {
                    postbtn.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        layoutheader = getActivity().findViewById(R.id.layoutheader);
        postcaption = view.findViewById(R.id.caption);
        addimage = view.findViewById(R.id.addimagebtn);
        postbtn = view.findViewById(R.id.postbtn);
        postimg = view.findViewById(R.id.postimg);
        postcaption.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String caption = postcaption.getText().toString();

                if (!caption.isEmpty()) {
                    FirebaseDatabase.getInstance().getReference().child("faculty").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.exists()) {
                                if (snapshot.hasChild("about")) {
                                    if (snapshot.child("about").getValue().toString().equals("Faculty")) {
                                        postbtn.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.rectengularbackground));
                                        postbtn.setTextColor(getContext().getResources().getColor(R.color.white));
                                        postbtn.setEnabled(true);
                                    } else {
                                        Toast.makeText(getContext(), "You don't have permision to publish", Toast.LENGTH_SHORT).show();
                                    }
                                }else {

                                }
                            }else {

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                } else {
                    if (postimg.getVisibility() == View.GONE) {
                        postbtn.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.whitewithroundcorner));
                        postbtn.setTextColor(getContext().getResources().getColor(R.color.disabledbuttontext));
                        postbtn.setEnabled(false);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        addimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1001);

            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setReorderingAllowed(true);

                transaction.replace(R.id.frame, post_fragment.class, null);

                transaction.commit();
            }
        });

        removeimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postimg.setImageURI(null);
                uri=null;
                postimg.setVisibility(View.GONE);
                removeimg.setVisibility(View.GONE);

                String caption = postcaption.getText().toString();

                if (caption.isEmpty()) {
                    postbtn.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.whitewithroundcorner));
                    postbtn.setTextColor(getContext().getResources().getColor(R.color.disabledbuttontext));
                    postbtn.setEnabled(false);
                }
            }
        });
        postbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uri != null) {
                    dialog.show();

                    final StorageReference reference = storage.getReference().child("posts")
                            .child(FirebaseAuth.getInstance().getUid())
                            .child(new Date().getTime() + "");
                    reference.putFile(uri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            postmodel post = new postmodel();
                                            post.setPostimage(uri.toString());
                                            post.setPostedbny(FirebaseAuth.getInstance().getUid());
                                            post.setCaption(postcaption.getText().toString());
                                            post.setDt(new Date().getTime());

                                            database.getReference().child("posts").push().setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    dialog.dismiss();
                                                    Toast.makeText(getContext(), "Posted", Toast.LENGTH_SHORT).show();

                                                    FragmentTransaction transaction = getFragmentManager().beginTransaction();

                                                    transaction.replace(R.id.frame, post_fragment.class, null);

                                                    transaction.commit();
                                                }
                                            });
                                        }
                                    });

                                }
                            });
                } else {
                    postmodel post = new postmodel();
                    post.setPostedbny(FirebaseAuth.getInstance().getUid());
                    post.setCaption(postcaption.getText().toString());
                    post.setDt(new Date().getTime());

                    database.getReference().child("posts").push().setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            dialog.dismiss();
                            Toast.makeText(getContext(), "Posted", Toast.LENGTH_SHORT).show();
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();

                            transaction.replace(R.id.frame, post_fragment.class, null);

                            transaction.commit();
                        }
                    });

                }
            }

        });
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        layoutheader.setVisibility(View.GONE);

    }

    @Override
    public void onStop() {
        super.onStop();
        layoutheader.setVisibility(View.VISIBLE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            uri = data.getData();
            postimg.setImageURI(uri);
            postimg.setVisibility(View.VISIBLE);
            removeimg.setVisibility(View.VISIBLE);

            FirebaseDatabase.getInstance().getReference().child("faculty").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists()) {
                        if (snapshot.hasChild("about")) {
                            if (snapshot.child("about").getValue().toString().equals("Faculty")) {
                                postbtn.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.rectengularbackground));
                                postbtn.setTextColor(getContext().getResources().getColor(R.color.white));
                                postbtn.setEnabled(true);
                            } else {
                                Toast.makeText(getContext(), "You don't have permision to publish", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "You don't have permision to publish", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "You don't have permision to publish", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }
}