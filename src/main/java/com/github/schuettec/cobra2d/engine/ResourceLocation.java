package com.github.schuettec.cobra2d.engine;

public enum ResourceLocation {

	CLASSPATH("classpath"),
	INSTALL_DIR("install-dir");

	private String protocol;

	private ResourceLocation(String protocol) {
		this.protocol = protocol;
	}

	@Override
	public String toString() {
		return protocol;
	}
}