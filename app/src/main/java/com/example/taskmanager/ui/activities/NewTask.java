package com.example.taskmanager.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.taskmanager.databinding.ActivityNewTaskBinding;
import com.example.taskmanager.R;
import com.example.taskmanager.database.MyDatabase;
import com.example.taskmanager.model.Tasks;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class NewTask extends AppCompatActivity {
    private ActivityNewTaskBinding binding;
    private ArrayList<Tasks> list;
    private MyDatabase mydb;
    private boolean updation = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        list = new ArrayList<>();
        Intent intent = getIntent();
        Tasks tt;
        if (intent != null){
            tt = (Tasks) intent.getSerializableExtra("Objpending");
            if (tt != null) {
                updation = intent.getBooleanExtra("boolpending",true);
                binding.EDTitletext.setText(tt.getTitle());
                binding.EDDescriptiontext.setText(tt.getDescription());
            }
        }
        mydb = new MyDatabase(this);
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        exitscreen();
    }

    public void exitscreen() {
        if (binding.EDTitletext.getText().toString().isEmpty() || binding.EDDescriptiontext.getText().toString().isEmpty()) {
            new AlertDialog.Builder(this)
                    .setTitle("Alert")
                    .setMessage("Are you Sure you want to cancel this ?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        binding.EDTitletext.setText("");
                        binding.EDDescriptiontext.setText("");
                        startActivity(new Intent(NewTask.this, HomeActivity.class));
                    }).setNegativeButton("No", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    }).show();
        }else{
            binding.EDTitletext.setText("");
            binding.EDDescriptiontext.setText("");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.newtaskmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){
            case R.id.itemsave:{
                String Titletxt=binding.EDTitletext.getText().toString();
                String Desctxt=binding.EDDescriptiontext.getText().toString();
                if(!Titletxt.isEmpty() ||!Desctxt.isEmpty()) {
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    String strDate = sdf.format(calendar.getTime());
                    if(updation){
                        Tasks t = new Tasks(Titletxt, Desctxt, getString(R.string.StrPending), "",strDate);
                        mydb.updateData(t);
                        Toast.makeText(this, "Task Successfully Updated", Toast.LENGTH_SHORT).show();
                        updation = false;
                    }else {
                        Tasks t = new Tasks(Titletxt, Desctxt, getString(R.string.StrPending),0, strDate, "");
                        mydb.insertData(t);
                        Toast.makeText(this, "Task Successfully added to the pending list", Toast.LENGTH_SHORT).show();
                    }
                    startActivity(new Intent(NewTask.this, HomeActivity.class));
                }
            }
                break;
            case R.id.itemcancle:exitscreen();
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}