package com.dynious.soundscool.network.packet.server;

import com.dynious.soundscool.handler.DelayedPlayHandler;
import com.dynious.soundscool.handler.SoundHandler;
import com.dynious.soundscool.helper.NetworkHelper;
import com.dynious.soundscool.network.packet.IPacket;
import com.dynious.soundscool.sound.Sound;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

import java.io.File;

public class ServerSoundPacket implements IPacket
{
    File soundFile;
    String fileName;
    public ServerSoundPacket()
    {
    }

    public ServerSoundPacket(Sound sound)
    {
        this.soundFile = sound.getSoundLocation();
        this.fileName = sound.getSoundName();
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
        fileName = String.valueOf(fileCars);

        int soundByteLength = bytes.readInt();
        byte[] soundByteArr = new byte[soundByteLength];
        for (int i = 0; i < soundByteLength; i++)
        {
            soundByteArr[i] = bytes.readByte();
        }
        NetworkHelper.createFileFromByteArr(soundByteArr, Minecraft.getMinecraft().func_147104_D().serverName, fileName);
        SoundHandler.findSounds();
        DelayedPlayHandler.onSoundReceived(fileName);
    }

    @Override
    public void writeBytes(ByteBuf bytes)
    {
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
