package com.example.project03_timekeeping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project03_timekeeping.R;
import com.example.project03_timekeeping.object.MonthData;

import java.util.List;

public class ShowAllAdapter extends RecyclerView.Adapter<ShowAllAdapter.Holder> {
    private Context context;
    private List<MonthData> list;

    public ShowAllAdapter(Context context){
        this.context = context;
    }
    public void setData(List<MonthData> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_show_all, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        MonthData monthData = list.get(position);
        if(monthData == null) return ;
        holder.tvMonth.setText(monthData.getMonth());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        holder.rcvListMember.setLayoutManager(linearLayoutManager);
        ChildShowAllAdapter adapter = new ChildShowAllAdapter(context, monthData.getMonth());
        adapter.setData(monthData.getListMember());
        holder.rcvListMember.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        if(list != null) return list.size();
        return 0;
    }

    public class Holder extends RecyclerView.ViewHolder{
        TextView tvMonth;
        RecyclerView rcvListMember;
        public Holder(@NonNull View itemView) {
            super(itemView);
            tvMonth = itemView.findViewById(R.id.tvItemTitleShowAll);
            rcvListMember = itemView.findViewById(R.id.rcvChildShowAll);
        }
    }
}
