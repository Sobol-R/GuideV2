package com.guideapp.guideapp.ttsapi;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TTSApiPost {

    @POST("/v1beta1/text:synthesize?key=AIzaSyDEMLlMGkMeGVyUzTC1_5DfattI_gXRgR")
    Call <TTSResponse> getTTS(@Body TTSBody ttsBody);

}
