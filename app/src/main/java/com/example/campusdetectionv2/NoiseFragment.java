package com.example.campusdetectionv2;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;

import android.media.MediaRecorder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;


public class NoiseFragment extends Fragment{

    private AudioRecord ar;
    private int bs;
    private static final int SAMPLE_RATE_IN_HZ = 44100;
    public boolean isRun = false;
    private int BASE = 1;
    private int SPACE = 100;// 间隔取样时间
    private final Handler mHandler = new Handler();
    private TextView textView;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_noise, container, false);
        textView = (TextView)view.findViewById(R.id.noiseTextView);

            // AudioRecord audioRecord.
            /* 获取开始时间* */
            Log.d("tag","开始");
           // updateMicStatus();


        return view;
    }


    @Override
    public void onResume() {

        Thread thread = new MainThread();
        thread.start();
        super.onResume();
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case 1:
                    textView.setText(msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    };;
    class MainThread extends Thread {
        public void run() {

            bs = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ,
                    AudioFormat.CHANNEL_IN_DEFAULT, AudioFormat.ENCODING_PCM_16BIT);
            ar = new AudioRecord(MediaRecorder.AudioSource.MIC,
                    SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_IN_DEFAULT,
                    AudioFormat.ENCODING_PCM_16BIT, bs);
            ar.startRecording();
            isRun = true;
            while (isRun) {
                short[] buffer = new short[bs];
                for(int i=0;i<bs;i++)  buffer[i]=0;
                int r=ar.read(buffer, 0, bs);
                long v = 0;
                for (int i = 0; i < r; i++) {
                    v += buffer[i]*buffer[i];
                }
                //short ww= buffer[3000];
                Log.d("tag", "dB:"+ Math.round(10*Math.log10(v/(double)r)));
                Message msg = new Message();
                msg.what = 1;
                msg.obj = (int)Math.round(10*Math.log10(v/r));//单位是dB
                handler.sendMessage(msg);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            ar.stop();
        }
    }



}
