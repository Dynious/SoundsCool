package com.dynious.soundscool.network.packet.client;

import com.dynious.soundscool.network.packet.IPacket;
import com.dynious.soundscool.tileentity.TileSoundPlayer;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class SoundPlayerSelectPacket implements IPacket
{
    int dimensionId;
    int x, y, z;
    String soundName;
    public SoundPlayerSelectPacket()
    {
    }

    public SoundPlayerSelectPacket(TileSoundPlayer tile)
    {
        this.dimensionId = tile.func_145831_w().provider.dimensionId;
        this.x = tile.field_145851_c;
        this.y = tile.field_145848_d;
        this.z = tile.field_145849_e;
        this.soundName = tile.getSelectedSound().getSoundName();
    }

    @Override
    public void readBytes(ByteBuf bytes)
    {
        dimensionId = bytes.readInt();
        x = bytes.readInt();
        y = bytes.readInt();
        z = bytes.readInt();
        World world = DimensionManager.getWorld(dimensionId);

        int soundNameLength = bytes.readInt();
        char[] soundNameCars = new char[soundNameLength];
        for (int i = 0; i < soundNameLength; i++)
        {
            soundNameCars[i] = bytes.readChar();
        }
        soundName = String.valueOf(soundNameCars);

        if (world != null)
        {
            TileEntity tile = world.func_147438_o(x, y, z);
            if (tile != null && tile instanceof TileSoundPlayer)
            {
                ((TileSoundPlayer)tile).selectSound(soundName);
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

        bytes.writeInt(soundName.length());
        for (char c : soundName.toCharArray())
        {
            bytes.writeChar(c);
        }
    }
}
