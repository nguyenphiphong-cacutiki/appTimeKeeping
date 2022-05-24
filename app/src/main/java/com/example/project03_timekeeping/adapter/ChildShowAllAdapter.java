package com.example.project03_timekeeping.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project03_timekeeping.R;
import com.example.project03_timekeeping.activity.ShowOneMemberActivity;
import com.example.project03_timekeeping.object.Member;

import java.util.List;

public class ChildShowAllAdapter extends RecyclerView.Adapter<ChildShowAllAdapter.Holder> {
    private Context context;
    List<Member> list;
    private String month;

    public ChildShowAllAdapter(Context context, String month){
        this.month = month;
        this.context = context;
    }
    public void setData(List<Member> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_child_show_all, parent, false);
        return new Holder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Member member = list.get(position);
        if (member == null) return;
        holder.tvName.setText(member.getName());
        holder.tvTime.setText(member.getCountTime() + "");
        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowOneMemberActivity.class);
                intent.putExtra("name", member.getName());
                intent.putExtra("month", month);
                intent.putExtra("count", member.getCountTime());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(list != null) return list.size();
        return 0;
    }

    public class Holder extends RecyclerView.ViewHolder{
        TextView tvName, tvTime;
        public Holder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvNameChildShowAll);
            tvTime = itemView.findViewById(R.id.tvTimeChildShowAll);
        }
    }
}
