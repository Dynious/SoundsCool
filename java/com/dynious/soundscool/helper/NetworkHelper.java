package com.dynious.soundscool.helper;

import com.dynious.soundscool.SoundsCool;
import com.dynious.soundscool.handler.NetworkHandler;
import com.dynious.soundscool.handler.SoundHandler;
import com.dynious.soundscool.network.packet.IPacket;
import com.dynious.soundscool.network.packet.SoundChunkPacket;
import com.dynious.soundscool.network.packet.SoundUploadedPacket;
import com.dynious.soundscool.sound.Sound;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.IOException;

public class NetworkHelper
{
    public static final int PARTITION_SIZE = 10000;

    @SideOnly(Side.SERVER)
    public static void sendPacketToPlayer(IPacket packet, EntityPlayer player)
    {
        SoundsCool.proxy.getChannel().attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
        SoundsCool.proxy.getChannel().attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
        SoundsCool.proxy.getChannel().writeOutbound(packet);
    }

    @SideOnly(Side.CLIENT)
    public static void clientSoundUpload(Sound sound)
    {
        uploadSound(sound);
        NetworkHandler.uploadedSounds.add(sound.getSoundName());
    }

    @SideOnly(Side.SERVER)
    public static void serverSoundUpload(Sound sound, EntityPlayer player)
    {
        SoundsCool.proxy.getChannel().attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
        SoundsCool.proxy.getChannel().attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
        uploadSound(sound);
    }

    private static void uploadSound(Sound sound)
    {
        byte[] soundBytes = convertFileToByteArr(sound.getSoundLocation());
        for (int i = 0; i < soundBytes.length; i += PARTITION_SIZE)
        {
            byte[] bytes = ArrayUtils.subarray(soundBytes, i, i + Math.min(PARTITION_SIZE, soundBytes.length - i));
            SoundsCool.proxy.getChannel().writeOutbound(new SoundChunkPacket(sound.getSoundName(), bytes));
        }
        String category = FMLCommonHandler.instance().getEffectiveSide().isClient()? Minecraft.getMinecraft().thePlayer.getDisplayName(): MinecraftServer.getServer().getServerHostname();
        SoundsCool.proxy.getChannel().writeOutbound(new SoundUploadedPacket(sound.getSoundName(), category));
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

    public static void createFileFromByteArr(byte[] byteFile, String category, String fileName)
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
        }
    }
}
