package com.quinny898.app.stickersforhangouts;

import android.content.Context;

import com.orm.SugarRecord;

public class History extends SugarRecord<History> {
    String filepath;

    public History(Context ctx) {
        super(ctx);
    }

    public History(Context ctx, String filepath) {
        super(ctx);
        this.filepath = filepath;
    }
}