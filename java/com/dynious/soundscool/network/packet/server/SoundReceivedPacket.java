package com.dynious.soundscool.network.packet.server;

import com.dynious.soundscool.handler.SoundHandler;
import com.dynious.soundscool.network.packet.IPacket;
import com.dynious.soundscool.sound.Sound;
import io.netty.buffer.ByteBuf;

public class SoundReceivedPacket implements IPacket
{
    String soundName, category;
    public SoundReceivedPacket()
    {
    }

    public SoundReceivedPacket(Sound sound)
    {
        this.soundName = sound.getSoundName();
        this.category = sound.getCategory();
    }

    @Override
    public void readBytes(ByteBuf bytes)
    {
        int soundLength = bytes.readInt();
        char[] fileCars = new char[soundLength];
        for (int i = 0; i < soundLength; i++)
        {
            fileCars[i] = bytes.readChar();
        }
        soundName = String.valueOf(fileCars);
        int soundCatLength = bytes.readInt();
        char[] soundCatChars = new char[soundCatLength];
        for (int i = 0; i < soundCatLength; i++)
        {
            soundCatChars[i] = bytes.readChar();
        }
        category = String.valueOf(soundCatChars);

        SoundHandler.addRemoteSound(soundName, category);
    }

    @Override
    public void writeBytes(ByteBuf bytes)
    {
        bytes.writeInt(soundName.length());
        for (char c : soundName.toCharArray())
        {
            bytes.writeChar(c);
        }
        bytes.writeInt(category.length());
        for (char c : category.toCharArray())
        {
            bytes.writeChar(c);
        }
    }
}
