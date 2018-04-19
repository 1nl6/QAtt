package com.example.qatt;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
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
    private static String[] pickerValues;
    private String state = Environment.getExternalStorageState();
    private int totalWeeks = 24;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_attendance);

        TextView info = (TextView) findViewById(R.id.exportPath) ;
        setPicker();


        //Check for external storage
        if(Environment.MEDIA_MOUNTED.equals(state)) {
            File root = Environment.getExternalStorageDirectory();
            File dir = new File(root.getAbsolutePath() + "/QAtt_attendance");
            if (!dir.exists()) {
                dir.mkdir();
            }
            info.setText("Your file(s) will be exported to: " + dir.getAbsolutePath());
        }else{

            info.setText("No external storage detected. You will be unable to export your file(s).");
        }

    }


    public void setPicker(){
        //Set values of picker
        pickerValues = new String[totalWeeks+1];
        pickerValues[0] = "All";
        for(int i = 1; i < pickerValues.length; i++){
            pickerValues[i] = String.valueOf(i);
        }

        //Set picker
        NumberPicker np = (NumberPicker) findViewById(R.id.exportWeek);
        np.setWrapSelectorWheel(true);
        np.setMinValue(0);
        np.setMaxValue(totalWeeks);
        np.setDisplayedValues(pickerValues);

        weekNum = 0;
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                weekNum = i1;
            }
        });
    }


    public void backup(View view){
        export(false);
    }

    public void email(View view){
        export(true);
    }

    public void export(boolean sendEmail){
        String filename;
        List<Scan> scanRecords;
        if(weekNum == 0){
            filename = "attendance-master.csv";
            scanRecords= ScanRepository.getAllScans(this);
            if(scanRecords.size() == 0){
                Toast.makeText(this, "No attendances have been saved yet.", Toast.LENGTH_SHORT).show();
                return;
            }
        } else{
            filename = "attendance-week"+weekNum +".csv";
            scanRecords= ScanRepository.getWeekScan(this, weekNum);
            if(scanRecords.size() == 0){
                Toast.makeText(this, "There are no attendance for this week yet.", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        exportCSV(scanRecords,filename,sendEmail);

    }




    public void exportCSV(List<Scan> scanRecords, String filename, boolean sendEmail){

        //Write to csv - external storage
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
                String[] header = {"NetID", "Attendance", "Time of Scan", "Day of Week", "Week", "End-of-line"};
                csvWrite.writeNext(header, false);
                for(int i = 0; i < scanRecords.size(); i++){
                    Scan record = scanRecords.get(i);
                    String res[] = {record.getNetID(), String.valueOf(record.getAttendance()), String.valueOf(record.getScanTime()), record.getScanDate(), String.valueOf(record.getWeek()), ";"};
                    csvWrite.writeNext(res, false);
                }
                csvWrite.close();
                Toast.makeText(this, "Attendance exported to csv", Toast.LENGTH_SHORT).show();
                if(sendEmail) {
                    emailFile(file);
                }
            }catch(IOException e){
                Log.e("Child", e.getMessage(), e);
            }
        }else{
            Toast.makeText(this, "No external storage", Toast.LENGTH_SHORT).show();
        }
    }

    public void emailFile(File file){
        Uri path = Uri.fromFile(file);
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_STREAM, path);
        intent.putExtra(Intent.EXTRA_SUBJECT, "QAtt attendance");
        startActivity(Intent.createChooser(intent , "Send email..."));

    }
}
