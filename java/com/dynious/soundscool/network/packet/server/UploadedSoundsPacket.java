package com.dynious.soundscool.network.packet.server;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.dynious.soundscool.handler.SoundHandler;
import com.dynious.soundscool.network.packet.client.CheckPresencePacket;
import com.dynious.soundscool.sound.Sound;

public class UploadedSoundsPacket implements IMessage
{
    public UploadedSoundsPacket()
    {
    }

    @Override
    public void fromBytes(ByteBuf bytes)
    {
        int sounds = bytes.readInt();
        for (int y = 0; y < sounds; y++)
        {
            int soundNameLength = bytes.readInt();
            char[] soundNameCars = new char[soundNameLength];
            for (int i = 0; i < soundNameLength; i++)
            {
                soundNameCars[i] = bytes.readChar();
            }
            int soundCatLength = bytes.readInt();
            char[] soundCatChars = new char[soundCatLength];
            for (int i = 0; i < soundCatLength; i++)
            {
                soundCatChars[i] = bytes.readChar();
            }
            SoundHandler.addRemoteSound(String.valueOf(soundNameCars), String.valueOf(soundCatChars));
        }
    }

    @Override
    public void toBytes(ByteBuf bytes)
    {
        bytes.writeInt(SoundHandler.getSounds().size());
        for (Sound sound : SoundHandler.getSounds())
        {
            bytes.writeInt(sound.getSoundName().length());
            for (char c : sound.getSoundName().toCharArray())
            {
                bytes.writeChar(c);
            }
            bytes.writeInt(sound.getCategory().length());
            for (char c : sound.getCategory().toCharArray())
            {
                bytes.writeChar(c);
            }
        }
    }
    
    public static class Handler implements IMessageHandler<UploadedSoundsPacket, IMessage> {
        @Override
        public IMessage onMessage(UploadedSoundsPacket message, MessageContext ctx) {
            return null;
        }
    }
}
