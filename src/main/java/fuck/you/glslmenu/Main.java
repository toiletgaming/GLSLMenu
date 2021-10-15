package fuck.you.glslmenu;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod( modid = Main.MODID, name = Main.NAME, version = Main.VERSION )
public class Main
{
    public static final String MODID = "glslmenu";
    public static final String NAME = "Custom GLSL Main Menu";
    public static final String VERSION = "1.0";

    public static Logger logger;
    public static Shaders shaders;

    @Mod.EventHandler
    public void init( FMLInitializationEvent event )
    {
        logger = LogManager.getLogger( "GLSLMenu" );
        shaders = new Shaders( );
    }
}
