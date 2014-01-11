package com.dynious.soundscool.network.packet;

import com.dynious.soundscool.SoundsCool;
import com.dynious.soundscool.handler.SoundHandler;
import com.dynious.soundscool.sound.Sound;
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

            SoundsCool.proxy.getServerChannel().attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
            SoundsCool.proxy.getServerChannel().attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set((EntityPlayer) entity);
            SoundsCool.proxy.getServerChannel().writeOutbound(new UploadedSoundsPacket());
        }
    }

    @Override
    public void writeBytes(ByteBuf bytes)
    {
        bytes.writeInt(entityID);
        bytes.writeInt(worldID);
    }
}
