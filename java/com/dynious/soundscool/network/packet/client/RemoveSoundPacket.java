package com.dynious.soundscool.network.packet.client;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.dynious.soundscool.handler.SoundHandler;
import com.dynious.soundscool.sound.Sound;

public class RemoveSoundPacket implements IMessage
{
    String soundName;

    public RemoveSoundPacket()
    {
    }

    public RemoveSoundPacket(String soundName)
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

        Sound sound = SoundHandler.getSound(soundName);
        SoundHandler.removeSound(sound);

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
    
    public static class Handler implements IMessageHandler<RemoveSoundPacket, IMessage> {
        @Override
        public IMessage onMessage(RemoveSoundPacket message, MessageContext ctx) {
            return null;
        }
    }
}
