// EventListener.aidl
package com.gene.libglayer;

// Declare any non-default types here with import statements
import com.gene.libglayer.model.Media;

interface  EventListener {
  oneway void onAllListChanged(in Media[] medialist);
  oneway void onPlayMediaChanged(int id);
  oneway void onProgressChanged(long  progress);
  oneway void onPlayStateChanged(int state);
}
