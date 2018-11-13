package com.example.tuannh2375.smsspam;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    public static final String mPath = "cc.txt";
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    private docfile file;
    private List<String> mLine;
    private StringBuilder text = new StringBuilder();
  //  public int[] i ={0};
    TextView txt1,txt2;
    Button btn1;
    String phoneNo;
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        txt1 = (TextView) findViewById( R.id.concac );
        txt2 = (TextView) findViewById( R.id.textView );
        btn1 = (Button) findViewById( R.id.button );
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(
                    new InputStreamReader( getAssets().open( "cc.txt" ) ) );

            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                text.append( mLine );
                text.append( '\n' );
            }
        } catch (IOException e) {
            Toast.makeText( getApplicationContext(), "Error reading file!", Toast.LENGTH_LONG ).show();
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }

            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add( text.toString() );

                try {

                    for (int i = 0; i < arrayList.size(); i++) {
                        Thread.sleep( 10000 );
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage( arrayList.get( i ), null, "abc", null, null );
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }



            btn1.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendSMSMessage();
                }
            } );


        }
    }
    private void sendSMSMessage() {

            phoneNo = txt1.getText().toString();
            message = txt2.getText().toString();

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, message, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }
}
