package de.teamlapen.vampirism.items;

import com.google.common.collect.Multimap;
import de.teamlapen.lib.lib.util.ItemStackUtil;
import de.teamlapen.lib.lib.util.UtilLib;
import de.teamlapen.vampirism.VampirismMod;
import de.teamlapen.vampirism.api.items.IItemWithTier;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;


public class ItemObsidianArmor extends VampirismHunterArmor implements IItemWithTier {

    private final static String baseRegName = "obsidian_armor";

    public static boolean isFullyEquipped(EntityPlayer player) {
        for (ItemStack stack : player.inventory.armorInventory) {
            if (ItemStackUtil.isEmpty(stack) || !(stack.getItem() instanceof ItemObsidianArmor)) {
                return false;
            }
        }
        return true;
    }

    private final int[] DAMAGE_REDUCTION_ULTIMATE = new int[]{4, 7, 9, 4};
    private final int[] DAMAGE_REDUCTION_ENHANCED = new int[]{3, 7, 8, 3};
    private final int[] DAMAGE_REDUCTION_NORMAL = new int[]{3, 6, 7, 3};

    private final float[] SPEED_REDUCTION = new float[]{-0.025F, -0.1F, -0.05F, -0.025F};

    public ItemObsidianArmor(EntityEquipmentSlot equipmentSlotIn) {
        super(ArmorMaterial.IRON, equipmentSlotIn, baseRegName);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);
        TIER t = getTier(stack);
        if (t != TIER.NORMAL) {
            tooltip.add(TextFormatting.AQUA + UtilLib.translate("text.vampirism.item_tier." + t.name().toLowerCase()));
        }
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        switch (getTier(stack)) {
            case ENHANCED:
                return getTextureLocation("obsidian_armor_of_hell_enhanced", slot, type);
            case ULTIMATE:
                return getTextureLocation("obsidian_armor_of_hell_ultimate", slot, type);
            default:
                return getTextureLocation("obsidian_armor_of_hell", slot, type);
        }

    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        Multimap<String, AttributeModifier> map = super.getAttributeModifiers(slot, stack);
        if (slot == this.armorType) {
            map.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), new AttributeModifier(VAMPIRISM_ARMOR_MODIFIER[slot.getIndex()], "Speed modifier", this.getSpeedReduction(slot.getIndex(), stack), 2));
        }
        return map;
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems) {
        for (TIER t : TIER.values()) {
            subItems.add(setTier(new ItemStack(itemIn), t));
        }
    }

    @Override
    public TIER getTier(@Nonnull ItemStack stack) {
        NBTTagCompound tag = UtilLib.checkNBT(stack);
        if (tag.hasKey("tier")) {
            try {
                return TIER.valueOf(tag.getString("tier"));
            } catch (IllegalArgumentException e) {
                VampirismMod.log.e("ObsidianArmor", e, "Unknown item tier %s", tag.getString("tier"));
            }

        }
        return TIER.NORMAL;
    }

    @Nonnull
    @Override
    public ItemStack setTier(@Nonnull ItemStack stack, TIER tier) {
        NBTTagCompound tag = UtilLib.checkNBT(stack);
        tag.setString("tier", tier.name());
        return stack;
    }

    @Override
    protected int getDamageReduction(int slot, ItemStack stack) {
        TIER tier = getTier(stack);
        switch (tier) {
            case ULTIMATE:
                return DAMAGE_REDUCTION_ULTIMATE[slot];
            case ENHANCED:
                return DAMAGE_REDUCTION_ENHANCED[slot];
            default:
                return DAMAGE_REDUCTION_NORMAL[slot];
        }
    }

    private float getSpeedReduction(int slot, ItemStack stack) {
        return SPEED_REDUCTION[slot];
    }
}
