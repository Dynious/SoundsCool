package com.dynious.soundscool.client.sound;

import java.io.File;
import java.util.ArrayList;

public class SoundHandler
{
    private static File soundsFolder;
    private static ArrayList<File> sounds;

    public static File getSoundsFolder()
    {
        if (soundsFolder == null)
        {
            findSounds();
        }
        return soundsFolder;
    }
    public static ArrayList<File> getSounds()
    {
        if (sounds == null)
        {
            findSounds();
        }
        return sounds;
    }

    public static void findSounds()
    {
        soundsFolder = new File("sounds");
        if (!soundsFolder.exists())
        {
            soundsFolder.mkdir();
        }
        sounds = new ArrayList<File>();
        addSoundsFromDir(soundsFolder);
    }

    private static void addSoundsFromDir(File dir)
    {
        for (File file : dir.listFiles())
        {
            if (file.isFile())
            {
                if (file.getName().endsWith(".ogg"))
                {
                    System.out.println(file.toString());
                    sounds.add(file);
                }
            }
            else if (file.isDirectory())
            {
                addSoundsFromDir(file);
            }
        }
    }
}
