package com.github.schuettec.cobra2d.network.data;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;

/**
 * Serializable class that transfers the minimal state of an entity for networking.
 */
public class EntityState implements Serializable {
	private String id;
	private Map<Integer, Serializable> attributes;

	public EntityState() {
		super();
	}

	public EntityState(String id) {
		super();
		this.id = id;
		this.attributes = new Hashtable<>();
	}

	public void addAttributeValue(int attributeHash, Serializable value) {
		attributes.put(attributeHash, value);
	}

	public Serializable getAttributeValue(int attributeHash) {
		return attributes.get(attributeHash);
	}

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return "EntityState [id=" + id + ", attributes=" + attributes + "]";
	}

}
