package rpt.tool.mementobibere.basic.appbasiclibs.mycustom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import rpt.tool.mementobibere.R;

@SuppressLint("AppCompatCustomView")
public class ShapeImageView extends ImageView
{
	Context mContext;

	private int viewWidth;
	private int viewHeight;
	private Bitmap image;

	private Paint paint;
	private Paint paintBorder;

	private BitmapShader shader;

	private boolean border=false;
	private int borderWidth = 8;
	private int borderColor = Color.BLUE;
	private int shape=0;
	private boolean shadow=false;
	private int shadowColor = Color.GRAY;
	private int shadowThickness=8;


	public ShapeImageView(Context context)
	{
		super(context);
		mContext=context;
		init(null);
	}

	public ShapeImageView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		mContext=context;
		init(attrs);
	}

	public ShapeImageView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		mContext=context;
		init(attrs);
	}

	public void init(AttributeSet attrs)
	{
		if(attrs!=null)
		{
			TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.ShapeImageView);

			shadow=typedArray.getBoolean(R.styleable.ShapeImageView_siv_shadow, shadow);
			shadowColor=typedArray.getColor(R.styleable.ShapeImageView_siv_shadow_color, shadowColor);
			shadowThickness=typedArray.getInteger(R.styleable.ShapeImageView_siv_shadow_thickness, shadowThickness);

			border=typedArray.getBoolean(R.styleable.ShapeImageView_siv_border, border);
			borderColor=typedArray.getColor(R.styleable.ShapeImageView_siv_border_color, borderColor);
			borderWidth=typedArray.getInteger(R.styleable.ShapeImageView_siv_border_width, borderWidth);

			shape=typedArray.getInteger(R.styleable.ShapeImageView_siv_shape, shape);
		}

		System.out.println("shape : "+shape);

		setup();
	}

	private void setup()
	{
		paint = new Paint();
		paint.setAntiAlias(true);

		paintBorder = new Paint();
		setBorderColor(borderColor);
		paintBorder.setAntiAlias(true);
		this.setLayerType(LAYER_TYPE_SOFTWARE, paintBorder);
	}

	public void setBorderWidth(int borderWidth)
	{
		this.borderWidth = borderWidth;
		this.invalidate();
	}

	public void setBorderColor(int borderColor)
	{
		if(paintBorder != null)
			paintBorder.setColor(borderColor);

		this.invalidate();
	}

	@NonNull
	private Bitmap getBitmapFromDrawable(@NonNull Drawable drawable)
	{
		final Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
		final Canvas canvas = new Canvas(bmp);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);
		return bmp;
	}

	private void loadBitmap()
	{
		/*BitmapDrawable bitmapDrawable = (BitmapDrawable) this.getDrawable();

		if(bitmapDrawable != null)
			image = bitmapDrawable.getBitmap();*/

		image=getBitmapFromDrawable(this.getDrawable());
	}

	@SuppressLint("DrawAllocation")
	@Override
	public void onDraw(Canvas canvas)
	{
		//load the bitmap
		loadBitmap();

		// init shader
		if(image !=null)
		{
			shader = new BitmapShader(Bitmap.createScaledBitmap(image, canvas.getWidth(), canvas.getHeight(), false), Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
			paint.setShader(shader);
			int centerX = viewWidth / 2;
            int centerY = viewHeight / 2;

			if(shadow && border)
				paintBorder.setShadowLayer(shadowThickness,0,0, shadowColor);
			else if(shadow)
				paint.setShadowLayer(shadowThickness,0,0, shadowColor);


			if(shape==0) //======Circle======
			{
				if(border)
					canvas.drawCircle(centerX + borderWidth, centerY + borderWidth, centerX + borderWidth, paintBorder);
				canvas.drawCircle(centerX + borderWidth, centerY + borderWidth, centerX, paint);
			}
            else if(shape==1)//======Square======
			{
				RectF r=new RectF(0,0,viewWidth,viewHeight);
				if(border)
					canvas.drawRect(r,paintBorder);
				RectF r2=new RectF(0+borderWidth,0+borderWidth,viewWidth-borderWidth,viewHeight-borderWidth);
				canvas.drawRect(r2,paint);
			}
			else if(shape==2)//======RoundRect======
			{
				RectF rr=new RectF(0,0,viewWidth,viewHeight);
				if(border)
					canvas.drawRoundRect(rr,20,20,paintBorder);
				RectF rr2=new RectF(0+borderWidth,0+borderWidth,viewWidth-borderWidth,viewHeight-borderWidth);
				canvas.drawRoundRect(rr2,20,20,paint);
			}
			else if(shape==3)//======ARC======
			{
				RectF ar=new RectF(0,0,viewWidth,viewHeight);
				if(border)
					canvas.drawArc(ar,180,180,true,paintBorder);
				RectF ar2=new RectF(0+borderWidth,0+borderWidth,viewWidth-borderWidth,viewHeight-borderWidth);
				canvas.drawArc(ar2,180,180,true,paint);
			}
			else if(shape==4)//======STAR======
			{
				float min = Math.min(viewWidth, viewHeight);

				float half = min / 2;

				float mid = centerX - half;

				Path spath = new Path();
				// top left
				spath.moveTo(mid + half * 0.5f, half * 0.84f);
				// top right
				spath.lineTo(mid + half * 1.5f, half * 0.84f);
				// bottom left
				spath.lineTo(mid + half * 0.68f, half * 1.45f);
				// top tip
				spath.lineTo(mid + half * 1.0f, half * 0.5f);
				// bottom right
				spath.lineTo(mid + half * 1.32f, half * 1.45f);
				if(border)
					canvas.drawPath(spath, paintBorder);

				Path spath2 = new Path();
				// top left
				spath2.moveTo(mid + half * 0.55f, half * 0.86f);
				// top right
				spath2.lineTo(mid + half * 1.45f, half * 0.86f);
				// bottom left
				spath2.lineTo(mid + half * 0.72f, half * 1.40f);
				// top tip
				spath2.lineTo(mid + half * 1.0f, half * 0.55f);
				// bottom right
				spath2.lineTo(mid + half * 1.28f, half * 1.40f);
				canvas.drawPath(spath2, paint);
			}
			else if(shape==5)//======HEART======
			{
				float width =viewWidth,height=viewHeight;

				Path hpath = new Path();

				// Starting point
				hpath.moveTo(width / 2, height / 5);

				// Upper left path
				hpath.cubicTo(5 * width / 14, 0,
						0, height / 15,
						width / 28, 2 * height / 5);

				// Lower left path
				hpath.cubicTo(width / 14, 2 * height / 3,
						3 * width / 7, 5 * height / 6,
						width / 2, height);

				// Lower right path
				hpath.cubicTo(4 * width / 7, 5 * height / 6,
						13 * width / 14, 2 * height / 3,
						27 * width / 28, 2 * height / 5);

				// Upper right path
				hpath.cubicTo(width, height / 15,
						9 * width / 14, 0,
						width / 2, height / 5);

				canvas.drawPath(hpath, paint);

				/*Path hpath2 = new Path();

				// Starting point
				hpath2.moveTo(width / 2, height / 5 + borderWidth);

				// Upper left path
				hpath2.cubicTo(5 * width / 14 + borderWidth, 0 + borderWidth,
						0 + borderWidth, height / 15,
						width / 28 + borderWidth, 2 * height / 5 + borderWidth);

				// Lower left path
				hpath2.cubicTo(width / 14 + borderWidth, 2 * height / 3 + borderWidth,
						3 * width / 7 + borderWidth*2, 5 * height / 6,
						width / 2, height - borderWidth);

				// Lower right path
				hpath2.cubicTo(4 * width / 7 - borderWidth, 5 * height / 6,
						13 * width / 14 - borderWidth*2, 2 * height / 3,
						27 * width / 28 - borderWidth, 2 * height / 5 - borderWidth);

				// Upper right path
				hpath2.cubicTo(width - borderWidth, height / 15 -borderWidth,
						9 * width / 14 - borderWidth*2, 0 -borderWidth,
						width / 2 - borderWidth, height / 5 +borderWidth);

				canvas.drawPath(hpath2, paint);*/
			}
			else if(shape==6)//======Hexagon======
			{
				Path path = new Path();
				path.moveTo(0, centerY);
				path.lineTo((viewWidth*25)/100,viewHeight);
				path.lineTo((viewWidth*75)/100,viewHeight);
				path.lineTo(viewWidth,centerY);
				path.lineTo((viewWidth*75)/100,0);
				path.lineTo((viewWidth*25)/100,0);
				if(border)
					canvas.drawPath(path, paintBorder);

				Path path2 = new Path();
				path2.moveTo(0+borderWidth, centerY);
				path2.lineTo((viewWidth*25)/100+borderWidth,viewHeight-borderWidth);
				path2.lineTo((viewWidth*75)/100-borderWidth,viewHeight-borderWidth);
				path2.lineTo(viewWidth-borderWidth,centerY);
				path2.lineTo((viewWidth*75)/100-borderWidth,0+borderWidth);
				path2.lineTo((viewWidth*25)/100+borderWidth,0+borderWidth);
				canvas.drawPath(path2, paint);
			}
			else if(shape==7)//======Octagon======
			{
				Path opath = new Path();
				opath.moveTo(0, (viewHeight*30)/100);
				opath.lineTo(0, (viewHeight*70)/100);
				opath.lineTo((viewWidth*30)/100,viewHeight);
				opath.lineTo((viewWidth*70)/100,viewHeight);
				opath.lineTo(viewWidth,(viewHeight*70)/100);
				opath.lineTo(viewWidth,(viewHeight*30)/100);
				opath.lineTo((viewWidth*70)/100,0);
				opath.lineTo((viewWidth*30)/100,0);
				if(border)
					canvas.drawPath(opath, paintBorder);

				Path opath2 = new Path();
				opath2.moveTo(0+borderWidth, (viewHeight*30)/100);
				opath2.lineTo(0+borderWidth, (viewHeight*70)/100);
				opath2.lineTo((viewWidth*30)/100+borderWidth,viewHeight-borderWidth);
				opath2.lineTo((viewWidth*70)/100-borderWidth,viewHeight-borderWidth);
				opath2.lineTo(viewWidth-borderWidth,(viewHeight*70)/100);
				opath2.lineTo(viewWidth-borderWidth,(viewHeight*30)/100);
				opath2.lineTo((viewWidth*70)/100-borderWidth,0+borderWidth);
				opath2.lineTo((viewWidth*30)/100+borderWidth,0+borderWidth);
				canvas.drawPath(opath2, paint);
			}
			else if(shape==8)//======Diamond======
			{
				Path dpath = new Path();
				dpath.moveTo(0, centerY);
				dpath.lineTo(centerX, viewHeight);
				dpath.lineTo(viewWidth,centerY);
				dpath.lineTo(centerX,0);
				if(border)
					canvas.drawPath(dpath, paintBorder);

				Path dpath2 = new Path();
				dpath2.moveTo(0+borderWidth, centerY);
				dpath2.lineTo(centerX, viewHeight-borderWidth);
				dpath2.lineTo(viewWidth-borderWidth,centerY);
				dpath2.lineTo(centerX,0+borderWidth);
					canvas.drawPath(dpath2, paint);
			}
			else if(shape==9)//======Pentagon======
			{
				Path ppath = new Path();
				ppath.moveTo(0, (viewHeight*30)/100);
				ppath.lineTo((viewWidth*15)/100,viewHeight);
				ppath.lineTo((viewWidth*85)/100,viewHeight);
				ppath.lineTo(viewWidth,(viewHeight*30)/100);
				ppath.lineTo(centerX,0);
				if(border)
					canvas.drawPath(ppath, paintBorder);

				Path ppath2 = new Path();
				ppath2.moveTo(0+borderWidth+(borderWidth/2), (viewHeight*30)/100+(borderWidth/2));
				ppath2.lineTo((viewWidth*15)/100+borderWidth,viewHeight-borderWidth);
				ppath2.lineTo((viewWidth*85)/100-borderWidth,viewHeight-borderWidth);
				ppath2.lineTo(viewWidth-borderWidth-(borderWidth/2),(viewHeight*30)/100+(borderWidth/2));
				ppath2.lineTo(centerX,0+borderWidth+(borderWidth/2));
				canvas.drawPath(ppath2, paint);
			}
			else if(shape==10)//======CUSTOM======
			{
				RectF cr=new RectF(0,0,viewWidth,viewHeight);
				if(border)
					canvas.drawArc(cr,135,270,false,paintBorder);
				RectF cr2=new RectF(0+borderWidth,0+borderWidth,viewWidth-borderWidth,viewHeight-borderWidth);
				canvas.drawArc(cr2,135,270,false,paint);
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int width = measureWidth(widthMeasureSpec);
		int height = measureHeight(heightMeasureSpec, widthMeasureSpec);

		viewWidth = width - (borderWidth *2);
		viewHeight = height - (borderWidth*2);

		setMeasuredDimension(width, height);
	}

	private int measureWidth(int measureSpec)
	{
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			// We were told how big to be
			result = specSize;
		} else {
			// Measure the text
			result = viewWidth;

		}

		return result;
	}

	private int measureHeight(int measureSpecHeight, int measureSpecWidth) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpecHeight);
		int specSize = MeasureSpec.getSize(measureSpecHeight);

		if (specMode == MeasureSpec.EXACTLY) {
			// We were told how big to be
			result = specSize;
		} else {
			// Measure the text (beware: ascent is a negative number)
			result = viewHeight;
		}
		return (result+2);
	}
}