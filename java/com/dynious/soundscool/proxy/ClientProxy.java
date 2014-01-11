package com.dynious.soundscool.proxy;

import com.dynious.soundscool.handler.event.SoundEventHandler;
import cpw.mods.fml.relauncher.Side;
import io.netty.channel.embedded.EmbeddedChannel;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy
{
    @Override
    public void initTileEntities()
    {
        super.initTileEntities();
    }

    @Override
    public void soundSetup()
    {
        super.soundSetup();

        MinecraftForge.EVENT_BUS.register(new SoundEventHandler());
    }

    @Override
    public EmbeddedChannel getChannel()
    {
        return channel.get(Side.CLIENT);
    }
}
