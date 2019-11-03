/*
 * Copyright (C) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.ros.android.android_tutorial_pubsub;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import org.ros.android.BitmapFromImage;
import org.ros.android.MessageCallable;
import org.ros.android.RosActivity;
import org.ros.android.view.RosImageView;
import org.ros.android.view.RosTextView;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;
import org.ros.rosjava_tutorial_pubsub.Talker;

import geometry_msgs.Pose;
import std_msgs.Bool;
import std_msgs.Float32;

/**
 * @author damonkohler@google.com (Damon Kohler)
 */
public class MainActivity extends RosActivity {

  private RosTextView<std_msgs.Bool> left_sensor_trigger, right_sensor_trigger;
  private RosTextView<std_msgs.Float32> left_sensor_distance, right_sensor_distance;
  private RosTextView<Pose> posex,posey,posez,orienx,orieny,orienz,orienw;
  private RosImageView vision_sensor;
  private Talker talker;

  public MainActivity() {
    // The RosActivity constructor configures the notification title and ticker
    // messages.
    super("Drone data coming", "Drone data coming");
  }

  @SuppressWarnings("unchecked")
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    vision_sensor = (RosImageView<sensor_msgs.Image>) findViewById(R.id.riv_vision_sensor);
    left_sensor_trigger = (RosTextView<Bool>) findViewById(R.id.tv_left_sensor_trigger_val);
    right_sensor_trigger = (RosTextView<Bool>) findViewById(R.id.tv_right_sensor_trigger_val);
    left_sensor_distance = (RosTextView<Float32>) findViewById(R.id.tv_left_sensor_distance_val);
    right_sensor_distance = (RosTextView<Float32>) findViewById(R.id.tv_right_sensor_distance_val);
    posex = (RosTextView<Pose>) findViewById(R.id.tv_pose_x);
      posey = (RosTextView<Pose>) findViewById(R.id.tv_pose_y);
      posez = (RosTextView<Pose>) findViewById(R.id.tv_pose_z);
      orienx = (RosTextView<Pose>) findViewById(R.id.tv_orien_x);
      orieny = (RosTextView<Pose>) findViewById(R.id.tv_orien_y);
      orienz = (RosTextView<Pose>) findViewById(R.id.tv_orien_z);
      orienw = (RosTextView<Pose>) findViewById(R.id.tv_orien_w);
    vision_sensor.setTopicName("frontVisionSensor");
    left_sensor_trigger.setTopicName("proximitySensorLeftBool");
    right_sensor_trigger.setTopicName("proximitySensorRightBool");
    left_sensor_distance.setTopicName("proximitySensorLeftDistance");
    right_sensor_distance.setTopicName("proximitySensorRightDistance");
    posex.setTopicName("gpsToROS");
      posey.setTopicName("gpsToROS");
      posez.setTopicName("gpsToROS");
      orienx.setTopicName("gpsToROS");
      orieny.setTopicName("gpsToROS");
      orienz.setTopicName("gpsToROS");
      orienw.setTopicName("gpsToROS");
    vision_sensor.setMessageType(sensor_msgs.Image._TYPE);
    left_sensor_trigger.setMessageType(std_msgs.Bool._TYPE);
    right_sensor_trigger.setMessageType(std_msgs.Bool._TYPE);
    left_sensor_distance.setMessageType(std_msgs.Float32._TYPE);
    right_sensor_distance.setMessageType(std_msgs.Float32._TYPE);
    posex.setMessageType(Pose._TYPE);
      posey.setMessageType(Pose._TYPE);
      posez.setMessageType(Pose._TYPE);
      orienx.setMessageType(Pose._TYPE);
      orieny.setMessageType(Pose._TYPE);
      orienz.setMessageType(Pose._TYPE);
      orienw.setMessageType(Pose._TYPE);
      final ColorStateList defColor =  left_sensor_trigger.getTextColors();
    vision_sensor.setMessageToBitmapCallable(new BitmapFromImage());
    left_sensor_trigger.setMessageToStringCallable(new MessageCallable<String, Bool>() {
      @Override
      public String call(std_msgs.Bool message) {
          if(String.valueOf(message.getData())== "true") {left_sensor_trigger.setTextColor(Color.RED); left_sensor_distance.setTextColor(Color.RED);}
          else {left_sensor_trigger.setTextColor(defColor); left_sensor_distance.setTextColor(defColor);}
        return String.valueOf(message.getData());
      }
    });
    right_sensor_trigger.setMessageToStringCallable(new MessageCallable<String, Bool>() {
          @Override
          public String call(std_msgs.Bool message) {
              if(String.valueOf(message.getData())== "true") {right_sensor_trigger.setTextColor(Color.RED); right_sensor_distance.setTextColor(Color.RED);}
              else {right_sensor_trigger.setTextColor(defColor); right_sensor_distance.setTextColor(defColor);}
              return String.valueOf(message.getData());
          }
      });
      left_sensor_distance.setMessageToStringCallable(new MessageCallable<String, Float32>() {
          @Override
          public String call(std_msgs.Float32 message) {
              return String.valueOf(message.getData());
          }
      });
      right_sensor_distance.setMessageToStringCallable(new MessageCallable<String, Float32>() {
          @Override
          public String call(std_msgs.Float32 message) {
              return String.valueOf(message.getData());
          }
      });
      posex.setMessageToStringCallable(new MessageCallable<String, Pose>() {
          @Override
          public String call(geometry_msgs.Pose message) {
              return String.valueOf(message.getPosition().getX());
          }
      });
      posey.setMessageToStringCallable(new MessageCallable<String, Pose>() {
          @Override
          public String call(geometry_msgs.Pose message) {
              return String.valueOf(message.getPosition().getY());
          }
      });
      posez.setMessageToStringCallable(new MessageCallable<String, Pose>() {
          @Override
          public String call(geometry_msgs.Pose message) {
              return String.valueOf(message.getPosition().getZ());
          }
      });
      orienx.setMessageToStringCallable(new MessageCallable<String, Pose>() {
          @Override
          public String call(geometry_msgs.Pose message) {
              return String.valueOf(message.getOrientation().getX());
          }
      });
      orieny.setMessageToStringCallable(new MessageCallable<String, Pose>() {
          @Override
          public String call(geometry_msgs.Pose message) {
              return String.valueOf(message.getOrientation().getY());
          }
      });
      orienz.setMessageToStringCallable(new MessageCallable<String, Pose>() {
          @Override
          public String call(geometry_msgs.Pose message) {
              return String.valueOf(message.getOrientation().getZ());
          }
      });
      orienw.setMessageToStringCallable(new MessageCallable<String, Pose>() {
          @Override
          public String call(geometry_msgs.Pose message) {
              return String.valueOf(message.getOrientation().getW());
          }
      });
  }


  @Override
  protected void init(NodeMainExecutor nodeMainExecutor) {
    talker = new Talker();

    // At this point, the user has already been prompted to either enter the URI
    // of a master to use or to start a master locally.

    // The user can easily use the selected ROS Hostname in the master chooser
    // activity.
    NodeConfiguration nodeConfiguration = NodeConfiguration.newPublic(getRosHostname());
    nodeConfiguration.setMasterUri(getMasterUri());
    nodeMainExecutor.execute(talker, nodeConfiguration);
    // The RosTextView is also a NodeMain that must be executed in order to
    // start displaying incoming messages.
    nodeConfiguration.setNodeName("vision_sensor");
    nodeMainExecutor.execute(vision_sensor, nodeConfiguration);
    nodeConfiguration.setNodeName("left_sensor_trigger");
    nodeMainExecutor.execute(left_sensor_trigger, nodeConfiguration);
    nodeConfiguration.setNodeName("right_sensor_trigger");
    nodeMainExecutor.execute(right_sensor_trigger, nodeConfiguration);
    nodeConfiguration.setNodeName("left_sensor_distance");
    nodeMainExecutor.execute(left_sensor_distance, nodeConfiguration);
    nodeConfiguration.setNodeName("right_sensor_distance");
    nodeMainExecutor.execute(right_sensor_distance, nodeConfiguration);
    nodeConfiguration.setNodeName("posex");
    nodeMainExecutor.execute(posex, nodeConfiguration);
      nodeConfiguration.setNodeName("posey");
      nodeMainExecutor.execute(posey, nodeConfiguration);
      nodeConfiguration.setNodeName("posez");
      nodeMainExecutor.execute(posez, nodeConfiguration);
      nodeConfiguration.setNodeName("orienx");
      nodeMainExecutor.execute(orienx, nodeConfiguration);
      nodeConfiguration.setNodeName("orieny");
      nodeMainExecutor.execute(orieny, nodeConfiguration);
      nodeConfiguration.setNodeName("orienz");
      nodeMainExecutor.execute(orienz, nodeConfiguration);
      nodeConfiguration.setNodeName("orienw");
      nodeMainExecutor.execute(orienw, nodeConfiguration);
  }
}
