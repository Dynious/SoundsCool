package com.dynious.soundscool.network.packet.client;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.dynious.soundscool.tileentity.TileSoundPlayer;

public class SoundPlayerPlayPacket implements IMessage
{
    int dimensionId;
    int x, y, z;
    public SoundPlayerPlayPacket()
    {
    }

    public SoundPlayerPlayPacket(TileSoundPlayer tile)
    {
        this.dimensionId = tile.getWorld().provider.getDimensionId();
        this.x = tile.getPos().getX();
        this.y = tile.getPos().getY();
        this.z = tile.getPos().getZ();
    }

    @Override
    public void fromBytes(ByteBuf bytes)
    {
        dimensionId = bytes.readInt();
        x = bytes.readInt();
        y = bytes.readInt();
        z = bytes.readInt();
        World world = DimensionManager.getWorld(dimensionId);
        if (world != null)
        {
            TileEntity tile = world.getTileEntity(new BlockPos(x,y,z));
            if (tile != null && tile instanceof TileSoundPlayer)
            {
                ((TileSoundPlayer)tile).playCurrentSound();
            }
        }
    }

    @Override
    public void toBytes(ByteBuf bytes)
    {
        bytes.writeInt(dimensionId);
        bytes.writeInt(x);
        bytes.writeInt(y);
        bytes.writeInt(z);
    }
    
    public static class Handler implements IMessageHandler<SoundPlayerPlayPacket, IMessage> {
        @Override
        public IMessage onMessage(SoundPlayerPlayPacket message, MessageContext ctx) {
            return null;
        }
    }
}
