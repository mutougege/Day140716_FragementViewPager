package com.zzw.day140716_fragementviewpager;

import java.util.ArrayList;

import android.os.Bundle;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {

	private Context mContext;

	private TextView ATab; // A的标题
	private TextView BTab; // B的标题
	private ImageView mCursorImgView; // 页卡切换指针
	private View mViewContainer;
	private ViewPager mPager;
	
	private static ArrayList<Fragment> mFragmeList;
	private AFragment mAFragment;
	private BFragment mBFragment;
	private MyAdapter mAdapter;
	
	

	private static final int TITLE_NUM = 2;
	private int offset = 0; // 动画图片偏移量
	private int bmpW; // 动画图片宽度
	private int currentPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = MainActivity.this;
		setUpViews();
	}

	private void setUpViews() {
		setContentView(R.layout.activity_main);

		mViewContainer = findViewById(R.id.viewpager_container);

		initTab(); // 初始化分页标题
		initCursor(TITLE_NUM); // 初始化分页指针
		initPager(); // 初始化分页
	}

	/**
	 * 初始化tab
	 */
	private void initTab() {
		ATab = (TextView) findViewById(R.id.left);
		ATab.setTextColor(getResources().getColor(R.color.black));

		BTab = (TextView) findViewById(R.id.right);
		
		ATab.setOnClickListener(new TextView.OnClickListener() {
			@Override
			public void onClick(View v) {
				mPager.setCurrentItem(0);
			}
		});
		BTab.setOnClickListener(new TextView.OnClickListener() {
			@Override
			public void onClick(View v) {
				mPager.setCurrentItem(1);
			}
		});
	}

	/**
	 * 初始化分页指针
	 * 
	 * @param titleNum 页卡数
	 */
	private void initCursor(int titleNum) {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels; // 获取分辨率宽度
		bmpW = dm.widthPixels / 2;
		mCursorImgView = (ImageView) findViewById(R.id.cursor);
		Bitmap cursorBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.tab_cursor2);// 获取图片宽度
		Bitmap bmp = Bitmap.createScaledBitmap(cursorBitmap, bmpW,
				cursorBitmap.getHeight(), true);
		mCursorImgView.setImageBitmap(bmp);
		offset = 0; // 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		mCursorImgView.setImageMatrix(matrix); // 设置动画初始位置
	}

	/**
	 * 初始化页卡
	 */
	private void initPager() {

		mFragmeList = new ArrayList<Fragment>();
		mAFragment = new AFragment();
		mBFragment = new BFragment();

		mFragmeList.add(mAFragment);
		mFragmeList.add(mBFragment);

		mAdapter = new MyAdapter(getSupportFragmentManager());

		mPager = (ViewPager) findViewById(R.id.viewpage);
		mPager.setAdapter(mAdapter);
		mAdapter.setOnReloadListener(new OnReloadListener(){
            @Override
            public void onReload(){
            	mAFragment.update();
            }
        });
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
		currentPager = 0;

	}

	/**
	 * 根据Pager的滑动更新指针的滑动
	 * 
	 * @param arg0 当前page
	 * @param arg1 滑动的比例
	 * @param arg2
	 */
	private void updateCursorRealTime(int arg0, float arg1, int arg2) {
		// 确定当前指针的位置，根据pager滑动的情况（arg1和arg2决定）
		int one = offset * 2 + bmpW;
		Matrix matrix = new Matrix();
		if (arg1 != 0) {
			matrix.postTranslate(offset + one * arg1, 0);
			mCursorImgView.setImageMatrix(matrix);
		}
	}

	/**
	 * 更新tab
	 * 
	 * @param arg0
	 */
	private void updateTab(int arg0) {
		switch (arg0) {
		case 0:
			ATab.setTextColor(getResources().getColor(R.color.black));
			BTab.setTextColor(getResources().getColor(
					R.color.silver_grey));
			currentPager = 0;
			break;
		case 1:
			ATab.setTextColor(getResources().getColor(
					R.color.silver_grey));
			BTab.setTextColor(getResources().getColor(R.color.black));
			currentPager = 1;
			break;
		default:
			break;
		}
	}

	/**
	 * 监听页卡切换动作，更新tab及指针状态
	 * 
	 * @author litingchang
	 * 
	 */
	class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// 实时更新指针，指针随着Pager的滑动滑动
			updateCursorRealTime(arg0, arg1, arg2);

		}

		@Override
		public void onPageSelected(int arg0) {
			// 更新title的选中状态O
			updateTab(arg0);
		}
	}

	public MyAdapter getAdapter(){
        return mAdapter;
    }
	
	/**
	 * 详情页Viewpager适配器
	 * 
	 * @author litingchang
	 * 
	 */
	public static class MyAdapter extends FragmentStatePagerAdapter{

		private OnReloadListener mListener;
		
		public MyAdapter(FragmentManager fm) {
			super(fm);
		}

		public void setOnReloadListener(OnReloadListener listener) {
			this.mListener = listener;
		}

		@Override
		public int getCount() {
			return mFragmeList.size();
		}

		@Override
		public Fragment getItem(int arg0) {
			return mFragmeList.get(arg0);
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			super.destroyItem(container, position, object);
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			return super.instantiateItem(arg0, arg1);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return super.isViewFromObject(view, object);
		}
		
		public void reLoad() {
			if(mListener != null){
	            mListener.onReload();
	        }
	        this.notifyDataSetChanged();
		}
		
	}

	
}
