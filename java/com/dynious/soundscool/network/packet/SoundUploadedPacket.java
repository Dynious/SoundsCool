package com.dynious.soundscool.network.packet;

import io.netty.buffer.ByteBuf;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.dynious.soundscool.handler.DelayedPlayHandler;
import com.dynious.soundscool.handler.NetworkHandler;
import com.dynious.soundscool.handler.SoundHandler;
import com.dynious.soundscool.helper.NetworkHelper;
import com.dynious.soundscool.network.packet.client.CheckPresencePacket;
import com.dynious.soundscool.network.packet.server.SoundReceivedPacket;

public class SoundUploadedPacket implements IMessage
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
    public void fromBytes(ByteBuf bytes)
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
            category = Minecraft.getMinecraft().getCurrentServerData().serverName;
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
            EntityPlayerMP player = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(category);
            if (player != null)
            {
                NetworkHelper.sendMessageToPlayer(new SoundReceivedPacket(SoundHandler.getSound(soundName)), player);
            }
        }
    }

    @Override
    public void toBytes(ByteBuf bytes)
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
    
    public static class Handler implements IMessageHandler<SoundUploadedPacket, IMessage> {
        @Override
        public IMessage onMessage(SoundUploadedPacket message, MessageContext ctx) {
            return null;
        }
    }
}
