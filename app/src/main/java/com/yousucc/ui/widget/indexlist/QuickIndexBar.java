package com.yousucc.ui.widget.indexlist;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.yousucc.R;


/**
 * Created by Administrator on 2015/5/6.
 */
public class QuickIndexBar extends View {

    private Paint paint;

    private TextView header;

    private float centerx;

    private float height;

    private ListView mListView;

    private char[] indexArr = new char[]{
            '↑', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
            'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
            'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '#'};

    public void setListView(ListView mListView) {
        this.mListView = mListView;
    }

    public QuickIndexBar(Context context) {
        this(context, null);
    }

    public QuickIndexBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuickIndexBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        int textSize;
        int textColor;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.QuickIndexBar);
        textSize = typedArray.getDimensionPixelSize(R.styleable.QuickIndexBar_textSize, 30);
        textColor = typedArray.getColor(R.styleable.QuickIndexBar_textColor, Color.DKGRAY);
        typedArray.recycle();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(textSize);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (centerx == 0) {
            centerx = getWidth() / 2;
            height = getHeight() / indexArr.length;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < indexArr.length; i++) {
            canvas.drawText(indexArr[i] + "", centerx, height * i + paint.getTextSize(), paint);
        }
    }

    private int sectionForPoint(float y) {
        int index = (int) (y / height);
        if (index < 0) {
            index = 0;
        }
        if (index > indexArr.length - 1) {
            index = indexArr.length - 1;
        }
        return index;
    }

    private void setHeaderTextAndScroll(MotionEvent event) {
        if (mListView == null) {
            return;
        }
        char headchar = indexArr[sectionForPoint(event.getY())];
        header.setText(headchar + "");
        IndexerAdapter adapter;
        Adapter listAdapter = mListView.getAdapter();
        if (listAdapter instanceof HeaderViewListAdapter) {
            adapter = (IndexerAdapter) ((HeaderViewListAdapter) listAdapter).getWrappedAdapter();
        } else {
            adapter = (IndexerAdapter) listAdapter;
        }
        Object[] adapterSections = adapter.getSections();
        for (int i = 0; i < adapterSections.length; i++) {
            if (adapterSections[i].equals(headchar)) {
                mListView.setSelection(adapter.getPositionForSection(i));
                break;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (header == null) {
                    header = (TextView) ((View) getParent()).findViewById(R.id.floating_header);
                }
                setHeaderTextAndScroll(event);
                header.setVisibility(View.VISIBLE);
                setBackgroundResource(R.drawable.sidebar_background_pressed);
                return true;
            case MotionEvent.ACTION_MOVE:
                setHeaderTextAndScroll(event);
                return true;
            case MotionEvent.ACTION_UP:
                header.setVisibility(View.INVISIBLE);
                setBackgroundColor(Color.TRANSPARENT);
                return true;
            case MotionEvent.ACTION_CANCEL:
                header.setVisibility(View.INVISIBLE);
                setBackgroundColor(Color.TRANSPARENT);
                return true;
        }
        return super.onTouchEvent(event);
    }
}
