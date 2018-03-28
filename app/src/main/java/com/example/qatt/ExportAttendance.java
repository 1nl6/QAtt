package com.example.qatt;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.qatt.database.ScanRepository;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import greendao.Scan;


public class ExportAttendance extends AppCompatActivity {

    private static int weekNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_attendance);

        //Number picker
        NumberPicker np = (NumberPicker) findViewById(R.id.exportWeek);
        np.setWrapSelectorWheel(true);

        //Set range
        np.setMinValue(1);
        np.setMaxValue(24);

        weekNum = 1;
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                weekNum = i1;
            }
        });
    }

    public void export(View view){
        String filename = "attendance-week"+weekNum +".csv";
        List<Scan> scanRecords= ScanRepository.getWeekScan(this, weekNum);
        if(scanRecords.size() == 0){
            Toast.makeText(this, "There are no attendance for this week yet.", Toast.LENGTH_SHORT).show();
        } else{
            saveCSV(scanRecords, filename);
            Toast.makeText(this, "Attendance for week "+weekNum+" exported to csv", Toast.LENGTH_SHORT).show();
        }
    }

    public void exportAll(View view){
        String filename = "attendance-master.csv";
        List<Scan> scanRecords= ScanRepository.getAllScans(this);
        saveCSV(scanRecords,filename);
        Toast.makeText(this, "All attendance exported to csv", Toast.LENGTH_SHORT).show();
    }

    public void saveCSV(List<Scan> scanRecords, String filename){

        //Write to csv - external storage
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            File root = Environment.getExternalStorageDirectory();
            File dir = new File(root.getAbsolutePath() + "/QAtt_attendance");
            if(!dir.exists()){
                dir.mkdir();
            }
            File file = new File(dir, filename);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    Log.e("Child", e.getMessage(), e);
                }
            }

            try{
                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));

                //Write header
                String[] header = {"NetID", "Attendance", "Time of Scan", "Day of Week", "Week"};
                csvWrite.writeNext(header, false);
                for(int i = 0; i < scanRecords.size(); i++){
                    Scan record = scanRecords.get(i);
                    String res[] = {record.getNetID(), String.valueOf(record.getAttendance()), String.valueOf(record.getScanTime()), record.getScanDate(), String.valueOf(record.getWeek())};
                    csvWrite.writeNext(res, false);
                }
                csvWrite.close();
            }catch(IOException e){
                Log.e("Child", e.getMessage(), e);
            }
        }else{
            Toast.makeText(this, "No external storage", Toast.LENGTH_SHORT).show();
        }
    }
}
