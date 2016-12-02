package com.example.seat;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.seat.SeatView.ChangeListener;

public class MovieSelectSeatActivity extends Activity {

	public static int ROW =15;
	public static int COL = 25;	
	public static int SIZE=30;
	public static int MAXSIZE=65;
	public static int MINSIZE=20;	
	public static int PRICE=50;
	public static String screenText="17.5今典花园" + "A厅" + " 荧幕";
 
	TextView screenTextView;
	LinearLayout seatRowView;
	SeatView seatView;
	Button submit_seat_btn;
	TextView cost;
	
	List<Integer> buySeatList = new ArrayList<Integer>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_movie_select_seat);
		initView();
	}

	private void initView() {
		cost=(TextView) this.findViewById(R.id.cost);
		submit_seat_btn=(Button) this.findViewById(R.id.submit_seat_btn);
		submit_seat_btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		screenTextView = (TextView) findViewById(R.id.screenText);
		screenTextView.setText(screenText);
		
		seatRowView = (LinearLayout) findViewById(R.id.seatraw);
		for (int i = 0; i < ROW; i++) {
			TextView imgView = new TextView(
					MovieSelectSeatActivity.this);
			imgView.setBackgroundResource(R.drawable.ticket);
			imgView.setText(i+1+"");
			imgView.setTextSize(9);
			imgView.setGravity(Gravity.CENTER);
			imgView.setLayoutParams(new LayoutParams(
					SIZE, SIZE));

			seatRowView.addView(imgView);
		}

		seatView=(SeatView) this.findViewById(R.id.seatview);
		seatView.setChangeListener(new ChangeListener() {
			public void ZoomChange(int seat_size) {
				// TODO Auto-generated method stub
				
				seatRowView.removeAllViews();
				for (int i = 0; i < ROW; i++) {
					TextView imgView = new TextView(
							MovieSelectSeatActivity.this);
					imgView.setBackgroundResource(R.drawable.ticket);
					imgView.setText(i+1+"");
					imgView.setTextSize(9*seat_size/SIZE);
					imgView.setGravity(Gravity.CENTER);
					imgView.setLayoutParams(new LayoutParams(
							seat_size, seat_size));

					seatRowView.addView(imgView);
				}
			}

			@Override
			public void costChange(int sum) {
				// TODO Auto-generated method stub
				cost.setText("￥"+sum+"元");
			}
		});
	}

}
