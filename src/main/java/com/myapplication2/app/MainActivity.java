package com.myapplication2.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import org.altbeacon.beacon.*;

import java.util.Collection;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MainActivity extends Activity implements BeaconConsumer{


    public static final String TAG = "BeaconsEverywhere";
    private BeaconManager beaconManager;
    int distance;
    Identifier minorID;
    double distanceLong;
    public Handler mHandler;
    public int i = 0;
    public TextView distanceText;
    public TextView distanceName1;
    public TextView descriptionTitle;
    public TextView descriptionText;
    public ImageView distanceImage;
    public String name1;
    public boolean exitRegion = false;
    public String minorString;
    public int a = 0;
    String distanceString;
    //List<String> myArray = new ArrayList<String>();








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

         name1 = intent.getStringExtra("beacon1");


        beaconManager = BeaconManager.getInstanceForApplication(this);

        beaconManager.getBeaconParsers().add(new BeaconParser()
                .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));

        beaconManager.bind(this);


        distanceText = (TextView) findViewById(R.id.distanceText);
        distanceText.setText("No beacons found yet, please wait");






            distanceName1 = (TextView) findViewById(R.id.name1);
            distanceName1.getText();
            distanceName1.setText("How far away is "+name1+"?");



        distanceText = (TextView) findViewById(R.id.distanceText);
        distanceText.getText();
        distanceText.setText("Distance: " + distance+ " metres");
        distanceImage = (ImageView) findViewById(R.id.distanceImage);

        descriptionTitle = (TextView) findViewById(R.id.distanceOtherBeacons);
        descriptionTitle.getText();
        descriptionTitle.setText("Other beacons in your region to watch :");

        descriptionText = (TextView) findViewById(R.id.distanceString);
        descriptionText.getText();
        descriptionText.setText(distanceString);

//        myArray.clear();
//        myArray.add(distanceString);



//      int arraySize = myArray.size();
//
//      for(int i = 0; i < arraySize; i++) {
//          descriptionText.setText(distanceString);
//          descriptionText.append(myArray.get(i));
//          descriptionText.append("\n");
//           i++;
//        }



        //alertDialogue();

        i = 0;
        mHandler = new Handler() {
            @Override
            public void close() {

            }

            @Override
            public void flush() {

            }

            @Override
            public void publish(LogRecord record) {

            }
        };




        distanceText.post(mUpdate);
        distanceImage.post(mUpdate);
        descriptionText.post(mUpdate);


    }





    private Runnable mUpdate = new Runnable() {
        public void run() {



            distanceText.setText("Distance: " + distance+ " ms");

//                        int arraySize = myArray.size();
//                        for(int i = 0; i < arraySize; i++) {
//                        descriptionText.append(myArray.get(i));
//                        descriptionText.append("\n");
//                        i++;
//                        }
            descriptionText.setText(distanceString);
            imageDistance(distance);
            alertDialogue(distance);
            i++;
            distanceText.postDelayed(this, 10000);
            descriptionText.postDelayed(this, 10000);


        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    public void onBeaconServiceConnect() {
        final Region region = new Region("myBeacons", Identifier.parse("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null);

        beaconManager.setMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                try {
                    Log.d(TAG, "didEnterRegion");
                    exitRegion=false;
                    beaconManager.startRangingBeaconsInRegion(region);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void didExitRegion(Region region) {
                try {
                    Log.d(TAG, "didExitRegion");
                    exitRegion = true;
                    beaconManager.stopRangingBeaconsInRegion(region);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void didDetermineStateForRegion(int i, Region region) {

            }
        });

        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {


                for(Beacon oneBeacon : beacons) {
                    Log.d(TAG, "Distance of " + minorID +" beacon: "+ (int) distanceLong+ " metres");
                    distanceLong = oneBeacon.getDistance();
                    distance = (int) distanceLong;
                    minorID = oneBeacon.getId3();
                    minorString = minorID.toString();
                    distanceString = String.valueOf("Distance of " + minorID +" beacon: "+ (int) distanceLong+ " metres");
                    Beacon.setHardwareEqualityEnforced(true);
                    //myArray.add(distanceString);

                }

            }
        }

        );

        try {
            beaconManager.startMonitoringBeaconsInRegion(region);
        } catch (RemoteException e) {
            e.printStackTrace();
        }


    }



    public void imageDistance(double distance)
    {
        double TOOFARrssi = 30.0;
        double FARrssi = 15.0;
        double NEARrssi = 5;
        double IMMEDIATErssi = 0.1;

        ImageView view = (ImageView) findViewById(R.id.distanceImage);
        if ((distance == TOOFARrssi) || (distance > FARrssi)) {
            view.setImageResource(R.drawable.kid_map_toofar);


        } else if ((distance == FARrssi) || (distance > NEARrssi)) {
            view.setImageResource(R.drawable.kid_map_far);

        } else if ((distance >= NEARrssi) || (distance > IMMEDIATErssi)) {
            view.setImageResource(R.drawable.kid_map_close);

        } else
            view.setImageResource(R.drawable.kid_map_immediate);


    }




    public void alertDialogue(final double distance)
    {

        if((distance >30)||(exitRegion)) {
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            // set title
            alertDialogBuilder.setTitle("GO CHECK ON YOUR CHILD!");

            // set dialog message
            alertDialogBuilder.setMessage("Have you found your child?");
            alertDialogBuilder.setCancelable(true);
            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int id) {


                    exitRegion = false;

                    dialog.cancel();
                    dialog.dismiss();
                    MainActivity.this.finish();
                }

            });


            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        }}

    public void openPage(View view)
    {
        String button_text;
        button_text = ((Button) view).getText().toString();

        if(button_text.equals("Home"))
        {
            Intent intent = new Intent (this, IntroActivity.class);
            startActivity(intent);
        }
    }


    }

