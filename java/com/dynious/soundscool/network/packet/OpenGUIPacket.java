package com.dynious.soundscool.network.packet;

import com.dynious.soundscool.SoundsCool;
import com.dynious.soundscool.handler.DelayedPlayHandler;
import com.dynious.soundscool.helper.GuiHelper;
import cpw.mods.fml.common.FMLCommonHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class OpenGUIPacket implements IPacket
{
    int ID = -1;
    public OpenGUIPacket()
    {
    }

    public OpenGUIPacket(int guiID)
    {
        this.ID = guiID;
    }

    @Override
    public void readBytes(ByteBuf bytes)
    {
        this.ID = bytes.readInt();
        GuiHelper.openGui(ID);
    }

    @Override
    public void writeBytes(ByteBuf bytes)
    {
        bytes.writeInt(ID);
    }
}
