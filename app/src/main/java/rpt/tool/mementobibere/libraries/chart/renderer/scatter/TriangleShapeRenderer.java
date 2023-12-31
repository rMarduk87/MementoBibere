package rpt.tool.mementobibere.libraries.chart.renderer.scatter;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import rpt.tool.mementobibere.libraries.chart.interfaces.datasets.IScatterDataSet;
import rpt.tool.mementobibere.libraries.chart.utils.ColorTemplate;
import rpt.tool.mementobibere.libraries.chart.utils.Utils;
import rpt.tool.mementobibere.libraries.chart.utils.ViewPortHandler;


public class TriangleShapeRenderer implements IShapeRenderer
{

    protected Path mTrianglePathBuffer = new Path();

    @Override
    public void renderShape(Canvas c, IScatterDataSet dataSet, ViewPortHandler viewPortHandler,
                            float posX, float posY, Paint renderPaint) {

        final float shapeSize = dataSet.getScatterShapeSize();
        final float shapeHalf = shapeSize / 2f;
        final float shapeHoleSizeHalf = Utils.convertDpToPixel(dataSet.getScatterShapeHoleRadius());
        final float shapeHoleSize = shapeHoleSizeHalf * 2.f;
        final float shapeStrokeSize = (shapeSize - shapeHoleSize) / 2.f;

        final int shapeHoleColor = dataSet.getScatterShapeHoleColor();

        renderPaint.setStyle(Paint.Style.FILL);

        // create a triangle path
        Path tri = mTrianglePathBuffer;
        tri.reset();

        tri.moveTo(posX, posY - shapeHalf);
        tri.lineTo(posX + shapeHalf, posY + shapeHalf);
        tri.lineTo(posX - shapeHalf, posY + shapeHalf);

        if (shapeSize > 0.0) {
            tri.lineTo(posX, posY - shapeHalf);

            tri.moveTo(posX - shapeHalf + shapeStrokeSize,
                    posY + shapeHalf - shapeStrokeSize);
            tri.lineTo(posX + shapeHalf - shapeStrokeSize,
                    posY + shapeHalf - shapeStrokeSize);
            tri.lineTo(posX,
                    posY - shapeHalf + shapeStrokeSize);
            tri.lineTo(posX - shapeHalf + shapeStrokeSize,
                    posY + shapeHalf - shapeStrokeSize);
        }

        tri.close();

        c.drawPath(tri, renderPaint);
        tri.reset();

        if (shapeSize > 0.0 &&
                shapeHoleColor != ColorTemplate.COLOR_NONE) {

            renderPaint.setColor(shapeHoleColor);

            tri.moveTo(posX,
                    posY - shapeHalf + shapeStrokeSize);
            tri.lineTo(posX + shapeHalf - shapeStrokeSize,
                    posY + shapeHalf - shapeStrokeSize);
            tri.lineTo(posX - shapeHalf + shapeStrokeSize,
                    posY + shapeHalf - shapeStrokeSize);
            tri.close();

            c.drawPath(tri, renderPaint);
            tri.reset();
        }

    }

}