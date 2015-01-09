package com.dynious.soundscool.handler;

import com.dynious.soundscool.SoundsCool;
import com.dynious.soundscool.client.audio.SoundPlayer;
import com.dynious.soundscool.helper.NetworkHelper;
import com.dynious.soundscool.helper.SoundHelper;
import com.dynious.soundscool.network.packet.client.CheckPresencePacket;
import com.dynious.soundscool.network.packet.server.SoundRemovedPacket;
import com.dynious.soundscool.sound.Sound;
import com.google.common.io.Files;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SoundHandler
{
    private static File soundsFolder;
    private static ArrayList<Sound> sounds;

    public static File getSoundsFolder()
    {
        if (soundsFolder == null)
        {
            findSounds();
        }
        return soundsFolder;
    }
    public static ArrayList<Sound> getSounds()
    {
        if (sounds == null)
        {
            findSounds();
        }
        return sounds;
    }

    public static ArrayList<Sound> getLocalSounds()
    {
        ArrayList<Sound> localSounds = new ArrayList<Sound>();
        for (Sound sound : getSounds())
        {
            if (sound.hasLocal())
                localSounds.add(sound);
        }
        return localSounds;
    }

    public static ArrayList<Sound> getRemoteSounds()
    {
        ArrayList<Sound> remoteSounds = new ArrayList<Sound>();
        for (Sound sound : getSounds())
        {
            if (sound.hasRemote())
                remoteSounds.add(sound);
        }
        return remoteSounds;
    }

    public static Sound getSound(String fileName)
    {
        for (Sound sound : getSounds())
        {
            if(sound.getSoundName().equals(fileName))
            {
                return sound;
            }
        }
        return null;
    }

    public static void findSounds()
    {
        soundsFolder = new File("sounds");
        if (!soundsFolder.exists())
        {
            soundsFolder.mkdir();
        }
        sounds = new ArrayList<Sound>();
        addSoundsFromDir(soundsFolder);
    }

    public static void removeSound(Sound sound)
    {
        if (sound != null)
        {
            if (!sound.getSoundLocation().delete())
            {
                sound.getSoundLocation().deleteOnExit();
            }
            sounds.remove(sound);
            if (FMLCommonHandler.instance().getEffectiveSide().isServer())
            {
                NetworkHelper.sendMessageToAll(new SoundRemovedPacket(sound.getSoundName()));
            }
        }
    }

    private static void addSoundsFromDir(File dir)
    {
        for (File file : dir.listFiles())
        {
            if (file.isFile())
            {
                if (file.getName().endsWith(".ogg") || file.getName().endsWith(".wav") || file.getName().endsWith(".mp3"))
                {
                    sounds.add(new Sound(file));
                }
            }
            else if (file.isDirectory())
            {
                addSoundsFromDir(file);
            }
        }
    }

    public static void addRemoteSound(String soundName, String remoteCategory)
    {
        Sound sound = getSound(soundName);
        if (sound != null)
        {
            if (sound.hasLocal())
            {
                sound.onSoundUploaded(remoteCategory);
            }
        }
        else
        {
            sounds.add(new Sound(soundName, remoteCategory));
        }
    }

    public static void addLocalSound(String soundName, File soundFile)
    {
        Sound sound = getSound(soundName);
        if (sound != null)
        {
            if (sound.getState() != Sound.SoundState.SYNCED)
            {
                sound.onSoundDownloaded(soundFile);
            }
        }
        else
        {
            sounds.add(new Sound(soundFile));
        }
    }

    public static void remoteRemovedSound(Sound sound)
    {
        if (!sound.hasLocal())
        {
            sounds.remove(sound);
        }
        else
        {
            sound.setState(Sound.SoundState.LOCAL_ONLY);
        }
    }

    @SideOnly(Side.CLIENT)
    public static void playSound(String soundName, String identifier, int x, int y, int z)
    {
        Sound sound = SoundHandler.getSound(soundName);
        if (sound.hasLocal())
        {
            SoundPlayer.playSound(sound.getSoundLocation(), identifier, x, y, z, true);
        }
        else if (sound.getState() != Sound.SoundState.DOWNLOADING)
        {
            sound.setState(Sound.SoundState.DOWNLOADING);
            DelayedPlayHandler.addDelayedPlay(soundName, identifier, x, y, z);
            SoundsCool.network.sendToServer(new CheckPresencePacket(soundName, Minecraft.getMinecraft().thePlayer));
        }
    }
    @SideOnly(Side.CLIENT)
    public static Sound setupSound(File file)
    {
        File category;
        if (Minecraft.getMinecraft().getCurrentServerData() != null)
        {
            //TODO: make this not return null, dammit MC!
            category = new File("sounds" + File.separator + Minecraft.getMinecraft().getCurrentServerData().serverMOTD);
        }
        else
        {
            category = new File("sounds" + File.separator + Minecraft.getMinecraft().thePlayer.getDisplayNameString());
        }
        if (!category.exists())
        {
            category.mkdir();
        }
        File newFile = new File(category.getAbsolutePath() + File.separator + file.getName());
        try
        {
            //TODO: FIXXXX
            if ((!newFile.exists() || !Files.equal(file, newFile)) && !SoundHelper.isSoundInSoundsFolder(file))
            {
                Files.copy(file, newFile);
                findSounds();
            }
            else
            {
                return new Sound(file);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return new Sound(newFile);
    }

}
