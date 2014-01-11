package com.dynious.soundscool.network.packet.server;

import com.dynious.soundscool.helper.GuiHelper;
import com.dynious.soundscool.network.packet.IPacket;
import io.netty.buffer.ByteBuf;

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
