package com.dynious.soundscool.network.packet.server;

import com.dynious.soundscool.client.audio.SoundPlayer;
import com.dynious.soundscool.handler.SoundHandler;
import com.dynious.soundscool.network.packet.IPacket;
import com.dynious.soundscool.sound.Sound;
import io.netty.buffer.ByteBuf;

public class StopSoundPacket implements IPacket
{
    String identifier;
    public StopSoundPacket()
    {
    }

    public StopSoundPacket(String identifier)
    {
        this.identifier = identifier;
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
        identifier = String.valueOf(fileCars);

        SoundPlayer.stopSound(identifier);
    }

    @Override
    public void writeBytes(ByteBuf bytes)
    {
        bytes.writeInt(identifier.length());
        for (char c : identifier.toCharArray())
        {
            bytes.writeChar(c);
        }
    }
}
