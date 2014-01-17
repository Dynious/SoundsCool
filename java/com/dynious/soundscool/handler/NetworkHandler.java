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
    public static boolean hasServerSound(String soundName)
    {
        return getServerSound(soundName) != null;
    }

    @SideOnly(Side.CLIENT)
    public static Sound getServerSound(String soundName)
    {
        for (Sound sound : uploadedSounds)
        {
            if (sound.getSoundName().equals(soundName))
            {
                return sound;
            }
        }
        return null;
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

    public static boolean isSoundUploading(String soundName)
    {
        return soundChunks.containsKey(soundName);
    }
}
