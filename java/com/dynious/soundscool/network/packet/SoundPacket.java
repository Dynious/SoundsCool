package com.dynious.soundscool.network.packet;

import com.dynious.soundscool.helper.NetworkHelper;
import io.netty.buffer.ByteBuf;

import java.io.File;

public class SoundPacket implements IPacket
{
    File sound;
    String category;
    String fileName;
    public SoundPacket()
    {
    }

    public SoundPacket(File sound, String category, String fileName)
    {
        this.sound = sound;
        this.category = category;
        this.fileName = fileName;
    }

    @Override
    public void readBytes(ByteBuf bytes)
    {
        int catLength = bytes.readInt();
        char[] catCars = new char[catLength];
        for (int i = 0; i < catLength; i++)
        {
            catCars[i] = bytes.readChar();
        }
        category = String.valueOf(catCars);

        int fileLength = bytes.readInt();
        char[] fileCars = new char[fileLength];
        for (int i = 0; i < fileLength; i++)
        {
            fileCars[i] = bytes.readChar();
        }
        fileName = String.valueOf(fileCars);

        int soundByteLength = bytes.readInt();
        byte[] soundByteArr = new byte[soundByteLength];
        for (int i = 0; i < soundByteLength; i++)
        {
            soundByteArr[i] = bytes.readByte();
        }
        NetworkHelper.createFileFromByteArr(soundByteArr, category, fileName);
    }

    @Override
    public void writeBytes(ByteBuf bytes)
    {
        bytes.writeInt(category.length());
        for (char c : category.toCharArray())
        {
            bytes.writeChar(c);
        }
        bytes.writeInt(fileName.length());
        for (char c : fileName.toCharArray())
        {
            bytes.writeChar(c);
        }
        byte[] soundByteArr = NetworkHelper.convertFileToByteArr(sound);
        bytes.writeInt(soundByteArr.length);
        bytes.writeBytes(soundByteArr);
    }
}
