package coer.notification.ui.post;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import coer.notification.R;
import coer.notification.ui.adupter.postaduptor;
import coer.notification.ui.model.postmodel;


public class post_fragment extends Fragment {
    RecyclerView postrv;
    ArrayList<postmodel> postlist;
    ExtendedFloatingActionButton addpostbtn;
    ConstraintLayout lh;
    FirebaseAuth auth;
    FirebaseDatabase database;
FirebaseStorage storage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_fragment, container, false);


        postrv = view.findViewById(R.id.prv);
        addpostbtn = view.findViewById(R.id.addpost);
        lh = getActivity().findViewById(R.id.layoutheader);
        postlist = new ArrayList<>();
        database =FirebaseDatabase.getInstance();
        storage =FirebaseStorage.getInstance();
        auth =FirebaseAuth.getInstance();

        postaduptor postaduptor = new postaduptor(postlist, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postrv.setLayoutManager(linearLayoutManager);
        postrv.setNestedScrollingEnabled(false);
        postrv.setAdapter(postaduptor);



        database.getReference().child("posts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot :snapshot.getChildren()){
                    postmodel postmodel= dataSnapshot.getValue(coer.notification.ui.model.postmodel.class);
                    postlist.add(postmodel);
                }
                postaduptor.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        addpostbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setReorderingAllowed(true);

                transaction.replace(R.id.frame, addpostfragment.class, null);

                transaction.commit();
            }
        });

        return view;


    }

    @Override
    public void onStart() {
        super.onStart();
        lh.setVisibility(View.GONE);
         FirebaseDatabase.getInstance().getReference().child("faculty").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {

                 if (snapshot.exists()) {
                     if (snapshot.hasChild("about")) {
                         if (snapshot.child("about").getValue().toString().equals("Faculty")){
                             addpostbtn.setVisibility(View.VISIBLE);

                         }
                 }}

             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });

    }

    @Override
    public void onStop() {
        super.onStop();
        lh.setVisibility(View.VISIBLE);
    }

}