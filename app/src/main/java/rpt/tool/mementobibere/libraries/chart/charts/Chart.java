
package rpt.tool.mementobibere.libraries.chart.charts;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.provider.MediaStore.Images;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.annotation.RequiresApi;

import rpt.tool.mementobibere.libraries.chart.animation.ChartAnimator;
import rpt.tool.mementobibere.libraries.chart.animation.Easing;
import rpt.tool.mementobibere.libraries.chart.components.Description;
import rpt.tool.mementobibere.libraries.chart.components.IMarker;
import rpt.tool.mementobibere.libraries.chart.components.Legend;
import rpt.tool.mementobibere.libraries.chart.components.XAxis;
import rpt.tool.mementobibere.libraries.chart.formatter.DefaultValueFormatter;
import rpt.tool.mementobibere.libraries.chart.formatter.IValueFormatter;
import rpt.tool.mementobibere.libraries.chart.highlight.ChartHighlighter;
import rpt.tool.mementobibere.libraries.chart.highlight.Highlight;
import rpt.tool.mementobibere.libraries.chart.highlight.IHighlighter;
import rpt.tool.mementobibere.libraries.chart.listener.ChartTouchListener;
import rpt.tool.mementobibere.libraries.chart.listener.OnChartGestureListener;
import rpt.tool.mementobibere.libraries.chart.listener.OnChartValueSelectedListener;
import rpt.tool.mementobibere.libraries.chart.renderer.DataRenderer;
import rpt.tool.mementobibere.libraries.chart.renderer.LegendRenderer;
import rpt.tool.mementobibere.libraries.chart.data.ChartData;
import rpt.tool.mementobibere.libraries.chart.data.Entry;
import rpt.tool.mementobibere.libraries.chart.interfaces.dataprovider.ChartInterface;
import rpt.tool.mementobibere.libraries.chart.interfaces.datasets.IDataSet;
import rpt.tool.mementobibere.libraries.chart.utils.MPPointF;
import rpt.tool.mementobibere.libraries.chart.utils.Utils;
import rpt.tool.mementobibere.libraries.chart.utils.ViewPortHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;


public abstract class Chart<T extends ChartData<? extends IDataSet<? extends Entry>>> extends
        ViewGroup
        implements ChartInterface {

    public static final String LOG_TAG = "MPAndroidChart";

    
    protected boolean mLogEnabled = false;

    
    protected T mData = null;

    
    protected boolean mHighLightPerTapEnabled = true;

    
    private boolean mDragDecelerationEnabled = true;

    
    private float mDragDecelerationFrictionCoef = 0.9f;

    
    protected DefaultValueFormatter mDefaultValueFormatter = new DefaultValueFormatter(0);

    
    protected Paint mDescPaint;

    
    protected Paint mInfoPaint;

    
    protected XAxis mXAxis;

    
    protected boolean mTouchEnabled = true;

    
    protected Description mDescription;

    
    protected Legend mLegend;

    
    protected OnChartValueSelectedListener mSelectionListener;

    protected ChartTouchListener mChartTouchListener;

    
    private String mNoDataText = "No chart data available.";

    
    private OnChartGestureListener mGestureListener;

    protected LegendRenderer mLegendRenderer;

    
    protected DataRenderer mRenderer;

    protected IHighlighter mHighlighter;

    
    protected ViewPortHandler mViewPortHandler = new ViewPortHandler();

    
    protected ChartAnimator mAnimator;

    
    private float mExtraTopOffset = 0.f,
            mExtraRightOffset = 0.f,
            mExtraBottomOffset = 0.f,
            mExtraLeftOffset = 0.f;

    
    public Chart(Context context) {
        super(context);
        init();
    }

    
    public Chart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    
    public Chart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    
    protected void init() {

        setWillNotDraw(false);
        // setLayerType(View.LAYER_TYPE_HARDWARE, null);

        mAnimator = new ChartAnimator(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // ViewCompat.postInvalidateOnAnimation(Chart.this);
                postInvalidate();
            }
        });

        // initialize the utils
        Utils.init(getContext());
        mMaxHighlightDistance = Utils.convertDpToPixel(500f);

        mDescription = new Description();
        mLegend = new Legend();

        mLegendRenderer = new LegendRenderer(mViewPortHandler, mLegend);

        mXAxis = new XAxis();

        mDescPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mInfoPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mInfoPaint.setColor(Color.rgb(247, 189, 51)); // orange
        mInfoPaint.setTextAlign(Align.CENTER);
        mInfoPaint.setTextSize(Utils.convertDpToPixel(12f));

        if (mLogEnabled)
            Log.i("", "Chart.init()");
    }

    // public void initWithDummyData() {
    // ColorTemplate template = new ColorTemplate();
    // template.addColorsForDataSets(ColorTemplate.COLORFUL_COLORS,
    // getContext());
    //
    // setColorTemplate(template);
    // setDrawYValues(false);
    //
    // ArrayList<String> xVals = new ArrayList<String>();
    // Calendar calendar = Calendar.getInstance();
    // for (int i = 0; i < 12; i++) {
    // xVals.add(calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT,
    // Locale.getDefault()));
    // }
    //
    // ArrayList<DataSet> dataSets = new ArrayList<DataSet>();
    // for (int i = 0; i < 3; i++) {
    //
    // ArrayList<Entry> yVals = new ArrayList<Entry>();
    //
    // for (int j = 0; j < 12; j++) {
    // float val = (float) (Math.random() * 100);
    // yVals.add(new Entry(val, j));
    // }
    //
    // DataSet set = new DataSet(yVals, "DataSet " + i);
    // dataSets.add(set); // add the datasets
    // }
    // // create a data object with the datasets
    // ChartData data = new ChartData(xVals, dataSets);
    // setData(data);
    // invalidate();
    // }

    
    public void setData(T data) {

        mData = data;
        mOffsetsCalculated = false;

        if (data == null) {
            return;
        }

        // calculate how many digits are needed
        setupDefaultFormatter(data.getYMin(), data.getYMax());

        for (IDataSet set : mData.getDataSets()) {
            if (set.needsFormatter() || set.getValueFormatter() == mDefaultValueFormatter)
                set.setValueFormatter(mDefaultValueFormatter);
        }

        // let the chart know there is new data
        notifyDataSetChanged();

        if (mLogEnabled)
            Log.i(LOG_TAG, "Data is set.");
    }

    
    public void clear() {
        mData = null;
        mOffsetsCalculated = false;
        mIndicesToHighlight = null;
        mChartTouchListener.setLastHighlighted(null);
        invalidate();
    }

    
    public void clearValues() {
        mData.clearValues();
        invalidate();
    }

    
    public boolean isEmpty() {

        if (mData == null)
            return true;
        else {

            if (mData.getEntryCount() <= 0)
                return true;
            else
                return false;
        }
    }

    
    public abstract void notifyDataSetChanged();

    
    protected abstract void calculateOffsets();

    
    protected abstract void calcMinMax();

    
    protected void setupDefaultFormatter(float min, float max) {

        float reference = 0f;

        if (mData == null || mData.getEntryCount() < 2) {

            reference = Math.max(Math.abs(min), Math.abs(max));
        } else {
            reference = Math.abs(max - min);
        }

        int digits = Utils.getDecimals(reference);

        // setup the formatter with a new number of digits
        mDefaultValueFormatter.setup(digits);
    }

    
    private boolean mOffsetsCalculated = false;

    @Override
    protected void onDraw(Canvas canvas) {
        // super.onDraw(canvas);

        if (mData == null) {

            boolean hasText = !TextUtils.isEmpty(mNoDataText);

            if (hasText) {
                MPPointF pt = getCenter();

                switch (mInfoPaint.getTextAlign()) {
                    case LEFT:
                        pt.x = 0;
                        canvas.drawText(mNoDataText, pt.x, pt.y, mInfoPaint);
                        break;

                    case RIGHT:
                        pt.x *= 2.0;
                        canvas.drawText(mNoDataText, pt.x, pt.y, mInfoPaint);
                        break;

                    default:
                        canvas.drawText(mNoDataText, pt.x, pt.y, mInfoPaint);
                        break;
                }
            }

            return;
        }

        if (!mOffsetsCalculated) {

            calculateOffsets();
            mOffsetsCalculated = true;
        }
    }

    
    protected void drawDescription(Canvas c) {

        // check if description should be drawn
        if (mDescription != null && mDescription.isEnabled()) {

            MPPointF position = mDescription.getPosition();

            mDescPaint.setTypeface(mDescription.getTypeface());
            mDescPaint.setTextSize(mDescription.getTextSize());
            mDescPaint.setColor(mDescription.getTextColor());
            mDescPaint.setTextAlign(mDescription.getTextAlign());

            float x, y;

            // if no position specified, draw on default position
            if (position == null) {
                x = getWidth() - mViewPortHandler.offsetRight() - mDescription.getXOffset();
                y = getHeight() - mViewPortHandler.offsetBottom() - mDescription.getYOffset();
            } else {
                x = position.x;
                y = position.y;
            }

            c.drawText(mDescription.getText(), x, y, mDescPaint);
        }
    }

    
    

    
    protected Highlight[] mIndicesToHighlight;

    
    protected float mMaxHighlightDistance = 0f;

    @Override
    public float getMaxHighlightDistance() {
        return mMaxHighlightDistance;
    }

    
    public void setMaxHighlightDistance(float distDp) {
        mMaxHighlightDistance = Utils.convertDpToPixel(distDp);
    }

    
    public Highlight[] getHighlighted() {
        return mIndicesToHighlight;
    }

    
    public boolean isHighlightPerTapEnabled() {
        return mHighLightPerTapEnabled;
    }

    
    public void setHighlightPerTapEnabled(boolean enabled) {
        mHighLightPerTapEnabled = enabled;
    }

    
    public boolean valuesToHighlight() {
        return mIndicesToHighlight == null || mIndicesToHighlight.length <= 0
                || mIndicesToHighlight[0] == null ? false
                : true;
    }

    
    protected void setLastHighlighted(Highlight[] highs) {

        if (highs == null || highs.length <= 0 || highs[0] == null) {
            mChartTouchListener.setLastHighlighted(null);
        } else {
            mChartTouchListener.setLastHighlighted(highs[0]);
        }
    }

    
    public void highlightValues(Highlight[] highs) {

        // set the indices to highlight
        mIndicesToHighlight = highs;

        setLastHighlighted(highs);

        // redraw the chart
        invalidate();
    }

    
    public void highlightValue(float x, int dataSetIndex, int dataIndex) {
        highlightValue(x, dataSetIndex, dataIndex, true);
    }

    
    public void highlightValue(float x, int dataSetIndex) {
        highlightValue(x, dataSetIndex, -1, true);
    }

    
    public void highlightValue(float x, float y, int dataSetIndex, int dataIndex) {
        highlightValue(x, y, dataSetIndex, dataIndex, true);
    }

    
    public void highlightValue(float x, float y, int dataSetIndex) {
        highlightValue(x, y, dataSetIndex, -1, true);
    }

    
    public void highlightValue(float x, int dataSetIndex, int dataIndex, boolean callListener) {
        highlightValue(x, Float.NaN, dataSetIndex, dataIndex, callListener);
    }

    
    public void highlightValue(float x, int dataSetIndex, boolean callListener) {
        highlightValue(x, Float.NaN, dataSetIndex, -1, callListener);
    }

    
    public void highlightValue(float x, float y, int dataSetIndex, int dataIndex, boolean callListener) {

        if (dataSetIndex < 0 || dataSetIndex >= mData.getDataSetCount()) {
            highlightValue(null, callListener);
        } else {
            highlightValue(new Highlight(x, y, dataSetIndex, dataIndex), callListener);
        }
    }

    
    public void highlightValue(float x, float y, int dataSetIndex, boolean callListener) {
        highlightValue(x, y, dataSetIndex, -1, callListener);
    }

    
    public void highlightValue(Highlight highlight) {
        highlightValue(highlight, false);
    }

    
    public void highlightValue(Highlight high, boolean callListener) {

        Entry e = null;

        if (high == null)
            mIndicesToHighlight = null;
        else {

            if (mLogEnabled)
                Log.i(LOG_TAG, "Highlighted: " + high.toString());

            e = mData.getEntryForHighlight(high);
            if (e == null) {
                mIndicesToHighlight = null;
                high = null;
            } else {

                // set the indices to highlight
                mIndicesToHighlight = new Highlight[]{
                        high
                };
            }
        }

        setLastHighlighted(mIndicesToHighlight);

        if (callListener && mSelectionListener != null) {

            if (!valuesToHighlight())
                mSelectionListener.onNothingSelected();
            else {
                // notify the listener
                mSelectionListener.onValueSelected(e, high);
            }
        }

        // redraw the chart
        invalidate();
    }

    
    public Highlight getHighlightByTouchPoint(float x, float y) {

        if (mData == null) {
            Log.e(LOG_TAG, "Can't select by touch. No data set.");
            return null;
        } else
            return getHighlighter().getHighlight(x, y);
    }

    
    public void setOnTouchListener(ChartTouchListener l) {
        this.mChartTouchListener = l;
    }

    
    public ChartTouchListener getOnTouchListener() {
        return mChartTouchListener;
    }

    
    

    
    protected boolean mDrawMarkers = true;

    
    protected IMarker mMarker;

    
    protected void drawMarkers(Canvas canvas) {

        // if there is no marker view or drawing marker is disabled
        if (mMarker == null || !isDrawMarkersEnabled() || !valuesToHighlight())
            return;

        for (int i = 0; i < mIndicesToHighlight.length; i++) {

            Highlight highlight = mIndicesToHighlight[i];

            IDataSet set = mData.getDataSetByIndex(highlight.getDataSetIndex());

            Entry e = mData.getEntryForHighlight(mIndicesToHighlight[i]);
            int entryIndex = set.getEntryIndex(e);

            // make sure entry not null
            if (e == null || entryIndex > set.getEntryCount() * mAnimator.getPhaseX())
                continue;

            float[] pos = getMarkerPosition(highlight);

            // check bounds
            if (!mViewPortHandler.isInBounds(pos[0], pos[1]))
                continue;

            // callbacks to update the content
            mMarker.refreshContent(e, highlight);

            // draw the marker
            mMarker.draw(canvas, pos[0], pos[1]);
        }
    }

    
    protected float[] getMarkerPosition(Highlight high) {
        return new float[]{high.getDrawX(), high.getDrawY()};
    }

    
    

    
    public ChartAnimator getAnimator() {
        return mAnimator;
    }

    
    public boolean isDragDecelerationEnabled() {
        return mDragDecelerationEnabled;
    }

    
    public void setDragDecelerationEnabled(boolean enabled) {
        mDragDecelerationEnabled = enabled;
    }

    
    public float getDragDecelerationFrictionCoef() {
        return mDragDecelerationFrictionCoef;
    }

    
    public void setDragDecelerationFrictionCoef(float newValue) {

        if (newValue < 0.f)
            newValue = 0.f;

        if (newValue >= 1f)
            newValue = 0.999f;

        mDragDecelerationFrictionCoef = newValue;
    }

    
    

    
    @RequiresApi(11)
    public void animateXY(int durationMillisX, int durationMillisY, Easing.EasingFunction easingX,
                          Easing.EasingFunction easingY) {
        mAnimator.animateXY(durationMillisX, durationMillisY, easingX, easingY);
    }

    
    @RequiresApi(11)
    public void animateXY(int durationMillisX, int durationMillisY, Easing.EasingFunction easing) {
        mAnimator.animateXY(durationMillisX, durationMillisY, easing);
    }

    
    @RequiresApi(11)
    public void animateX(int durationMillis, Easing.EasingFunction easing) {
        mAnimator.animateX(durationMillis, easing);
    }

    
    @RequiresApi(11)
    public void animateY(int durationMillis, Easing.EasingFunction easing) {
        mAnimator.animateY(durationMillis, easing);
    }

    
    

    
    

    
    @RequiresApi(11)
    public void animateX(int durationMillis) {
        mAnimator.animateX(durationMillis);
    }

    
    @RequiresApi(11)
    public void animateY(int durationMillis) {
        mAnimator.animateY(durationMillis);
    }

    
    @RequiresApi(11)
    public void animateXY(int durationMillisX, int durationMillisY) {
        mAnimator.animateXY(durationMillisX, durationMillisY);
    }

    
    


    
    public XAxis getXAxis() {
        return mXAxis;
    }

    
    public IValueFormatter getDefaultValueFormatter() {
        return mDefaultValueFormatter;
    }

    
    public void setOnChartValueSelectedListener(OnChartValueSelectedListener l) {
        this.mSelectionListener = l;
    }

    
    public void setOnChartGestureListener(OnChartGestureListener l) {
        this.mGestureListener = l;
    }

    
    public OnChartGestureListener getOnChartGestureListener() {
        return mGestureListener;
    }

    
    public float getYMax() {
        return mData.getYMax();
    }

    
    public float getYMin() {
        return mData.getYMin();
    }

    @Override
    public float getXChartMax() {
        return mXAxis.mAxisMaximum;
    }

    @Override
    public float getXChartMin() {
        return mXAxis.mAxisMinimum;
    }

    @Override
    public float getXRange() {
        return mXAxis.mAxisRange;
    }

    
    public MPPointF getCenter() {
        return MPPointF.getInstance(getWidth() / 2f, getHeight() / 2f);
    }

    
    @Override
    public MPPointF getCenterOffsets() {
        return mViewPortHandler.getContentCenter();
    }

    
    public void setExtraOffsets(float left, float top, float right, float bottom) {
        setExtraLeftOffset(left);
        setExtraTopOffset(top);
        setExtraRightOffset(right);
        setExtraBottomOffset(bottom);
    }

    
    public void setExtraTopOffset(float offset) {
        mExtraTopOffset = Utils.convertDpToPixel(offset);
    }

    
    public float getExtraTopOffset() {
        return mExtraTopOffset;
    }

    
    public void setExtraRightOffset(float offset) {
        mExtraRightOffset = Utils.convertDpToPixel(offset);
    }

    
    public float getExtraRightOffset() {
        return mExtraRightOffset;
    }

    
    public void setExtraBottomOffset(float offset) {
        mExtraBottomOffset = Utils.convertDpToPixel(offset);
    }

    
    public float getExtraBottomOffset() {
        return mExtraBottomOffset;
    }

    
    public void setExtraLeftOffset(float offset) {
        mExtraLeftOffset = Utils.convertDpToPixel(offset);
    }

    
    public float getExtraLeftOffset() {
        return mExtraLeftOffset;
    }

    
    public void setLogEnabled(boolean enabled) {
        mLogEnabled = enabled;
    }

    
    public boolean isLogEnabled() {
        return mLogEnabled;
    }

    
    public void setNoDataText(String text) {
        mNoDataText = text;
    }

    
    public void setNoDataTextColor(int color) {
        mInfoPaint.setColor(color);
    }

    
    public void setNoDataTextTypeface(Typeface tf) {
        mInfoPaint.setTypeface(tf);
    }

    
    public void setNoDataTextAlignment(Align align) {
        mInfoPaint.setTextAlign(align);
    }

    
    public void setTouchEnabled(boolean enabled) {
        this.mTouchEnabled = enabled;
    }

    
    public void setMarker(IMarker marker) {
        mMarker = marker;
    }

    
    public IMarker getMarker() {
        return mMarker;
    }

    @Deprecated
    public void setMarkerView(IMarker v) {
        setMarker(v);
    }

    @Deprecated
    public IMarker getMarkerView() {
        return getMarker();
    }

    
    public void setDescription(Description desc) {
        this.mDescription = desc;
    }

    
    public Description getDescription() {
        return mDescription;
    }

    
    public Legend getLegend() {
        return mLegend;
    }

    
    public LegendRenderer getLegendRenderer() {
        return mLegendRenderer;
    }

    
    @Override
    public RectF getContentRect() {
        return mViewPortHandler.getContentRect();
    }

    
    public void disableScroll() {
        ViewParent parent = getParent();
        if (parent != null)
            parent.requestDisallowInterceptTouchEvent(true);
    }

    
    public void enableScroll() {
        ViewParent parent = getParent();
        if (parent != null)
            parent.requestDisallowInterceptTouchEvent(false);
    }

    
    public static final int PAINT_GRID_BACKGROUND = 4;

    
    public static final int PAINT_INFO = 7;

    
    public static final int PAINT_DESCRIPTION = 11;

    
    public static final int PAINT_HOLE = 13;

    
    public static final int PAINT_CENTER_TEXT = 14;

    
    public static final int PAINT_LEGEND_LABEL = 18;

    
    public void setPaint(Paint p, int which) {

        switch (which) {
            case PAINT_INFO:
                mInfoPaint = p;
                break;
            case PAINT_DESCRIPTION:
                mDescPaint = p;
                break;
        }
    }

    
    public Paint getPaint(int which) {
        switch (which) {
            case PAINT_INFO:
                return mInfoPaint;
            case PAINT_DESCRIPTION:
                return mDescPaint;
        }

        return null;
    }

    @Deprecated
    public boolean isDrawMarkerViewsEnabled() {
        return isDrawMarkersEnabled();
    }

    @Deprecated
    public void setDrawMarkerViews(boolean enabled) {
        setDrawMarkers(enabled);
    }

    
    public boolean isDrawMarkersEnabled() {
        return mDrawMarkers;
    }

    
    public void setDrawMarkers(boolean enabled) {
        mDrawMarkers = enabled;
    }

    
    public T getData() {
        return mData;
    }

    
    public ViewPortHandler getViewPortHandler() {
        return mViewPortHandler;
    }

    
    public DataRenderer getRenderer() {
        return mRenderer;
    }

    
    public void setRenderer(DataRenderer renderer) {

        if (renderer != null)
            mRenderer = renderer;
    }

    public IHighlighter getHighlighter() {
        return mHighlighter;
    }

    
    public void setHighlighter(ChartHighlighter highlighter) {
        mHighlighter = highlighter;
    }

    
    @Override
    public MPPointF getCenterOfView() {
        return getCenter();
    }

    
    public Bitmap getChartBitmap() {
        // Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.RGB_565);
        // Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        // Get the view's background
        Drawable bgDrawable = getBackground();
        if (bgDrawable != null)
            // has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            // does not have background drawable, then draw white background on
            // the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        draw(canvas);
        // return the bitmap
        return returnedBitmap;
    }

    
    public boolean saveToPath(String title, String pathOnSD) {



        Bitmap b = getChartBitmap();

        OutputStream stream = null;
        try {
            stream = new FileOutputStream(Environment.getExternalStorageDirectory().getPath()
                    + pathOnSD + "/" + title
                    + ".png");

            
            b.compress(CompressFormat.PNG, 40, stream);

            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    
    public boolean saveToGallery(String fileName, String subFolderPath, String fileDescription, CompressFormat
            format, int quality) {
        // restrain quality
        if (quality < 0 || quality > 100)
            quality = 50;

        long currentTime = System.currentTimeMillis();

        File extBaseDir = Environment.getExternalStorageDirectory();
        File file = new File(extBaseDir.getAbsolutePath() + "/DCIM/" + subFolderPath);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                return false;
            }
        }

        String mimeType = "";
        switch (format) {
            case PNG:
                mimeType = "image/png";
                if (!fileName.endsWith(".png"))
                    fileName += ".png";
                break;
            case WEBP:
                mimeType = "image/webp";
                if (!fileName.endsWith(".webp"))
                    fileName += ".webp";
                break;
            case JPEG:
            default:
                mimeType = "image/jpeg";
                if (!(fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")))
                    fileName += ".jpg";
                break;
        }

        String filePath = file.getAbsolutePath() + "/" + fileName;
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filePath);

            Bitmap b = getChartBitmap();
            b.compress(format, quality, out);

            out.flush();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();

            return false;
        }

        long size = new File(filePath).length();

        ContentValues values = new ContentValues(8);

        // store the details
        values.put(Images.Media.TITLE, fileName);
        values.put(Images.Media.DISPLAY_NAME, fileName);
        values.put(Images.Media.DATE_ADDED, currentTime);
        values.put(Images.Media.MIME_TYPE, mimeType);
        values.put(Images.Media.DESCRIPTION, fileDescription);
        values.put(Images.Media.ORIENTATION, 0);
        values.put(Images.Media.DATA, filePath);
        values.put(Images.Media.SIZE, size);

        return getContext().getContentResolver().insert(Images.Media.EXTERNAL_CONTENT_URI, values) != null;
    }

    
    public boolean saveToGallery(String fileName, int quality) {
        return saveToGallery(fileName, "", "MPAndroidChart-Library Save", CompressFormat.PNG, quality);
    }

    
    public boolean saveToGallery(String fileName) {
        return saveToGallery(fileName, "", "MPAndroidChart-Library Save", CompressFormat.PNG, 40);
    }

    
    protected ArrayList<Runnable> mJobs = new ArrayList<Runnable>();

    public void removeViewportJob(Runnable job) {
        mJobs.remove(job);
    }

    public void clearAllViewportJobs() {
        mJobs.clear();
    }

    
    public void addViewportJob(Runnable job) {

        if (mViewPortHandler.hasChartDimens()) {
            post(job);
        } else {
            mJobs.add(job);
        }
    }

    
    public ArrayList<Runnable> getJobs() {
        return mJobs;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).layout(left, top, right, bottom);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = (int) Utils.convertDpToPixel(50f);
        setMeasuredDimension(
                Math.max(getSuggestedMinimumWidth(),
                        resolveSize(size,
                                widthMeasureSpec)),
                Math.max(getSuggestedMinimumHeight(),
                        resolveSize(size,
                                heightMeasureSpec)));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (mLogEnabled)
            Log.i(LOG_TAG, "OnSizeChanged()");

        if (w > 0 && h > 0 && w < 10000 && h < 10000) {
            if (mLogEnabled)
                Log.i(LOG_TAG, "Setting chart dimens, width: " + w + ", height: " + h);
            mViewPortHandler.setChartDimens(w, h);
        } else {
            if (mLogEnabled)
                Log.w(LOG_TAG, "*Avoiding* setting chart dimens! width: " + w + ", height: " + h);
        }

        // This may cause the chart view to mutate properties affecting the view port --
        //   lets do this before we try to run any pending jobs on the view port itself
        notifyDataSetChanged();

        for (Runnable r : mJobs) {
            post(r);
        }

        mJobs.clear();

        super.onSizeChanged(w, h, oldw, oldh);
    }

    
    public void setHardwareAccelerationEnabled(boolean enabled) {

        if (enabled)
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
        else
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        //Log.i(LOG_TAG, "Detaching...");

        if (mUnbind)
            unbindDrawables(this);
    }

    
    private boolean mUnbind = false;

    
    private void unbindDrawables(View view) {

        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }

    
    public void setUnbindEnabled(boolean enabled) {
        this.mUnbind = enabled;
    }
}
