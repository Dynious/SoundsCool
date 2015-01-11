package com.dynious.soundscool.network.packet.server;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.dynious.soundscool.handler.DelayedPlayHandler;
import com.dynious.soundscool.network.packet.client.CheckPresencePacket;

public class SoundNotFoundPacket implements IMessage
{
    String soundName;
    public SoundNotFoundPacket()
    {
    }

    public SoundNotFoundPacket(String soundName)
    {
        this.soundName = soundName;
    }

    @Override
    public void fromBytes(ByteBuf bytes)
    {
        int fileLength = bytes.readInt();
        char[] fileCars = new char[fileLength];
        for (int i = 0; i < fileLength; i++)
        {
            fileCars[i] = bytes.readChar();
        }
        soundName = String.valueOf(fileCars);
        DelayedPlayHandler.removeSound(soundName);
    }

    @Override
    public void toBytes(ByteBuf bytes)
    {
        bytes.writeInt(soundName.length());
        for (char c : soundName.toCharArray())
        {
            bytes.writeChar(c);
        }
    }
    
    public static class Handler implements IMessageHandler<SoundNotFoundPacket, IMessage> {
        @Override
        public IMessage onMessage(SoundNotFoundPacket message, MessageContext ctx) {
            return null;
        }
    }
}
