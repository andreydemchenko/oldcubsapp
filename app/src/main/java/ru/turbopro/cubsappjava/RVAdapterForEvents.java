package ru.turbopro.cubsappjava;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

interface ClickListener<T> {
    void onItemClick(T data);
}

public class RVAdapterForEvents extends RecyclerView.Adapter<RVAdapterForEvents.MyViewHolder> {

    private List<CardEvent> cardEvents;
    private ClickListener<CardEvent> clickListener;

    RVAdapterForEvents(List<CardEvent> cardEvents){
        this.cardEvents = cardEvents;
    }

    @Override
    public RVAdapterForEvents.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_event, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final CardEvent event = cardEvents.get(position);
        holder.title.setText(event.getTitle());
        Bitmap bmp = BitmapFactory.decodeByteArray(event.getImage(), 0, event.getImage().length);
        holder.image.setImageBitmap(bmp);
        holder.cardView.setOnClickListener(v -> clickListener.onItemClick(event));
    }

    @Override
    public int getItemCount() {
        return cardEvents.size();
    }

    public void setOnItemClickListener(ClickListener<CardEvent> eventClickListener) {
        this.clickListener = eventClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private ImageView image;
        private CardView cardView;
        public MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            image = itemView.findViewById(R.id.image);
            cardView = itemView.findViewById(R.id.carView);
        }
    }
}