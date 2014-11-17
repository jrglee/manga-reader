package com.jrglee.mangareader.reader;

import android.content.Context;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ImageView;
import com.google.inject.Inject;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import roboguice.util.Ln;
import rx.Observable;

import static rx.android.schedulers.AndroidSchedulers.mainThread;

@ContentView(R.layout.activity_main)
public class MainActivity extends RoboActivity {

    @InjectView(R.id.content)
    ImageView content;

    @Inject
    Context context;

    Observable<MotionEvent> obs;

    Matrix matrix = new Matrix();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Ln.d("Opening image into %s", content);
        Picasso.with(context)
                .load("http://e465.enterprise.fastwebserver.de/series/Naruto/0001-001.png")
                .into(content, new Callback() {
                    @Override
                    public void onSuccess() {
                        matrix.set(content.getImageMatrix());
                    }

                    @Override
                    public void onError() {
                    }
                });

        obs = Observable.create(new DoubleTapSubscriber(context, content));
        obs.observeOn(mainThread())
                .subscribe(motionEvent -> {
                    Ln.d("Matrix %s", matrix);
                    content.setScaleType(ImageView.ScaleType.MATRIX);
                    matrix.setScale(4.0f, 4.0f);
                    content.setImageMatrix(matrix);
                });
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
