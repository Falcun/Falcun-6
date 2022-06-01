package mchorse.emoticons.skin_n_bones.api.asm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Inject check annotation.
 * 
 * This annotation used to mark methods that should have a check for 
 * UUID injected. 
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface InjectCheck
{}