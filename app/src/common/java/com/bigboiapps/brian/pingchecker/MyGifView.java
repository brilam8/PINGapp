package com.bigboiapps.brian.pingchecker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.view.View;

import java.io.InputStream;

public class MyGifView extends View {

    Movie movie,movie1;
    InputStream is=null,is1=null;
    long moviestart;
    public MyGifView(Context context,String gifname) {
        super(context);



        is=context.getResources().openRawResource(getResources().getIdentifier(gifname, "drawable", context.getPackageName()));
        movie=Movie.decodeStream(is);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawColor(Color.TRANSPARENT);
        super.onDraw(canvas);
        long now=android.os.SystemClock.uptimeMillis();
        System.out.println("now="+now);
        if (moviestart == 0) { // first time
            moviestart = now;

        }
        System.out.println("\tmoviestart="+moviestart);
        int relTime = (int)((now - moviestart) % movie.duration()) ;
        System.out.println("time="+relTime+"\treltime="+movie.duration());
        movie.setTime(relTime);
        float scalefactorx = (float) this.getWidth() / (float) movie.width();
        float scalefactory = (float) this.getHeight() / (float) movie.height();
        canvas.scale(scalefactorx,scalefactory);
        movie.draw(canvas, scalefactorx, scalefactory);
        //movie.draw(canvas,this.getWidth()/2-20,this.getHeight()/2-40);
        //movie.draw(canvas,scalefactorx,scalefactory);
        this.invalidate();
    }
}