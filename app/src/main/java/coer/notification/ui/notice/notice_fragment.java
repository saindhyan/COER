package coer.notification.ui.notice;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import coer.notification.R;
import coer.notification.adapter2;
import coer.notification.model;
import coer.notification.model2;
import coer.notification.myadapter;

public class notice_fragment extends Fragment {

    RecyclerView noticerec,infrec;
    myadapter adapter;
    coer.notification.adapter2 adapter2;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notice_fragment, container, false);

        noticerec = (RecyclerView) view.findViewById(R.id.noticerec);
        infrec = (RecyclerView) view.findViewById(R.id.infocusrec);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        noticerec.setLayoutManager(linearLayoutManager);

        FirebaseRecyclerOptions<model> options =
                new FirebaseRecyclerOptions.Builder<model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("all notice"), model.class)
                        .build();
        adapter = new myadapter(options);
        noticerec.setAdapter(adapter);


        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext());
        linearLayoutManager2.setReverseLayout(true);
        linearLayoutManager2.setStackFromEnd(true);
        infrec.setLayoutManager(linearLayoutManager2);



        FirebaseRecyclerOptions<model2> options2 =
                new FirebaseRecyclerOptions.Builder<model2>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("infocus"), model2.class)
                        .build();
        adapter2 = new adapter2(options2);
        infrec.setAdapter(adapter2);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
        adapter2.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
        adapter2.stopListening();
    }

}