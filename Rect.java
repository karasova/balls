package com.example.views;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Rect {
    float left, top, right, bottom, rw, rh;
    int color;

    public Rect(int x, int y, int color) {
        redraw(x, y);
        this.color = color;
    }

    public void redraw (int x, int y) {
        this.left = x - 100;
        this.top = y - 70;
        this.right = x + 100;
        this.bottom = y + 70;
        this.rw = x;
        this.rh = y;
    }

    public void draw(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(color);
        canvas.drawRect(left, top, right, bottom, p);
    }

    public boolean inRect(float point_x, float point_y, float rad) {
        float d = (float)Math.sqrt((point_x - this.rw) * (point_x - this.rw) + (point_y - this.rh) * (point_y - this.rh));
        float R = (float) (120 * Math.sqrt(2) / 2);
        if (d <= R + rad) {
            return true;
        }
        return false;
    }
}
