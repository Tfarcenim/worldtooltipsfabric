package tfar.worldtooltips;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.TooltipFlag;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Hooks {

	public static final Minecraft mc = Minecraft.getInstance();

	public static boolean transparent = false;

	public static void renderItemEntityHook(ItemEntity entity, float p_225623_2_, PoseStack matrices, MultiBufferSource buffer, int light) {
		double dist = mc.getEntityRenderDispatcher().distanceToSqr(entity);
		if (isVisible(dist)) {
			List<Component> tooltip = entity.getItem().getTooltipLines(mc.player, mc.options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL);
			tooltip.add(new TextComponent(ModUtils.getModName(entity)).withStyle(ChatFormatting.BLUE,ChatFormatting.ITALIC));
			renderTooltip(entity, matrices, buffer, tooltip,dist);
		}
		float f = entity.getBbHeight() + 0.5F;
		List<Component> tooltip = entity.getItem().getTooltipLines(mc.player, mc.
						options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL);

		tooltip.add(new TextComponent(ModUtils.getModName(entity)).withStyle(ChatFormatting.BLUE,ChatFormatting.ITALIC));
		int i =  - 10 * tooltip.size();
		for (int i1 = 0; i1 < tooltip.size(); i1++) {
			Component string = tooltip.get(i1);

			if (i1 == 0) {
				int count = entity.getItem().getCount();
				if (count > 1) ;
					//string = string.copy().append(new LiteralText(string + " x " + count));
			}

			matrices.pushPose();
			matrices.translate(0, f, 0);

			matrices.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());
			float scale = (float) (ClientConfig.scale * -.025);

			matrices.scale(scale, scale, scale);
			Font fontrenderer = mc.font;
			float f2 = -fontrenderer.width(string) / 2f;

			int alpha = alpha(dist);
			int fontcolor = (alpha << 24) + 0x00ffffff;
			transparent = true;
			fontrenderer.draw(matrices,string,f2, light,fontcolor);
			transparent = false;
			i += 10;
			matrices.popPose();
		}
	}

	public static int getColor(int old) {
    	int alpha = old << 24;

		return alpha < 1 && transparent ? 1 : old;
	}

	public static boolean isVisible(double dist){
		return dist < 512 && alpha(dist) > 0;
	}

	public static int alpha(double dist){
		return (int) (0xff - dist * 4);
	}

	private static void renderTooltip(ItemEntity entity, PoseStack matrices, MultiBufferSource buffer, List<Component> tooltip,double dist) {

		matrices.pushPose();
		float f = entity.getBbHeight() + 0.5F;


		matrices.translate(0,f,0);

		matrices.scale(ClientConfig.scale,ClientConfig.scale,ClientConfig.scale);


		matrices.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());

		MultiBufferSource.BufferSource immediate = mc.renderBuffers().bufferSource();
		VertexConsumer builder = immediate.getBuffer(RenderType.entityTranslucent(new ResourceLocation(WorldTooltips.MODID,WorldTooltips.MODID), false));
		Matrix4f matrix4f = matrices.last().pose();
		float height = tooltip.size() * .25f + .10f;

		List<Integer> list = tooltip.stream().map(mc.font::width).collect(Collectors.toList());
		float width = Collections.max(list) * .026f + .12f;

		//main background

		int alpha = Math.max((int)(0xff - dist * 4),0);

		float z = 0;
		fill(builder, matrix4f, -width / 2, width, .0f, height,z, (alpha << 24) | 0x0020021C);

		z+= -.001;

		float thickness = .01f;

		float padding = .04f;

		int color = entity.getItem().getRarity().color.getColor();


		//left
		fill(builder,matrix4f,width / 2 - padding,thickness,padding / 2,height - padding+thickness,z,(alpha << 24) | color);

		//right
		fill(builder,matrix4f,-width / 2 + padding,thickness,padding / 2, height - padding,z,(alpha << 24) | color);

		//bottom
		fill(builder,matrix4f,-width / 2 + padding,width - 2 * padding,padding/2 ,thickness,z,(alpha << 24) | color);

		//top
		fill(builder,matrix4f,-width / 2 + padding,width - 2 * padding,padding/2 + height - 1 * padding,thickness,z,(alpha << 24) | color);

		//mc.renderBuffers().bufferSource().endBatch(WorldTooltipsRenderType.getType());
		matrices.popPose();

	}


	public static void fill(VertexConsumer consumer, Matrix4f matrix4f, float u, float width, float v, float height,float z, int aarrggbb) {
		float a = (aarrggbb >> 24 & 0xff) / 255f;
		float r = (aarrggbb >> 16 & 0xff) / 255f;
		float g = (aarrggbb >> 8 & 0xff) / 255f;
		float b = (aarrggbb & 0xff) / 255f;

		fill(consumer, matrix4f, u, width, v, height,z, r, g, b, a);
	}

	public static void fill(VertexConsumer consumer, Matrix4f matrix4f, float u, float width, float v, float height,float z, float r, float g, float b, float a) {
		final int overlay = OverlayTexture.NO_OVERLAY;
		int light = 0xffffff;

		consumer.vertex(matrix4f, u, v, z).color(r, g, b, a).overlayCoords(overlay).uv2(light).normal(Vector3f.YP.x(), Vector3f.YP.y(), Vector3f.YP.z()).endVertex();
		consumer.vertex(matrix4f, u, v + height, z).color(r, g, b, a).overlayCoords(overlay).uv2(light).normal(Vector3f.YP.x(), Vector3f.YP.y(), Vector3f.YP.z()).endVertex();
		consumer.vertex(matrix4f, u + width, v + height, z).color(r, g, b, a).overlayCoords(overlay).uv2(light).normal(Vector3f.YP.x(), Vector3f.YP.y(), Vector3f.YP.z()).endVertex();
		consumer.vertex(matrix4f, u + width, v, z).color(r, g, b, a).overlayCoords(overlay).uv2(light).normal(Vector3f.YP.x(), Vector3f.YP.y(), Vector3f.YP.z()).endVertex();
	}
}
