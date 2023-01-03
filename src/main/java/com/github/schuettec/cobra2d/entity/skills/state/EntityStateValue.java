package com.github.schuettec.cobra2d.entity.skills.state;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that marks an attribute within an entity to be part of the entity state. An entity state is the sum of all
 * attributes with this annotation, representing the minimal set of variables needed to recreate the entity in the game
 * world.
 * 
 * 
 * The entity state is used for networking, so be careful and keep the set of entity state attributes minimal.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.FIELD)
public @interface EntityStateValue {

}
