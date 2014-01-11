package com.dynious.soundscool.network.packet.client;

import com.dynious.soundscool.SoundsCool;
import com.dynious.soundscool.handler.SoundHandler;
import com.dynious.soundscool.network.packet.IPacket;
import com.dynious.soundscool.network.packet.server.ServerSoundPacket;
import com.dynious.soundscool.network.packet.server.SoundNotFoundPacket;
import com.dynious.soundscool.sound.Sound;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.DimensionManager;

public class CheckPresencePacket implements IPacket
{
    String fileName;
    int entityID;
    int worldID;

    public CheckPresencePacket()
    {
    }

    public CheckPresencePacket(String soundName, EntityPlayer player)
    {
        this.fileName = soundName;
        this.entityID = player.func_145782_y();
        this.worldID = player.getEntityWorld().provider.dimensionId;
    }

    @Override
    public void readBytes(ByteBuf bytes)
    {
        int fileLength = bytes.readInt();
        char[] fileCars = new char[fileLength];
        for (int i = 0; i < fileLength; i++)
        {
            fileCars[i] = bytes.readChar();
        }
        fileName = String.valueOf(fileCars);
        entityID = bytes.readInt();
        worldID = bytes.readInt();

        Entity entity = DimensionManager.getProvider(worldID).worldObj.getEntityByID(entityID);
        if (entity != null && entity instanceof EntityPlayer)
        {
            Sound sound = SoundHandler.getSound(fileName);

            SoundsCool.proxy.getServerChannel().attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
            SoundsCool.proxy.getServerChannel().attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set((EntityPlayer) entity);
            if (sound != null)
            {
                SoundsCool.proxy.getServerChannel().writeOutbound(new ServerSoundPacket(sound));
            }
            else
            {
                SoundsCool.proxy.getServerChannel().writeOutbound(new SoundNotFoundPacket(fileName));
            }
        }
    }

    @Override
    public void writeBytes(ByteBuf bytes)
    {
        bytes.writeInt(fileName.length());
        for (char c : fileName.toCharArray())
        {
            bytes.writeChar(c);
        }
        bytes.writeInt(entityID);
        bytes.writeInt(worldID);
    }
}
