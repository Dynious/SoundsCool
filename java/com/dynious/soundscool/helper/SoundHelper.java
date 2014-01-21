package com.dynious.soundscool.helper;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.generic.GenericAudioHeader;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;

import java.io.File;

public class SoundHelper
{
    public static double getSoundLength(File soundFile)
    {
        try
        {
            AudioHeader audioHeader = AudioFileIO.read(soundFile).getAudioHeader();
            if (soundFile.getName().endsWith(".mp3"))
            {
                return ((MP3AudioHeader)audioHeader).getPreciseTrackLength();
            }
            else
            {
                return ((GenericAudioHeader)audioHeader).getPreciseLength();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return 0;
    }
}
