package com.dynious.soundscool.handler;

import java.util.ArrayList;

public class NetworkHandler
{
    public static ArrayList<String> uploadedSounds = new ArrayList<String>();

    public static boolean hasServerSound(String soundName)
    {
        return uploadedSounds.contains(soundName);
    }
}
