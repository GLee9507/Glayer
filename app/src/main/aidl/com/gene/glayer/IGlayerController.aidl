// IGlayerController.aidl
package com.gene.glayer;

// Declare any non-default types here with import statements
import com.gene.glayer.EventListener;
import com.gene.glayer.model.Media;


interface IGlayerController {
    void registerEventListenerListener(in String uuid,EventListener listener);
    void prepare(int id);
    void setPlayList(String name,in int[] playList);
    void play();
    void pause();
    void scan(String path);
}
