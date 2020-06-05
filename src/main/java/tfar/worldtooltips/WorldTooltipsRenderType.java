package tfar.worldtooltips;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormats;

public class WorldTooltipsRenderType extends RenderPhase {
	public WorldTooltipsRenderType(String p_i225973_1_, Runnable p_i225973_2_, Runnable p_i225973_3_) {
		super(p_i225973_1_, p_i225973_2_, p_i225973_3_);
	}

	public static RenderLayer getType() {
		RenderLayer.MultiPhaseParameters renderTypeState = RenderLayer.MultiPhaseParameters.builder().transparency(TRANSLUCENT_TRANSPARENCY).build(true);
		return RenderLayer.of(WorldTooltips.MODID, VertexFormats.POSITION_TEXTURE_COLOR_NORMAL, 7, 256, true, true, renderTypeState);
	}

}
