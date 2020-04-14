package com.example.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.Thread.sleep;

public class TestSurfaceView extends SurfaceView implements SurfaceHolder.Callback2 {
    SurfaceHolder holder;
    DrawThread thread;
    int number = 3, backColor = Color.BLACK, colors_number = 7, over_circles = 1;
    float rad = 50;
    int width, height;
    Rect rect;
    boolean visit = false, over = false;

    ArrayList<Circle> circles = new ArrayList<>();

    ArrayList<Integer> colors = new ArrayList<>();


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (rect.left <= event.getX() && rect.right >= event.getX() && rect.top <= event.getY() && rect.bottom >= event.getY()) {
                visit = true;
            }
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (visit) {
                rect.redraw((int) event.getX(), (int) event.getY());
            }
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            visit = false;
        }

        return true;
    }

    class DrawThread extends Thread {
        boolean runflag = true;

        public void stopFlag() {
            runflag = false;
        }

        @Override
        public void run() {
            super.run();
            while (runflag && !over) {

                Canvas c = holder.lockCanvas();
                if (c != null) {
                    c.drawColor(backColor);


                    for (int i = 0; i < number; i++) {
                        circles.get(i).draw(c);
                    }
                    rect.draw(c);

                    for (int i = 0; i < number - 1; i++) {
                        if (circles.get(i).color == circles.get(i + 1).color) {
                            over_circles++;
                        } else {
                            over_circles = 1;
                            break;
                        }
                    }


                    if (over_circles == number) {
                        over = true;
                    }


                    holder.unlockCanvasAndPost(c);


                        for (int i = 0; i < circles.size(); i++) {
                            circles.get(i).changeDirection(width, height);
                            for (int j = i; j < circles.size(); j++) {
                                float rast = (float) Math.sqrt((circles.get(i).x - circles.get(j).x) * (circles.get(i).x - circles.get(j).x) + (circles.get(i).y - circles.get(j).y) * (circles.get(i).x - circles.get(j).y));
                                if (rast <= circles.get(i).rad + circles.get(j).rad) {
                                    float temp = circles.get(i).dx;
                                    circles.get(i).dx = circles.get(j).dx;
                                    circles.get(j).dx = temp;
                                    temp = circles.get(i).dy;
                                    circles.get(i).dy = circles.get(j).dy;
                                    circles.get(j).dy = temp;
                                }
                            }

                            if (rect.inRect(circles.get(i).x, circles.get(i).y, circles.get(i).rad)) {
                                circles.get(i).dx *= -1;
                                circles.get(i).dy *= -1;

                                for (int j = 0; j < colors_number; j++) {

                                    if (circles.get(i).color == colors.get(j)) {
                                        Log.i("colors1", circles.get(i).color + " " + i);
                                        Log.i("colors", colors.get(j) + " " + j);
                                        if (j == colors_number - 1) {
                                            circles.get(i).color = colors.get(0);
                                            Log.i("color", "true");
                                        } else {
                                            circles.get(i).color = colors.get(j + 1);
                                            Log.i("color", "false");
                                        }
                                        break;
                                    }
                                }

                            }
                        }

                }
            }
            if (over) {
                gameover();
            }
        }
    }


    public void gameover() {
        Canvas c = holder.lockCanvas();
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        p.setTextSize(100);
        try {
            sleep(500);
        } catch (InterruptedException e) {}

        c.drawColor(backColor);

        c.drawText("Вы выиграли!", width/5, height/2, p);

        holder.unlockCanvasAndPost(c);
    }

    public TestSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceRedrawNeeded(SurfaceHolder holder) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Random r = new Random();
        width = getWidth();
        height = getHeight();
        int color;

        for (int i = 0; i < colors_number; i++) {
            color = Color.rgb(r.nextInt(255), r.nextInt(255), r.nextInt(255));
            colors.add(color);
        }


        for (int i = 0; i < number; i++) {
           float x = r.nextInt(width);
           float y = r.nextInt(height);
           color = colors.get(r.nextInt(colors_number));
           Circle temp = new Circle(x, y, color, rad, i);
           circles.add(temp);
        }

        color = Color.rgb(r.nextInt(255), r.nextInt(255), r.nextInt(255));
        rect = new Rect(width/2, height/2, color);


        holder = surfaceHolder;
        thread = new DrawThread();
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        thread.stopFlag();
        thread.runflag = true;

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        thread.stopFlag();
    }
}
