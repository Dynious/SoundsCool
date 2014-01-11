package com.dynious.soundscool.network.packet;

import com.dynious.soundscool.handler.NetworkHandler;
import com.dynious.soundscool.handler.SoundHandler;
import com.dynious.soundscool.helper.NetworkHelper;
import io.netty.buffer.ByteBuf;

public class SoundUploadedPacket implements IPacket
{
    String category;
    String soundName;
    public SoundUploadedPacket()
    {
    }

    public SoundUploadedPacket(String soundName, String category)
    {
        this.category = category;
        this.soundName = soundName;
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
        soundName = String.valueOf(fileCars);

        NetworkHelper.createFileFromByteArr(NetworkHandler.soundUploaded(soundName), category, soundName);
        SoundHandler.findSounds();
    }

    @Override
    public void writeBytes(ByteBuf bytes)
    {
        bytes.writeInt(category.length());
        for (char c : category.toCharArray())
        {
            bytes.writeChar(c);
        }
        bytes.writeInt(soundName.length());
        for (char c : soundName.toCharArray())
        {
            bytes.writeChar(c);
        }
    }
}
