
package rpt.tool.mementobibere.libraries.chart.jobs;

import android.graphics.Matrix;
import android.view.View;

import rpt.tool.mementobibere.libraries.chart.charts.BarLineChartBase;
import rpt.tool.mementobibere.libraries.chart.components.YAxis;
import rpt.tool.mementobibere.libraries.chart.utils.ObjectPool;
import rpt.tool.mementobibere.libraries.chart.utils.Transformer;
import rpt.tool.mementobibere.libraries.chart.utils.ViewPortHandler;


public class ZoomJob extends ViewPortJob {

    private static ObjectPool<ZoomJob> pool;

    static {
        pool = ObjectPool.create(1, new ZoomJob(null, 0, 0, 0, 0, null, null, null));
        pool.setReplenishPercentage(0.5f);
    }

    public static ZoomJob getInstance(ViewPortHandler viewPortHandler, float scaleX, float scaleY, float xValue, float yValue,
                                      Transformer trans, YAxis.AxisDependency axis, View v) {
        ZoomJob result = pool.get();
        result.xValue = xValue;
        result.yValue = yValue;
        result.scaleX = scaleX;
        result.scaleY = scaleY;
        result.mViewPortHandler = viewPortHandler;
        result.mTrans = trans;
        result.axisDependency = axis;
        result.view = v;
        return result;
    }

    public static void recycleInstance(ZoomJob instance) {
        pool.recycle(instance);
    }

    protected float scaleX;
    protected float scaleY;

    protected YAxis.AxisDependency axisDependency;

    public ZoomJob(ViewPortHandler viewPortHandler, float scaleX, float scaleY, float xValue, float yValue, Transformer trans,
                   YAxis.AxisDependency axis, View v) {
        super(viewPortHandler, xValue, yValue, trans, v);

        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.axisDependency = axis;
    }

    protected Matrix mRunMatrixBuffer = new Matrix();

    @Override
    public void run() {

        Matrix save = mRunMatrixBuffer;
        mViewPortHandler.zoom(scaleX, scaleY, save);
        mViewPortHandler.refresh(save, view, false);

        float yValsInView = ((BarLineChartBase) view).getAxis(axisDependency).mAxisRange / mViewPortHandler.getScaleY();
        float xValsInView = ((BarLineChartBase) view).getXAxis().mAxisRange / mViewPortHandler.getScaleX();

        pts[0] = xValue - xValsInView / 2f;
        pts[1] = yValue + yValsInView / 2f;

        mTrans.pointValuesToPixel(pts);

        mViewPortHandler.translate(pts, save);
        mViewPortHandler.refresh(save, view, false);

        ((BarLineChartBase) view).calculateOffsets();
        view.postInvalidate();

        recycleInstance(this);
    }

    @Override
    protected ObjectPool.Poolable instantiate() {
        return new ZoomJob(null, 0, 0, 0, 0, null, null, null);
    }
}
