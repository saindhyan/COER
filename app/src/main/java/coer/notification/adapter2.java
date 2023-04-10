package coer.notification;

import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


public class adapter2 extends FirebaseRecyclerAdapter<model2, adapter2.myviewholder>
{
    public adapter2(@NonNull FirebaseRecyclerOptions<model2> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull final myviewholder holder, int position, @NonNull final model2 model2) {




        holder.header.setText(model2.getFilename());
        holder.date.setText(model2.getDate());

        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(holder.rl.getContext(),viewpdf2.class);
                intent.putExtra("filename",model2.getFilename());
                intent.putExtra("fileurl",model2.getFileurl());
                intent.putExtra("date",model2.getDate());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.img1.getContext().startActivity(intent);

            }
        });

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singelrowdesign,parent,false);
       return  new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder
    {
        ImageView img1;
        TextView header,date;
        LinearLayout rl;

        public myviewholder(@NonNull View itemView)
        {
            super(itemView);

            img1=itemView.findViewById(R.id.img1);
            header=itemView.findViewById(R.id.header);
            date = itemView.findViewById(R.id.date);
            rl = itemView.findViewById(R.id.rl);

            header.setPaintFlags(header.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);


        }
    }

}
