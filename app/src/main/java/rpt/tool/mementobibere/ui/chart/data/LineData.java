
package rpt.tool.mementobibere.ui.chart.data;

import rpt.tool.mementobibere.ui.chart.interfaces.datasets.ILineDataSet;

import java.util.List;


public class LineData extends BarLineScatterCandleBubbleData<ILineDataSet> {

    public LineData() {
        super();
    }

    public LineData(ILineDataSet... dataSets) {
        super(dataSets);
    }

    public LineData(List<ILineDataSet> dataSets) {
        super(dataSets);
    }
}
