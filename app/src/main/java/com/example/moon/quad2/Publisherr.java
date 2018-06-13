package com.example.moon.quad2;

import android.content.Context;
import android.util.Log;

import org.ros.concurrent.CancellableLoop;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.topic.Publisher;

import std_msgs.String;
import std_msgs.Int32;

public class Publisherr extends AbstractNodeMain {

    int current_state = 0;//목표 위치로 가는 동안 지나가는 spot을 저장
    int waitTime = 100;
    int datas;

    public void setDatas(int data) {
        datas = data;}

    @Override
    public GraphName getDefaultNodeName(){
        return GraphName.of("talker");
    }
    @Override
    public void onStart(final ConnectedNode connectedNode) {
        Log.i("Publisher started","talker" + " ");
        final Publisher<Int32> publisher = connectedNode.newPublisher("talker", Int32._TYPE);
        // Define any publishers, subscribers, servers or clients..

        connectedNode.executeCancellableLoop(new CancellableLoop() {
            protected void loop() throws InterruptedException {
                //compose and send off message
                Int32 msg = (Int32) publisher.newMessage();
                if(current_state < datas)//목표위치에 가지못했다면 current_State 를 ++ 해서 다음 spot으로 이동
                    current_state++;
                else if(current_state > datas)
                    current_state--;
                msg.setData(current_state); // spot 으로 이동
                Log.d("loop msg",msg.toString());
                publisher.publish(msg);
                Thread.sleep(5000); // 전송 delay는 5초
            }
        });
    }
    @Override
    public void onShutdown(Node node) {
        //
    }

    @Override
    public void onShutdownComplete(Node node) {
        //
    }
}
