/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentfinder;

/**
 *
 * @author neel
 */
public interface SwingWorkerCallbacks {

    void onDoInBckgroundStarted();

    void onDone(Object returnObject, Object otherParam);
}
