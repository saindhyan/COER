package coer.notification.ui.adupter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import coer.notification.R;
import coer.notification.ui.model.postmodel;

public class postaduptor extends RecyclerView.Adapter<postaduptor.viewHolder> {

    ArrayList<postmodel> list;

    public postaduptor(ArrayList<postmodel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    Context context;

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_rv, parent, false);
        return new viewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        postmodel postmodel = list.get(position);

//        try {

//        }catch ( Exception e){
//            holder.postimage.setVisibility(View.GONE);

//        }
        if (postmodel.getPostimage()==null){
            holder.postimage.setVisibility(View.GONE);
        }else {
            Picasso.get()
                    .load(postmodel.getPostimage())
                    .placeholder(R.drawable.placeholder)
                    .into(holder.postimage);
            holder.postimage.setVisibility(View.VISIBLE);

        }
        holder.postimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Dialog builder = new Dialog(context);
                    builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    builder.getWindow().setBackgroundDrawable(
                            new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            //nothing;
                        }
                    });
                ImageView imageView = new ImageView(context);
imageView.setImageDrawable(Drawable.createFromPath("https://firebasestorage.googleapis.com/v0/b/coer-notification.appspot.com/o/profileimages%2Fimg1628934644581?alt=media&token=4daf2055-fd64-42e3-b496-201808108233"));
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
                    builder.show();
            }
        });

        holder.caption.setText(postmodel.getCaption());

        FirebaseDatabase.getInstance().getReference().child("faculty")
                .child(postmodel.getPostedbny()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.hasChild("fname")) {
                        holder.name.setText(snapshot.child("fname").getValue().toString());
                    }
                    if (snapshot.hasChild("about")) {
                        holder.about.setText(snapshot.child("about").getValue().toString());
                    }
                    if (snapshot.hasChild("fname")) {
                        Glide.with(context).load(snapshot.child("fimage").getValue().toString()).into(holder.profile);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView profile, postimage, like, comment, share;
        TextView name, about, likecount, commentcount, dt, caption;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            profile = itemView.findViewById(R.id.fimage);
            postimage = itemView.findViewById(R.id.postimage);
            like = itemView.findViewById(R.id.likebuton);
            comment = itemView.findViewById(R.id.commentbuton);
            share = itemView.findViewById(R.id.share);
            name = itemView.findViewById(R.id.fname);
            about = itemView.findViewById(R.id.fabout);
            likecount = itemView.findViewById(R.id.likecount);
            commentcount = itemView.findViewById(R.id.commentcount);
            dt = itemView.findViewById(R.id.dt);
            caption = itemView.findViewById(R.id.captiontv);
        }


    }


}
