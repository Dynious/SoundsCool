package com.dynious.soundscool.tileentity;

import com.dynious.soundscool.SoundsCool;
import com.dynious.soundscool.handler.SoundHandler;
import com.dynious.soundscool.helper.SoundHelper;
import com.dynious.soundscool.network.packet.client.SoundPlayerSelectPacket;
import com.dynious.soundscool.network.packet.server.ServerPlaySoundPacket;
import com.dynious.soundscool.network.packet.server.StopSoundPacket;
import com.dynious.soundscool.sound.Sound;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.UUID;

public class TileSoundPlayer extends TileEntity
{
    private boolean isPowered = false;
    private Sound selectedSound;
    private String lastSoundIdentifier;
    private long timeSoundFinishedPlaying;

    public void setPowered(boolean powered)
    {
        if (!isPowered && powered)
        {
            playCurrentSound();
            isPowered = true;
        }
        else if (isPowered && !powered)
        {
            isPowered = false;
        }
    }

    public void selectSound(String soundName)
    {
        this.selectedSound = SoundHandler.getSound(soundName);

        if (this.getWorldObj().isRemote)
        {
            SoundsCool.proxy.getChannel().writeOutbound(new SoundPlayerSelectPacket(this));
        }
    }

    public Sound getSelectedSound()
    {
        return selectedSound;
    }

    public void playCurrentSound()
    {
        if (selectedSound != null)
        {
            if (timeSoundFinishedPlaying < System.currentTimeMillis())
            {
                if (SoundHandler.getSound(selectedSound.getSoundName()) != null)
                {
                    lastSoundIdentifier = UUID.randomUUID().toString();
                    SoundsCool.proxy.getChannel().attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
                    SoundsCool.proxy.getChannel().attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(new NetworkRegistry.TargetPoint(getWorldObj().provider.dimensionId, xCoord, yCoord, zCoord, 64));
                    SoundsCool.proxy.getChannel().writeOutbound(new ServerPlaySoundPacket(selectedSound.getSoundName(), lastSoundIdentifier, xCoord, yCoord, zCoord));
                    timeSoundFinishedPlaying = (long)(SoundHelper.getSoundLength(selectedSound.getSoundLocation())*1000) + System.currentTimeMillis();
                }
                else
                {
                selectedSound = null;
                }
            }
            else
            {
                stopCurrentSound();
            }
        }
    }

    public void stopCurrentSound()
    {
        if (System.currentTimeMillis() < timeSoundFinishedPlaying)
        {
            SoundsCool.proxy.getChannel().attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
            SoundsCool.proxy.getChannel().attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(new NetworkRegistry.TargetPoint(getWorldObj().provider.dimensionId, xCoord, yCoord, zCoord, 64));
            SoundsCool.proxy.getChannel().writeOutbound(new StopSoundPacket(lastSoundIdentifier));
            timeSoundFinishedPlaying = 0;
        }
    }

    @Override
    public boolean canUpdate()
    {
        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        selectedSound = SoundHandler.getSound(compound.getString("selectedSound"));
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        if (selectedSound != null)
        {
            compound.setString("selectedSound", selectedSound.getSoundName());
        }
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        String soundName = pkt.func_148857_g().getString("selected");
        this.selectedSound = SoundHandler.getSound(soundName);
    }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound compound = new NBTTagCompound();
        if (selectedSound != null)
        {
            compound.setString("selectedSound", selectedSound.getSoundName());
        }
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, compound);
    }
}
