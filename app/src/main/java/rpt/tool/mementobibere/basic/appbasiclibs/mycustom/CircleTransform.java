package rpt.tool.mementobibere.basic.appbasiclibs.mycustom;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.annotation.NonNull;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

import rpt.tool.mementobibere.basic.appbasiclibs.utils.String_Helper;

public class CircleTransform extends BitmapTransformation
{
    static Paint paintBorder1;
    String_Helper sh;

    public CircleTransform(Context context,String color)
    {
        super();
        sh=new String_Helper(context, (Activity)context);
        paintBorder1 = new Paint();
        if(!sh.check_blank_data(color))
            paintBorder1.setColor(Color.parseColor(color));
        else
            paintBorder1.setColor(Color.TRANSPARENT);

        paintBorder1.setAntiAlias(true);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight)
    {
        return circleCrop(pool, toTransform);
    }

    private static Bitmap circleCrop(BitmapPool pool, Bitmap source)
    {
        if (source == null) return null;

        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        // TODO this could be acquired from the pool too
        Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

        Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        float r = size / 2f;
        //canvas.drawCircle(r, r, r, paint);
        
        Paint paintBorder;
        paintBorder = new Paint();
	    
	    paintBorder.setAntiAlias(true);

	    //canvas.drawCircle(r,r,r - 1.0f, paintBorder);
	    canvas.drawCircle(r,r,r,paintBorder1);
	    //paintBorder1.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	    canvas.drawCircle(r, r, r-1.5f, paint);
        
        return result;
    }

    public String getId() {
        return getClass().getName();
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }
}
