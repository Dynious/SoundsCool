package com.dynious.soundscool.network.packet.server;

import com.dynious.soundscool.handler.SoundHandler;
import com.dynious.soundscool.network.packet.IPacket;
import io.netty.buffer.ByteBuf;

public class ServerPlaySoundPacket implements IPacket
{
    String soundName, identifier;
    int x, y, z;
    public ServerPlaySoundPacket()
    {
    }

    public ServerPlaySoundPacket(String soundName, String identifier, int x, int y, int z)
    {
        this.soundName = soundName;
        this.identifier = identifier;
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
        int identLength = bytes.readInt();
        char[] identCars = new char[identLength];
        for (int i = 0; i < identLength; i++)
        {
            identCars[i] = bytes.readChar();
        }
        identifier = String.valueOf(identCars);
        x = bytes.readInt();
        y = bytes.readInt();
        z = bytes.readInt();
        SoundHandler.playSound(soundName, identifier, x, y, z);
    }

    @Override
    public void writeBytes(ByteBuf bytes)
    {
        bytes.writeInt(soundName.length());
        for (char c : soundName.toCharArray())
        {
            bytes.writeChar(c);
        }
        bytes.writeInt(identifier.length());
        for (char c : identifier.toCharArray())
        {
            bytes.writeChar(c);
        }
        bytes.writeInt(x);
        bytes.writeInt(y);
        bytes.writeInt(z);
    }
}
