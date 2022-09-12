package com.example.taskmanager.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.databinding.RowcompletetaskBinding;
import com.example.taskmanager.model.Tasks;
import com.example.taskmanager.ui.activities.HomeActivity;
import com.example.taskmanager.ui.activities.NewTask;
import com.example.taskmanager.R;
import com.example.taskmanager.database.MyDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CompleteAdapter extends RecyclerView.Adapter<CompleteAdapter.ComplateViewHolder>{
    private ArrayList<Tasks> list;
    private ArrayList<Tasks> copylist;
    private ArrayList<Tasks> Cfilteredlist;
    private MyDatabase mydb;
    private HomeActivity home = new HomeActivity();
    private String[] Actions = {"pending","Completed"};
    Context context;

    public CompleteAdapter(ArrayList<Tasks> list) {
        this.list = list;
        copylist = list;
    }

    @NonNull
    @Override
    public ComplateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        RowcompletetaskBinding binding = RowcompletetaskBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        ComplateViewHolder holder = new ComplateViewHolder(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ComplateViewHolder holder, int position) {
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
                    .setSingleChoiceItems(Actions, 1,(dialogInterface, i) -> {
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        String strDate = sdf.format(calendar.getTime());
                        t.setFlag(context.getString(R.string.StrPending));
                        t.setDateupdated(strDate);
                        mydb.updateData(t);
                        home.FeatchData();
                        notifyDataSetChanged();
                    }).show();
        });
        holder.itemView.setOnLongClickListener(view -> {
            new AlertDialog.Builder(context)
                    .setTitle("Action")
                    .setPositiveButton("Update", (dialogInterface, i) -> {
                        Intent intent = new Intent(context, NewTask.class);
                        intent.putExtra("Objcomplete",t);
                        intent.putExtra("boolcomplete", true);
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

                    Cfilteredlist = copylist;

                } else {
                    Cfilteredlist = new ArrayList<>();

                    for (Tasks t : copylist) {
                        if (t.getTitle().toLowerCase().contains(charSequence.toString().toLowerCase()) ||
                                t.getDescription().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                            Cfilteredlist.add(t);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = Cfilteredlist;

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

    public class ComplateViewHolder extends RecyclerView.ViewHolder {
        RowcompletetaskBinding RBinding;
        public ComplateViewHolder(RowcompletetaskBinding RBinding) {
            super(RBinding.getRoot());
            this.RBinding=RBinding;

        }
    }
}
