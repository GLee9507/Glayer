// EventListener.aidl
package com.gene.glayer;

// Declare any non-default types here with import statements
import com.gene.glayer.model.Media;

interface  EventListener {
  oneway void onPlayListChanged(in List<Media> list);
  oneway void onPlayMediaChanged(int id);
  oneway void onProgressChanged(int  progress,int  duration);
  oneway void onPlayStateChanged(int state);
}
