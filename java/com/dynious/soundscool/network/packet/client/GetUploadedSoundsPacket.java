package com.dynious.soundscool.network.packet.client;

import com.dynious.soundscool.SoundsCool;
import com.dynious.soundscool.helper.NetworkHelper;
import com.dynious.soundscool.network.packet.IPacket;
import com.dynious.soundscool.network.packet.server.UploadedSoundsPacket;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.DimensionManager;

public class GetUploadedSoundsPacket implements IPacket
{
    int entityID;
    int worldID;

    public GetUploadedSoundsPacket()
    {
    }

    public GetUploadedSoundsPacket(EntityPlayer player)
    {
        this.entityID = player.func_145782_y();
        this.worldID = player.getEntityWorld().provider.dimensionId;
    }

    @Override
    public void readBytes(ByteBuf bytes)
    {
        entityID = bytes.readInt();
        worldID = bytes.readInt();

        Entity entity = DimensionManager.getProvider(worldID).worldObj.getEntityByID(entityID);
        if (entity != null && entity instanceof EntityPlayer)
        {
            NetworkHelper.sendPacketToPlayer(new UploadedSoundsPacket(), (EntityPlayer) entity);
        }
    }

    @Override
    public void writeBytes(ByteBuf bytes)
    {
        bytes.writeInt(entityID);
        bytes.writeInt(worldID);
    }
}
