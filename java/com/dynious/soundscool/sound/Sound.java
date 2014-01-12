package com.dynious.soundscool.sound;

import java.io.File;

public class Sound
{
    private File soundLocation;
    private String category;
    private String soundName;

    public Sound(File soundLocation)
    {
        this.soundLocation = soundLocation;
        String path = soundLocation.getAbsolutePath();
        path = path.substring(0, path.lastIndexOf(File.separator));
        path = path.substring(path.lastIndexOf(File.separator) + 1);
        if (path.equals("sounds"))
            path = "";
        this.category = path;
        this.soundName = soundLocation.getName();
    }

    public Sound(String soundName, String category)
    {
        this.soundLocation = null;
        this.soundName = soundName;
        this.category = category;
    }

    public String getCategory()
    {
        return category;
    }

    public String getSoundName()
    {
        return soundName;
    }

    public File getSoundLocation()
    {
        return soundLocation;
    }
}
