package ru.turbopro.cubsappjava;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ListTopAdapter extends ArrayAdapter<UserTop> {

    public ListTopAdapter(Context context, ArrayList<UserTop> userArrayList){
        super(context,R.layout.list_item_top,userArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        UserTop user = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_top, parent, false);

        ImageView imageView = convertView.findViewById(R.id.profile_pic_user_item);
        TextView name = convertView.findViewById(R.id.name_user_item);
        TextView points = convertView.findViewById(R.id.points_user_item);

        imageView.setImageResource(user.getImageId());
        name.setText(user.getName());
        points.setText(user.getPoints());

        return convertView;
    }
}
