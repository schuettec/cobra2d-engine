package com.github.schuettec.cobra2d.network.external.reflection;

import static java.util.Objects.requireNonNull;

import java.io.Serializable;
import java.util.Objects;

public abstract class ReflectiveField {

	private int fieldIdentityHash;

	private Class<?> declaringType;

	private Class<?> fieldType;

	private String fieldName;

	public ReflectiveField(Class<?> declaringType, Class<?> fieldType, String fieldName) {
		super();
		requireNonNull(declaringType);
		requireNonNull(fieldType);
		requireNonNull(fieldName);
		this.declaringType = declaringType;
		this.fieldType = fieldType;
		this.fieldName = fieldName;
		this.fieldIdentityHash = hashIdentity();
	}

	public abstract Serializable getFieldValue(Object target);

	public abstract void setFieldValue(Object target, Serializable newValue);

	private int hashIdentity() {
		return Objects.hash(declaringType.getName() + ";" + fieldType.getName() + ";" + fieldName);
	}

	@Override
	public int hashCode() {
		return fieldIdentityHash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReflectiveField other = (ReflectiveField) obj;
		return Objects.equals(fieldIdentityHash, other.fieldIdentityHash);
	}

	@Override
	public String toString() {
		return "ReflectiveField [fieldIdentityHash=" + fieldIdentityHash + ", declaringType=" + declaringType
		    + ", fieldType=" + fieldType + ", fieldName=" + fieldName + "]";
	}

}
