package com.anikrakib.tourday.Adapter.Profile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anikrakib.tourday.Models.Profile.Hobby;
import com.anikrakib.tourday.R;

import java.util.List;

public class HobbyAdapter extends RecyclerView.Adapter<HobbyAdapter.ViewHolder> {
    private final List<Hobby> data;
    private final Context context;
    EditText editText;

    public HobbyAdapter(List<Hobby> data, Context context, EditText editText) {
        this.data = data;
        this.editText = editText;
        this.context = context;
    }

    @NonNull
    @Override
    public HobbyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view1 = inflater.inflate(R.layout.hobby_list_item, parent, false);
        return new ViewHolder(view1);
    }

    @Override
    public void onBindViewHolder(HobbyAdapter.ViewHolder holder, final int position) {
        holder.hobbyName.setText(data.get(position).getHobbyName());
        String hobby = data.get(position).getHobbyName();
        holder.hobbyName.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if(editText.getText().toString().isEmpty()){
                    editText.setText(hobby+" ");
                }else{
                    editText.setText(editText.getText().toString()+" "+hobby);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView hobbyName;

        public ViewHolder(View itemView) {
            super(itemView);
            hobbyName = itemView.findViewById(R.id.hobbyItemTextView);
        }
    }


}
