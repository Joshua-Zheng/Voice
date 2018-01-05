package com.aistrong.voice.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.view.Gravity;
import android.widget.Toast;
import com.aistrong.voice.R;
import com.baidu.speech.VoiceRecognitionService;

public class SpeechRec implements RecognitionListener{
    public interface ResultsCallback {
        void onResults(String result);
    }

    private Context mContext;

    private SpeechRecognizer mSpeechRecognizer;

    private ResultsCallback mResultsCallback;

    public SpeechRec(Context context) {
        mContext = context;
    }

    public synchronized void createTool() {
        if (null == mSpeechRecognizer) {

            // 创建识别器
            mSpeechRecognizer = android.speech.SpeechRecognizer.createSpeechRecognizer(mContext,
                    new ComponentName(mContext, VoiceRecognitionService.class));

            // 注册监听器
            mSpeechRecognizer.setRecognitionListener(this);
        }
    }

    public synchronized void destroyTool() {
        mSpeechRecognizer.stopListening();
        mSpeechRecognizer.destroy();
        mSpeechRecognizer = null;
    }

    // 开始识别
    public void startASR(ResultsCallback callback) {
        mResultsCallback = callback;
        Intent intent = new Intent();
        bindParams(intent);
        mSpeechRecognizer.startListening(intent);
    }

    //停止识别
    public void stopASR() {
        mSpeechRecognizer.stopListening();
        Toast toast = Toast.makeText(mContext, R.string.stop_rec, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void bindParams(Intent intent) {
        // 设置识别参数
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        // 准备就绪
        Toast toast = Toast.makeText(mContext, R.string.start_speak, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void onBeginningOfSpeech() {
        // 开始说话处理
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        // 音量变化处理
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        // 录音数据传出处理
    }

    @Override
    public void onEndOfSpeech() {
        // 说话结束处理
    }

    @Override
    public void onError(int error) {
    }

    @Override
    public void onResults(Bundle results) {

        // 最终结果处理
        if (mResultsCallback != null) {
            String text = results.get(android.speech.SpeechRecognizer.RESULTS_RECOGNITION)
                    .toString().replace("]", "").replace("[", "");
            mResultsCallback.onResults(text);
        }
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        // 临时结果处理
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
    }

}
