package com.jrglee.mangareader.reader;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import rx.Observable;
import rx.Subscriber;

import static com.jrglee.mangareader.reader.GestureEvent.move;
import static com.jrglee.mangareader.reader.GestureEvent.zoom;

public class GestureSubscriber implements Observable.OnSubscribe<GestureEvent> {

    public final Context context;
    public final View view;

    public GestureSubscriber(Context context, View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void call(Subscriber<? super GestureEvent> subscriber) {
        SubscribableGestureListener listener = new SubscribableGestureListener(subscriber);

        GestureDetector detector = new GestureDetector(context, listener);
        detector.setOnDoubleTapListener(listener);

        view.setOnTouchListener((v, event) -> detector.onTouchEvent(event));
    }

    public static class SubscribableGestureListener extends GestureDetector.SimpleOnGestureListener {

        private final Subscriber<? super GestureEvent> subscriber;

        public SubscribableGestureListener(Subscriber<? super GestureEvent> subscriber) {
            this.subscriber = subscriber;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            subscriber.onNext(zoom(e));
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            subscriber.onNext(move(e1, e2, distanceX, distanceY));
            return true;
        }
    }
}
