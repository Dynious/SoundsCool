package com.dynious.soundscool.network.packet;

import com.dynious.soundscool.handler.DelayedPlayHandler;
import com.dynious.soundscool.handler.NetworkHandler;
import com.dynious.soundscool.handler.SoundHandler;
import com.dynious.soundscool.sound.Sound;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;

public class UploadedSoundsPacket implements IPacket
{
    public UploadedSoundsPacket()
    {
    }

    @Override
    public void readBytes(ByteBuf bytes)
    {
        ArrayList<String> soundList = new ArrayList<String>();
        int sounds = bytes.readInt();
        for (int y = 0; y < sounds; y++)
        {
            int fileLength = bytes.readInt();
            char[] fileCars = new char[fileLength];
            for (int i = 0; i < fileLength; i++)
            {
                fileCars[i] = bytes.readChar();
            }
            soundList.add(String.valueOf(fileCars));
        }
        NetworkHandler.uploadedSounds = soundList;
        System.out.println(NetworkHandler.uploadedSounds);
    }

    @Override
    public void writeBytes(ByteBuf bytes)
    {
        bytes.writeInt(SoundHandler.getSounds().size());
        for (Sound sound : SoundHandler.getSounds())
        {
            bytes.writeInt(sound.getSoundName().length());
            for (char c : sound.getSoundName().toCharArray())
            {
                bytes.writeChar(c);
            }
        }
    }
}
