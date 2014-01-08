package com.dynious.soundscool.proxy;

import com.dynious.soundscool.client.sound.SoundPlayer;
import com.dynious.soundscool.lib.Names;
import com.dynious.soundscool.lib.Reference;
import com.dynious.soundscool.network.ChannelHandler;
import com.dynious.soundscool.tileentity.TileSoundPlayer;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import io.netty.channel.embedded.EmbeddedChannel;

import java.util.EnumMap;

public class CommonProxy
{
    public EnumMap<Side, FMLEmbeddedChannel> channel;

    public void initTileEntities()
    {
        GameRegistry.registerTileEntity(TileSoundPlayer.class, Names.soundPlayer);
    }

    public void initNetworking()
    {
        channel = NetworkRegistry.INSTANCE.newChannel(Reference.modid, new ChannelHandler());
    }

    public EmbeddedChannel getServerChannel() {
        return channel.get(Side.SERVER);
    }

    public EmbeddedChannel getClientChannel() {
        return channel.get(Side.CLIENT);
    }

    public void initSounds()
    {

    }
}
