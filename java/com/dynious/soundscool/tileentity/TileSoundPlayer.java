package com.dynious.soundscool.tileentity;

import com.dynious.soundscool.SoundsCool;
import com.dynious.soundscool.handler.NetworkHandler;
import com.dynious.soundscool.handler.SoundHandler;
import com.dynious.soundscool.network.packet.client.SoundPlayerSelectPacket;
import com.dynious.soundscool.network.packet.server.ServerPlaySoundPacket;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.tileentity.TileEntity;

public class TileSoundPlayer extends TileEntity
{
    private boolean isPowered = false;
    private String selectedSound = "";
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
                this.selectedSound = NetworkHandler.uploadedSounds.get(selected);
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
                this.selectedSound = SoundHandler.getSounds().get(selected).getSoundName();
            }
            else
            {
                this.selectedSound = null;
            }
        }
    }

    public int getSelected()
    {
        return this.selected;
    }

    @SideOnly(Side.SERVER)
    public void playCurrentSound()
    {
        SoundsCool.proxy.getChannel().attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
        SoundsCool.proxy.getChannel().attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(NetworkRegistry.INSTANCE.new TargetPoint(func_145831_w().provider.dimensionId, field_145851_c, field_145848_d, field_145849_e, 64));
        SoundsCool.proxy.getChannel().writeOutbound(new ServerPlaySoundPacket(selectedSound, field_145851_c, field_145848_d, field_145849_e));
    }
}
