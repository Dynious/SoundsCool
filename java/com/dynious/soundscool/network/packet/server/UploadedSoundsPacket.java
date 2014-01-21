package com.dynious.soundscool.network.packet.server;

import com.dynious.soundscool.handler.SoundHandler;
import com.dynious.soundscool.network.packet.IPacket;
import com.dynious.soundscool.sound.Sound;
import io.netty.buffer.ByteBuf;

public class UploadedSoundsPacket implements IPacket
{
    public UploadedSoundsPacket()
    {
    }

    @Override
    public void readBytes(ByteBuf bytes)
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
    public void writeBytes(ByteBuf bytes)
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
}
