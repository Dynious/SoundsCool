package com.dynious.soundscool.handler;

import com.dynious.soundscool.lib.Coords;

import java.util.HashMap;
import java.util.Map;

public class DelayedPlayHandler
{
    private static Map<String, Coords> map = new HashMap<String, Coords>();

    public static void addDelayedPlay(String soundName, int x, int y, int z)
    {
        map.put(soundName, new Coords(x, y, z));
    }

    public static void onSoundReceived(String soundName)
    {
        Coords coords = map.get(soundName);
        if (coords != null)
        {
            SoundHandler.playSound(soundName, coords.x, coords.y, coords.z);
            map.remove(soundName);
        }
    }

    public static void removeSound(String soundName)
    {
        map.remove(soundName);
    }
}
