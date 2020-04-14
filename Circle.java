package com.example.views;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Circle {

    float x, y, rad, dx = 20, dy = 20;
    int n, color;
    Paint p = new Paint();

    public Circle(float x, float y, int color, float rad, int n) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.rad = rad;
        this.n = n;
    }

    public void draw(Canvas canvas) {

        p.setColor(color);
        canvas.drawCircle(x, y, rad, p);
    }

    public void changeDirection(float width, float height) {
        if ((x+dx+50) >= width || ((x+dx-50)<=0)) {
            dx = -dx;
        }

        if ((y+dy+50) >= height || ((y+dy-50)<=0)) {
            dy = -dy;
        }
        x += dx;
        y += dy;
    }
}
