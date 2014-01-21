package com.dynious.soundscool.proxy;

import com.dynious.soundscool.handler.event.SoundEventHandler;
import cpw.mods.fml.relauncher.Side;
import io.netty.channel.embedded.EmbeddedChannel;
import net.minecraftforge.common.MinecraftForge;

import javax.swing.*;

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
    public void UISetup()
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public EmbeddedChannel getChannel()
    {
        return channels.get(Side.CLIENT);
    }
}
