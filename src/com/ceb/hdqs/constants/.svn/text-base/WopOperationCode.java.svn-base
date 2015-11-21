package com.ceb.hdqs.constants;

// WOP操作方式0-新增,1-查询,2-修改,3-删除
public enum WopOperationCode {
	SAVE(0), QUERY(1), UPDATE(2), DELETE(3);

	private final int code;

	private WopOperationCode(final int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	/**
	 * Cannot rely on enum ordinals . They change if item is removed or moved.
	 * Do our own codes.
	 * 
	 * @param b
	 * @return Type associated with passed code.
	 */
	public static WopOperationCode codeToType(final int b) {
		for (WopOperationCode t : WopOperationCode.values()) {
			if (t.getCode() == b) {
				return t;
			}
		}
		throw new RuntimeException("Unknown code " + b);
	}
}
