package rpt.tool.mementobibere.libraries.chart.interfaces.datasets;

import android.graphics.Paint;

import rpt.tool.mementobibere.libraries.chart.data.CandleEntry;


public interface ICandleDataSet extends ILineScatterCandleRadarDataSet<CandleEntry> {


    float getBarSpace();


    boolean getShowCandleBar();


    float getShadowWidth();


    int getShadowColor();


    int getNeutralColor();


    int getIncreasingColor();


    int getDecreasingColor();


    Paint.Style getIncreasingPaintStyle();


    Paint.Style getDecreasingPaintStyle();


    boolean getShadowColorSameAsCandle();
}
