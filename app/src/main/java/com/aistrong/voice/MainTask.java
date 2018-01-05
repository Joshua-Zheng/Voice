package com.aistrong.voice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.aistrong.voice.util.JSONModel;
import com.aistrong.voice.util.PostUtil;


public class MainTask extends AppCompatActivity {

    final String TASK_URL = "http://119.29.193.252/gddw/getTaskList?userid=";

    private PostUtil postUtil  = new PostUtil();
    private JSONModel jsonModel = new JSONModel();
    Button mAllTask, mFirstTask, mSecondTask, mThirdTask;
    TextView mTask1Name, mTask3Name, mTask1, mTask3;
    String getTask, mJsonTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_main);

        mAllTask = findViewById(R.id.all_task);
        mFirstTask = findViewById(R.id.first_task);
        mSecondTask = findViewById(R.id.second_task);
        mThirdTask = findViewById(R.id.third_task);
        mTask1Name = findViewById(R.id.task1_name);
        mTask3Name = findViewById(R.id.task3_name);
        mTask1 = findViewById(R.id.task1);
        mTask3 = findViewById(R.id.task3);
        mAllTask.setBackgroundColor(getResources().getColor(R.color.ready_send));

        if (MainSpeech.taskNum == 1){
            mTask1Name.setVisibility(View.VISIBLE);
            mTask1Name.setText("\"" + MainLogin.sendUserID+"\"创建的任务：");
            mTask1.setVisibility(View.VISIBLE);
            getTask = postUtil.LoginByPost(TASK_URL, MainLogin.sendUserID,"&type=", String.valueOf(MainSpeech.taskNum));
            mJsonTask = jsonModel.taskJOSN(getTask);
            mTask1.setText(mJsonTask);
            mAllTask.setOnClickListener( v -> {
                mJsonTask = jsonModel.taskJOSN(getTask);
                mTask1.setText(mJsonTask);
                mAllTask.setBackgroundColor(getResources().getColor(R.color.ready_send));
                mAllTask.setTextColor(getResources().getColor(R.color.default_bubble_color_rcv));
                mFirstTask.getBackground().setAlpha(0);
                mFirstTask.setTextColor(getResources().getColor(R.color.default_message_color_rcv));
                mSecondTask.getBackground().setAlpha(0);
                mSecondTask.setTextColor(getResources().getColor(R.color.default_message_color_rcv));
                mThirdTask.getBackground().setAlpha(0);
                mThirdTask.setTextColor(getResources().getColor(R.color.default_message_color_rcv));
            });
            mFirstTask.setOnClickListener( v -> {
                mJsonTask = jsonModel.task1JOSN(getTask);
                mTask1.setText(mJsonTask);
                mFirstTask.setBackgroundColor(getResources().getColor(R.color.ready_send));
                mFirstTask.setTextColor(getResources().getColor(R.color.default_bubble_color_rcv));
                mAllTask.getBackground().setAlpha(0);
                mAllTask.setTextColor(getResources().getColor(R.color.default_message_color_rcv));
                mSecondTask.getBackground().setAlpha(0);
                mSecondTask.setTextColor(getResources().getColor(R.color.default_message_color_rcv));
                mThirdTask.getBackground().setAlpha(0);
                mThirdTask.setTextColor(getResources().getColor(R.color.default_message_color_rcv));
            });
            mSecondTask.setOnClickListener( v -> {
                mJsonTask = jsonModel.task2JOSN(getTask);
                mTask1.setText(mJsonTask);
                mSecondTask.setBackgroundColor(getResources().getColor(R.color.ready_send));
                mSecondTask.setTextColor(getResources().getColor(R.color.default_bubble_color_rcv));
                mAllTask.getBackground().setAlpha(0);
                mAllTask.setTextColor(getResources().getColor(R.color.default_message_color_rcv));
                mFirstTask.getBackground().setAlpha(0);
                mFirstTask.setTextColor(getResources().getColor(R.color.default_message_color_rcv));
                mThirdTask.getBackground().setAlpha(0);
                mThirdTask.setTextColor(getResources().getColor(R.color.default_message_color_rcv));
            });
            mThirdTask.setOnClickListener( v -> {
                mJsonTask = jsonModel.task3JOSN(getTask);
                mTask1.setText(mJsonTask);
                mThirdTask.setBackgroundColor(getResources().getColor(R.color.ready_send));
                mThirdTask.setTextColor(getResources().getColor(R.color.default_bubble_color_rcv));
                mAllTask.getBackground().setAlpha(0);
                mAllTask.setTextColor(getResources().getColor(R.color.default_message_color_rcv));
                mFirstTask.getBackground().setAlpha(0);
                mFirstTask.setTextColor(getResources().getColor(R.color.default_message_color_rcv));
                mSecondTask.getBackground().setAlpha(0);
                mSecondTask.setTextColor(getResources().getColor(R.color.default_message_color_rcv));
            });
        }else if (MainSpeech.taskNum == 3){
            mTask3Name.setVisibility(View.VISIBLE);
            mTask3Name.setText("\"" + MainLogin.sendUserID+"\"相关的任务：");
            mTask3.setVisibility(View.VISIBLE);
            getTask = postUtil.LoginByPost(TASK_URL, MainLogin.sendUserID,"&type=", String.valueOf(MainSpeech.taskNum));
            mJsonTask = jsonModel.taskJOSN(getTask);
            mTask3.setText(mJsonTask);
            mAllTask.setOnClickListener( v -> {
                mJsonTask = jsonModel.taskJOSN(getTask);
                mTask3.setText(mJsonTask);
                mAllTask.setBackgroundColor(getResources().getColor(R.color.ready_send));
                mAllTask.setTextColor(getResources().getColor(R.color.default_bubble_color_rcv));
                mFirstTask.getBackground().setAlpha(0);
                mFirstTask.setTextColor(getResources().getColor(R.color.default_message_color_rcv));
                mSecondTask.getBackground().setAlpha(0);
                mSecondTask.setTextColor(getResources().getColor(R.color.default_message_color_rcv));
                mThirdTask.getBackground().setAlpha(0);
                mThirdTask.setTextColor(getResources().getColor(R.color.default_message_color_rcv));
            });
            mFirstTask.setOnClickListener( v -> {
                mJsonTask = jsonModel.task1JOSN(getTask);
                mTask3.setText(mJsonTask);
                mFirstTask.setBackgroundColor(getResources().getColor(R.color.ready_send));
                mFirstTask.setTextColor(getResources().getColor(R.color.default_bubble_color_rcv));
                mAllTask.getBackground().setAlpha(0);
                mAllTask.setTextColor(getResources().getColor(R.color.default_message_color_rcv));
                mSecondTask.getBackground().setAlpha(0);
                mSecondTask.setTextColor(getResources().getColor(R.color.default_message_color_rcv));
                mThirdTask.getBackground().setAlpha(0);
                mThirdTask.setTextColor(getResources().getColor(R.color.default_message_color_rcv));
            });
            mSecondTask.setOnClickListener( v -> {
                mJsonTask = jsonModel.task2JOSN(getTask);
                mTask3.setText(mJsonTask);
                mSecondTask.setBackgroundColor(getResources().getColor(R.color.ready_send));
                mSecondTask.setTextColor(getResources().getColor(R.color.default_bubble_color_rcv));
                mAllTask.getBackground().setAlpha(0);
                mAllTask.setTextColor(getResources().getColor(R.color.default_message_color_rcv));
                mFirstTask.getBackground().setAlpha(0);
                mFirstTask.setTextColor(getResources().getColor(R.color.default_message_color_rcv));
                mThirdTask.getBackground().setAlpha(0);
                mThirdTask.setTextColor(getResources().getColor(R.color.default_message_color_rcv));
            });
            mThirdTask.setOnClickListener( v -> {
                mJsonTask = jsonModel.task3JOSN(getTask);
                mTask3.setText(mJsonTask);
                mThirdTask.setBackgroundColor(getResources().getColor(R.color.ready_send));
                mThirdTask.setTextColor(getResources().getColor(R.color.default_bubble_color_rcv));
                mAllTask.getBackground().setAlpha(0);
                mAllTask.setTextColor(getResources().getColor(R.color.default_message_color_rcv));
                mFirstTask.getBackground().setAlpha(0);
                mFirstTask.setTextColor(getResources().getColor(R.color.default_message_color_rcv));
                mSecondTask.getBackground().setAlpha(0);
                mSecondTask.setTextColor(getResources().getColor(R.color.default_message_color_rcv));
            });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK && event.getAction()==KeyEvent.ACTION_DOWN){
            MainTask.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
