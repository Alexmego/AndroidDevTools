1. ViewPager 设置了setPagerMargin(20) 之后，滑动几页后，横竖屏切换，发现横屏后页面发生便宜，解决方法是 重写onSizeChanged。
2. ViewPager控制是否可以左右滑动，通过setPagingEnabled进行设置
```java
public class FixedViewPager extends ViewPager {
    private boolean isPagingEnabled = true;

    public FixedViewPager(Context context) {
        super(context);
    }

    public FixedViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void setPagingEnabled(boolean b) {
        this.isPagingEnabled = b;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            return this.isPagingEnabled && super.onTouchEvent(event);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        try {
            return this.isPagingEnabled && super.onInterceptTouchEvent(event);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w - this.getPageMargin(), h, oldw - this.getPageMargin(), oldh);
    }
}

```
