package rpt.tool.mementobibere.basic.appbasiclibs.mycustom;

import static rpt.tool.mementobibere.utils.log.LogUtilsKt.d;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

import rpt.tool.mementobibere.R;


@SuppressLint("AppCompatCustomView")
public class HTMLTextView extends WebView
{
    String fontName="";
    int text_size=12;
    String text_align="justify";
    String text_align_last="left";
    boolean r2l=false;
    boolean text_bold=false;
    String language_code="";
    String text="";
    float line_height=1.5f;
    float total_height=0.0f;
    int view_line=0;
    int text_color=Color.BLACK;
    String text_color_hash="#000000";


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HTMLTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    public HTMLTextView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public HTMLTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(attrs);
    }

    public HTMLTextView(Context context)
    {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs)
    {
        if (attrs != null)
        {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.HTMLTextView);
            fontName = a.getString(R.styleable.HTMLTextView_htv_fontname);
            text = a.getString(R.styleable.HTMLTextView_htv_text);
            language_code = a.getString(R.styleable.HTMLTextView_htv_language_code);
            text_align = get_align_type(a.getInteger(R.styleable.HTMLTextView_htv_text_align,0));
            text_align_last = get_align_type(a.getInteger(R.styleable.HTMLTextView_htv_text_align_last,0));
            text_size = a.getInteger(R.styleable.HTMLTextView_htv_text_size,text_size);
            r2l=a.getBoolean(R.styleable.HTMLTextView_htv_rtl, r2l);
            text_bold=a.getBoolean(R.styleable.HTMLTextView_htv_text_bold, text_bold);
            view_line = a.getInteger(R.styleable.HTMLTextView_htv_view_line,view_line);
            text_color=a.getColor(R.styleable.HTMLTextView_htv_text_color, text_color);
            text_color_hash="#"+String.format("%X", text_color).substring(2);

            //System.out.print("text_color_hash :"+text_color_hash);

            a.recycle();
        }

        load_data();
    }

    @NonNull
    @Contract(pure = true)
    private String get_align_type(int index)
    {
        if(index==1)
            return "left";
        else if(index==2)
            return "right";
        else if(index==3)
            return "center";

        return "justify";
    }

    private void get_total_height()
    {
        total_height=view_line*line_height;
    }

    private void load_data()
    {
        try
        {
            get_total_height();

            String str_direction="";
            String str_lang_code="";
            String str_style="";

            if(view_line>0)
                str_style+="<style>p{line-height:"+line_height+"em;height:"+total_height+"em;overflow:hidden;}</style>";

            if(r2l)
                str_direction="dir=\"rtl\"";

            if(!TextUtils.isEmpty(language_code))
                str_lang_code="lang="+language_code;

            if(!TextUtils.isEmpty(fontName))
                str_style+="<style>@font-face {font-family: 'verdana';src: url('file:///android_asset/"+fontName+"');}body {font-family: 'verdana';}</style>";
//white-space: nowrap;

            String str_start="<html "+str_direction+" "+str_lang_code+">\n" +
                    " <head>"+str_style+"</head>\n" +
                    " <body style=\"text-align:"+text_align+";text-align-last:"+text_align_last+";font-size:"+text_size+"px;color:"+text_color_hash+";\">";
            String str_stop="</body></html>";

            if(text_bold)
                text="<p><b>"+text+"</b></p>";
            else
                text="<p>"+text+"</p>";

            setBackgroundColor(Color.TRANSPARENT);
            if(!TextUtils.isEmpty(text))
            {
                System.out.println("\n\n DATA START : "+str_start);
                System.out.println("\n\n DATA BODY : "+text);
                System.out.println("\n\n DATA STOP : "+str_stop);

                loadData(str_start + text + str_stop, "text/html; charset=utf-8", "utf-8");
            }

            System.out.println("DATA : "+str_start+text+str_stop);
            d("DATA : ",str_start+text+str_stop);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setFont(String fontName)
    {
        this.fontName=fontName;
        load_data();
    }

    public String getFont()
    {
        return fontName;
    }

    public void setText(String text)
    {
        this.text=text;
        load_data();
    }

    public String getText()
    {
        return text;
    }

    public void setLanguageCode(String language_code)
    {
        this.language_code=language_code;
        load_data();
    }

    public String getLanguageCode()
    {
        return language_code;
    }

    public void setTextAlign(String text_align)
    {
        this.text_align=text_align;
        load_data();
    }

    public void setTextAlign(int text_align)
    {
        this.text_align=get_align_type(text_align);
        load_data();
    }

    public String getTextAlign()
    {
        return text_align;
    }

    public void setLastLineTextAlign(int text_align_last)
    {
        this.text_align_last=get_align_type(text_align_last);
        load_data();
    }

    public String getLastLineTextAlign()
    {
        return text_align_last;
    }

    public void setTextSize(int text_size)
    {
        this.text_size=text_size;
        load_data();
    }

    public int getTextSize()
    {
        return text_size;
    }

    public void setMaxLine(int view_line)
    {
        this.view_line=view_line;
        load_data();
    }

    public int getMaxLine()
    {
        return view_line;
    }

    public void setTextColor(int text_color)
    {
        this.text_color=text_color;
        text_color_hash="#"+String.format("%X", text_color).substring(2);
        load_data();
    }

    public void setTextColor(String text_color_hash)
    {
        this.text_color_hash=text_color_hash;
        load_data();
    }

    public int getTextColor()
    {
        return text_color;
    }

    public String getTextColorHash()
    {
        return text_color_hash;
    }

    public void SetRTL(boolean r2l)
    {
        this.r2l=r2l;
        load_data();
    }
    public boolean isRTL()
    {
        return r2l;
    }

    public void setTextBold(boolean text_bold)
    {
        this.text_bold=text_bold;
        load_data();
    }

    public boolean getTextBold()
    {
        return text_bold;
    }
}
