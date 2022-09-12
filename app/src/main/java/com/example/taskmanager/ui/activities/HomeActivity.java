package com.example.taskmanager.ui.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.taskmanager.databinding.ActivityHomeBinding;
import com.example.taskmanager.R;
import com.example.taskmanager.database.MyDatabase;
import com.example.taskmanager.model.Tasks;
import com.example.taskmanager.ui.adapters.CompleteAdapter;
import com.example.taskmanager.ui.adapters.PendingAdapter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Arrays;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private ArrayList<Tasks> Plist;
    private ArrayList<Tasks> Clist;
    private PendingAdapter padapter;
    private CompleteAdapter cadapter;
    private MyDatabase mydb;
    boolean isGridLayout=false;
    public  HomeActivity(){}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.pendingrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.CompletesrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        FeatchData();
        padapter = new PendingAdapter(Plist);
        binding.pendingrecyclerView.setAdapter(padapter);
        padapter.notifyDataSetChanged();
        cadapter = new CompleteAdapter(Clist);
        binding.CompletesrecyclerView.setAdapter(cadapter);
        cadapter.notifyDataSetChanged();
        binding.AddtaskFAB.setOnClickListener(view -> {
            startActivity(new Intent(HomeActivity.this, NewTask.class));
        });

    }

    public void FeatchData() {
        mydb = new MyDatabase(this);
        Plist = new ArrayList<>();
        Clist = new ArrayList<>();
        Cursor pc = mydb.getpendingdata();
        Cursor cc = mydb.getComplatedata();
        if(pc != null) {
            if (pc.getCount() > 0) {
                Showhideview(binding.textpending,View.VISIBLE);
                Showhideview(binding.pendingrecyclerView,View.VISIBLE);
                if(pc.moveToFirst());
                do {
                    Tasks t = new Tasks(pc.getInt(0),pc.getString(1),pc.getString(2),pc.getString(3),pc.getInt(4),pc.getString(5),pc.getString(6));
                    Plist.add(t);
                } while (pc.moveToNext());
            } else {
               Showhideview(binding.textpending,View.GONE);
               Showhideview(binding.pendingrecyclerView,View.GONE);
            }
        }
        if(cc != null) {
            if (cc.getCount() > 0) {
                Showhideview(binding.textCompleted,View.VISIBLE);
                Showhideview(binding.CompletesrecyclerView,View.VISIBLE);
                if(cc.moveToFirst());
                do {
                    Tasks t = new Tasks(cc.getInt(0),cc.getString(1),cc.getString(2),cc.getString(3),cc.getInt(4),cc.getString(5),cc.getString(6));
                    Clist.add(t);
                } while (cc.moveToNext());
            } else{
                Showhideview(binding.textCompleted,View.GONE);
                Showhideview(binding.CompletesrecyclerView,View.GONE);
        }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_search_menu,menu);

        MenuItem item = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) item.getActionView();

        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                padapter.getFilter().filter(newText);
                cadapter.getFilter().filter(newText);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);

    }@Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){
            case R.id.typeview:{
                isGridLayout = !isGridLayout;
                item.setIcon(isGridLayout ? R.drawable.ic_baseline_view_module_24 : R.drawable.ic_baseline_list_24);
                binding.pendingrecyclerView.setLayoutManager(isGridLayout ? new GridLayoutManager(this, 2) : new LinearLayoutManager(this));
                binding.CompletesrecyclerView.setLayoutManager(isGridLayout ? new GridLayoutManager(this, 2) : new LinearLayoutManager(this));            
            }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void Showhideview(View view,int type){
        if (view.getVisibility() != type) {
            view.setVisibility(type);
        }

    }
}