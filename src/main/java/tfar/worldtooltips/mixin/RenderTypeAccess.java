package tfar.worldtooltips.mixin;

import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RenderType.class)
public interface RenderTypeAccess {
    @Invoker("create")
    static RenderType.CompositeRenderType $create(String string, VertexFormat vertexFormat, VertexFormat.Mode mode, int i, boolean bl, boolean bl2, RenderType.CompositeState compositeState) {
        throw new RuntimeException();
    }
}
