package rpt.tool.mementobibere.basic.appbasiclibs.mycustom;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import androidx.appcompat.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import rpt.tool.mementobibere.R;


public class PrefixEditText extends AppCompatEditText {
    private ColorStateList mPrefixTextColor;

    String prefix="$";
    int prefix_color= Color.BLACK;
    int prefix_text_align=0;

    public PrefixEditText(Context context) {
        this(context, null);
        init(null);
    }

    public PrefixEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
        init(attrs);
    }

    public PrefixEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mPrefixTextColor = getTextColors();
        init(attrs);
    }



    private void init(AttributeSet attrs)
    {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.PrefixTextView);

            prefix = a.getString(R.styleable.PrefixTextView_prefix_text);

            if (TextUtils.isEmpty(prefix))
                prefix = "$";

            prefix_color = a.getInteger(R.styleable.PrefixTextView_prefix_text_color,prefix_color);
            mPrefixTextColor = ColorStateList.valueOf(prefix_color);
            prefix_text_align = a.getInteger(R.styleable.PrefixTextView_prefix_text_align,prefix_text_align);
        }

        load();
    }

    private void load()
    {
        Configuration config = getResources().getConfiguration();
        if(config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL)
        {
            if(prefix_text_align==0)
                setCompoundDrawables(null, null, new TextDrawable(" " + prefix), null);
            else
                setCompoundDrawables(new TextDrawable(prefix + " "), null, null, null);
        }
        else
        {
            if(prefix_text_align==0)
                setCompoundDrawables(new TextDrawable(prefix + " "), null, null, null);
            else
                setCompoundDrawables(null, null, new TextDrawable(prefix + " "), null);
        }
    }

    public void setPrefix(String prefix)
    {
        this.prefix=prefix;

        load();

        /*Configuration config = getResources().getConfiguration();
        if(config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL)
        {
            setCompoundDrawables(null, null, new TextDrawable(" " + prefix), null);
        }
        else
        {
            setCompoundDrawables(new TextDrawable(prefix + " "), null, null, null);
        }*/

        /*if (Locale.getDefault().getLanguage().equalsIgnoreCase("en"))
            setCompoundDrawables(new TextDrawable(prefix + " "), null, null, null);
        else if (Locale.getDefault().getLanguage().equalsIgnoreCase("ar"))
            setCompoundDrawables(null, null, new TextDrawable(" " + prefix), null);*/

    }

    public void setPrefixAlign(int prefix_text_align)
    {
        this.prefix_text_align=prefix_text_align;
        load();
    }

    public void setPrefixTextColor(int color) {
        mPrefixTextColor = ColorStateList.valueOf(color);
        load();
    }

    private class TextDrawable extends Drawable {
        private String mText = "";

        TextDrawable(String text) {
            mText = text;
            setBounds(0, 0, (int) getPaint().measureText(mText) + 3, (int) getTextSize());
        }

        @Override
        public void draw(Canvas canvas) {
            Paint paint = getPaint();
            paint.setColor(mPrefixTextColor.getColorForState(getDrawableState(), 0));
            int lineBaseline = getLineBounds(0, null);
            canvas.drawText(mText, 0, canvas.getClipBounds().top + lineBaseline, paint);
        }

        @Override
        public void setAlpha(int alpha) {/* Not supported */}

        @Override
        public void setColorFilter(ColorFilter colorFilter) {/* Not supported */}

        @Override
        public int getOpacity() {
            return PixelFormat.UNKNOWN;
        }
    }
}
