package snowmada.main.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class Sliding extends LinearLayout {
	private Paint innerPaint, borderPaint;

	public Sliding(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public Sliding(Context context) {
		super(context);
		init();
	}

	private void init() {
		innerPaint = new Paint();
		innerPaint.setARGB(225, 75, 75, 75); // gray
		//innerPaint.setARGB(0, 0, 0, 0);
		innerPaint.setAntiAlias(true);
		borderPaint = new Paint();
		borderPaint.setARGB(0, 0, 0, 0);
		borderPaint.setAntiAlias(true);
		borderPaint.setStyle(Style.STROKE);
		//borderPaint.setStrokeWidth(2);
		borderPaint.setStrokeWidth(0);

	}

	public void setInnerPaint(Paint innerPaint) {
		this.innerPaint = innerPaint;
	}

	public void setBorderPaint(Paint borderPaint) {
		this.borderPaint = borderPaint;
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		RectF drawRect = new RectF();
		drawRect.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
		canvas.drawRoundRect(drawRect, 5, 5, innerPaint);
		canvas.drawRoundRect(drawRect, 5, 5, borderPaint);
		super.dispatchDraw(canvas);
	}
}