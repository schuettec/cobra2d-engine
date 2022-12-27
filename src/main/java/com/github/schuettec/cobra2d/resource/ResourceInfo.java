package com.github.schuettec.cobra2d.resource;

import java.net.URL;
import java.util.Objects;

import com.github.schuettec.cobra2d.renderer.RendererResource;

public class ResourceInfo {
	private String id;
	private URL url;
	private RendererResource resource;

	ResourceInfo(String id, URL url) {
		super();
		this.id = id;
		this.url = url;
	}

	@SuppressWarnings("unchecked")
	public <T extends RendererResource> T getRendererResource(Class<T> renderResourceType) {
		return (T) resource;
	}

	public RendererResource getRendererResource() {
		return resource;
	}

	public void setResource(RendererResource resource) {
		this.resource = resource;
	}

	public String getId() {
		return id;
	}

	public URL getUrl() {
		return url;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResourceInfo other = (ResourceInfo) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "ResourceInfo [id=" + id + ", url=" + url.toString() + "]";
	}

}
