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
import org.ros.android.MessageCallable;
import org.ros.android.RosActivity;
import org.ros.android.view.RosTextView;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;
import org.ros.rosjava_tutorial_pubsub.Talker;

import std_msgs.Bool;

/**
 * @author damonkohler@google.com (Damon Kohler)
 */
public class MainActivity extends RosActivity {

  private RosTextView<std_msgs.Bool> left_sensor_trigger, right_sensor_trigger;
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
    left_sensor_trigger = (RosTextView<Bool>) findViewById(R.id.tv_left_sensor_trigger_val);
    right_sensor_trigger = (RosTextView<Bool>) findViewById(R.id.tv_right_sensor_trigger_val);
    left_sensor_trigger.setTopicName("proximitySensorLeftBool");
    right_sensor_trigger.setTopicName("proximitySensorRightBool");
    left_sensor_trigger.setMessageType(std_msgs.Bool._TYPE);
    right_sensor_trigger.setMessageType(std_msgs.Bool._TYPE);
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
    nodeMainExecutor.execute(left_sensor_trigger, nodeConfiguration);
    nodeConfiguration.setNodeName("right_sensor_trigger");
    nodeMainExecutor.execute(right_sensor_trigger, nodeConfiguration);
  }
}
