package com.example.taskmanager.ui.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.databinding.RowpendingtaskBinding;
import com.example.taskmanager.model.Tasks;
import com.example.taskmanager.ui.activities.HomeActivity;
import com.example.taskmanager.ui.activities.NewTask;
import com.example.taskmanager.R;
import com.example.taskmanager.database.MyDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class PendingAdapter extends RecyclerView.Adapter<PendingAdapter.PendingViewHolder>{
    private ArrayList<Tasks> list;
    private ArrayList<Tasks> copylist;
    private ArrayList<Tasks> Pfilteredlist;
    private HomeActivity home = new HomeActivity();
    private MyDatabase mydb;
    private String[] Actions = {"pending","Completed"};
    Context context;

    public PendingAdapter(ArrayList<Tasks> list) {
        this.list = list;
        copylist = list;
    }


    @NonNull
    @Override
    public PendingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        RowpendingtaskBinding binding = RowpendingtaskBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        PendingViewHolder holder = new PendingViewHolder(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PendingViewHolder holder, int position) {
        mydb = new MyDatabase(context);
        Tasks t = list.get(position);
        holder.RBinding.setObj(t);
        holder.RBinding.pushpinimg.setOnClickListener(view -> {
            mydb.updatepushpin(t.getID(), t.getPushpin() == 0);
            holder.RBinding.pushpinimg.setImageResource(t.getPushpin() == 0? R.drawable.ic_baseline_push_pin_black : R.drawable.ic_baseline_push_pin_gray);
            notifyDataSetChanged();
        });
        holder.itemView.setOnClickListener(view -> {
            new AlertDialog.Builder(context)
                    .setTitle("Action")
                    .setSingleChoiceItems(Actions, 0, (dialogInterface, i) -> {
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        String strDate = sdf.format(calendar.getTime());
                        t.setFlag(context.getString(R.string.StrComplate));
                        t.setDateupdated(strDate);
                        mydb.updateData(t);
                        home.FeatchData();
                        PendingAdapter.this.notifyDataSetChanged();
                    }).show();
        });
        holder.itemView.setOnLongClickListener(view -> {
            new AlertDialog.Builder(context)
                    .setTitle("Action")
                    .setPositiveButton("Update", (dialogInterface, i) -> {
                        Intent intent = new Intent(context, NewTask.class);
                        intent.putExtra("Objpending",t);
                        intent.putExtra("boolpending", true);
                        context.startActivity(intent);
                    }).setNegativeButton("Delete", (dialogInterface, i) -> {
                        mydb.deletedata(t);
                        notifyDataSetChanged();
                    })
                    .show();
            return false;
        });
    }

    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                if (charSequence.toString().equals("")) {

                    Pfilteredlist = copylist;

                } else {
                    Pfilteredlist = new ArrayList<>();

                    for (Tasks t : copylist) {
                        if (t.getTitle().toLowerCase().contains(charSequence.toString().toLowerCase()) ||
                                t.getDescription().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                            Pfilteredlist.add(t);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = Pfilteredlist;

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                list = (ArrayList<Tasks>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PendingViewHolder extends RecyclerView.ViewHolder {
        RowpendingtaskBinding RBinding;
        public PendingViewHolder(RowpendingtaskBinding RBinding) {
            super(RBinding.getRoot());
            this.RBinding=RBinding;

        }
    }
}
