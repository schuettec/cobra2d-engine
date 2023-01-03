package com.github.schuettec.cobra2d.network.external.reflection;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Interface to externalize reflective access using providers.
 */
public interface ReflectionProvider {

	public List<ReflectiveField> getAllInheritedFieldsAnnotatedWith(Class<? extends Annotation> annotation,
	    Class<?> type);

}
