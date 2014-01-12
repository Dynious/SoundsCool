package com.dynious.soundscool.client.gui;

import com.dynious.soundscool.SoundsCool;
import com.dynious.soundscool.handler.NetworkHandler;
import com.dynious.soundscool.network.packet.client.ClientPlaySoundPacket;
import com.dynious.soundscool.network.packet.client.GetUploadedSoundsPacket;
import com.dynious.soundscool.network.packet.client.SoundPlayerPlayPacket;
import com.dynious.soundscool.network.packet.client.SoundPlayerSelectPacket;
import com.dynious.soundscool.tileentity.TileSoundPlayer;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiSoundPlayer extends GuiScreen implements IListGui
{
    private GuiRemoteSoundsList soundsList;
    private TileSoundPlayer tile;

    public GuiSoundPlayer(TileSoundPlayer tile)
    {
        this.tile = tile;
        SoundsCool.proxy.getChannel().writeOutbound(new GetUploadedSoundsPacket(Minecraft.getMinecraft().thePlayer));
    }

    @Override
    public void initGui()
    {
        super.initGui();
        soundsList = new GuiRemoteSoundsList(this, 150);
        this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 75, this.field_146295_m - 38, "Play"));
    }

    @Override
    public void drawScreen(int p_571_1_, int p_571_2_, float p_571_3_)
    {
        this.soundsList.drawScreen(p_571_1_, p_571_2_, p_571_3_);
        super.drawScreen(p_571_1_, p_571_2_, p_571_3_);
    }

    @Override
    protected void func_146284_a(GuiButton button)
    {
        if (button.field_146124_l)
            switch (button.field_146127_k)
            {
                case 0:
                    SoundsCool.proxy.getChannel().writeOutbound(new SoundPlayerPlayPacket(tile));
                    break;
            }
    }

    @Override
    public Minecraft getMinecraftInstance()
    {
        return field_146297_k;
    }

    @Override
    public FontRenderer getFontRenderer()
    {
        return field_146289_q;
    }

    @Override
    public void selectSoundIndex(int selected)
    {
        tile.selectSoundIndex(selected);
    }

    @Override
    public boolean soundIndexSelected(int var1)
    {
        return tile.getSelected() == var1;
    }

    @Override
    public int getWidth()
    {
        return field_146295_m;
    }

    @Override
    public int getHeight()
    {
        return field_146295_m;
    }

    @Override
    public void drawBackground()
    {
        func_146276_q_();
    }
}
