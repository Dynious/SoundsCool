package com.dynious.soundscool.network.packet.server;

import com.dynious.soundscool.handler.SoundHandler;
import com.dynious.soundscool.helper.GuiHelper;
import com.dynious.soundscool.network.packet.IPacket;
import com.dynious.soundscool.sound.Sound;
import io.netty.buffer.ByteBuf;

public class ServerPlaySoundPacket implements IPacket
{
    String soundName;
    int x, y, z;
    public ServerPlaySoundPacket()
    {
    }

    public ServerPlaySoundPacket(String soundName, int x, int y, int z)
    {
        this.soundName = soundName;
        this.x = x;
        this.y = y;
        this.z = z;
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
        x = bytes.readInt();
        y = bytes.readInt();
        z = bytes.readInt();
        SoundHandler.playSound(soundName, x, y, z);
    }

    @Override
    public void writeBytes(ByteBuf bytes)
    {
        bytes.writeInt(soundName.length());
        for (char c : soundName.toCharArray())
        {
            bytes.writeChar(c);
        }
        bytes.writeInt(x);
        bytes.writeInt(y);
        bytes.writeInt(z);
    }
}
