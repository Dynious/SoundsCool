package com.dynious.soundscool.network.packet;

import com.dynious.soundscool.handler.SoundHandler;
import com.dynious.soundscool.helper.NetworkHelper;
import com.dynious.soundscool.sound.Sound;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

import java.io.File;

public class ClientSoundPacket implements IPacket
{
    File soundFile;
    String category;
    String fileName;
    public ClientSoundPacket()
    {
    }

    public ClientSoundPacket(Sound sound, EntityPlayer player)
    {
        this.soundFile = sound.getSoundLocation();
        this.category = player.getDisplayName();
        this.fileName = sound.getSoundName();
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
        bytes.writeInt(fileName.length());
        for (char c : fileName.toCharArray())
        {
            bytes.writeChar(c);
        }
        byte[] soundByteArr = NetworkHelper.convertFileToByteArr(soundFile);
        bytes.writeInt(soundByteArr.length);
        bytes.writeBytes(soundByteArr);
    }
}
