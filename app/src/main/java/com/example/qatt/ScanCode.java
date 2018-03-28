package com.example.qatt;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.qatt.database.ScanRepository;
import com.google.zxing.Result;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import greendao.Scan;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class ScanCode extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;
    private int week;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get week
        Bundle data = getIntent().getExtras();
        week = data.getInt("week");

        //set up scannerView
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);

    }


    @Override
    protected void onResume() {
        super.onResume();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(ScanCode.this, CAMERA) == PackageManager.PERMISSION_GRANTED){
                if(scannerView == null){
                    scannerView = new ZXingScannerView(this);
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            } else{
                ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }


    @Override
    public void handleResult(Result result) {
        String scanResult = result.getText();
        String[] lines = scanResult.split("\t");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                scannerView.resumeCameraPreview(ScanCode.this);
            }
        });

        //Check QR code (last field is empty)
        if(lines.length != 6){
            builder.setMessage("Invalid scan");
        } else{
            String name = lines[1];
            String netID = lines[2];
            String weekday = lines[3];
            String labTime = lines[4];

            //Check if date and time is valid
            SimpleDateFormat weekDayFormat = new SimpleDateFormat("EEEE");
            Date d = new Date();
            String scanDay = weekDayFormat.format(d);
            //Current time in 24 hour format
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            String scanTime = timeFormat.format(d);

            //Check day of scan
            if(!scanDay.equals(weekday)){
                saveScan(netID,0,scanTime,scanDay,week);
                builder.setMessage("Saved. Note: " + name + " usual lab is on " + weekday);
                AlertDialog alert = builder.create();
                alert.show();
                return;
            }

            //Check time of scan
            int status = checkTime(labTime, scanTime);
            String message;
            switch (status) {
                case 0:
                    saveScan(netID,status,scanTime,scanDay,week);
                    message = "Saved but absent. " + name + " is late.";
                    break;
                case 1:
                    saveScan(netID,status,scanTime,scanDay,week);
                    message = "Saved. " + name + " scanned for week " + week + " " + weekday + " " + labTime + " at " + scanTime;
                    break;
                case 2:
                    saveScan(netID,0,scanTime,scanDay,week);
                    message = "Saved but absent. " + name + " usual lab time is " + labTime + ". If attending with permission, please note down the student's name.";
                    break;
                default:
                    message = "Invalid scan!";
                    break;
            }
            builder.setMessage(message);

            //Check if student has already been scanned (check by week and student) - Dis[play if scanned

        }


        //Show alert
        AlertDialog alert = builder.create();
        alert.show();


    }


    //Check time of scan
    public int checkTime(String labTime, String scanTime){
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        try{
            //Time of scan
            Date scan = timeFormat.parse(scanTime);

            Date lab = timeFormat.parse(labTime);
            Calendar cal = Calendar.getInstance();
            cal.setTime(lab);

            //Time of early lab attendance
            cal.add(Calendar.MINUTE, -16);
            Date labEarly = cal.getTime();

            //Time of late lab attendance
            cal.add(Calendar.MINUTE, 32);
            Date labLate = cal.getTime();

            //Time of end of lab
            cal.add(Calendar.MINUTE, 30);
            Date endLab = cal.getTime();

            if(scan.after(labLate) && scan.before(endLab)){
                return 0; //Late, saved but absent
            } else if(scan.after(labEarly) && scan.before(labLate)){
                return 1; //Good
            } else {
                return 3; //Attending different lab
            }
        } catch (ParseException e){
            return -1;
        }
    }

    public void saveScan(String netID, int status, String scanTime, String scanDate, int week){
        Scan s = new Scan();
        s.setNetID(netID);
        s.setAttendance(status);
        s.setScanTime(scanTime);
        s.setScanDate(scanDate);
        s.setWeek(week);
        ScanRepository.insertOrUpdate(this, s);
    }
}
