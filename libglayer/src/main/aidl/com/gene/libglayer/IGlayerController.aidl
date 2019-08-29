// IGlayerController.aidl
package com.gene.libglayer;

// Declare any non-default types here with import statements
import com.gene.libglayer.EventListener;
import com.gene.libglayer.model.Media;
import android.os.Message;

interface IGlayerController {
    void registerEventListenerListener(in String uuid,EventListener listener);

    void send(in Bundle data);
}
