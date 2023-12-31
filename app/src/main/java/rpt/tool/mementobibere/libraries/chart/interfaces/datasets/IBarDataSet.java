package rpt.tool.mementobibere.libraries.chart.interfaces.datasets;

import rpt.tool.mementobibere.libraries.chart.data.BarEntry;
import rpt.tool.mementobibere.libraries.chart.utils.Fill;

import java.util.List;


public interface IBarDataSet extends IBarLineScatterCandleBubbleDataSet<BarEntry> {

    List<Fill> getFills();

    Fill getFill(int index);

    
    boolean isStacked();

    
    int getStackSize();

    
    int getBarShadowColor();

    
    float getBarBorderWidth();

    
    int getBarBorderColor();

    
    int getHighLightAlpha();


    
    String[] getStackLabels();
}
