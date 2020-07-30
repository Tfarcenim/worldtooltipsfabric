package tfar.worldtooltips.mixin;

import net.minecraft.client.font.TextRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import tfar.worldtooltips.Hooks;

@Mixin(TextRenderer.class)
public class TextRendererMixin {
	//@ModifyConstant(method = "drawInternal", constant = @Constant(),
	//				slice = @Slice(to = @At(value = "INVOKE",
	//								target = "Lnet/minecraft/client/util/math/Matrix4f;copy()Lnet/minecraft/client/util/math/Matrix4f;")))
	//public int alpha(int old) {
		//return Hooks.getColor(old);
	//}
}
