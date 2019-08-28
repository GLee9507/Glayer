// IGlayerController.aidl
package com.gene.libglayer;

// Declare any non-default types here with import statements
import com.gene.libglayer.EventListener;
import com.gene.libglayer.model.Media;


interface IGlayerController {
    void registerEventListenerListener(in String uuid,EventListener listener);
    void prepare(int id);
    void setPlayList(String name,in int[] playList);
    void play();
    void pause();
    void scan(String path);
}
