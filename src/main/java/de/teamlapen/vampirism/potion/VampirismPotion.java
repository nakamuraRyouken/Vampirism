package de.teamlapen.vampirism.potion;

import de.teamlapen.lib.lib.util.UtilLib;
import de.teamlapen.vampirism.util.REFERENCE;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Base class for Vampirism's potions
 */
public class VampirismPotion extends Potion {

    private static final ResourceLocation ICONS = new ResourceLocation(REFERENCE.MODID, "textures/gui/potions.png");
    @SideOnly(Side.CLIENT)
    private static final int ICON_TEXTURE_WIDTH = 144;
    @SideOnly(Side.CLIENT)
    private static final int ICON_TEXTURE_HEIGHT = 36;

    public VampirismPotion(String name, boolean badEffect, int potionColor) {
        super(badEffect, potionColor);
        this.setRegistryName(REFERENCE.MODID, name);
        this.setPotionName("effect.vampirism." + name);
    }


    @Override
    public boolean hasStatusIcon() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderHUDEffect(int k, int l, PotionEffect effect, Minecraft mc, float alpha) {
        int index = getStatusIconIndex();
        if (index >= 0) {
            mc.getTextureManager().bindTexture(ICONS);
            UtilLib.drawTexturedModalRect(0, k + 3, l + 3, index % 8 * 18, index / 8 * 18, 18, 18, ICON_TEXTURE_WIDTH, ICON_TEXTURE_HEIGHT);

        }

    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc) {
        int index = getStatusIconIndex();
        if (index >= 0) {
            mc.getTextureManager().bindTexture(ICONS);
            UtilLib.drawTexturedModalRect(0, x + 6, y + 7, index % 8 * 18, index / 8 * 18, 18, 18, ICON_TEXTURE_WIDTH, ICON_TEXTURE_HEIGHT);
        }
    }

    @Override
    public VampirismPotion setIconIndex(int p_76399_1_, int p_76399_2_) {
        super.setIconIndex(p_76399_1_, p_76399_2_);
        return this;
    }
}
