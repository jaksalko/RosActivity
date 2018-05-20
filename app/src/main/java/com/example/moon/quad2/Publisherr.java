package com.example.moon.quad2;

import android.content.Context;
import android.util.Log;

import org.ros.concurrent.CancellableLoop;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.topic.Publisher;

import java.util.ArrayList;
import java.util.List;
import std_msgs.Int32;

public class Publisherr extends AbstractNodeMain {
    private String topic;
    private Context app;
    private String msg_type;
    int waitTime = 100;
    List<String> datas;
    public Publisherr(Context c, String topic)
    {
        this.app = c;
        this.topic = topic;
    }
    public void setWaitTime(String time) {
        this.waitTime = Integer.parseInt(time);
    }
    public void setMsgType(String msg_type) {
        this.msg_type = msg_type;
    }
    public void setDatas(List<String> dataList) {
        this.datas = new ArrayList<String>();
        this.datas.addAll(dataList);
    }
    private void chooseAndSendMessage(Publisher publisher) {
        publisher.publish(createIntMsg(publisher));
    }
    public Int32 createIntMsg(Publisher publisher)
    {
        Int32 msg = (Int32) publisher.newMessage();
        msg.setData(Integer.parseInt(datas.get(0)));
        return msg;
    }


    @Override
    public GraphName getDefaultNodeName(){
        return GraphName.of("ExampleNode");
    }
    @Override
    public void onStart(final ConnectedNode connectedNode) {
        Log.i("Publisher started",topic + " " + msg_type);
        final Publisher publisher = connectedNode.newPublisher(this.topic,this.msg_type);
        // Define any publishers, subscribers, servers or clients..

        connectedNode.executeCancellableLoop(new CancellableLoop() {
            protected void loop() throws InterruptedException {
                //compose and send off message
                chooseAndSendMessage(publisher);
                Thread.sleep(waitTime);
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
