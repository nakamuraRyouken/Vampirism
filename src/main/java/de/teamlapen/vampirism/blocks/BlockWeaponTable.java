package de.teamlapen.vampirism.blocks;

import de.teamlapen.vampirism.VampirismMod;
import de.teamlapen.vampirism.api.VReference;
import de.teamlapen.vampirism.api.entity.factions.IPlayableFaction;
import de.teamlapen.vampirism.core.ModBlocks;
import de.teamlapen.vampirism.entity.factions.FactionPlayerHandler;
import de.teamlapen.vampirism.network.ModGuiHandler;
import de.teamlapen.vampirism.player.hunter.skills.HunterSkills;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;


public class BlockWeaponTable extends VampirismBlock {
    public final static String regName = "weapon_table";
    public static final int MAX_LAVA = 5;
    public static final int MB_PER_META = 200;
    public static final PropertyInteger LAVA = PropertyInteger.create("lava", 0, MAX_LAVA);
    private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0, 0, 0, 0.93, 0.6, 1);

    public BlockWeaponTable() {
        super(regName, Material.IRON);
        this.setDefaultState(this.blockState.getBaseState().withProperty(LAVA, 0));
        this.setHardness(3);

    }


    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BOUNDING_BOX;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(LAVA);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(Item.getItemFromBlock(ModBlocks.weapon_table), 1);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(LAVA, meta);
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            int lava = state.getValue(LAVA);
            boolean flag = false;
            ItemStack heldItem = playerIn.getHeldItem(hand);
            if (lava < MAX_LAVA) {
                IFluidHandlerItem fluidHandler = FluidUtil.getFluidHandler(heldItem);
                if (fluidHandler != null) {
                    FluidStack missing = new FluidStack(FluidRegistry.LAVA, (MAX_LAVA - lava) * MB_PER_META);
                    FluidStack drainable = fluidHandler.drain(missing, false);
                    if (drainable != null && drainable.amount >= MB_PER_META) {
                        FluidStack drained = fluidHandler.drain(missing, true);
                        if (drained != null) {
                            IBlockState changed = state.withProperty(LAVA, Math.min(MAX_LAVA, lava + drained.amount / MB_PER_META));
                            worldIn.setBlockState(pos, changed);
                            flag = true;
                            playerIn.setHeldItem(hand, fluidHandler.getContainer());
                        }
                    }

                }
            }
            if (!flag) {

                if (canUse(playerIn))
                    playerIn.openGui(VampirismMod.instance, ModGuiHandler.ID_WEAPON_TABLE, worldIn, pos.getX(), pos.getY(), pos.getZ());
                else {
                    playerIn.sendMessage(new TextComponentTranslation("tile.vampirism." + regName + ".cannot_use"));
                }
            }
        }
        return true;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, LAVA);
    }

    /**
     * @return If the given player is allowed to use this.
     */
    private boolean canUse(EntityPlayer player) {
        IPlayableFaction faction = FactionPlayerHandler.get(player).getCurrentFaction();
        if (faction != null && faction.equals(VReference.HUNTER_FACTION)) {
            if (faction.getPlayerCapability(player).getSkillHandler().isSkillEnabled(HunterSkills.weapon_table)) {
                return true;
            }
        }
        return false;
    }
}
