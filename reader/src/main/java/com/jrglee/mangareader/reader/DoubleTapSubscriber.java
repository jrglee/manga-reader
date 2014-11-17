package com.jrglee.mangareader.reader;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import rx.Observable;
import rx.Subscriber;

public class DoubleTapSubscriber implements Observable.OnSubscribe<MotionEvent> {

    public final Context context;
    public final View view;

    public DoubleTapSubscriber(Context context, View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void call(Subscriber<? super MotionEvent> subscriber) {
        GestureDetector detector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }
        });

        detector.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                subscriber.onNext(e);
                return true;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                return true;
            }
        });

        view.setOnTouchListener((v, event) -> detector.onTouchEvent(event));
    }
}
