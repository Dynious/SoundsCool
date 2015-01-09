package com.dynious.soundscool.network.packet.client;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.dynious.soundscool.helper.NetworkHelper;
import com.dynious.soundscool.network.packet.server.UploadedSoundsPacket;

public class GetUploadedSoundsPacket implements IMessage
{
    int entityID;
    int worldID;

    public GetUploadedSoundsPacket()
    {
    }

    public GetUploadedSoundsPacket(EntityPlayer player)
    {
        this.entityID = player.getEntityId();
        this.worldID = player.getEntityWorld().provider.getDimensionId();
    }

    @Override
    public void fromBytes(ByteBuf bytes)
    {
        entityID = bytes.readInt();
        worldID = bytes.readInt();

        Entity entity = DimensionManager.getWorld(worldID).getEntityByID(entityID);
        if (entity != null && entity instanceof EntityPlayer)
        {
            NetworkHelper.sendMessageToAll(new UploadedSoundsPacket());
        }
    }

    @Override
    public void toBytes(ByteBuf bytes)
    {
        bytes.writeInt(entityID);
        bytes.writeInt(worldID);
    }
    
    public static class Handler implements IMessageHandler<GetUploadedSoundsPacket, IMessage> {
        @Override
        public IMessage onMessage(GetUploadedSoundsPacket message, MessageContext ctx) {
            return null;
        }
    }
}
