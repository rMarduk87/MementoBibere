package rpt.tool.mementobibere.libraries.chart.components;


import android.graphics.DashPathEffect;

import rpt.tool.mementobibere.libraries.chart.utils.ColorTemplate;

public class LegendEntry {
    public LegendEntry() {

    }

    
    public LegendEntry(String label,
                       Legend.LegendForm form,
                       float formSize,
                       float formLineWidth,
                       DashPathEffect formLineDashEffect,
                       int formColor)
    {
        this.label = label;
        this.form = form;
        this.formSize = formSize;
        this.formLineWidth = formLineWidth;
        this.formLineDashEffect = formLineDashEffect;
        this.formColor = formColor;
    }

    
    public String label;

    
    public Legend.LegendForm form = Legend.LegendForm.DEFAULT;

    
    public float formSize = Float.NaN;

    
    public float formLineWidth = Float.NaN;

    
    public DashPathEffect formLineDashEffect = null;

    
    public int formColor = ColorTemplate.COLOR_NONE;

}