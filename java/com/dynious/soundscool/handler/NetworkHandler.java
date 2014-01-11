package com.dynious.soundscool.handler;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NetworkHandler
{
    public static ArrayList<String> uploadedSounds = new ArrayList<String>();

    private static Map<String, byte[]> soundChunks = new HashMap<String, byte[]>();

    @SideOnly(Side.CLIENT)
    public static boolean hasServerSound(String soundName)
    {
        return uploadedSounds.contains(soundName);
    }

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
