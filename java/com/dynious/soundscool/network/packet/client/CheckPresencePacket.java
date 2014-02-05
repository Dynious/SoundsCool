package com.dynious.soundscool.network.packet.client;

import com.dynious.soundscool.handler.SoundHandler;
import com.dynious.soundscool.helper.NetworkHelper;
import com.dynious.soundscool.network.packet.IPacket;
import com.dynious.soundscool.network.packet.server.SoundNotFoundPacket;
import com.dynious.soundscool.sound.Sound;
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
        this.entityID = player.getEntityId();
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

            if (sound != null)
            {
                NetworkHelper.serverSoundUpload(sound, (EntityPlayer) entity);
            }
            else
            {
                NetworkHelper.sendPacketToPlayer(new SoundNotFoundPacket(fileName), (EntityPlayer)entity);
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
