package me.yeojoy.surfaceviewdraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

/**
 * Created by yeojoy on 15. 1. 29..
 */
class MyView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = MyView.class.getSimpleName();
    
    ArrayList<Ball> arBall = new ArrayList<Ball>();
    final static int DELAY = 100;
    final static int RADIUS = 50;
    SurfaceHolder mHolder;
    DrawThread mThread;

    public MyView(Context context) {
        super(context);

        // 표면에 변화가 생길때의 이벤트를 처리할 콜백을 자신으로 지정한다.
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    // 표면이 생성될 때 그리기 스레드를 시작한다.
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated()");
        mThread = new DrawThread(mHolder);
        mThread.start();
    }

    // 표면이 파괴될 때 그리기를 중지한다.
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed()");
        mThread.wantToExit = true;
        for (; ; ) {
            try {
                mThread.join();
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 표면의 크기가 바뀔 때 크기를 기록해 놓는다.
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged()");
        if (mThread != null) {
            mThread.sizeChange(width, height);
        }
    }

    // 새로운 볼 생성
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            synchronized (mHolder) {
                Ball NewBall = Ball.createBall((int) event.getX(), (int) event.getY(), RADIUS);
                arBall.add(NewBall);
            }
            return true;
        }
        return false;
    }

    class DrawThread extends Thread {
        boolean wantToExit;
        int mWidth, mHeight;
        SurfaceHolder mHolder;

        DrawThread(SurfaceHolder Holder) {
            mHolder = Holder;
            wantToExit = false;
        }

        public void sizeChange(int width, int height) {
            Log.d(TAG, "sizeChange()");
//            mWidth = width;
//            mHeight = height;
            mWidth = 400;
            mHeight = 400;
        }

        // 스레드에서 그리기를 수행한다.
        public void run() {
            Canvas canvas;
            Ball B;

            while (wantToExit == false) {
                // 애니메이션 진행
                for (int idx = 0; idx < arBall.size(); idx++) {
                    B = arBall.get(idx);
                    B.moveBall(mWidth, mHeight);
                    if (B.count > 100) {
                        arBall.remove(idx);
                        idx--;
                    }
                }

                // 그리기
                synchronized (mHolder) {
                    canvas = mHolder.lockCanvas();
                    if (canvas == null) break;
                    canvas.drawColor(Color.BLACK);
                    Rect dst = new Rect(0, 0, getWidth(), getHeight());
                    canvas.drawColor(Color.LTGRAY);

                    for (int idx = 0; idx < arBall.size(); idx++) {
                        arBall.get(idx).drawBall(canvas);
                        if (wantToExit) break;
                    }
                    mHolder.unlockCanvasAndPost(canvas);
                }

                try {
                    Thread.sleep(MyView.DELAY);
                } catch (Exception e) {
                    ;
                }
            }
        }
    }
}