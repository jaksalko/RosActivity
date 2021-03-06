package com.example.moon.quad2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.ros.address.InetAddressFactory;
import org.ros.android.RosActivity;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

import java.util.ArrayList;
import java.util.List;

public class RRosActivity extends RosActivity {

    public static List<Publisherr> publisherList;

    Publisherr publisher = new Publisherr();

    Intent intent_voice;
    SpeechRecognizer mRecognizer;
    int desNum = 0 ;

    protected RRosActivity() {
        super("Example", "Example");
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "RosActivity onCreate", Toast.LENGTH_LONG).show();
        final TextView text = findViewById(R.id.textView1);
        Button btn = findViewById(R.id.btn);
        Button sendbtn = findViewById(R.id.sendbtn);

        //음성인식 intent
        intent_voice = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent_voice.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent_voice.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko_KR");

        //LISTENER ->> 음성인식
        RecognitionListener listener = new RecognitionListener() {
            @Override
            public void onRmsChanged(float rmsdB) {
                // TODO Auto-generated method stub
            }

            //음성인식 결과처리
            @Override
            public void onResults(Bundle results) {

                Log.d("tag", "On Results");
                String voice_text = "";
                int success_count = 0;
                voice_text = SpeechRecognizer.RESULTS_RECOGNITION;
                ArrayList mResult = results.getStringArrayList(voice_text);
                String[] rs = new String[mResult.size()];
                mResult.toArray(rs);
                text.setText("" + rs[0]);                                       //음성인식 저장
                String[] destination = {"화전역" ,"뷁쉛","뷁쉛", "과학관", "뷁쉛","뷁쉛","전자관","착륙"};//목표 위치 배열 화전역(0) -> 과학관(3) -> 전자관(6) -> 착륙(7)
                                                                                                    //나머지 배열 idx는 스팟값으로 사용하지 않는다

                for (int i = 0; i < 8; i++) {//음성인식과 목표위치 비교
                    Log.d("tag",rs[0] + destination[i]+"++++++++++++++++++++++++++++++++++++++++++=");
                    if (destination[i].equals(rs[0])) {
                        Log.d("tag",rs[0] + destination[i]+"++++++++++++++++++++++++++++++++++++++++++=");
                        if(i<7)
                        {
                            Toast.makeText(RRosActivity.this, rs[0] + "(으)로 안내를 시작합니다.", Toast.LENGTH_SHORT).show();
                        }
                        if(i == 7)
                            Toast.makeText(RRosActivity.this, "착륙합니다", Toast.LENGTH_SHORT).show();

                        success_count = 1;
                        desNum = i;
                    }

                }
                if (success_count == 0) {
                    Toast.makeText(RRosActivity.this, "다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onReadyForSpeech(Bundle params) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onEvent(int eventType, Bundle params) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onError(int error) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onEndOfSpeech() {
                // TODO Auto-generated method stub
            }

            @Override
            public void onBufferReceived(byte[] buffer) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onBeginningOfSpeech() { // TODO Auto-generated method stub
            }
        };

        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(listener);


        btn.setOnClickListener(new View.OnClickListener() {//BTN CLICK LISTENER
            @Override
            public void onClick(View v) {
                Log.d("tag", "Button Listener***********************************8");
                mRecognizer.startListening(intent_voice);//음성인식 시작
            }
        });

        sendbtn.setOnClickListener(new View.OnClickListener() {//Send 버튼 클릭 시 ROS에 데이터 전송(미구현상태)
            @Override
            public void onClick(View v) {
                //desNum(목표위치 idx)를 전송
                publisher.setDatas(desNum);
            }
        });

    }

    //IS CALLED WHEN IP IS  ENTERED
    @Override
    protected void init(NodeMainExecutor nodeMainExecutor) {
    //At this point, the user has already been prompted to either enter the URI
    // of a master to use or to start a master locally.

    //The user can easily use the selected ROS Hostname in the master chooser

        //ACTIVITY
        Log.d("0425",InetAddressFactory.newNonLoopback().getHostAddress()+"          "+getMasterUri());
        NodeConfiguration nodeConfiguration = NodeConfiguration.newPublic(InetAddressFactory.newNonLoopback().getHostAddress());//GET HOST ADDRESS
        nodeConfiguration.setMasterUri(getMasterUri());//SET MASTER uri

        //Toast.makeText(RRosActivity.this,"execute PublishNode",Toast.LENGTH_SHORT).show();
        nodeMainExecutor.execute(publisher, nodeConfiguration);//publisher : the NodeMain to execute // nodeConfiguration is used to create Node


        Listener listener = new Listener();
        nodeConfiguration.setNodeName("listener");
        nodeMainExecutor.execute(listener, nodeConfiguration);
    }




}
