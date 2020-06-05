package tfar.worldtooltips;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.ItemEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Hooks {

	public static final MinecraftClient mc = MinecraftClient.getInstance();

	public static boolean transparent = false;

	public static final Identifier id = new Identifier(WorldTooltips.MODID,"default");

    static {
		}

	public static void renderItemEntityHook(ItemEntity entity, float p_225623_2_, MatrixStack matrices, VertexConsumerProvider buffer, int light) {
		double dist = mc.getEntityRenderManager().getSquaredDistanceToCamera(entity);
		if (isVisible(dist)) {
			float f = entity.getHeight() + 0.5F;
			List<Text> tooltip = entity.getStack().getTooltip(MinecraftClient.getInstance().player, MinecraftClient.getInstance().
							options.advancedItemTooltips ? TooltipContext.Default.ADVANCED : TooltipContext.Default.NORMAL);

			tooltip.add(new LiteralText(ModUtils.getModName(entity)).formatted(Formatting.BLUE,Formatting.ITALIC));
			int i =  - 10 * tooltip.size();
			for (int i1 = 0; i1 < tooltip.size(); i1++) {
				Text string = tooltip.get(i1);

				if (i1 == 0) {
					int count = entity.getStack().getCount();
					if (count > 1)
						string = string.append(new LiteralText(" x " + count));
				}

				matrices.push();
				matrices.translate(0, f, 0);

				matrices.multiply(MinecraftClient.getInstance().getEntityRenderManager().getRotation());
				float scale = (float) (ClientConfig.scale * -.025);

				matrices.scale(scale, scale, scale);
				Matrix4f matrix4f = matrices.peek().getModel();
				TextRenderer fontrenderer = MinecraftClient.getInstance().getEntityRenderManager().getTextRenderer();
				float f2 = -fontrenderer.getStringWidth(string.asFormattedString()) / 2f;

				int alpha = alpha(dist);
				int fontcolor = (alpha << 24) + 0x00ffffff;
				transparent = true;
				fontrenderer.draw(string.asFormattedString(), f2, i, fontcolor, false, matrix4f, buffer, false, 0, light);
				transparent = false;
				i += 10;
				matrices.pop();
			}
			renderTooltip(entity, matrices, buffer, tooltip,dist);
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

	private static void renderTooltip(ItemEntity entity, MatrixStack matrices, VertexConsumerProvider buffer, List<Text> tooltip,double dist) {

		matrices.push();
		float f = entity.getHeight() + 0.5F;


		matrices.translate(0,f,0);

		matrices.scale(ClientConfig.scale,ClientConfig.scale,ClientConfig.scale);


		matrices.multiply(MinecraftClient.getInstance().getEntityRenderManager().getRotation());

		VertexConsumer builder = buffer.getBuffer(WorldTooltipsRenderType.getType());
		Matrix4f matrix4f = matrices.peek().getModel();
		float height = tooltip.size() * .25f + .10f;

		List<Integer> list = tooltip.stream().map(Text::asFormattedString).map(mc.textRenderer::getStringWidth).collect(Collectors.toList());
		float width = Collections.max(list) * .026f + .12f;

		//main background

		int alpha = Math.max((int)(0xff - dist * 4),0);

		float z = 0;
		fill(builder, matrix4f, -width / 2, width, .0f, height,z, (alpha << 24) | 0x0020021C);

		z+= -.001;

		float thickness = .01f;

		float padding = .04f;

		int color = entity.getStack().getRarity().formatting.getColorValue();


		//left
		fill(builder,matrix4f,width / 2 - padding,thickness,padding / 2,height - padding+thickness,z,(alpha << 24) | color);

		//right
		fill(builder,matrix4f,-width / 2 + padding,thickness,padding / 2, height - padding,z,(alpha << 24) | color);

		//bottom
		fill(builder,matrix4f,-width / 2 + padding,width - 2 * padding,padding/2 ,thickness,z,(alpha << 24) | color);

		//top
		fill(builder,matrix4f,-width / 2 + padding,width - 2 * padding,padding/2 + height - 1 * padding,thickness,z,(alpha << 24) | color);

		matrices.pop();

	}


	public static void fill(VertexConsumer consumer, Matrix4f matrix4f, float u, float width, float v, float height,float z, int aarrggbb) {
		float a = (aarrggbb >> 24 & 0xff) / 255f;
		float r = (aarrggbb >> 16 & 0xff) / 255f;
		float g = (aarrggbb >> 8 & 0xff) / 255f;
		float b = (aarrggbb & 0xff) / 255f;

		fill(consumer, matrix4f, u, width, v, height,z, r, g, b, a);
	}

	public static void fill(VertexConsumer consumer, Matrix4f matrix4f, float u, float width, float v, float height,float z, float r, float g, float b, float a) {
		consumer.vertex(matrix4f, u, v, z).texture(0.0F, 0.0F).color(r, g, b, a).normal(Vector3f.POSITIVE_Y.getX(), Vector3f.POSITIVE_Y.getY(), Vector3f.POSITIVE_Y.getZ()).next();
		consumer.vertex(matrix4f, u, v + height, z).texture(0.0F, 0.5F).color(r, g, b, a).normal(Vector3f.POSITIVE_Y.getX(), Vector3f.POSITIVE_Y.getY(), Vector3f.POSITIVE_Y.getZ()).next();
		consumer.vertex(matrix4f, u + width, v + height, z).texture(1.0F, 0.5F).color(r, g, b, a).normal(Vector3f.POSITIVE_Y.getX(), Vector3f.POSITIVE_Y.getY(), Vector3f.POSITIVE_Y.getZ()).next();
		consumer.vertex(matrix4f, u + width, v, z).texture(1.0F, 0.0F).color(r, g, b, a).normal(Vector3f.POSITIVE_Y.getX(), Vector3f.POSITIVE_Y.getY(), Vector3f.POSITIVE_Y.getZ()).next();
	}
}
