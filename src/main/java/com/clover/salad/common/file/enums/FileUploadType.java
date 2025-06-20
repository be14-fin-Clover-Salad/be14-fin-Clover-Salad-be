package com.clover.salad.common.file.enums;

public enum FileUploadType {
	CONTRACT("계약서"),
	PRODUCT("상품"),
	PROFILE("프로필");

	private final String label;

	FileUploadType(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
