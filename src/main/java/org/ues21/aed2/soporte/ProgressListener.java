package org.ues21.aed2.soporte;

public interface ProgressListener {
    void onProgressUpdate(long progress);

    void onComplete();
}