package rpt.tool.mementobibere.basic.appbasiclibs.mycustom;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import rpt.tool.mementobibere.R;


@SuppressLint("AppCompatCustomView")
public class MyTextView extends TextView
{
    String fontName=null;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public MyTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(attrs);
    }

    public MyTextView(Context context)
    {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs)
    {
        if (attrs != null)
        {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MyTextView);
            fontName = a.getString(R.styleable.MyTextView_fontname);

            setTypeFace();

            a.recycle();
        }
    }

    public void setFontName(String fontName)
    {
        this.fontName=fontName;
        setTypeFace();
    }

    private void setTypeFace()
    {
        try
        {
            if (fontName != null)
            {
                Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), fontName);
                setTypeface(myTypeface);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
