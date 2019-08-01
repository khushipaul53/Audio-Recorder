package com.example.audiorecoder;

import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Random;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Environment.getExternalStorageDirectory;

public class MainActivity extends AppCompatActivity {


    public static final int RequestPermissionCode = 1;
    private ImageView imgMic;
    private Button btnStop, btnRecord, btnPlay, btnStopPlay;
    private String AudioSavePathInDevice = null;
    private MediaRecorder mediaRecorder;
    private Random random;
    private String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    private MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        initViews();
        setContentView(R.layout.activity_main);
        btnRecord = findViewById(R.id.button);
        btnStop = findViewById(R.id.button2);
        btnPlay = findViewById(R.id.button3);
        btnStopPlay = findViewById(R.id.button4);


        random = new Random();

        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkPermission()) {

                    AudioSavePathInDevice = getExternalStorageDirectory().getAbsolutePath() + "/" + CreateRandomAudioFileName(5) + "AudioRecording.3gp";

                    MediaRecorderReady();

                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (IllegalStateException e) {

                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    Toast.makeText(MainActivity.this, "Recording started", Toast.LENGTH_LONG).show();
                } else {

                    requestPermission();

                }

            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mediaRecorder.stop();
                Toast.makeText(MainActivity.this, "Recording Completed", Toast.LENGTH_LONG).show();

            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws IllegalArgumentException, SecurityException, IllegalStateException {


                mediaPlayer = new MediaPlayer();

                try {
                    mediaPlayer.setDataSource(AudioSavePathInDevice);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaPlayer.start();

                Toast.makeText(MainActivity.this, "Recording Playing", Toast.LENGTH_LONG).show();

            }
        });

        btnStopPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (mediaPlayer != null) {

                    mediaPlayer.stop();
                    mediaPlayer.release();

                    MediaRecorderReady();

                } else {
                    Toast.makeText(MainActivity.this, "Recording Stopped", Toast.LENGTH_LONG).show();


                }

            }
        });
    }

//

    public void MediaRecorderReady() {

        mediaRecorder = new MediaRecorder();

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);

        mediaRecorder.setOutputFile(AudioSavePathInDevice);

    }

    public String CreateRandomAudioFileName(int string) {

        StringBuilder stringBuilder = new StringBuilder(string);

        int i = 0;
        while (i < string) {

            stringBuilder.append(RandomAudioFileName.charAt(random.nextInt(RandomAudioFileName.length())));

            i++;
        }
        return stringBuilder.toString();

    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {

                    boolean StoragePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {

                        Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();

                    }
                }

                break;
        }
    }

    public boolean checkPermission() {

        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

}

