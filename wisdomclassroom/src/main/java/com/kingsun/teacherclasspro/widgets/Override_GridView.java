package com.kingsun.teacherclasspro.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/***
 * 不可滑动GridView
 * @author hai.huang
 *
 */
public class Override_GridView extends  GridView {

	public Override_GridView(Context context) {
		super(context);
	}
	public Override_GridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public Override_GridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, mExpandSpec);
	}
}
