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
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * by qssq qssq666@foxmail.com qq694886526
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    int currentPosition = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ImageView iv = (ImageView) findViewById(R.id.image);
        final Bitmap dstBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.logo_b);//dsc底层，src顶层。
        final Bitmap srcBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.imggirl);
/*        srcBitmap.setBounds(0, 0, (int) (1 * dWidth),
                (int) (scale * dHeight));*/


        Log.w(TAG, "WIDTH:" + dstBitmap.getWidth() + ",height:" + srcBitmap.getHeight());
        iv.setImageBitmap(getPorterDuffBitmap(dstBitmap, srcBitmap, currentPosition, this));

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPosition++;
                if (currentPosition > 15) {
                    currentPosition = 0;
                }
                Bitmap porterDuffBitmap = getPorterDuffBitmap(dstBitmap, srcBitmap, currentPosition, MainActivity.this);
                iv.setImageBitmap(porterDuffBitmap);
                Toast.makeText(MainActivity.this, "current porterDuff mode:" + intToMode(currentPosition), Toast.LENGTH_SHORT).show();

            }
        });

    }


    /*
  显示相交部分的上层
   */

    /**
     * @param dstBitmap 黄色 底层
     * @param srcBitmap 蓝色 上层
     * @param context
     * @return
     */
    public static Bitmap getPorterDuffBitmap(Bitmap dstBitmap, Bitmap srcBitmap, int position, Context context) {
        Paint paint = new Paint();
        Bitmap bgBitmap = Bitmap.createBitmap(dstBitmap.getWidth(), dstBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bgBitmap);

        canvas.drawColor(Color.TRANSPARENT);//
        RectF srcRectF = new RectF(0, 0, dstBitmap.getWidth(), dstBitmap.getHeight());
        int saveCount = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            saveCount = canvas.saveLayerAlpha(srcRectF, 255);
            saveCount = canvas.saveLayer(srcRectF, paint);
//            saveCount = canvas.saveLayer(0, 0,500, 500, paint);
        } else {

            saveCount = canvas.saveLayer(srcRectF, paint, Canvas.ALL_SAVE_FLAG);
        }
        PorterDuff.Mode mode = intToMode(position);//相交的部分取第二层的

//        PorterDuff.Mode mode = PorterDuff.Mode.DST_IN;//相交的部分取第二层的
        RectF dstRect = new RectF(0, 0, dstBitmap.getWidth(), dstBitmap.getHeight());
        canvas.drawBitmap(dstBitmap, null, dstRect, paint);
        paint.setXfermode(new PorterDuffXfermode(mode));
        canvas.drawBitmap(srcBitmap, null, srcRectF, paint);
        paint.setXfermode(null);
        canvas.restoreToCount(saveCount);

        return bgBitmap;

    }

    public static final PorterDuff.Mode intToMode(int position) {
        switch (position) {
            default:
            case 0:
                return PorterDuff.Mode.CLEAR;//啥都没有
            case 1:
                return PorterDuff.Mode.SRC;//只显示顶层
            case 2:
                return PorterDuff.Mode.DST;//只显示底层
            case 3://显示底层和顶层 但是相交部分则显示顶层区域
                return PorterDuff.Mode.SRC_OVER;
            case 4://显示底层和顶层但是相交部分是显示 底层 区域
                return PorterDuff.Mode.DST_OVER;
            case 5://相交部分显示顶层view src
                return PorterDuff.Mode.SRC_IN;
            case 6://相交部分显示底层view,
                return PorterDuff.Mode.DST_IN;
            case 7://显示不相交部分的顶层
                return PorterDuff.Mode.SRC_OUT;
            case 8://显示不相交的地方的底层
                return PorterDuff.Mode.DST_OUT;
            case 9://显示底层和相交部分的顶层
                return PorterDuff.Mode.SRC_ATOP;
            case 10://显示相交部分的底层和不相交部分的顶层
                return PorterDuff.Mode.DST_ATOP;
            case 11://显示底层和顶层 但是不包含相交部分
                return PorterDuff.Mode.XOR;
            case 16://显示不相交的顶层和底层 ，相交的部分类似遮盖变暗效果
                return PorterDuff.Mode.DARKEN;
            case 17://显示不相交的顶层和底层，相交部分则变量
                return PorterDuff.Mode.LIGHTEN;
            case 13://只显示相交部分而且是叠加效果
                return PorterDuff.Mode.MULTIPLY;
            case 14://显示不相交的顶层和底层 ，相交的部分类似 看不出啥名堂
                return PorterDuff.Mode.SCREEN;
            case 12:
                return PorterDuff.Mode.ADD;
            case 15:
                return PorterDuff.Mode.OVERLAY;
        }
    }
}
