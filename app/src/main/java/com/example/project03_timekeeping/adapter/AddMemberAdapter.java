package com.example.project03_timekeeping.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project03_timekeeping.R;
import com.example.project03_timekeeping.database.Database;
import com.example.project03_timekeeping.object.Member;

import java.util.List;

public class AddMemberAdapter extends RecyclerView.Adapter<AddMemberAdapter.Holder>{
    private Context context;
    List<Member> list;
    Database database;

    public AddMemberAdapter (Context context){
        this.context = context;
    }
    public void setData(List<Member> list){
        this.list = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_member, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Member member = list.get(position);
        if(member == null) return;
        holder.tvName.setText(member.getName());
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDelete(member.getName(), holder, member);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(list != null) return list.size();
        return 0;
    }

    public class Holder extends RecyclerView.ViewHolder{
        TextView tvName;
        ImageView imgDelete;
        public Holder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvNameAddMember);
            imgDelete = itemView.findViewById(R.id.imgDeleteMember);
        }
    }
    private void dialogDelete(String name, Holder holder, Member member){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage("Are you sure delete "+ name);
        dialog.setIcon(android.R.drawable.ic_delete);
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                database = new Database(context);
                database.queryData("DELETE FROM Member WHERE id = '"+member.getId()+"'");
                database.queryData("DELETE FROM Info WHERE keyName = '"+member.getId()+"'");
                list.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
                dialog.cancel();
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();
    }
}
