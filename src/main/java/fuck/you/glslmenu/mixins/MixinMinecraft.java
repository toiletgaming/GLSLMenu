package fuck.you.glslmenu.mixins;

import fuck.you.glslmenu.Main;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( Minecraft.class )
public class MixinMinecraft
{
    @Inject( method = "getLimitFramerate", at = @At( "HEAD" ), cancellable = true )
    public void getLimitFramerate( CallbackInfoReturnable< Integer > info )
    {
        if( Main.shaders.currentshader != null )
            info.setReturnValue( 60 );
    }
}
