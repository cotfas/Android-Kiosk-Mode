package com.cashdivider.deviceadmin.other;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

/**
 * Created by cotfas on 3/21/2016.
 */
public class RecordAudio {

    public static boolean isAlarmRunning = false;

    private static final int sampleRate = 8000;
    private AudioRecord audio;
    private int bufferSize;
    private double lastLevel = 0;
    private Thread thread;
    private static final int SAMPLE_DELAY = 75;


    private void init() {
        try {
            bufferSize = AudioRecord
                    .getMinBufferSize(sampleRate, AudioFormat.CHANNEL_IN_MONO,
                            AudioFormat.ENCODING_PCM_16BIT);
        } catch (Exception e) {
            android.util.Log.e("TrackingFlow", "Exception", e);
        }
    }

    public void start() {
        if (isRunning()) {
            return;
        }

        init();

        audio = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT, bufferSize);

        audio.startRecording();
        thread = new Thread(new Runnable() {
            public void run() {
                while(thread != null && !thread.isInterrupted()){
                    //Let's make the thread sleep for a the approximate sampling time
                    try{Thread.sleep(SAMPLE_DELAY);}catch(InterruptedException ie){ie.printStackTrace();}
                    readAudioBuffer();//After this call we can get the last value assigned to the lastLevel variable


                    Log.i("RecordAudio", " sound level is " + lastLevel);


                    if(lastLevel < 100){
                        isAlarmRunning = false;

                        //stop();
                    }else
                    {
                        isAlarmRunning = true;
                    }

                    /*runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if(lastLevel > 0 && lastLevel <= 50){
                                mouthImage.setImageResource(R.drawable.mouth4);
                            }else
                            if(lastLevel > 50 && lastLevel <= 100){
                                mouthImage.setImageResource(R.drawable.mouth3);
                            }else
                            if(lastLevel > 100 && lastLevel <= 170){
                                mouthImage.setImageResource(R.drawable.mouth2);
                            }
                            if(lastLevel > 170){
                                mouthImage.setImageResource(R.drawable.mouth1);
                            }
                        }
                    });*/
                }
            }
        });
        thread.start();
    }

    private void stop() {
        try {
            thread.interrupt();
            thread = null;

            if (audio != null) {
                audio.stop();
                audio.release();
                audio = null;
            }
        } catch (Exception e) {e.printStackTrace();}
    }

    private boolean isRunning() {
        if (thread == null || audio == null) {
            return false;
        }

        return true;
    }

    /**
     * Functionality that gets the sound level out of the sample
     */
    private void readAudioBuffer() {

        try {
            short[] buffer = new short[bufferSize];

            int bufferReadResult = 1;

            if (audio != null) {

                // Sense the voice...
                bufferReadResult = audio.read(buffer, 0, bufferSize);
                double sumLevel = 0;
                for (int i = 0; i < bufferReadResult; i++) {
                    sumLevel += buffer[i];
                }
                lastLevel = Math.abs((sumLevel / bufferReadResult));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
