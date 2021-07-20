package tfar.worldtooltips;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderType;
import tfar.worldtooltips.mixin.RenderTypeAccess;

public class WorldTooltipsRenderType extends RenderType {


	public WorldTooltipsRenderType(String string, VertexFormat vertexFormat, VertexFormat.Mode mode, int i, boolean bl, boolean bl2, Runnable runnable, Runnable runnable2) {
		super(string, vertexFormat, mode, i, bl, bl2, runnable, runnable2);
	}

	public static final RenderType TYPE = createType();

	public static RenderType getType() {
		return TYPE;
	}

	public static RenderType createType() {
		RenderType.CompositeState renderTypeState = RenderType.CompositeState.builder().setTransparencyState(TRANSLUCENT_TRANSPARENCY).createCompositeState(true);
		return RenderTypeAccess.$create(WorldTooltips.MODID, DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL, VertexFormat.Mode.QUADS, 256, true, true, renderTypeState);
	}

}
