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
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;


public class ItemArmorOfSwiftness extends VampirismHunterArmor implements IItemWithTier {

    private final static String baseRegName = "armor_of_swiftness";
    private final int[] DAMAGE_REDUCTION_ULTIMATE = new int[]{2, 5, 6, 2};
    private final int[] DAMAGE_REDUCTION_ENHANCED = new int[]{2, 3, 4, 2};
    private final int[] DAMAGE_REDUCTION_NORMAL = new int[]{1, 2, 3, 1};

    public ItemArmorOfSwiftness(EntityEquipmentSlot equipmentSlotIn) {
        super(ArmorMaterial.LEATHER, equipmentSlotIn, baseRegName);
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
        if (type == null) {
            return getTextureLocationLeather(slot);
        }
        switch (getTier(stack)) {
            case ENHANCED:
                return getTextureLocation("swiftness_enhanced", slot, type);
            case ULTIMATE:
                return getTextureLocation("swiftness_ultimate", slot, type);
            default:
                return getTextureLocation("swiftness", slot, type);
        }
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot equipmentSlot, ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(equipmentSlot, stack);

        if (equipmentSlot == this.armorType) {
            TIER tier = getTier(stack);
            multimap.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), new AttributeModifier(VAMPIRISM_ARMOR_MODIFIER[equipmentSlot.getIndex()], "Armor Swiftness", getSpeedBoost(tier), 2));
        }

        return multimap;
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
                VampirismMod.log.e("ArmorOfSwiftness", e, "Unknown item tier %s", tag.getString("tier"));
            }

        }
        return TIER.NORMAL;
    }


    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        super.onArmorTick(world, player, itemStack);
        if (player.ticksExisted % 45 == 3) {
            if (this.armorType == EntityEquipmentSlot.CHEST) {
                boolean flag = true;
                int boost = Integer.MAX_VALUE;
                for (ItemStack stack : player.inventory.armorInventory) {
                    if (!ItemStackUtil.isEmpty(stack) && stack.getItem() instanceof ItemArmorOfSwiftness) {
                        int b = getJumpBoost(getTier(stack));
                        if (b < boost) {
                            boost = b;
                        }
                    } else {
                        flag = false;
                        break;
                    }
                }
                if (flag && boost > -1) {
                    player.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 50, boost, false, false));
                }
            }
        }
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

    /**
     * Applied if complete armor is worn
     *
     * @return -1 if none
     */
    private int getJumpBoost(TIER tier) {
        switch (tier) {
            case ULTIMATE:
                return 1;
            case ENHANCED:
                return 0;
            default:
                return -1;
        }
    }

    /**
     * Applied per piece
     */
    private double getSpeedBoost(TIER tier) {
        switch (tier) {
            case ULTIMATE:
                return 0.1;
            case ENHANCED:
                return 0.075;
            default:
                return 0.035;
        }
    }

    private String getTextureLocationLeather(EntityEquipmentSlot slot) {
        return String.format("minecraft:textures/models/armor/leather_layer_%d.png", slot == EntityEquipmentSlot.LEGS ? 2 : 1);
    }


}
