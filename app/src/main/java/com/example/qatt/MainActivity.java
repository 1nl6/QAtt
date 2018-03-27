package com.example.qatt;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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

import static android.Manifest.permission.CAMERA;

public class MainActivity extends AppCompatActivity{

    private static final int REQUEST_CAMERA =1;
    private static int week;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Permissions
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(!checkPermission()){
                requestPermission();
            }
        }

        //Number picker
        NumberPicker np = (NumberPicker) findViewById(R.id.week);
        np.setWrapSelectorWheel(true);

        //Set range
        np.setMinValue(1);
        np.setMaxValue(24);

        week = 1;
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                week = i1;
            }
        });

    }

    private boolean checkPermission(){
        return (ContextCompat.checkSelfPermission(MainActivity.this, CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    public void onRequestPermissionsResult(int requestCode, String permission[], int grantResults[]){
        switch(requestCode){
            case REQUEST_CAMERA:
                if(grantResults.length > 0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(!cameraAccepted){
                        Toast.makeText(MainActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            if(shouldShowRequestPermissionRationale(CAMERA)){
                                displayAlertMessage("You need to allow access for both permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{CAMERA}, REQUEST_CAMERA);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }


    public void displayAlertMessage(String message, DialogInterface.OnClickListener listener){
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", listener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();

    }


    public void scan(View view){
        Intent intent = new Intent(this, ScanCode.class);
        //intent.putExtra("Permission", ContextCompat.checkSelfPermission(MainActivity.this, CAMERA));
        intent.putExtra("week", week);
        startActivity(intent);
    }

    public void exportDB(View view){
        /*
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"1nl6@queesu.ca"});
        i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
        i.putExtra(Intent.EXTRA_TEXT   , "body of email");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
        */

        /*
        File file = new File(Environment.getDataDirectory(), "data.csv");
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"nikilin1993@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Backup");
        intent.putExtra(Intent.EXTRA_TEXT, "Fail");
        intent.setType("application/octet-stream");
        //intent.putExtra(Intent.EXTRA_STREAM, file.toURI());
        File root = Environment.getExternalStorageDirectory();
        String fileName = "attendance-db";
        if (root.canWrite()) {
            File attachment = new File(root, fileName);
            intent.putExtra(Intent.EXTRA_TEXT, "Success");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(attachment));
        }

        startActivity(Intent.createChooser(intent, "Send Email"));
        */

        File dbFile =getDatabasePath("attendance-db");

        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
        File file = new File(exportDir, "data.csv");


        try{
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));

            List<Scan> scanRecords= ScanRepository.getAllScans(this);

            //Write header
            String[] header = {"NetID", "Attendance", "Time of Scan", "Day of Week", "Week"};
            csvWrite.writeNext(header);
            for(int i = 0; i < scanRecords.size(); i++){
                Scan record = scanRecords.get(i);
                String res[] = {record.getNetID(), String.valueOf(record.getAttendance()), String.valueOf(record.getScanTime()), record.getScanDate(), String.valueOf(record.getWeek())};
                csvWrite.writeNext(res);
            }
            csvWrite.close();
        }catch(IOException e){
            Log.e("Child", e.getMessage(), e);
        }
    }

    /*
    private void exportDB() {

        File dbFile = getDatabasePath("MyDBName.db");

        DBHelper dbhelper = new DBHelper(getApplicationContext());
        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, "csvname.csv");
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM contacts", null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while (curCSV.moveToNext()) {
                //Which column you want to exprort
                String arrStr[] = {curCSV.getString(0), curCSV.getString(1), curCSV.getString(2)};
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
        } catch (Exception sqlEx) {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }
    }
    */
}
