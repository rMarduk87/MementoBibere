package rpt.tool.mementobibere.basic.appbasiclibs.mycustom;

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
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import rpt.tool.mementobibere.R;

@SuppressLint("AppCompatCustomView")
public class ReadMoreView extends LinearLayout
{
	Context mContext;

	TextView txt_desc,txt_read_more;

	String text="Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.<br><br>";

	String read_more_text="Read More";
    String read_less_text="Read Less";

	String fontName="";
	int text_size=12;
	int text_color=Color.BLACK;
	int readmore_text_color=Color.BLUE;
	int max_line=1;

	boolean text_bold=false;

	public ReadMoreView(Context context)
	{
		super(context);
		mContext=context;
		setup(null);
	}

	public ReadMoreView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		mContext=context;
		setup(attrs);
	}

	public ReadMoreView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		mContext=context;
		setup(attrs);
	}

	private void setup(AttributeSet attrs)
	{
		if (attrs != null)
		{
			TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ReadMoreView);

			fontName = a.getString(R.styleable.ReadMoreView_rm_fontname);
			text = a.getString(R.styleable.ReadMoreView_rm_text);

			if(TextUtils.isEmpty(text))
				text="Text";

			read_more_text = a.getString(R.styleable.ReadMoreView_rm_read_more_text);

			if(TextUtils.isEmpty(read_more_text))
				read_more_text="Read More";

			read_less_text = a.getString(R.styleable.ReadMoreView_rm_read_less_text);

			if(TextUtils.isEmpty(read_less_text))
				read_more_text="Read Less";

			text_size = a.getInteger(R.styleable.ReadMoreView_rm_text_size,text_size);
			text_color=a.getColor(R.styleable.ReadMoreView_rm_text_color, text_color);
			readmore_text_color = a.getInteger(R.styleable.ReadMoreView_rm_read_more_text_color,readmore_text_color);

			text_bold=a.getBoolean(R.styleable.ReadMoreView_rm_text_bold, text_bold);
			max_line = a.getInteger(R.styleable.ReadMoreView_rm_max_line,max_line);

			a.recycle();
		}

		this._create();
	}

	public void _create()
	{
		setOrientation(LinearLayout.HORIZONTAL);
		setPadding(20,0,20,0);

		txt_desc=new TextView(mContext);
		txt_desc.setTextColor(getTextColor());
		txt_desc.setTextSize(getTextSize());
		setTypeFace(txt_desc);
		String des=Html.fromHtml(getText().trim()).toString();
		txt_desc.setText(Html.fromHtml(des));

		txt_desc.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));


		txt_read_more=new TextView(mContext);
		txt_read_more.setTextColor(getReadMoreTextColor());
		txt_read_more.setTextSize(getTextSize());
		setTypeFace(txt_read_more);
		txt_read_more.setText(getReadMoreText());


		if(text_bold)
		{
			txt_desc.setTypeface(txt_desc.getTypeface(), Typeface.BOLD);
			txt_read_more.setTypeface(txt_read_more.getTypeface(), Typeface.BOLD);
		}

		txt_desc.post(new Runnable()
		{
			@Override
			public void run()
			{
				int lineCount = txt_desc.getLineCount();

				if(lineCount<=getMaxLine())
					txt_read_more.setVisibility(View.GONE);
				else
				{
					txt_read_more.setVisibility(View.VISIBLE);

					txt_desc.setMaxLines(getMaxLine());
					if(max_line==1)
						txt_desc.setSingleLine(true);
					txt_desc.setEllipsize(TextUtils.TruncateAt.END);
				}

				//============

				/*Layout layout = txt_desc.getLayout();
				String text = txt_desc.getText().toString();
				int start=0;
				int end;
				String line[]=new String[txt_desc.getLineCount()];

				for (int i=0; i<txt_desc.getLineCount(); i++)
				{
					end = layout.getLineEnd(i);
					line[i] = text.substring(start,end);
					System.out.println("List Line "+i+" : "+text.substring(start,end));
					d("List Line "+i,""+text.substring(start,end));
					Toast.makeText(mContext,"List Line "+i+" : "+text.substring(start,end),1).show();
					start = end;
				}*/
			}
		});

		clickify_read_more_click(txt_desc,txt_read_more,getText());

		addView(txt_desc);
		addView(txt_read_more);

	}

	public void setText(String text)
	{
		this.text=text;
		_create();
	}

	public String getText()
	{
		return text;
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

	public void setTextSize(int text_size)
	{
		this.text_size=text_size;
		_create();
	}

	public int getTextSize()
	{
		return text_size;
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

	public void setTextColor(int text_color)
	{
		this.text_color=text_color;
		_create();
	}

	public int getTextColor()
	{
		return text_color;
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

	public void setTextBold(boolean text_bold)
	{
		this.text_bold=text_bold;
		_create();
	}

	public boolean getTextBold()
	{
		return text_bold;
	}



	private void setTypeFace(TextView txt)
	{
		try
		{
			if (fontName != null)
			{
				Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), fontName);
				txt.setTypeface(myTypeface);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void clickify_read_more_click(final TextView txt, @NonNull final View read_more, final String str)
	{
		read_more.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				String des=Html.fromHtml(getText().trim() + " "+getReadLessText()).toString();
				txt.setText(Html.fromHtml(des));

				txt.setMaxLines(Integer.MAX_VALUE);//your TextView
				txt.setSingleLine(false);
				txt.setEllipsize(null);
				read_more.setVisibility(View.GONE);

				clickify_read_less_click(txt,read_more,str);
			}
		});
	}

	private void clickify_read_less_click(final TextView txt,final View read_more,final String str)
	{
		clickify(txt,""+read_less_text, new ClickSpan.OnClickListener()
		{
			@Override
			public void onClick()
			{
				try
				{
					txt.setMaxLines(getMaxLine());//your TextView
					if(max_line==1)
						txt.setSingleLine(true);

					String des= Html.fromHtml(getText().trim()).toString();
					txt.setText(Html.fromHtml(des));

					read_more.setVisibility(View.VISIBLE);
					txt.setEllipsize(TextUtils.TruncateAt.END);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	private void clickify(@NonNull TextView view, final String clickableText, final ClickSpan.OnClickListener listener)
	{
		CharSequence text = view.getText();
		String string = text.toString();
		ClickSpan span = new ClickSpan(listener);
		span.setTextColor(readmore_text_color);

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