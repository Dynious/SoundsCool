package com.dynious.soundscool.tileentity;

import com.dynious.soundscool.SoundsCool;
import com.dynious.soundscool.handler.NetworkHandler;
import com.dynious.soundscool.handler.SoundHandler;
import com.dynious.soundscool.network.packet.client.SoundPlayerSelectPacket;
import com.dynious.soundscool.network.packet.server.ServerPlaySoundPacket;
import com.dynious.soundscool.sound.Sound;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileSoundPlayer extends TileEntity
{
    private boolean isPowered = false;
    private Sound selectedSound;

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
        if (this.func_145831_w().isRemote)
        {
            Sound sound = SoundHandler.getSound(soundName);
            if (sound != null)
            {
                this.selectedSound = sound;
            }
            else
            {
                this.selectedSound = NetworkHandler.getServerSound(soundName);
            }

            SoundsCool.proxy.getChannel().writeOutbound(new SoundPlayerSelectPacket(this));
        }
        else
        {
            this.selectedSound = SoundHandler.getSound(soundName);
        }
    }

    public Sound getSelectedSound()
    {
        if (selectedSound != null && NetworkHandler.getServerSound(selectedSound.getSoundName()) == null)
        {
            selectedSound = null;
        }
        return selectedSound;
    }

    public void playCurrentSound()
    {
        if (selectedSound != null)
        {
            if (SoundHandler.getSound(selectedSound.getSoundName()) != null)
            {
                SoundsCool.proxy.getChannel().attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
                SoundsCool.proxy.getChannel().attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(new NetworkRegistry.TargetPoint(func_145831_w().provider.dimensionId, field_145851_c, field_145848_d, field_145849_e, 64));
                SoundsCool.proxy.getChannel().writeOutbound(new ServerPlaySoundPacket(selectedSound.getSoundName(), field_145851_c, field_145848_d, field_145849_e));
            }
            else
            {
                selectedSound = null;
            }
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
        Sound sound = SoundHandler.getSound(soundName);
        if (sound != null)
        {
            this.selectedSound = sound;
        }
        else
        {
            this.selectedSound = NetworkHandler.getServerSound(soundName);
        }
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
