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
    private int selected = -1;

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

    public void selectSoundIndex(int selected)
    {
        this.selected = selected;
        if (this.func_145831_w().isRemote)
        {
            SoundsCool.proxy.getChannel().writeOutbound(new SoundPlayerSelectPacket(this));

            if (selected >= 0 && selected <= NetworkHandler.uploadedSounds.size())
            {
                Sound sound = SoundHandler.getSound(NetworkHandler.uploadedSounds.get(selected).getSoundName());
                if (sound != null)
                {
                    this.selectedSound = sound;
                }
                else
                {
                    this.selectedSound = NetworkHandler.uploadedSounds.get(selected);
                }
            }
            else
            {
                this.selectedSound = null;
            }
        }
        else
        {
            if (selected >= 0 && selected <= SoundHandler.getSounds().size())
            {
                this.selectedSound = SoundHandler.getSounds().get(selected);
            }
            else
            {
                this.selectedSound = null;
            }
        }
    }

    public int getSelectedIndex()
    {
        return this.selected;
    }

    public Sound getSelectedSound()
    {
        if (selected != -1 && selectedSound == null)
        {
            selectSoundIndex(selected);
        }
        return selectedSound;
    }

    public void playCurrentSound()
    {
        if (selected != -1 && selectedSound == null)
        {
            selectSoundIndex(selected);
        }
        if (selectedSound != null)
        {
            SoundsCool.proxy.getChannel().attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
            SoundsCool.proxy.getChannel().attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(NetworkRegistry.INSTANCE.new TargetPoint(func_145831_w().provider.dimensionId, field_145851_c, field_145848_d, field_145849_e, 64));
            SoundsCool.proxy.getChannel().writeOutbound(new ServerPlaySoundPacket(selectedSound.getSoundName(), field_145851_c, field_145848_d, field_145849_e));
        }
    }

    //readFromNBT
    @Override
    public void func_145839_a(NBTTagCompound compound)
    {
        super.func_145839_a(compound);
        selected = compound.getInteger("selected");
    }

    //writeToNBT
    @Override
    public void func_145841_b(NBTTagCompound compound)
    {
        super.func_145841_b(compound);
        compound.setInteger("selected", selected);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        selected = pkt.func_148857_g().getInteger("selected");
    }

    //getDescriptionPacket()
    @Override
    public Packet func_145844_m()
    {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setInteger("selected", selected);
        return new S35PacketUpdateTileEntity(field_145851_c, field_145848_d, field_145849_e, 1, compound);
    }
}
