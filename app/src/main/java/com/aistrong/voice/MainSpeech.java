package com.aistrong.voice;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
import com.aistrong.voice.chatui.ChatMessagesAdapter;
import com.aistrong.voice.chatui.InsetDecoration;
import com.aistrong.voice.chatui.ChatMessage;
import com.aistrong.voice.chatui.ChatMessageModel;
import com.aistrong.voice.util.CleanCache;
import com.aistrong.voice.util.CompareVersion;
import com.aistrong.voice.util.JSONModel;
import com.aistrong.voice.util.PostUtil;
import com.aistrong.voice.util.SpeechRec;
import com.aistrong.voice.util.VoiceUtils;
import com.baidu.tts.client.SpeechSynthesizer;
import util.UpdateAppUtils;


public class MainSpeech extends AppCompatActivity implements SpeechRec.ResultsCallback,
        ChatMessagesAdapter.OnItemClickListener,ChatMessagesAdapter.OnInformRecyclerViewToScrollDownListener{

    public static int flag = 0;
    public static int taskNum = 0;
    final String LOGIN_URL = "http://119.29.193.252/gddw/getReply?userid=";

    private PostUtil postUtil  = new PostUtil();
    private JSONModel jsonModel = new JSONModel();
    private SpeechRec speechRec = new SpeechRec(this);
    private VoiceUtils utils = new VoiceUtils();
    private SpeechSynthesizer mSpeechSynthesizer;
    private RecyclerView mRecyclerView;
    ChatMessagesAdapter mAdapter;
    LinearLayoutManager mVerticalManager;

    ImageButton mPreSpeechButton, mSendButton;
    private Button mSpeechButton;
    private EditText mEditText, mRecEditText;
    private String mJsonMsg, mSpeakContent, mTextContent, mCounterpartJid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speech_main);

        Toolbar mToolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        //初始化
        mPreSpeechButton = findViewById(R.id.preSpeechBtn);
        mSpeechButton = findViewById(R.id.speechBtn);
        mSendButton = findViewById(R.id.sendBtn);
        mEditText = findViewById(R.id.edit_text);
        mRecEditText = findViewById(R.id.rec_edit_text);
        mRecyclerView = findViewById(R.id.chat_message_recycler_view);

        //主界面
        mCounterpartJid = MainLogin.sendUserID;
        mVerticalManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAdapter = new ChatMessagesAdapter(this,mCounterpartJid);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setmOnInformRecyclerViewToScrollDownListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new InsetDecoration(this));
        mRecyclerView.setLayoutManager(mVerticalManager);

        //进入应用自动播放欢迎语音
        utils.init(this,0);
        mSpeechSynthesizer = utils.getSyntheszer();
        this.mSpeechSynthesizer.speak(getString(R.string.welcome));

        //Speech Button初始化
        SpeechRecButton sp = new SpeechRecButton();
        mSpeechButton.setOnTouchListener(sp);

        //开启语音识别按钮功能
        mPreSpeechButton.setOnClickListener(v ->{
            if (flag == 1) {
                // 第一次单击触发的事件
                mPreSpeechButton.setImageDrawable(getResources().getDrawable(R.drawable.speech));
                mEditText.getText().clear();
                mEditText.setVisibility(View.GONE);
                mSpeechButton.setVisibility(View.VISIBLE);
                flag = 0;
            } else {
                mPreSpeechButton.setImageDrawable(getResources().getDrawable(R.drawable.keyboard));
                mRecEditText.getText().clear();
                mRecEditText.setVisibility(View.GONE);
                mSpeechButton.setVisibility(View.GONE);
                mEditText.setVisibility(View.VISIBLE);
                flag = 1;
            }
        });

        //通过mEditText改变发送按钮的颜色
        mEditText.addTextChangedListener(textChange);
        mRecEditText.addTextChangedListener(textChange);

        //发送按钮
        mSendButton.setOnClickListener(v ->{
            mSpeakContent = mRecEditText.getText().toString();
            mTextContent = mEditText.getText().toString();
            if (TextUtils.isEmpty(mSpeakContent) && TextUtils.isEmpty(mTextContent)) {
                Toast toast = Toast.makeText(MainSpeech.this, R.string.null_text, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else if (!TextUtils.isEmpty(mSpeakContent)){
                mRecEditText.getText().clear();
                mRecEditText.setVisibility(View.GONE);
                autoSpeak(mSpeakContent);
            }else {
                mEditText.getText().clear();
                mEditText.setVisibility(View.GONE);
                mSpeechButton.setVisibility(View.VISIBLE);
                autoSpeak(mTextContent);
                mPreSpeechButton.setImageDrawable(getResources().getDrawable(R.drawable.speech));
                flag = 0;
            }
        });

        //Android 2.3 版本之后，需要强制设置主线程权限
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setTitle(getString(R.string.login_id)+ MainLogin.sendUserID);
    }

    //语音识别按钮
    class SpeechRecButton implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (v.getId() == R.id.speechBtn) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    mSpeechSynthesizer.pause();
                    speechRec.startASR(MainSpeech.this);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    speechRec.stopASR();
                    v.performClick();
                }
            }
            return false;
        }
    }

    //语音识别,将识别结果写入mEditText
    @Override
    public void onResults(String result) {
        MainSpeech.this.runOnUiThread(() -> {
            if (mRecEditText.getVisibility() == View.GONE) {
                mRecEditText.setVisibility(View.VISIBLE);
            }
            mRecEditText.setText(result);
        });
    }

    //语音首次自动播放
    private void autoSpeak(String s){
        String getMsg;
        if(s.equals("天王盖地虎") || s.equals("clear") || s.equals("quit")){
            ChatMessageModel.get(getApplicationContext(),mCounterpartJid).addMessage(new ChatMessage("#"+ s,System.currentTimeMillis(), ChatMessage.Type.SENT,mCounterpartJid));
            getMsg = postUtil.LoginByPost(LOGIN_URL, MainLogin.sendUserID,"&query=","#" + s);
        }else {
            ChatMessageModel.get(getApplicationContext(),mCounterpartJid).addMessage(new ChatMessage(s,System.currentTimeMillis(), ChatMessage.Type.SENT,mCounterpartJid));
            getMsg = postUtil.LoginByPost(LOGIN_URL, MainLogin.sendUserID, "&query=",s);
        }
        this.mSpeechSynthesizer.pause();
        mJsonMsg = jsonModel.toJOSN(getMsg);
        ChatMessageModel.get(getApplicationContext(),mCounterpartJid).addMessage(new ChatMessage(mJsonMsg,System.currentTimeMillis(), ChatMessage.Type.RECEIVED,mCounterpartJid));
        utils.init(this,0);
        mSpeechSynthesizer = utils.getSyntheszer();
        this.mSpeechSynthesizer.speak(mJsonMsg);
}

    //语音合成 普通女声 用于System
    public void puTongWoman(View view){
        speakWomen();
    }
    private void speakWomen() {
        this.mSpeechSynthesizer.pause();
        utils.init(this,0);
        mSpeechSynthesizer = utils.getSyntheszer();
        //需要合成的文本text的长度不能超过1024个GBK字节。
        this.mSpeechSynthesizer.speak(mJsonMsg);
    }

    //语音合成 普通男声 用于User
    public void puTongMan(View view){
        speakMan();
    }
    private void speakMan() {
        this.mSpeechSynthesizer.pause();
        utils.init(this,1);
        mSpeechSynthesizer = utils.getSyntheszer();
        //需要合成的文本text的长度不能超过1024个GBK字节。
        this.mSpeechSynthesizer.speak(mSpeakContent);
    }

    //右上角菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.breakOff){
            mSpeakContent = "quit";
            mRecEditText.getText().clear();
            mRecEditText.setVisibility(View.GONE);
            mEditText.getText().clear();
            autoSpeak(mSpeakContent);
        }else if (id == R.id.clearState){
            mSpeakContent = "clear";
            mRecEditText.getText().clear();
            mRecEditText.setVisibility(View.GONE);
            mEditText.getText().clear();
            autoSpeak(mSpeakContent);
        }else if (id == R.id.update){
            CompareVersion compareVersion = new CompareVersion();
            if (compareVersion.getLocalVersion(this).compareTo(compareVersion.getNetVersion()) >= 0){
                AlertDialog.Builder builder = new AlertDialog.Builder(MainSpeech.this);
                builder.setTitle(R.string.dialog_title);
                builder.setMessage(R.string.last_version);
                builder.setPositiveButton(R.string.dialog_confirm, (dialogInterface, i) -> dialogInterface.cancel());
                AlertDialog dialog = builder.create();
                dialog.show();
            }else {
                UpdateAppUtils.from(this)
                        .checkBy(UpdateAppUtils.CHECK_BY_VERSION_NAME)
                        .serverVersionName(compareVersion.getNetVersion())
                        .apkPath("https://coding.net/u/Joshua-Zheng/p/Voice/git/raw/master/voice.apk")
                        .update();
            }
        }else if (id == R.id.logout){
            new MainLogin().clearInfo();
            Intent intent = new Intent(MainSpeech.this,MainLogin.class);
            startActivity(intent);
            CleanCache.cleanDatabases(this);
            CleanCache.cleanInternalCache(this);
            MainSpeech.this.finish();
            MainSpeech.this.onDestroy();
            System.exit(0);
        }else if (id == R.id.relate_task){
            taskNum = 3;
            Intent intent = new Intent(MainSpeech.this,MainTask.class);
            startActivity(intent);
        }else if (id == R.id.create_task){
            taskNum = 1;
            Intent intent = new Intent(MainSpeech.this,MainTask.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    //实时监听mEditText输入情况，改变发送颜色
    TextWatcher textChange = new TextWatcher(){
        @Override
        public void afterTextChanged(Editable s) {
            if(mEditText.length()>0 || mRecEditText.length()>0){
                mSendButton.setImageDrawable(getResources().getDrawable(R.drawable.send_ready));
            }
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            mSendButton.setImageDrawable(getResources().getDrawable(R.drawable.send));
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }};

     //双击返回键退出应用
    private long firstTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK && event.getAction()==KeyEvent.ACTION_DOWN){
            if (System.currentTimeMillis()-firstTime>2000){
                Toast toast = Toast.makeText(MainSpeech.this,R.string.exit,Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                firstTime=System.currentTimeMillis();
            }else{
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onItemClick(ChatMessagesAdapter.ChatMessageViewHolder item, int position) {
        Toast.makeText(this, item.getSummary(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInformRecyclerViewToScrollDown(int size) {
        mRecyclerView.scrollToPosition(size-1);
    }

    private void scrollRecycerViewDown() {
        ChatMessagesAdapter adapter = (ChatMessagesAdapter)  mRecyclerView.getAdapter();
        adapter.notifyViewToScrollDown();
    }

    @Override
    protected void onResume() {
        scrollRecycerViewDown();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        this.mSpeechSynthesizer.release();
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        speechRec.createTool();
        super.onStart();
    }

    @Override
    protected void onStop() {
        speechRec.destroyTool();
        super.onStop();
    }

}
