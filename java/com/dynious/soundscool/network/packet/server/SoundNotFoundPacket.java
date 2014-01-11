package com.dynious.soundscool.network.packet.server;

import com.dynious.soundscool.handler.DelayedPlayHandler;
import com.dynious.soundscool.network.packet.IPacket;
import io.netty.buffer.ByteBuf;

public class SoundNotFoundPacket implements IPacket
{
    String soundName;
    public SoundNotFoundPacket()
    {
    }

    public SoundNotFoundPacket(String soundName)
    {
        this.soundName = soundName;
    }

    @Override
    public void readBytes(ByteBuf bytes)
    {
        int fileLength = bytes.readInt();
        char[] fileCars = new char[fileLength];
        for (int i = 0; i < fileLength; i++)
        {
            fileCars[i] = bytes.readChar();
        }
        soundName = String.valueOf(fileCars);
        DelayedPlayHandler.removeSound(soundName);
    }

    @Override
    public void writeBytes(ByteBuf bytes)
    {
        bytes.writeInt(soundName.length());
        for (char c : soundName.toCharArray())
        {
            bytes.writeChar(c);
        }
    }
}
