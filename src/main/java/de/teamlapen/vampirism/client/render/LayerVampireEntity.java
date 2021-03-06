package de.teamlapen.vampirism.client.render;

import de.teamlapen.vampirism.client.render.entities.RenderConvertedCreature;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityCreature;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Render the vampire overlay
 */
@SideOnly(Side.CLIENT)
public class LayerVampireEntity implements LayerRenderer<EntityCreature> {

    private final RenderLivingBase renderer;
    private final ResourceLocation overlay;
    private final boolean checkIfRender;

    /**
     * @param renderer
     * @param overlay
     * @param checkIfRender If it should check if {@link RenderConvertedCreature#renderOverlay} is true
     */
    public LayerVampireEntity(RenderLivingBase renderer, ResourceLocation overlay, boolean checkIfRender) {
        this.renderer = renderer;
        this.overlay = overlay;
        this.checkIfRender = checkIfRender;
    }

    @Override
    public void doRenderLayer(EntityCreature entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale) {
        if (!entitylivingbaseIn.isInvisible() && (!checkIfRender || RenderConvertedCreature.renderOverlay)) {
            renderer.bindTexture(overlay);
            renderer.getMainModel().render(entitylivingbaseIn, p_177141_2_, p_177141_3_, p_177141_5_, p_177141_6_, p_177141_7_, scale);
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
}
