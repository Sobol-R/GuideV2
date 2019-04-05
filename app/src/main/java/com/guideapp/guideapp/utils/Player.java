package com.guideapp.guideapp.utils;

import android.media.MediaPlayer;
import android.util.Base64;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Player {

    public static MediaPlayer mediaPlayer = new MediaPlayer();

    public static void prepare(String encoded) {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                EventBus.getDefault().post(new CompleteEvent());
            }
        });

        byte[] decoded = Base64.decode(encoded, 0);
        try {
            File tempMp3 = File.createTempFile("test", "mp3");
            tempMp3.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(tempMp3);
            fos.write(decoded);
            fos.close();
            mediaPlayer.reset();

            FileInputStream fis = new FileInputStream(tempMp3);
            mediaPlayer.setDataSource(fis.getFD());

            mediaPlayer.prepare();
        } catch (IOException ex) {
            String s = ex.toString();
            ex.printStackTrace();
        }
    }

    public static void play() {
        mediaPlayer.start();
    }

    public static void stop() {
        mediaPlayer.pause();
    }

    public static class CompleteEvent {
    }
}
