
package rpt.tool.mementobibere.ui.libraries.chart.charts;

import android.content.Context;
import android.util.AttributeSet;

import rpt.tool.mementobibere.ui.libraries.chart.data.BubbleData;
import rpt.tool.mementobibere.ui.libraries.chart.interfaces.dataprovider.BubbleDataProvider;
import rpt.tool.mementobibere.ui.libraries.chart.renderer.BubbleChartRenderer;


public class BubbleChart extends BarLineChartBase<BubbleData> implements BubbleDataProvider {

    public BubbleChart(Context context) {
        super(context);
    }

    public BubbleChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BubbleChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();

        mRenderer = new BubbleChartRenderer(this, mAnimator, mViewPortHandler);
    }

    public BubbleData getBubbleData() {
        return mData;
    }
}
