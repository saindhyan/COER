package coer.notification;

import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


public class myadapter extends FirebaseRecyclerAdapter<model, coer.notification.myadapter.myviewholder>
{
    public myadapter(@NonNull FirebaseRecyclerOptions<model> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull final myviewholder holder, int position, @NonNull final model model) {




        holder.header.setText(model.getFilename());
        holder.date.setText(model.getDate());

        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(holder.rl.getContext(),viewpdf2.class);
                intent.putExtra("filename",model.getFilename());
                intent.putExtra("fileurl",model.getFileurl());
                intent.putExtra("date",model.getDate());

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
