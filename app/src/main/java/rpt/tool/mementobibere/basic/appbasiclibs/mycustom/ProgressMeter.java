package rpt.tool.mementobibere.basic.appbasiclibs.mycustom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import rpt.tool.mementobibere.R;

@SuppressLint("AppCompatCustomView")
public class ProgressMeter extends View
{
    Context mContext;
    TypedArray typedArray;
    AttributeSet attrs;

    private int viewWidth;
    private int viewHeight;

    boolean shadow=false;
    int shadow_thickness=8;

    String text="5/7";
    int text_size=50;
    int text_color= Color.WHITE;

    int progress=50;
    int progress_color= Color.YELLOW;
    int progress_width=20;

    int background= Color.BLUE;

    boolean indicator_shadow=false;
    int indicator_color= Color.RED;
    boolean indicator=false;
    int indicator_width=4;

    boolean animation=false;
    int animation_duration=5;

    private Paint paintInnerBackgroud;
    private Paint paintProgressBackground;
    private Paint paintIndicatorBackground;
    TextPaint textPaint;

    int margin_left=40;

    float currentAngle=10;



    public ProgressMeter(Context context)
    {
        super(context);
        setup();
    }

    public ProgressMeter(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        mContext=context;
        this.attrs=attrs;

        init();
        setup();
    }

    public ProgressMeter(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);

        mContext=context;
        this.attrs=attrs;

        init();
        setup();
    }

    public void init()
    {
        typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.ProgressMeter);


        background=typedArray.getColor(R.styleable.ProgressMeter_cpv_background, background);

        text=typedArray.getString(R.styleable.ProgressMeter_cpv_text);
        text_size=typedArray.getInteger(R.styleable.ProgressMeter_cpv_text_size, text_size);
        text_color=typedArray.getColor(R.styleable.ProgressMeter_cpv_text_color, text_color);

        shadow=typedArray.getBoolean(R.styleable.ProgressMeter_cpv_shadow, shadow);
        shadow_thickness=typedArray.getInteger(R.styleable.ProgressMeter_cpv_shadow_thickness, shadow_thickness);

        progress=typedArray.getInteger(R.styleable.ProgressMeter_cpv_progress, progress);
        progress_color=typedArray.getColor(R.styleable.ProgressMeter_cpv_progress_color, progress_color);
        progress_width=typedArray.getInteger(R.styleable.ProgressMeter_cpv_progress_width, progress_width);

        indicator=typedArray.getBoolean(R.styleable.ProgressMeter_cpv_indicator, indicator);
        indicator_shadow=typedArray.getBoolean(R.styleable.ProgressMeter_cpv_indicator_shadow, indicator_shadow);
        indicator_color=typedArray.getColor(R.styleable.ProgressMeter_cpv_indicator_color, indicator_color);
        indicator_width=typedArray.getInteger(R.styleable.ProgressMeter_cpv_indicator_width, indicator_width);

        animation=typedArray.getBoolean(R.styleable.ProgressMeter_cpv_animation, animation);
        animation_duration=typedArray.getInteger(R.styleable.ProgressMeter_cpv_animation_duration, animation_duration);

        if(progress>100)
            progress=100;
    }

    private void setup()
    {
        textPaint = new TextPaint();
        //textPaint.setColor(Color.rgb(255,255,255));
        setTextColor(text_color);
        textPaint.setTextSize(text_size);
        textPaint.setAntiAlias(true);


        paintInnerBackgroud = new Paint();
        setInnerBackgroundColor(background);
        paintInnerBackgroud.setAntiAlias(true);
        //paintInnerBackgroud.setStyle(Paint.Style.STROKE);
        //paintInnerBackgroud.setStrokeWidth(5);
        this.setLayerType(LAYER_TYPE_SOFTWARE, paintInnerBackgroud);


        paintProgressBackground = new Paint();
        setProgressBackgroundColor(progress_color);
        paintProgressBackground.setAntiAlias(true);
        //paintProgressBackground.setStrokeCap(Paint.Cap.ROUND);
        //paintProgressBackground.setStyle(Paint.Style.STROKE);
        //paintProgressBackground.setStrokeWidth(5);
        this.setLayerType(LAYER_TYPE_SOFTWARE, paintProgressBackground);


        paintIndicatorBackground = new Paint();
        setIndicatorBackground(indicator_color);
        paintIndicatorBackground.setAntiAlias(true);
        this.setLayerType(LAYER_TYPE_SOFTWARE, paintIndicatorBackground);


    }

    @SuppressLint("DrawAllocation")
    @Override
    public void onDraw(Canvas canvas)
    {
        //canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        int circleCenter = viewWidth / 2;
        int hh=viewHeight/2;

        //final RectF rec = new RectF();
        //rec.set(0, 0, viewWidth, viewHeight);
        //canvas.drawRect(rec,paintBorder2);

        margin_left=(viewWidth*40)/300;

        if(shadow)
            paintProgressBackground.setShadowLayer(shadow_thickness,0,0, Color.BLACK);

        int tmp_progress_width=margin_left-progress_width;
        int progress_angle=(progress*270)/100;

        RectF oval = new RectF();
        oval.set(tmp_progress_width, tmp_progress_width, viewWidth-tmp_progress_width, viewHeight-tmp_progress_width);
        if(animation)
            canvas.drawArc(oval,135,currentAngle,true,paintProgressBackground);
        else
            canvas.drawArc(oval,135,progress_angle,true,paintProgressBackground);



        if(indicator_shadow)
            paintIndicatorBackground.setShadowLayer(2,0,0, Color.BLACK);

        if(indicator)
        {
            int total_progress=135+progress_angle;

            if(total_progress>360)
                total_progress=total_progress-360;

            final RectF oval3 = new RectF();
            oval3.set(tmp_progress_width-2, tmp_progress_width-2, viewWidth-tmp_progress_width+2, viewHeight-tmp_progress_width+2);
            canvas.drawArc(oval3,total_progress,indicator_width,true,paintIndicatorBackground);
        }


        final RectF oval2 = new RectF();
        oval2.set(margin_left, margin_left, viewWidth-margin_left, viewHeight-margin_left);
        canvas.drawArc(oval2,0,360,true,paintInnerBackgroud);


        if(!TextUtils.isEmpty(getTextString()))
        {
            float textHeight = textPaint.descent() + textPaint.ascent();
            canvas.drawText(text, (viewWidth - textPaint.measureText(text)) / 2.0f, (viewHeight - textHeight) / 2.0f, textPaint);
        }

        if(currentAngle<=progress_angle && animation==true)
        {
            currentAngle++;
            try {
                Thread.sleep(animation_duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            invalidate();
        }
    }

    public void setInnerBackgroundColor(int borderColor)
    {
        if(paintInnerBackgroud != null)
            paintInnerBackgroud.setColor(borderColor);

        this.invalidate();
    }

    public void setProgressBackgroundColor(int borderColor)
    {
        if(paintProgressBackground != null)
            paintProgressBackground.setColor(borderColor);

        this.invalidate();
    }

    public void setIndicatorBackground(int borderColor)
    {
        if(paintIndicatorBackground != null)
            paintIndicatorBackground.setColor(borderColor);

        this.invalidate();
    }

    public void setTextColor(int text_color)
    {
        this.text_color=text_color;

        if(textPaint != null)
            textPaint.setColor(text_color);

        this.invalidate();
    }

    public void setTextString(String text)
    {
        this.text=text;
        this.invalidate();
    }

    public String getTextString() {
        return text;
    }

    public void setTextSize(int text)
    {
        this.text_size=text_size;
        this.invalidate();
    }

    public void showShadow(boolean shadow)
    {
        this.shadow=shadow;
        this.invalidate();
    }

    public void setShadowThickness(int shadow_thickness)
    {
        this.shadow_thickness=shadow_thickness;
        this.invalidate();
    }

    public void setProgress(int progress)
    {
        if(progress>100)
            progress=100;

        currentAngle=0;
        this.progress=progress;
        this.invalidate();
    }

    public void setProgressThickness(int progress_width)
    {
        this.progress_width=progress_width;
        this.invalidate();
    }

    public void showIndicator(boolean indicator)
    {
        this.indicator=indicator;
        this.invalidate();
    }

    public void showIndicatorShadow(boolean indicator_shadow)
    {
        this.indicator_shadow=indicator_shadow;
        this.invalidate();
    }

    public void setIndicatorThickness(int indicator_width)
    {
        this.indicator_width=indicator_width;
        this.invalidate();
    }

    public void animation(boolean animation)
    {
        this.animation=animation;
        currentAngle=0;
        this.invalidate();
    }

    public boolean is_animation()
    {
        return animation;
    }

    public void setAnimationDuration(int animation_duration)
    {
        this.animation_duration=animation_duration;
        this.invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        System.out.println("onMeasure : "+widthMeasureSpec+" : "+heightMeasureSpec);
        int borderWidth = 3;

        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);

        viewWidth = width - (borderWidth *2);
        viewHeight = height - (borderWidth*2);

        setMeasuredDimension(width, height);
    }

    private int measureWidth(int measureSpec)
    {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY)
        {
            // We were told how big to be
            result = specSize;
        }
        else
        {
            // Measure the text
            result = viewWidth;
        }

        return result;
    }

    private int measureHeight(int measureSpecHeight)
    {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpecHeight);
        int specSize = MeasureSpec.getSize(measureSpecHeight);

        if (specMode == MeasureSpec.EXACTLY)
        {
            // We were told how big to be
            result = specSize;
        }
        else
        {
            // Measure the text (beware: ascent is a negative number)
            result = viewHeight;
        }
        return (result+2);
    }
}
