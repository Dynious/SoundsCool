package com.dynious.soundscool.network.packet.client;

import com.dynious.soundscool.SoundsCool;
import com.dynious.soundscool.network.packet.IPacket;
import com.dynious.soundscool.network.packet.server.ServerPlaySoundPacket;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraft.world.World;

public class ClientPlaySoundPacket implements IPacket
{
    String soundName;
    int dimensionId;
    int x, y, z;
    public ClientPlaySoundPacket()
    {
    }

    public ClientPlaySoundPacket(String soundName, World world, int x, int y, int z)
    {
        this.soundName = soundName;
        this.dimensionId = world.provider.dimensionId;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void readBytes(ByteBuf bytes)
    {
        int soundLength = bytes.readInt();
        char[] fileCars = new char[soundLength];
        for (int i = 0; i < soundLength; i++)
        {
            fileCars[i] = bytes.readChar();
        }
        soundName = String.valueOf(fileCars);
        dimensionId = bytes.readInt();
        x = bytes.readInt();
        y = bytes.readInt();
        z = bytes.readInt();
        SoundsCool.proxy.getChannel().attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
        SoundsCool.proxy.getChannel().attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(new NetworkRegistry.TargetPoint(dimensionId, x, y, z, 64));
        SoundsCool.proxy.getChannel().writeOutbound(new ServerPlaySoundPacket(soundName, "", x, y, z));
    }

    @Override
    public void writeBytes(ByteBuf bytes)
    {
        bytes.writeInt(soundName.length());
        for (char c : soundName.toCharArray())
        {
            bytes.writeChar(c);
        }
        bytes.writeInt(dimensionId);
        bytes.writeInt(x);
        bytes.writeInt(y);
        bytes.writeInt(z);
    }
}
