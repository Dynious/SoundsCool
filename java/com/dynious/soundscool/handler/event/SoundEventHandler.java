package com.dynious.soundscool.handler.event;

import com.dynious.soundscool.SoundsCool;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.sound.SoundLoadEvent;

public class SoundEventHandler
{
    @SubscribeEvent
    public void onSoundLoad(SoundLoadEvent event)
    {
        SoundsCool.proxy.initSounds();
    }
}
