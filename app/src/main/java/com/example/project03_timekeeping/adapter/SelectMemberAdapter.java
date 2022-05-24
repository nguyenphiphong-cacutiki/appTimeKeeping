package com.example.project03_timekeeping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project03_timekeeping.R;
import com.example.project03_timekeeping.object.Member;

import java.util.List;

public class SelectMemberAdapter extends RecyclerView.Adapter<SelectMemberAdapter.Holder> {
    private Context context;
    private List<Member> list;

    public SelectMemberAdapter(Context context){
        this.context = context;
    }
    public void setData(List<Member> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_member, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Member member = list.get(position);
        if(member == null) return;
        holder.tvName.setText(member.getName());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                member.setCheck(holder.checkBox.isChecked());
            }
        });
    }

    @Override
    public int getItemCount() {
        if(list == null) return 0;
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        public CheckBox checkBox;
        public TextView tvName;
        public Holder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.cbSelectMember);
            tvName = itemView.findViewById(R.id.tvNameSelectMember);
        }
    }
}
