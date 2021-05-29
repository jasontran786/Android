package com.jerry.mp3player;

/**
 * This interface allow MP3Service communicate with MP3ControlFragment
 */
public interface MP3ControlListener {

    public void onDurationPrepared();

    public void onUpdateMusicName(String name);

    public void onUpdatePlayPauseButton(boolean play);

}
