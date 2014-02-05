package com.dynious.soundscool.network.packet.client;

import com.dynious.soundscool.network.packet.IPacket;
import com.dynious.soundscool.tileentity.TileSoundPlayer;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class SoundPlayerPlayPacket implements IPacket
{
    int dimensionId;
    int x, y, z;
    public SoundPlayerPlayPacket()
    {
    }

    public SoundPlayerPlayPacket(TileSoundPlayer tile)
    {
        this.dimensionId = tile.getWorldObj().provider.dimensionId;
        this.x = tile.xCoord;
        this.y = tile.yCoord;
        this.z = tile.zCoord;
    }

    @Override
    public void readBytes(ByteBuf bytes)
    {
        dimensionId = bytes.readInt();
        x = bytes.readInt();
        y = bytes.readInt();
        z = bytes.readInt();
        World world = DimensionManager.getWorld(dimensionId);
        if (world != null)
        {
            TileEntity tile = world.getTileEntity(x, y, z);
            if (tile != null && tile instanceof TileSoundPlayer)
            {
                ((TileSoundPlayer)tile).playCurrentSound();
            }
        }
    }

    @Override
    public void writeBytes(ByteBuf bytes)
    {
        bytes.writeInt(dimensionId);
        bytes.writeInt(x);
        bytes.writeInt(y);
        bytes.writeInt(z);
    }
}
