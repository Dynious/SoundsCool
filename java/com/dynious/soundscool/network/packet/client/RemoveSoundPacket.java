package com.dynious.soundscool.network.packet.client;

import com.dynious.soundscool.handler.SoundHandler;
import com.dynious.soundscool.network.packet.IPacket;
import com.dynious.soundscool.sound.Sound;
import io.netty.buffer.ByteBuf;

public class RemoveSoundPacket implements IPacket
{
    String soundName;

    public RemoveSoundPacket()
    {
    }

    public RemoveSoundPacket(String soundName)
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

        Sound sound = SoundHandler.getSound(soundName);
        SoundHandler.removeSound(sound);
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
