package com.dynious.soundscool.handler;

import com.dynious.soundscool.sound.Sound;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NetworkHandler
{
    public static ArrayList<Sound> uploadedSounds = new ArrayList<Sound>();

    private static Map<String, byte[]> soundChunks = new HashMap<String, byte[]>();

    @SideOnly(Side.CLIENT)
    public static boolean hasServerSound(Sound sound)
    {
        return uploadedSounds.contains(sound);
    }

    @SideOnly(Side.CLIENT)
    public static boolean hasServerSound(String soundName)
    {
        for (Sound sound : uploadedSounds)
        {
            if (sound.getSoundName().equals(soundName))
            {
                return true;
            }
        }
        return false;
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
