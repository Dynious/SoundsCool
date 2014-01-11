package com.dynious.soundscool.handler;

import com.dynious.soundscool.SoundsCool;
import com.dynious.soundscool.client.audio.SoundPlayer;
import com.dynious.soundscool.network.packet.client.CheckPresencePacket;
import com.dynious.soundscool.sound.Sound;
import com.google.common.io.Files;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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

    public static Sound getSound(String category, String fileName)
    {
        for (Sound sound : getSounds())
        {
            if(sound.getCategory().equals(category) && sound.getSoundName().equals(fileName))
            {
                return sound;
            }
        }
        return null;
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

    @SideOnly(Side.CLIENT)
    public static void playSound(String soundName, int x, int y, int z)
    {
        Sound sound = SoundHandler.getSound(Minecraft.getMinecraft().func_147104_D().serverName, soundName);
        if (sound != null)
        {
            SoundPlayer.playSound(sound.getSoundLocation(), x, y, z);
        }
        else
        {
            DelayedPlayHandler.addDelayedPlay(soundName, x, y, z);
            SoundsCool.proxy.getClientChannel().writeOutbound(new CheckPresencePacket(soundName, Minecraft.getMinecraft().thePlayer));
        }
    }
    @SideOnly(Side.CLIENT)
    public static Sound setupSound(File file)
    {
        File category = new File("sounds" + File.separator + Minecraft.getMinecraft().func_147104_D().serverName);
        if (!category.exists())
        {
            category.mkdir();
        }
        File newFile = new File(category.getAbsolutePath() + File.separator + file.getName());
        try
        {
            if (!newFile.exists() || !Files.equal(file, newFile))
            {
                Files.copy(file, newFile);
                findSounds();
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return new Sound(newFile);
    }

}
