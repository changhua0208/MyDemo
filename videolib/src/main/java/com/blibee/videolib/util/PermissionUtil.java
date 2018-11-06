package com.blibee.videolib.util;

import android.hardware.Camera;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;

public class PermissionUtil {

    /**
     * 返回 false 则没有权限
     *
     * @return
     */
    public static boolean hasCameraPermission() {
        Camera camera = null;
        try {
            camera = Camera.open(0);
            if (Build.MANUFACTURER.equals("Meizu")) {
                Camera.Parameters mParameters = camera.getParameters();
                camera.setParameters(mParameters);
                camera.startPreview();
                camera.stopPreview();
                return true;
            } else {
                return camera != null;
            }
        } catch (Exception e) {
            return false;
        } finally {
            if (camera != null) {
                camera.release();
            }
        }
    }


    /**
     * 判断是是否有录音权限
     */
    public static boolean hasAudioPermission() {

        try {
            // 音频获取源i
            int audioSource = MediaRecorder.AudioSource.MIC;
            // 设置音频采样率，44100是目前的标准，但是某些设备仍然支持22050，16000，11025
            int sampleRateInHz = 44100;
            // 设置音频的录制的声道CHANNEL_IN_STEREO为双声道，CHANNEL_CONFIGURATION_MONO为单声道
            int channelConfig = AudioFormat.CHANNEL_IN_STEREO;
            // 音频数据格式:PCM 16位每个样本。保证设备支持。PCM 8位每个样本。不一定能得到设备支持。
            int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
            // 缓冲区字节大小
            int bufferSizeInBytes = 0;
            bufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRateInHz,
                    channelConfig, audioFormat);
            AudioRecord audioRecord = new AudioRecord(audioSource, sampleRateInHz,
                    channelConfig, audioFormat, bufferSizeInBytes);
            //开始录制音频

            // 防止某些手机崩溃，例如联想
            audioRecord.startRecording();
            /**
             * 根据开始录音判断是否有录音权限
             */
            if (audioRecord.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING) {
                return false;
            }
            audioRecord.stop();
            audioRecord.release();
            return true;
        } catch (Exception e) {
            return false;
        }

    }
}
