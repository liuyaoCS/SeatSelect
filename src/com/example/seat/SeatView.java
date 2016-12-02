package com.example.seat;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

@SuppressLint("DrawAllocation")
public class SeatView extends View{
	Context mContext;
	Bitmap seat_ok;
	Bitmap seat_selled;
	Bitmap seat_select;
	Bitmap seat_null;
	
	int seat_size,maxSize,minSize;
	int row,col;
	int PRICE,sum=0;
	int SCROLL_COUNT=0;
	double zoom=1,zoom_saved=1;
	
	private List<Integer> mySeatList;// 保存选中座位
	private List<Integer> unavaliableSeatList;
	Bitmap SeatOk, SeatSelled, SeatSelect, SeatNull;
	private boolean isAllowLayout;

	public SeatView(Context context) {
		super(context);
		mContext=context;
		mySeatList = new ArrayList<Integer>();
		unavaliableSeatList = new ArrayList<Integer>();
		
		col = MovieSelectSeatActivity.COL;
		row = MovieSelectSeatActivity.ROW;
		PRICE = MovieSelectSeatActivity.PRICE;
		seat_size = MovieSelectSeatActivity.SIZE;
		maxSize = MovieSelectSeatActivity.MAXSIZE;
		minSize = MovieSelectSeatActivity.MINSIZE;

		SeatOk = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.seat_ok);
		SeatSelled = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.seat_selled);
		SeatSelect = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.seat_select);
		SeatNull = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.seat_null);
		
	}

	public SeatView(Context context, AttributeSet attr) {
		super(context, attr);
		mContext=context;
		mySeatList = new ArrayList<Integer>();
		unavaliableSeatList = new ArrayList<Integer>();
		
		col = MovieSelectSeatActivity.COL;
		row = MovieSelectSeatActivity.ROW;
		PRICE = MovieSelectSeatActivity.PRICE;
		seat_size = MovieSelectSeatActivity.SIZE;
		maxSize = MovieSelectSeatActivity.MAXSIZE;
		minSize = MovieSelectSeatActivity.MINSIZE;

		SeatOk = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.seat_ok);
		SeatSelled = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.seat_selled);
		SeatSelect = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.seat_select);
		SeatNull = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.seat_null);
		
	}
	
	

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(seat_size * col, seat_size * row);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		seat_size = (int) ( seat_size*zoom);
		seat_size = (int) (seat_size*zoom);
		
		if(seat_size<minSize){
			seat_size=minSize;
			seat_size=minSize;
		}
		if(seat_size>maxSize){
			seat_size=maxSize;
			seat_size=maxSize;
		}
		
		// 可购买座位
		seat_ok = Bitmap.createScaledBitmap(SeatOk, seat_size, seat_size, true);		
		// 红色 已售座位
		seat_selled = Bitmap.createScaledBitmap(SeatSelled, seat_size,
				seat_size, true);
		// 绿色 我的选择
		seat_select = Bitmap.createScaledBitmap(SeatSelect, seat_size,
				seat_size, true);
		// 没座位 替换成透明图
		seat_null = Bitmap.createScaledBitmap(SeatNull, seat_size, seat_size,
				true);
		
		
		// 画座位
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				 if (i == 10 || j == 13) {
						canvas.drawBitmap(seat_null, j * (seat_size),i * (seat_size), null);								
						unavaliableSeatList.add(i * col + j);
					}else if (j==10) {
						canvas.drawBitmap(seat_selled, j * (seat_size), i* (seat_size), null);									
						unavaliableSeatList.add(i * col + j);
				}else 	canvas.drawBitmap(seat_ok, j * (seat_size), i * (seat_size), null);
				
			}
		}
		// 我的座位 变成绿色
		for (int i = 0; i < mySeatList.size(); i++) {
			canvas.drawBitmap(seat_select,
					(mySeatList.get(i) % col) * seat_size,
					(mySeatList.get(i) / col) * seat_size, null);
		}					
	}

	int mode = 0;
	float oldDist;
	float newDist;
	int oldClick;
	int newClick;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_CANCEL:
	        Log.d("ly", "ACTION_CANCEL");
	        break;
		case MotionEvent.ACTION_DOWN:
			Log.i("ly", "ACTION_DOWN");
			mode = 1;
			oldClick = getClickPoint(event);
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			Log.i("ly", "ACTION_UP");
			invalidate();
			mode = 0;
			newClick = getClickPoint(event);
			if (newClick == oldClick) {
				if (mySeatList.contains(newClick)) {
					mySeatList.remove(mySeatList.indexOf(newClick));					
					sum-=PRICE;
					OnChangeListener.costChange(sum);					
					invalidate();
				} else if (!unavaliableSeatList.contains(newClick)) {
					mySeatList.add(newClick);
					int[] point=getClickPointF(event);
					Toast.makeText(mContext, "您选择了"+point[0]+"行"+point[1]+"列", Toast.LENGTH_SHORT).show();
					sum+=PRICE;
					OnChangeListener.costChange(sum);
					invalidate();
				}
			}
			break;
		case MotionEvent.ACTION_POINTER_UP:
			Log.i("ly", "ACTION_POINTER_UP");
			mode -= 1;
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			Log.i("ly", "ACTION_POINTER_DOWN");
			oldDist = spacing(event);
			mode += 1;
			break;

		case MotionEvent.ACTION_MOVE:
			if (mode >= 2) {
				this.getParent().requestDisallowInterceptTouchEvent(true); 
				float newDist = spacing(event);
				if (newDist > oldDist) {
					zoom=newDist / oldDist;
					oldDist = newDist;					
					Log.i("ly", "ACTION_ACTION_MOVE>=2::zoom="+zoom);
					
					OnChangeListener.ZoomChange(seat_size);
					
					this.requestLayout();
					invalidate();
				}
				if (newDist < oldDist ) {
					zoom=newDist / oldDist;
					oldDist = newDist;
					Log.i("ly", "ACTION_ACTION_MOVE>=2::zoom="+zoom);
					
					OnChangeListener.ZoomChange(seat_size);
										
					this.requestLayout();
					invalidate();
				}
				
			}else this.getParent().requestDisallowInterceptTouchEvent(false); 
			break;
		}
		return true;
	}

	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	private int getClickPoint(MotionEvent event) {
		float currentXPosition = event.getX();
		float currentYPosition = event.getY();
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				if ((j * seat_size) < currentXPosition
						&& currentXPosition < j * seat_size + seat_size
						&& (i * seat_size) < currentYPosition
						&& currentYPosition < i * seat_size + seat_size) {
					return i * col + j;
				}
			}
		}
		return 0;
	}
	private int[] getClickPointF(MotionEvent event) {
		int[] point=new int[2];
		float currentXPosition = event.getX();
		float currentYPosition = event.getY();
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				if ((j * seat_size) < currentXPosition
						&& currentXPosition < j * seat_size + seat_size
						&& (i * seat_size) < currentYPosition
						&& currentYPosition < i * seat_size + seat_size) {
					point[0]=i;
					point[1]=j;
					return point;
				}
			}
		}
		return point;
	}


	public interface ChangeListener {
		public void ZoomChange(int seat_size);
		public void costChange(int sum);
	}

	private ChangeListener OnChangeListener = null;

	public void setChangeListener(ChangeListener listener) {
		OnChangeListener = listener;
	}
}
