package rpt.tool.mementobibere.basic.appbasiclibs.mycustom;

import static rpt.tool.mementobibere.utils.log.LogUtilsKt.d;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;

import rpt.tool.mementobibere.R;
import rpt.tool.mementobibere.basic.appbasiclibs.mycustom.justify.JustifiedTextView;


@SuppressLint("AppCompatCustomView")
public class ReadMoreTextView extends JustifiedTextView
{
	Context mContext;

	String read_more_text="Read More";
    String read_less_text="Read Less";

	String fontName="";
	int readmore_text_color=Color.BLUE;
	int max_line=1;

	String full_desc="";

	public ReadMoreTextView(Context context)
	{
		super(context);
		mContext=context;
		setup(null);
	}

	public ReadMoreTextView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		mContext=context;
		setup(attrs);
	}

	public ReadMoreTextView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		mContext=context;
		setup(attrs);
	}

	private void setup(AttributeSet attrs)
	{
		if (attrs != null)
		{
			TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ReadMoreTextView);

			fontName = a.getString(R.styleable.ReadMoreTextView_rmtv_fontname);

			read_more_text = a.getString(R.styleable.ReadMoreTextView_rmtv_read_more_text);

			if(TextUtils.isEmpty(read_more_text))
				read_more_text="Read More";

			read_less_text = a.getString(R.styleable.ReadMoreTextView_rmtv_read_less_text);

			if(TextUtils.isEmpty(read_less_text))
				read_more_text="Read Less";

			readmore_text_color = a.getInteger(R.styleable.ReadMoreTextView_rmtv_read_more_text_color,readmore_text_color);

			max_line = a.getInteger(R.styleable.ReadMoreTextView_rmtv_max_line,max_line);

			a.recycle();
		}

		this._create();
	}

	public void _create()
	{
		full_desc=getText().toString().trim();

		setTypeFace();
		String des=Html.fromHtml(getText().toString().trim()).toString();
		setText(Html.fromHtml(des));

		if(!TextUtils.isEmpty(getText().toString()))
			txt_post();

	}

	public void txt_post()
	{
		this.post(new Runnable()
		{
			@Override
			public void run()
			{
				Layout layout = getLayout();
				String text = getText().toString();
				int start = 0;
				int end;
				String line[] = new String[getLineCount()];

				d("@@@@@ : ",getLineCount()+" @@@ "+getMaxLine());
				System.out.println(getLineCount()+" @@@ "+getMaxLine());

				if(getLineCount()>getMaxLine()) {

					String str = "";

					for (int i = 0; i < getLineCount(); i++) {
						if (i > getMaxLine() - 1)
							break;

						end = layout.getLineEnd(i);
						line[i] = text.substring(start, end);
						str += line[i] + " ";

						start = end;
					}

					if (!TextUtils.isEmpty(str)) // && str.length()>(getReadMoreText().length() + 4)
					{
						if(read_more_text.contains("..."))
						{
							str = str.substring(0, str.length() - 4) + "...";
						}
						else
						{
							str = str.substring(0, str.length() - (getReadMoreText().length() + 4)) + "... " + read_more_text;
						}
					}

					setText(str);

					read_more_click();
				}

			}
		});
	}

	public void read_more_click()
	{
		clickify(this, read_more_text, new ClickSpan.OnClickListener()
		{
			@Override
			public void onClick()
			{
				setText(full_desc+" "+getReadLessText());
				read_less_click();
			}
		});
	}

	public void read_less_click()
	{
		clickify(this, read_less_text, new ClickSpan.OnClickListener()
		{
			@Override
			public void onClick()
			{
				setText(full_desc);
				txt_post();
			}
		});
	}

	public void setReadMoreText(String read_more_text)
	{
		this.read_more_text=read_more_text;
		_create();
	}

	public String getReadMoreText()
	{
		return read_more_text;
	}

	public void setReadLessText(String read_less_text)
	{
		this.read_less_text=read_less_text;
		_create();
	}

	public String getReadLessText()
	{
		return read_less_text;
	}

	public void setFont(String fontName)
	{
		this.fontName=fontName;
		_create();
	}

	public String getFont()
	{
		return fontName;
	}

	public void setMyText(@NonNull String text)
	{
		full_desc=text.trim();
		setText(text);
		_create();
	}

	public String getMyText()
	{
		return full_desc;
	}

	public void setMaxLine(int max_line)
	{
		this.max_line=max_line;
		_create();
	}

	public int getMaxLine()
	{
		return max_line;
	}

	public void setReadMoreTextColor(int readmore_text_color)
	{
		this.readmore_text_color=readmore_text_color;
		_create();
	}

	public int getReadMoreTextColor()
	{
		return readmore_text_color;
	}

	private void setTypeFace()
	{
		try
		{
			if (fontName != null)
			{
				Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), fontName);
				setTypeface(myTypeface);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}


	public void clickify(@NonNull TextView view, final String clickableText, final ClickSpan.OnClickListener listener)
	{
		CharSequence text = view.getText();
		String string = text.toString();
		ClickSpan span = new ClickSpan(listener);
		span.setTextColor(getReadMoreTextColor());

		int start = string.indexOf(clickableText);
		int end = start + clickableText.length();
		if (start == -1) return;

		if (text instanceof Spannable) {
			((Spannable)text).setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		} else {
			SpannableString s = SpannableString.valueOf(text);
			s.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			view.setText(s);
		}

		MovementMethod m = view.getMovementMethod();
		if ((m == null) || !(m instanceof LinkMovementMethod)) {
			view.setMovementMethod(LinkMovementMethod.getInstance());
		}
	}

}