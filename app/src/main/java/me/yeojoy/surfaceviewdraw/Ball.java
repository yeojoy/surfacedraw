package me.yeojoy.surfaceviewdraw;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.Random;

/**
 * Created by yeojoy on 15. 1. 29..
 */
class Ball {
    private static final String TAG = Ball.class.getSimpleName();
    
    int x, y;
    int mRadius;
    int dx, dy;
    int color;
    int count;

    // 새로운 볼 생성
    static Ball createBall(int x, int y, int radius) {
        Log.d(TAG, "createBall()");
        
        Random random = new Random();
        Ball newBall = new Ball();

        newBall.x = x;
        newBall.y = y;
//        newBall.x = 50;
//        newBall.y = 350;
        newBall.mRadius = radius;
        do {
            newBall.dx = random.nextInt(5) + 5;
            newBall.dy = random.nextInt(5) + 5;
        } while (newBall.dx == 0 || newBall.dy == 0);
//        double angle = Math.toRadians(-30);
//        newBall.dx = (int) (Math.cos(angle) * 10);
//        newBall.dy = (int) (Math.sin(angle) * 10);

        newBall.count = 0;
        newBall.color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));

        return newBall;
    }

    // 볼 이동
    void moveBall(int width, int height) {
        x += dx;
        y += dy;
        Log.d(TAG, "moveBall(), x : " + x + ", dx : " + dx + ", y : " + y + ", dy : " + dy);

        if (x < mRadius || x > width - mRadius) {
            dx *= -1;
            count++;
        }
        if (y < mRadius || y > height - mRadius) {
            dy *= -1;
            count++;
        }
    }

    // 그리기
    void drawBall(Canvas canvas) {
        Paint pnt = new Paint();
        pnt.setAntiAlias(true);

        int r;
        int alpha;

        for (r = mRadius, alpha = 1; r > 4; r --, alpha += 5) {
            pnt.setColor(Color.argb(alpha, Color.red(color),
                    Color.green(color), Color.blue(color)));
            canvas.drawCircle(x, y, r, pnt);
        }
    }
}
