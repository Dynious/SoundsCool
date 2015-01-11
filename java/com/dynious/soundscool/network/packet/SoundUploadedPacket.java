package com.dynious.soundscool.network.packet;

import com.dynious.soundscool.handler.DelayedPlayHandler;
import com.dynious.soundscool.handler.NetworkHandler;
import com.dynious.soundscool.handler.SoundHandler;
import com.dynious.soundscool.helper.NetworkHelper;
import com.dynious.soundscool.network.packet.server.SoundReceivedPacket;
import cpw.mods.fml.common.FMLCommonHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

import java.io.File;

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
        if (FMLCommonHandler.instance().getEffectiveSide().isClient() && (category.equalsIgnoreCase("null") || category.isEmpty()))
        {
            category = Minecraft.getMinecraft().func_147104_D().serverIP;
        }

        int fileLength = bytes.readInt();
        char[] fileCars = new char[fileLength];
        for (int i = 0; i < fileLength; i++)
        {
            fileCars[i] = bytes.readChar();
        }
        soundName = String.valueOf(fileCars);

        File soundFile = NetworkHelper.createFileFromByteArr(NetworkHandler.soundUploaded(soundName), category, soundName);
        SoundHandler.addLocalSound(soundName, soundFile);
        if (FMLCommonHandler.instance().getEffectiveSide().isClient())
        {
            DelayedPlayHandler.onSoundReceived(soundName);
        }
        else
        {
            EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(category);
            if (player != null)
            {
                NetworkHelper.sendPacketToPlayer(new SoundReceivedPacket(SoundHandler.getSound(soundName)), player);
            }
        }
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
