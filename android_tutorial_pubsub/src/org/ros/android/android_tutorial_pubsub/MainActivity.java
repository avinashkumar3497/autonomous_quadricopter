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

import android.os.Bundle;
import android.renderscript.Float2;

import org.ros.android.BitmapFromImage;
import org.ros.android.MessageCallable;
import org.ros.android.RosActivity;
import org.ros.android.view.RosImageView;
import org.ros.android.view.RosTextView;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;
import org.ros.rosjava_tutorial_pubsub.Talker;

import sensor_msgs.Image;
import std_msgs.Bool;
import std_msgs.Float32;
import std_msgs.Float64;

/**
 * @author damonkohler@google.com (Damon Kohler)
 */
public class MainActivity extends RosActivity {

  private RosTextView<std_msgs.Bool> left_sensor_trigger, right_sensor_trigger;
  private RosTextView<std_msgs.Float32> left_sensor_distance, right_sensor_distance;
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
    vision_sensor.setTopicName("frontVisionSensor");
    left_sensor_trigger.setTopicName("proximitySensorLeftBool");
    right_sensor_trigger.setTopicName("proximitySensorRightBool");
    left_sensor_distance.setTopicName("proximitySensorLeftDistance");
    right_sensor_distance.setTopicName("proximitySensorRightDistance");
    vision_sensor.setMessageType(sensor_msgs.Image._TYPE);
    left_sensor_trigger.setMessageType(std_msgs.Bool._TYPE);
    right_sensor_trigger.setMessageType(std_msgs.Bool._TYPE);
    left_sensor_distance.setMessageType(std_msgs.Float32._TYPE);
    right_sensor_distance.setMessageType(std_msgs.Float32._TYPE);
    vision_sensor.setMessageToBitmapCallable(new BitmapFromImage());
    left_sensor_trigger.setMessageToStringCallable(new MessageCallable<String, Bool>() {
      @Override
      public String call(std_msgs.Bool message) {
        return String.valueOf(message.getData());
      }
    });
    right_sensor_trigger.setMessageToStringCallable(new MessageCallable<String, Bool>() {
          @Override
          public String call(std_msgs.Bool message) {
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
  }
}
