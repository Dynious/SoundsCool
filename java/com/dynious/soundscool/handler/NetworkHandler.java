package com.dynious.soundscool.handler;

import org.apache.commons.lang3.ArrayUtils;

import java.util.HashMap;
import java.util.Map;

public class NetworkHandler
{

    private static Map<String, byte[]> soundChunks = new HashMap<String, byte[]>();

    public static void addSoundChunk(String soundName, byte[] soundChunk)
    {
        if (soundChunks.containsKey(soundName))
        {
            soundChunks.put(soundName, ArrayUtils.addAll(soundChunks.get(soundName), soundChunk));
        }
        else
        {
            soundChunks.put(soundName, soundChunk);
        }
    }

    public static byte[] soundUploaded(String soundName)
    {
        return soundChunks.remove(soundName);
    }
}
