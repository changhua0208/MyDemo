package com.jch.mydemo.verify;

import android.content.Context;

import com.jch.mydemo.R;
import com.oliveapp.face.liboffline_face_verification.FaceVerifier;

import java.io.IOException;

public class ServiceFactory {
    private static ServiceFactory instance;

    private final FaceVerifier faceVerifier;

    public static synchronized void init(Context context) throws IOException {

        if (instance == null) instance = new ServiceFactory(context);
    }

    private ServiceFactory(Context context) throws IOException {
        faceVerifier = new FaceVerifier(context, R.raw.offline_verification_data);
    }

    public static synchronized ServiceFactory getInstance() {
            return instance;
    }

    public FaceVerifier getFaceVerifier() {
        return faceVerifier;
    }
}