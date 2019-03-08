package org.ues21.aed2.vista;

public interface ProgressListener {
    void onProgressUpdate(long progress);

    void onComplete();
}