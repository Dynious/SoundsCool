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

public class SoundPlayerSelectPacket implements IMessage
{
    int dimensionId;
    int x, y, z;
    String soundName;
    public SoundPlayerSelectPacket()
    {
    }

    public SoundPlayerSelectPacket(TileSoundPlayer tile)
    {
        this.dimensionId = tile.getWorld().provider.getDimensionId();
        this.x = tile.getPos().getX();;
        this.y = tile.getPos().getY();;
        this.z = tile.getPos().getZ();;
        this.soundName = tile.getSelectedSound().getSoundName();
    }

    @Override
    public void fromBytes(ByteBuf bytes)
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
            TileEntity tile = world.getTileEntity(new BlockPos(x,y,z));
            if (tile != null && tile instanceof TileSoundPlayer)
            {
                ((TileSoundPlayer)tile).selectSound(soundName);
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

        bytes.writeInt(soundName.length());
        for (char c : soundName.toCharArray())
        {
            bytes.writeChar(c);
        }
    }
    
    public static class Handler implements IMessageHandler<SoundPlayerSelectPacket, IMessage> {
        @Override
        public IMessage onMessage(SoundPlayerSelectPacket message, MessageContext ctx) {
            return null;
        }
    }
}
