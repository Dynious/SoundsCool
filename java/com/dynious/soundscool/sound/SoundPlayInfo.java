package com.dynious.soundscool.sound;

public class SoundPlayInfo
{
    public String identifier;
    public int x, y, z;

    public SoundPlayInfo(String identifier, int x, int y, int z)
    {
        this.identifier = identifier;
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
