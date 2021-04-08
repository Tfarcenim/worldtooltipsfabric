package tfar.worldtooltips.mixin;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.worldtooltips.Hooks;

@Mixin(ItemEntityRenderer.class)
public class ItemEntityRendererMixin {
	@Inject(method = "render",at = @At("RETURN"))
	private void renderInWorldTooltips(ItemEntity itemEntity, float v, float p_225623_3_, MatrixStack p_225623_4_, VertexConsumerProvider p_225623_5_, int p_225623_6_, CallbackInfo ci){
		Hooks.renderItemEntityHook(itemEntity, v, p_225623_4_, p_225623_5_, p_225623_6_);
	}
}
