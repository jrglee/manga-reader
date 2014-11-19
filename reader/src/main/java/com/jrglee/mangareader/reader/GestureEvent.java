package com.jrglee.mangareader.reader;

import android.view.MotionEvent;

public abstract class GestureEvent {

    public enum Type {
        MOVE, ZOOM_TOGGLE
    }

    public final Type type;

    protected GestureEvent(Type type) {
        this.type = type;
    }

    public static GestureEvent zoom(MotionEvent e) {
        return new ZoomToggleEvent(e);
    }

    public static GestureEvent move(MotionEvent origin, MotionEvent currentMove, float distanceX, float distanceY) {
        return new MoveEvent(origin, currentMove, distanceX, distanceY);
    }

    public static class ZoomToggleEvent extends GestureEvent {
        public final MotionEvent originator;

        public ZoomToggleEvent(MotionEvent originator) {
            super(Type.ZOOM_TOGGLE);
            this.originator = originator;
        }
    }

    public static class MoveEvent extends GestureEvent {
        public final MotionEvent origin;
        public final MotionEvent currentMove;
        public final float distanceX;
        public final float distanceY;

        public MoveEvent(MotionEvent origin, MotionEvent currentMove, float distanceX, float distanceY) {
            super(Type.MOVE);
            this.origin = origin;
            this.currentMove = currentMove;
            this.distanceX = distanceX;
            this.distanceY = distanceY;
        }
    }
}
