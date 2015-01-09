package com.dynious.soundscool.network.packet.server;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.dynious.soundscool.helper.GuiHelper;
import com.dynious.soundscool.network.packet.client.CheckPresencePacket;

public class OpenGUIPacket implements IMessage
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
    public void fromBytes(ByteBuf bytes)
    {
        this.ID = bytes.readInt();
        GuiHelper.openGui(ID);
    }

    @Override
    public void toBytes(ByteBuf bytes)
    {
        bytes.writeInt(ID);
    }
    
    public static class Handler implements IMessageHandler<OpenGUIPacket, IMessage> {
        @Override
        public IMessage onMessage(OpenGUIPacket message, MessageContext ctx) {
            return null;
        }
    }
}
