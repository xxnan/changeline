package com.vanke.changeline;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaxn on 2016/12/30.
 */

public class ChangeLine extends ViewGroup {
    private static final int LEFT=0;
    private static final int RIGHT=1;
    private int orientation=LEFT;
    public ChangeLine(Context context) {
        super(context);
//        init(context, null);
    }

    public ChangeLine(Context context, AttributeSet attrs) {
        super(context, attrs);
//        init(context, attrs);
    }

    public ChangeLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        init(context, attrs);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    /**
     * if you want use this method,you must create styles.xml and define
     *  <declare-styleable name="direction">
        <attr name="direct" format="integer" />
        </declare-styleable>
     * @param context
     * @param attrs
     */
    /*private void init(Context context, AttributeSet attrs)
    {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.direction);
        int direct = ta.getInteger(R.styleable.direction_direct, -1);
        if(direct==0)
            orientation=LEFT;
        else
            orientation=RIGHT;
        ta.recycle();
    }*/
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        //获取模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //view的宽度
        int width = 0;
        //view的高度
        int height = 0;
        //行宽
        int lineHeight = 0;
        //行高
        int lineWidth = 0;
        //子控件的个数
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            //加上下一个子view的宽度大于行宽
            if (lineWidth + childWidth > widthSize) {
                //宽度取行宽和子view宽度的最大
                width = Math.max(lineWidth, childWidth);
                //整体高度加上一行的高度
                height += lineHeight;
                lineWidth = childWidth;//开启新的一行
                lineHeight = childHeight;

            } else {
                //宽度累加
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);//取高度最大的作为行高
            }
            if (i == count - 1)//最后一个view的时候加上最后一行
            {
                width = Math.max(lineWidth, childWidth);
                height += lineHeight;
            }
        }
        setMeasuredDimension((widthMode == MeasureSpec.EXACTLY) ? widthSize : width,
                heightMode == MeasureSpec.EXACTLY ? heightSize : height);

    }

    private List<List<View>> viewList = new ArrayList<List<View>>();//
    private List<Integer> heightList = new ArrayList<Integer>();//每一行的高度

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        viewList.clear();
        heightList.clear();
        int width = getWidth();
        int lineWidth = 0;
        int lineHeight = 0;
        // 存储每一行所有的childView
        List<View> lineViews = new ArrayList<View>();
        int count = getChildCount();
        for (int j = 0; j < count; j++) {
            View child = getChildAt(j);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            if (lineWidth + childWidth > width) {
                heightList.add(lineHeight);
                lineWidth = 0;
                lineHeight = 0;
                viewList.add(lineViews);
                lineViews = new ArrayList<>();
            }
            lineWidth += childWidth;
            lineHeight = Math.max(childHeight, lineHeight);
            lineViews.add(child);
        }
        heightList.add(lineHeight);
        viewList.add(lineViews);
        if(orientation==LEFT)
        childFristLeftLayout(heightList, viewList);
        else
        childFristRightLayout(heightList, viewList);
    }

    /**
     * begin left
     * @param heightList
     * @param viewList
     */
    private void childFristLeftLayout(List<Integer> heightList, List<List<View>> viewList) {
        int left=0;
        int top=0;
        int width = getWidth();
        List<View> lineViews = new ArrayList<View>();
        int lineHeight;
        int num=viewList.size();
        for(int k=0;k<num;k++)
        {
            lineViews=viewList.get(k);
            lineHeight = heightList.get(k);
            int lineNum=lineViews.size();
            for(int t=0;t<lineNum;t++)
            {
                View child=lineViews.get(t);
                if(child.getVisibility()==View.GONE)
                    continue;
                MarginLayoutParams mlp= (MarginLayoutParams) child.getLayoutParams();
                int lc=left+mlp.leftMargin;
                int rc=lc+child.getMeasuredWidth();
                int tc=top+mlp.topMargin;
                int bc=tc+child.getMeasuredHeight();
                child.layout(lc,tc,rc,bc);
                left+=child.getMeasuredWidth()+mlp.leftMargin+mlp.rightMargin;
            }
            left = 0;
            //高度累加
            top+=lineHeight;
        }
    }

    /**
     * begin right
     * @param heightList
     * @param viewList
     */
    private void childFristRightLayout(List<Integer> heightList, List<List<View>> viewList) {
        int top = 0;
        int right = 0;
        int width = getWidth();
        List<View> lineViews = new ArrayList<View>();
        int lineHeight;
        int num = viewList.size();
        for (int k = 0; k < num; k++) {
            lineViews = viewList.get(k);
            lineHeight = heightList.get(k);
            int lineNum = lineViews.size();
            for (int t = lineNum - 1; t >= 0; t--) {
                View child = lineViews.get(t);
                if (child.getVisibility() == View.GONE)
                    continue;
                MarginLayoutParams mlp = (MarginLayoutParams) child.getLayoutParams();
                int lc = width - (right + mlp.rightMargin + child.getMeasuredWidth());
                int rc = lc + child.getMeasuredWidth();
                int tc = top + mlp.topMargin;
                int bc = tc + child.getMeasuredHeight();
                child.layout(lc, tc, rc, bc);
                right += child.getMeasuredWidth() + mlp.leftMargin + mlp.rightMargin;


            }
            right = 0;
            //高度累加
            top += lineHeight;
        }
    }
}
