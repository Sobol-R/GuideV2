package com.guideapp.guideapp.ttsapi;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SendTTSRequest {

    String textToBeSpeaked;
    String encodeToken;

    public SendTTSRequest(String textToBeSpeaked) {
        this.textToBeSpeaked = textToBeSpeaked;
    }

    public String getToken() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://texttospeech.googleapis.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TTSApiPost ttsApiPost = retrofit.create(TTSApiPost.class);

        AudioConfig audioConfig = new AudioConfig();
        audioConfig.setAudioEncoding("LINEAR16");
        audioConfig.setPitch("0.00");
        audioConfig.setSpeakingRate("1.00");

        Input input = new Input();
        input.setText(textToBeSpeaked);

        Voice voice = new Voice();
        voice.setLanguageCode("ru-RU");
        voice.setName("ru-RU-Wavenet-A");

        TTSBody ttsBody = new TTSBody();
        ttsBody.setAudioConfig(audioConfig);
        ttsBody.setInput(input);
        ttsBody.setVoice(voice);

        ttsApiPost.getTTS(ttsBody)
                .enqueue(new Callback<TTSResponse>() {
                    @Override
                    public void onResponse(Call<TTSResponse> call, Response<TTSResponse> response) {
                        encodeToken = response.body().token;
                        System.out.println("token : " + encodeToken);
                    }

                    @Override
                    public void onFailure(Call<TTSResponse> call, Throwable t) {

                    }
                });

        return encodeToken;
    }

}
