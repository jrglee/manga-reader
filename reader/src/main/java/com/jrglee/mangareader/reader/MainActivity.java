package com.jrglee.mangareader.reader;

import android.content.Context;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ImageView;
import com.google.inject.Inject;
import com.jrglee.mangareader.reader.GestureEvent.MoveEvent;
import com.jrglee.mangareader.reader.GestureEvent.ZoomToggleEvent;
import com.squareup.picasso.Picasso;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import roboguice.util.Ln;
import rx.Observable;

import static android.widget.ImageView.ScaleType.CENTER_INSIDE;
import static android.widget.ImageView.ScaleType.MATRIX;
import static com.jrglee.mangareader.reader.GestureEvent.Type.MOVE;
import static com.jrglee.mangareader.reader.GestureEvent.Type.ZOOM_TOGGLE;
import static rx.android.schedulers.AndroidSchedulers.mainThread;

@ContentView(R.layout.activity_main)
public class MainActivity extends RoboActivity {

    public static final float ZOOM_FACTOR = 4.0f;

    @InjectView(R.id.content)
    ImageView content;

    @Inject
    Context context;

    Observable<GestureEvent> obs;

    boolean zoomed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Ln.d("Opening image into %s", content);
        Picasso.with(context)
                .load("http://e465.enterprise.fastwebserver.de/series/Naruto/0001-001.png")
                .into(content);

        obs = Observable.create(new GestureSubscriber(context, content));
        obs.observeOn(mainThread()).subscribe(this::handleGesture);
    }

    private void handleGesture(GestureEvent event) {
        content.setScaleType(MATRIX);
        Matrix matrix = new Matrix(content.getImageMatrix());
        if (event.type == ZOOM_TOGGLE) {
            handleZoom((ZoomToggleEvent) event, matrix);
        } else if (event.type == MOVE){
            handleMove((MoveEvent) event, matrix);
        }
        content.setImageMatrix(matrix);
    }

    private void handleMove(MoveEvent event, Matrix matrix) {
        if (zoomed) {
            matrix.postTranslate(-event.distanceX, -event.distanceY);
            Ln.d("Moving");
            Ln.d("Matrix  %s", matrix);
        }
    }

    private void handleZoom(ZoomToggleEvent event, Matrix matrix) {
        if (zoomed) {
            content.setScaleType(CENTER_INSIDE);
            zoomed = false;
        } else {
            MotionEvent originator = event.originator;
            matrix.postScale(ZOOM_FACTOR, ZOOM_FACTOR, originator.getX(), originator.getY());
            zoomed = true;
        }
        Ln.d("Zooming");
        Ln.d("Matrix %s", matrix);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
