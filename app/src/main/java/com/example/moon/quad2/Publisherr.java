package com.example.moon.quad2;

import android.content.Context;
import android.util.Log;

import org.ros.concurrent.CancellableLoop;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.topic.Publisher;

import std_msgs.Int32;

public class Publisherr extends AbstractNodeMain {

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
        final Publisher<std_msgs.Int32> publisher = connectedNode.newPublisher("talker", Int32._TYPE);
        // Define any publishers, subscribers, servers or clients..

        connectedNode.executeCancellableLoop(new CancellableLoop() {
            protected void loop() throws InterruptedException {
                //compose and send off message
                Int32 msg = (Int32) publisher.newMessage();
                publisher.publish(msg);
                msg.setData(datas);
                Thread.sleep(1000);
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
