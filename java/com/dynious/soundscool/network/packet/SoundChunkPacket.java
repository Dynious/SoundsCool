package com.dynious.soundscool.network.packet;

import com.dynious.soundscool.handler.NetworkHandler;
import io.netty.buffer.ByteBuf;

public class SoundChunkPacket implements IPacket
{
    String soundName;
    byte[] soundChunk;

    public SoundChunkPacket()
    {
    }

    public SoundChunkPacket(String soundName, byte[] soundChunk)
    {
        this.soundName = soundName;
        this.soundChunk = soundChunk;
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

        int soundByteLength = bytes.readInt();
        byte[] soundByteArr = new byte[soundByteLength];
        for (int i = 0; i < soundByteLength; i++)
        {
            soundByteArr[i] = bytes.readByte();
        }
        NetworkHandler.addSoundChunk(soundName, soundByteArr);
    }

    @Override
    public void writeBytes(ByteBuf bytes)
    {
        bytes.writeInt(soundName.length());
        for (char c : soundName.toCharArray())
        {
            bytes.writeChar(c);
        }
        bytes.writeInt(soundChunk.length);
        bytes.writeBytes(soundChunk);
    }
}
