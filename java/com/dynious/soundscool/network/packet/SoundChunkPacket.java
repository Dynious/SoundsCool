package com.dynious.soundscool.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.dynious.soundscool.handler.NetworkHandler;
import com.dynious.soundscool.network.packet.client.CheckPresencePacket;

public class SoundChunkPacket implements IMessage
{
    String soundName;
    byte[] soundChunk;

    public SoundChunkPacket()
    {
    }

    public SoundChunkPacket(String soundName, byte[] soundChunk)
    {
        this.soundName = soundName;
        this.soundChunk = soundChunk;
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

        int soundByteLength = bytes.readInt();
        byte[] soundByteArr = new byte[soundByteLength];
        for (int i = 0; i < soundByteLength; i++)
        {
            soundByteArr[i] = bytes.readByte();
        }
        NetworkHandler.addSoundChunk(soundName, soundByteArr);
    }

    @Override
    public void toBytes(ByteBuf bytes)
    {
        bytes.writeInt(soundName.length());
        for (char c : soundName.toCharArray())
        {
            bytes.writeChar(c);
        }
        bytes.writeInt(soundChunk.length);
        bytes.writeBytes(soundChunk);
    }
    
    public static class Handler implements IMessageHandler<SoundChunkPacket, IMessage> {
        @Override
        public IMessage onMessage(SoundChunkPacket message, MessageContext ctx) {
            return null;
        }
    }
}
