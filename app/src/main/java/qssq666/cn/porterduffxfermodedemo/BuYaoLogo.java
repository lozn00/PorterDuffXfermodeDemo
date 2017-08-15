package qssq666.cn.porterduffxfermodedemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by qssq on 2017/8/15 qssq666@foxmail.com
 */

public class BuYaoLogo extends View {
    public BuYaoLogo(Context context) {
        super(context);
    }

    public BuYaoLogo(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BuYaoLogo(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
        canvas.drawColor(Color.RED);//
        Bitmap dstBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.logo_b);
        Bitmap srcBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.imggirl111);
        RectF srcRectF = new RectF(0, 0, dstBitmap.getWidth(), dstBitmap.getHeight());
        int saveCount = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            saveCount = canvas.saveLayerAlpha(srcRectF, 255);
            saveCount = canvas.saveLayer(srcRectF, paint);
//            saveCount = canvas.saveLayer(0, 0,500, 500, paint);
        } else {

            saveCount = canvas.saveLayer(srcRectF, paint, Canvas.ALL_SAVE_FLAG);
        }
        PorterDuff.Mode mode = PorterDuff.Mode.MULTIPLY;//相交的部分取第二层的
//        PorterDuff.Mode mode = PorterDuff.Mode.DST_IN;//相交的部分取第二层的
        RectF dstRect = new RectF(0, 0, dstBitmap.getWidth(), dstBitmap.getHeight());
        canvas.drawBitmap(dstBitmap, null, dstRect, paint);
        paint.setXfermode(new PorterDuffXfermode(mode));
        canvas.drawBitmap(srcBitmap, null, srcRectF, paint);
        paint.setXfermode(null);
        canvas.restoreToCount(saveCount);
    }
}
