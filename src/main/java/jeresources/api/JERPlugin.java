package jeresources.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Apply this annotation to a static field of {@link IJERAPI}
 * During {@link net.minecraftforge.fml.common.event.FMLPreInitializationEvent}
 * this field will be set to an instance of {@link IJERAPI}
 */
@Target(ElementType.FIELD)
public @interface JERPlugin
{
}
