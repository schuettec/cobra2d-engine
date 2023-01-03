package com.github.schuettec.cobra2d.network.external.reflection.javase;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import com.github.schuettec.cobra2d.network.external.reflection.ReflectionProvider;
import com.github.schuettec.cobra2d.network.external.reflection.ReflectiveField;

public class JavaReflectionProvider implements ReflectionProvider {

	@Override
	public List<ReflectiveField> getAllInheritedFieldsAnnotatedWith(Class<? extends Annotation> annotation,
	    Class<?> type) {
		List<ReflectiveField> fields = new LinkedList<>();
		for (Class<?> c = type; c != null; c = c.getSuperclass()) {
			Field[] declaredFields = c.getDeclaredFields();
			for (Field field : declaredFields) {
				if (field.getAnnotation(annotation) != null) {
					ReflectiveField javaField = new ReflectiveField(c, field.getType(), field.getName()) {

						@Override
						public Serializable getFieldValue(Object target) {
							try {
								field.setAccessible(true);
								return (Serializable) field.get(target);
							} catch (Exception e) {
								throw new RuntimeException("Cannot reflectively read field: " + this, e);
							}
						}

						@Override
						public void setFieldValue(Object target, Serializable newValue) {
							try {
								field.setAccessible(true);
								field.set(target, newValue);
							} catch (Exception e) {
								throw new RuntimeException("Cannot reflectively write field: " + this, e);
							}
						}

					};
					fields.add(javaField);
				}
			}
		}
		return fields;
	}

}
