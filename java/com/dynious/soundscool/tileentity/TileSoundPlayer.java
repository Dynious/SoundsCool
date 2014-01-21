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

        if (this.func_145831_w().isRemote)
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
        if (selectedSound != null && timeSoundFinishedPlaying < System.currentTimeMillis())
        {
            if (SoundHandler.getSound(selectedSound.getSoundName()) != null)
            {
                lastSoundIdentifier = UUID.randomUUID().toString();
                SoundsCool.proxy.getChannel().attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
                SoundsCool.proxy.getChannel().attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(new NetworkRegistry.TargetPoint(func_145831_w().provider.dimensionId, field_145851_c, field_145848_d, field_145849_e, 64));
                SoundsCool.proxy.getChannel().writeOutbound(new ServerPlaySoundPacket(selectedSound.getSoundName(), lastSoundIdentifier, field_145851_c, field_145848_d, field_145849_e));
                timeSoundFinishedPlaying = (long)(SoundHelper.getSoundLength(selectedSound.getSoundLocation())*1000) + System.currentTimeMillis();
            }
            else
            {
                selectedSound = null;
            }
        }
    }

    public void stopCurrentSound()
    {
        if (System.currentTimeMillis() < timeSoundFinishedPlaying)
        {
            SoundsCool.proxy.getChannel().attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
            SoundsCool.proxy.getChannel().attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(new NetworkRegistry.TargetPoint(func_145831_w().provider.dimensionId, field_145851_c, field_145848_d, field_145849_e, 64));
            SoundsCool.proxy.getChannel().writeOutbound(new StopSoundPacket(lastSoundIdentifier));
        }
    }

    //readFromNBT
    @Override
    public void func_145839_a(NBTTagCompound compound)
    {
        super.func_145839_a(compound);
        selectedSound = SoundHandler.getSound(compound.getString("selectedSound"));
    }

    //writeToNBT
    @Override
    public void func_145841_b(NBTTagCompound compound)
    {
        super.func_145841_b(compound);
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

    //getDescriptionPacket()
    @Override
    public Packet func_145844_m()
    {
        NBTTagCompound compound = new NBTTagCompound();
        if (selectedSound != null)
        {
            compound.setString("selectedSound", selectedSound.getSoundName());
        }
        return new S35PacketUpdateTileEntity(field_145851_c, field_145848_d, field_145849_e, 1, compound);
    }
}
