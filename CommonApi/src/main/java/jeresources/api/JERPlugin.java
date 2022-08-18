package jeresources.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Apply this annotation to a class that implements {@link IJERPlugin}
 * An instance of {@link IJERAPI} will be injected
 */
@Target(ElementType.TYPE)
public @interface JERPlugin {
}
