package com.example.suraksha;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;


@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
@SuppressLint("HandlerLeak")

public class BgService extends Service implements AccelerometerListener, LocationListener {

    DbHandler db;
    protected LocationManager locationManager;
    protected double latitude, longitude;


    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    String provider;

    @Override
    public void onLocationChanged(Location location) {

        latitude = location.getLatitude();
        longitude = location.getLongitude();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");

    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");

    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");

    }

    // Handler that receives messages from the thread.
    private final class ServiceHandler extends Handler {

        public ServiceHandler(Looper looper) {

            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {

            // REPLACE THIS CODE WITH YOUR APP CODE
            // Wait before Toasting Service Message
            // to give the Service Started message time to display.

            // Toast Service Message.
	/*  		Context context = getApplicationContext();
			CharSequence text = "Service Message";
			int duration = Toast.LENGTH_LONG;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
	*/

            // Service can stop itself using the stopSelf() method.
            // Not using in this app.  Example statement shown below.
            //stopSelf(msg.arg1);
        }
    }


    @Override
    public IBinder onBind(Intent arg0) {

        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();


        db = new DbHandler(BgService.this);

        if (AccelerometerManager.isSupported(this)) {

            AccelerometerManager.startListening(this);
        }
        HandlerThread thread = new HandlerThread("ServiceStartArguments", android.os.Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        mServiceLooper = thread.getLooper();

        mServiceHandler = new ServiceHandler(mServiceLooper);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Criteria c=new Criteria();
        provider= locationManager.getBestProvider(c,false);
        if(provider!=null && !provider.equals("")) {
            Location location = locationManager.getLastKnownLocation(provider);
            locationManager.requestLocationUpdates(provider, 20000, 1, this);
            if (location != null)
                onLocationChanged(location);
            else
                Toast.makeText(getBaseContext(), "Location cannot be retrived", Toast.LENGTH_SHORT).show();

        }else {
            Toast.makeText(getBaseContext(), "No provider found", Toast.LENGTH_SHORT).show();

        }



    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // Get message from message pool using handler.
        Message msg = mServiceHandler.obtainMessage();

        // Set start ID (unique to the specific start) in message.
        msg.arg1 = startId;

        // Send message to start job.
        mServiceHandler.sendMessage(msg);

        // Toast Service Started message.
        //	Context context = getApplicationContext();




	/*	CharSequence text = "Service Started";
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
    */

        // Start a sticky.
        return START_STICKY;
    }







    @Override
    public void onAccelerationChanged(float x, float y, float z) {
        // TODO Auto-generated method stub

    }


    MediaPlayer mp;
    boolean flashLightStatus = false;
    int i=0;

    @Override
    public void onShake(float force) {


        if(db.check()==1) {

            final boolean hasCameraFlash = getPackageManager().
                    hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
            boolean isEnabled = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED;

            try {

            Cursor rs = db.getData();
            rs.moveToFirst();

            SmsManager msg = SmsManager.getDefault();



           Geocoder geocoder = new Geocoder(BgService.this,Locale.getDefault());

                latitude=18.52145292;
                longitude=73.83893725;

              List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);


              String addr="";
              StringBuffer smsBody = new StringBuffer();
              smsBody.append("HELP Its an Emergency!!! trace me at ");
              smsBody.append("http://maps.google.com/maps?daddr=");
              smsBody.append(latitude);
              smsBody.append(",");
              smsBody.append(longitude);
              smsBody.append(" !!! \n ");


              String msgbody = smsBody.toString();

                  msg.sendTextMessage(rs.getString(1), null, msgbody, null, null);

                  if (!rs.getString(2).isEmpty())
                      msg.sendTextMessage(rs.getString(2), null, msgbody, null, null);

                  if (!rs.getString(3).isEmpty())
                      msg.sendTextMessage(rs.getString(3), null, msgbody, null, null);


                if (addresses.size() > 0) {
                    addr = addresses.get(0).getAddressLine(0);

                    msg.sendTextMessage(rs.getString(1),null,"Address: "+addr,null,null);

                    if (!rs.getString(2).isEmpty())
                        msg.sendTextMessage(rs.getString(2), null, "Address: "+addr, null, null);

                    if (!rs.getString(3).isEmpty())
                        msg.sendTextMessage(rs.getString(3), null, "Address: "+addr, null, null);


                    }

                mp=MediaPlayer.create(getApplicationContext(),R.raw.song);// the song is a filename which i have pasted inside a folder **raw** created under the **res** folder.//
                mp.start();

                Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show();

                if (hasCameraFlash) {

                    while (i<10) {
                        flashLightOn();
                        flashLightOff();
                        i++;
                    }
                    call();
                } else {
                    Toast.makeText(this, "No flash available on your device",
                            Toast.LENGTH_SHORT).show();
                }





            }
          catch (IOException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
              Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
          }
        }
        else
        {
            Toast.makeText(this, "Registration Required", Toast.LENGTH_SHORT).show();
        }

    }

    int cnt=0;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void flashLightOn() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraManager.setTorchMode(cameraId, true);
            }
            flashLightStatus = true;
            Thread.sleep(500);

        } catch (CameraAccessException e) {
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void flashLightOff() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraManager.setTorchMode(cameraId, false);
            }
            flashLightStatus = false;
            Thread.sleep(500);

        } catch (CameraAccessException e) {
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void call(){


        Cursor rs = db.getData();
        rs.moveToFirst();

        Toast.makeText(this, "Calling... "+rs.getString(1), Toast.LENGTH_SHORT).show();

        String num = "tel:"+rs.getString(1);
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse(num));

        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);
    }


    // onDestroy method.   Display toast that service has stopped.
    @Override
    public void onDestroy() {
        mp.release();
        super.onDestroy();

        // Toast Service Stopped.
        Context context = getApplicationContext();

        Log.i("Sensor", "Service  distroy");

        if (AccelerometerManager.isListening()) {

            AccelerometerManager.stopListening();

        }

        CharSequence text = "Suraksha Service Stopped";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();


    }



}
