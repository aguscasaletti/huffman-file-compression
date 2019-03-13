package org.ues21.view;

public interface ProgressListener {
    void onProgressUpdate(long progress);

    void onComplete();
}