package de.teamlapen.vampirism.modcompat.jei;

import de.teamlapen.vampirism.client.gui.GuiAlchemicalCauldron;
import de.teamlapen.vampirism.client.gui.GuiHunterWeaponTable;
import de.teamlapen.vampirism.core.ModBlocks;
import de.teamlapen.vampirism.core.ModItems;
import de.teamlapen.vampirism.inventory.AlchemicalCauldronContainer;
import de.teamlapen.vampirism.inventory.AlchemicalCauldronCraftingManager;
import de.teamlapen.vampirism.inventory.HunterWeaponCraftingManager;
import de.teamlapen.vampirism.inventory.HunterWeaponTableContainer;
import mezz.jei.api.*;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;

/**
 * Plugin for Just Enough Items
 */
@JEIPlugin
public class VampirismJEIPlugin extends BlankModPlugin {
    public static final String HUNTER_WEAPON_RECIPE_UID = "vampirism.hunter_weapon";
    public static final String ALCHEMICAL_CAULDRON_RECIPE_UID = "vampirism.alchemical_cauldron";

    @Override
    public void register(@Nonnull IModRegistry registry) {
        IJeiHelpers jeiHelpers = registry.getJeiHelpers();

        jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModBlocks.block_blood_fluid));
        jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModItems.blood_potion, 1, OreDictionary.WILDCARD_VALUE));
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();

        //Weapon crafting table
        registry.addRecipeCategories(new HunterWeaponRecipeCategory(guiHelper));
        registry.addRecipeHandlers(new ShapedHunterWeaponRecipesHandler());
        registry.addRecipeHandlers(new ShapelessHunterWeaponRecipeHandler());
        registry.addRecipeClickArea(GuiHunterWeaponTable.class, 113, 46, 28, 23, HUNTER_WEAPON_RECIPE_UID);
        registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.weapon_table), HUNTER_WEAPON_RECIPE_UID);
        registry.addRecipes(HunterWeaponCraftingManager.getInstance().getRecipes());
        recipeTransferRegistry.addRecipeTransferHandler(HunterWeaponTableContainer.class, HUNTER_WEAPON_RECIPE_UID, 1, 16, 17, 36);
        //Alchemical cauldron
        registry.addRecipeCategories(new AlchemicalCauldronRecipeCategory(guiHelper));
        registry.addRecipeHandlers(new AlchemicalCauldronRecipesHandler(jeiHelpers.getStackHelper()));
        registry.addRecipes(AlchemicalCauldronCraftingManager.getInstance().getRecipes());
        registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.alchemical_cauldron), ALCHEMICAL_CAULDRON_RECIPE_UID);
        registry.addRecipeClickArea(GuiAlchemicalCauldron.class, 80, 35, 25, 16, ALCHEMICAL_CAULDRON_RECIPE_UID);
        recipeTransferRegistry.addRecipeTransferHandler(AlchemicalCauldronContainer.class, ALCHEMICAL_CAULDRON_RECIPE_UID, 1, 2, 4, 36);


        //TODO add recipe handler for hunter table
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistry registry) {
        registry.useNbtForSubtypes(ModItems.armor_of_swiftness_feet, ModItems.armor_of_swiftness_chest, ModItems.armor_of_swiftness_head, ModItems.armor_of_swiftness_legs);
        registry.useNbtForSubtypes(ModItems.hunter_axe);
        registry.useNbtForSubtypes(ModItems.hunter_coat_feet, ModItems.hunter_coat_chest, ModItems.hunter_coat_head, ModItems.hunter_coat_legs);
        registry.useNbtForSubtypes(ModItems.obsidian_armor_feet, ModItems.obsidian_armor_chest, ModItems.obsidian_armor_legs, ModItems.obsidian_armor_head);
        registry.useNbtForSubtypes(ModItems.holy_water_bottle);
        registry.useNbtForSubtypes(ModItems.crossbow_arrow);
    }
}
