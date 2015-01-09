package com.dynious.soundscool.proxy;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

import com.dynious.soundscool.SoundsCool;
import com.dynious.soundscool.lib.Names;
import com.dynious.soundscool.network.packet.SoundChunkPacket;
import com.dynious.soundscool.network.packet.SoundUploadedPacket;
import com.dynious.soundscool.network.packet.client.CheckPresencePacket;
import com.dynious.soundscool.network.packet.client.GetUploadedSoundsPacket;
import com.dynious.soundscool.network.packet.client.RemoveSoundPacket;
import com.dynious.soundscool.network.packet.client.SoundPlayerPlayPacket;
import com.dynious.soundscool.network.packet.client.SoundPlayerSelectPacket;
import com.dynious.soundscool.network.packet.server.OpenGUIPacket;
import com.dynious.soundscool.network.packet.server.ServerPlaySoundPacket;
import com.dynious.soundscool.network.packet.server.SoundNotFoundPacket;
import com.dynious.soundscool.network.packet.server.SoundReceivedPacket;
import com.dynious.soundscool.network.packet.server.SoundRemovedPacket;
import com.dynious.soundscool.network.packet.server.StopSoundPacket;
import com.dynious.soundscool.network.packet.server.UploadedSoundsPacket;
import com.dynious.soundscool.tileentity.TileSoundPlayer;

public class CommonProxy
{

    public void initTileEntities()
    {
        GameRegistry.registerTileEntity(TileSoundPlayer.class, Names.soundPlayer);
    }

    public void registerMessages()
    {
    	SoundsCool.network.registerMessage(CheckPresencePacket.Handler.class, CheckPresencePacket.class, 0, Side.SERVER);
    	SoundsCool.network.registerMessage(GetUploadedSoundsPacket.Handler.class, GetUploadedSoundsPacket.class, 1, Side.SERVER);
    	SoundsCool.network.registerMessage(RemoveSoundPacket.Handler.class, RemoveSoundPacket.class, 2, Side.SERVER);
    	SoundsCool.network.registerMessage(SoundPlayerPlayPacket.Handler.class, SoundPlayerPlayPacket.class, 3, Side.SERVER);
    	SoundsCool.network.registerMessage(SoundPlayerSelectPacket.Handler.class, SoundPlayerSelectPacket.class, 4, Side.SERVER);
    	
    	SoundsCool.network.registerMessage(OpenGUIPacket.Handler.class, OpenGUIPacket.class, 5, Side.CLIENT);
    	SoundsCool.network.registerMessage(ServerPlaySoundPacket.Handler.class, ServerPlaySoundPacket.class, 6, Side.CLIENT);
    	SoundsCool.network.registerMessage(SoundNotFoundPacket.Handler.class, SoundNotFoundPacket.class, 7, Side.CLIENT);
    	SoundsCool.network.registerMessage(SoundReceivedPacket.Handler.class, SoundReceivedPacket.class, 8, Side.CLIENT);
    	SoundsCool.network.registerMessage(SoundRemovedPacket.Handler.class, SoundRemovedPacket.class, 9, Side.CLIENT);
    	SoundsCool.network.registerMessage(StopSoundPacket.Handler.class, StopSoundPacket.class, 10, Side.CLIENT);
    	SoundsCool.network.registerMessage(UploadedSoundsPacket.Handler.class, UploadedSoundsPacket.class, 11, Side.CLIENT);
        
    	SoundsCool.network.registerMessage(SoundChunkPacket.Handler.class, SoundChunkPacket.class, 12, Side.CLIENT);
    	SoundsCool.network.registerMessage(SoundChunkPacket.Handler.class, SoundChunkPacket.class, 12, Side.SERVER);
    	
    	SoundsCool.network.registerMessage(SoundUploadedPacket.Handler.class, SoundUploadedPacket.class, 13, Side.CLIENT);
    	SoundsCool.network.registerMessage(SoundUploadedPacket.Handler.class, SoundUploadedPacket.class, 13, Side.SERVER);
    }

    public void soundSetup()
    {
    }

    public void UISetup()
    {
    }
    
    public void registerBlocks()
    {
    }
}
