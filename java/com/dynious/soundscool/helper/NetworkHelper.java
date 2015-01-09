package com.dynious.soundscool.helper;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.FMLOutboundHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;

import com.dynious.soundscool.SoundsCool;
import com.dynious.soundscool.handler.SoundHandler;
import com.dynious.soundscool.network.packet.SoundChunkPacket;
import com.dynious.soundscool.network.packet.SoundUploadedPacket;
import com.dynious.soundscool.network.packet.client.GetUploadedSoundsPacket;
import com.dynious.soundscool.network.packet.server.ServerPlaySoundPacket;
import com.dynious.soundscool.network.packet.server.UploadedSoundsPacket;
import com.dynious.soundscool.sound.Sound;

public class NetworkHelper
{
    public static final int PARTITION_SIZE = 30000;

    public static void sendMessageToPlayer(IMessage message, EntityPlayerMP player)
    {
        SoundsCool.network.sendTo(message, player);
    }

    public static void sendMessageToAll(IMessage message)
    {
    	//sendToAll causing client disconnect in MP. Iterating over players instead until reason known
    	Iterator playerList = MinecraftServer.getServer().getConfigurationManager().playerEntityList.iterator();
        while(playerList.hasNext())
        {
        	SoundsCool.network.sendTo(message, (EntityPlayerMP)playerList.next());
        }
    }
    
    public static void syncPlayerSounds(EntityPlayer player)
    {
    	SoundsCool.network.sendToServer(new GetUploadedSoundsPacket(player));
    }
    
    public static void syncAllPlayerSounds()
    {
    	NetworkHelper.sendMessageToAll(new UploadedSoundsPacket());
    }

    @SideOnly(Side.CLIENT)
    public static void clientSoundUpload(Sound sound)
    {
        sound.setState(Sound.SoundState.UPLOADING);
        uploadSound(sound, Minecraft.getMinecraft().thePlayer.getDisplayName().getUnformattedTextForChat());
    }

    public static void serverSoundUpload(Sound sound, EntityPlayerMP player)
    {
        byte[] soundBytes = convertFileToByteArr(sound.getSoundLocation());
        for (int i = 0; i < soundBytes.length; i += PARTITION_SIZE)
        {
            byte[] bytes = ArrayUtils.subarray(soundBytes, i, i + Math.min(PARTITION_SIZE, soundBytes.length - i));
            SoundsCool.network.sendTo(new SoundChunkPacket(sound.getSoundName(), bytes), player);
        }
        SoundsCool.network.sendTo(new SoundUploadedPacket(sound.getSoundName(), MinecraftServer.getServer().getMOTD()), player);
    }

    private static void uploadSound(Sound sound, String category)
    {
        byte[] soundBytes = convertFileToByteArr(sound.getSoundLocation());
        for (int i = 0; i < soundBytes.length; i += PARTITION_SIZE)
        {
            byte[] bytes = ArrayUtils.subarray(soundBytes, i, i + Math.min(PARTITION_SIZE, soundBytes.length - i));
            SoundsCool.network.sendToServer(new SoundChunkPacket(sound.getSoundName(), bytes));
        }
        SoundsCool.network.sendToServer(new SoundUploadedPacket(sound.getSoundName(), category));
    }

    public static byte[] convertFileToByteArr(File file)
    {
        if (file != null && file.exists())
        {
            try
            {
                return FileUtils.readFileToByteArray(file);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static File createFileFromByteArr(byte[] byteFile, String category, String fileName)
    {
        if (byteFile != null && byteFile.length > 0 && !category.isEmpty() && !fileName.isEmpty())
        {
            File file = new File(SoundHandler.getSoundsFolder().getAbsolutePath() + File.separator + category + File.separator + fileName);
            try
            {
                FileUtils.writeByteArrayToFile(file, byteFile);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            return file;
        }
        return null;
    }
}
