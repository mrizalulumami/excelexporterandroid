package com.rt.excelfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rt.excelfirebase.model.ModelSensor;
import com.rt.excelfirebase.model.MyInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {
    File directory, sd, file;
    WritableWorkbook workbook;
    List<ModelSensor> listdata1;
    Button saveBtn;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        saveBtn=findViewById(R.id.btnsave);


        listdata1 = new ArrayList<>();
        ref= FirebaseDatabase.getInstance().getReference("datasensor").child("ijang").child("senin");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ModelSensor modelSensor=snapshot.getValue(ModelSensor.class);
                listdata1.add(modelSensor);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveTheFile();
            }
        });
    }
    public void createExcelSheet() {
        if(isStoragePermissionGranted()) {
            String csvFile = "Mytest.xls";
            sd = Environment.getExternalStorageDirectory();
            directory = new File(sd.getAbsolutePath());
            file = new File(directory, csvFile);
            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            try {
                workbook = Workbook.createWorkbook(file, wbSettings);
                createFirstSheet();

                //closing cursor
                workbook.write();
                workbook.close();

                Toast.makeText(this, "File Saved !", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{

            Toast.makeText(this, "Permission Denied !", Toast.LENGTH_SHORT).show();
        }
    }



    public void createFirstSheet() {

        try {

            //Excel sheet name. 0 (number)represents first sheet
            WritableSheet sheet = workbook.createSheet("sheet1", 0);
            // column and row title
            sheet.addCell(new Label(0, 0, "pagi"));
            sheet.addCell(new Label(1, 0, "siang"));
            sheet.addCell(new Label(2, 0, "sore"));
            sheet.addCell(new Label(3, 0, "malam"));


            for (int i = 0; i < listdata1.size(); i++) {
                sheet.addCell(new Label(0, i + 1, listdata1.get(i).getPagi()));
                sheet.addCell(new Label(1, i + 1, listdata1.get(i).getSiang()));
                sheet.addCell(new Label(2, i + 1, listdata1.get(i).getSore()));
                sheet.addCell(new Label(3, i + 1, listdata1.get(i).getMalam()));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    public void SaveTheFile() {
        createExcelSheet();
    }


    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }
}