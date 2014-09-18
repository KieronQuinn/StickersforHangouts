package com.quinny898.app.stickersforhangouts;

import com.orm.SugarRecord;

public class History extends SugarRecord<History> {
	String filepath;

	public History() {
	}

	public History(String filepath) {
		this.filepath = filepath;
	}
}