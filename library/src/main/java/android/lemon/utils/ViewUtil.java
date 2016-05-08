/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.lemon.utils;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

// TODO: Auto-generated Javadoc
/**
 * © 2012 amsoft.cn 名称：AbViewUtil.java 描述：View工具类.
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-01-17 下午11:52:13
 */

public class ViewUtil {

	/**
	 * 无效值
	 */
	public static final int INVALID = Integer.MIN_VALUE;

	/**
	 * 描述：重置AbsListView的高度. item 的最外层布局要用
	 * RelativeLayout,如果计算的不准，就为RelativeLayout指定一个高度
	 * 
	 * @param absListView
	 *            the abs list view
	 * @param lineNumber
	 *            每行几个 ListView一行一个item
	 * @param verticalSpace
	 *            the vertical space
	 */
	public static void setAbsListViewHeight(AbsListView absListView, int lineNumber, int verticalSpace) {

		int totalHeight = getAbsListViewHeight(absListView, lineNumber, verticalSpace);
		ViewGroup.LayoutParams params = absListView.getLayoutParams();
		params.height = totalHeight;
		((MarginLayoutParams) params).setMargins(0, 0, 0, 0);
		absListView.setLayoutParams(params);
	}

	/**
	 * 得到屏幕宽度
	 * 
	 * @param context
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static int getScreenWidth(Context context) {
		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getWidth();
	}

	/**
	 * 得到屏幕高度
	 * 
	 * @param context
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static int getScreenHeight(Context context) {
		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getHeight();
	}

	/**
	 * 描述：获取AbsListView的高度.
	 *
	 * @param absListView
	 *            the abs list view
	 * @param lineNumber
	 *            每行几个 ListView一行一个item
	 * @param verticalSpace
	 *            the vertical space
	 * @return the abs list view height
	 */
	public static int getAbsListViewHeight(AbsListView absListView, int lineNumber, int verticalSpace) {
		int totalHeight = 0;
		int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		absListView.measure(w, h);
		ListAdapter mListAdapter = absListView.getAdapter();
		if (mListAdapter == null) {
			return totalHeight;
		}

		int count = mListAdapter.getCount();
		if (absListView instanceof ListView) {
			for (int i = 0; i < count; i++) {
				View listItem = mListAdapter.getView(i, null, absListView);
				listItem.measure(w, h);
				totalHeight += listItem.getMeasuredHeight();
			}
			if (count == 0) {
				totalHeight = verticalSpace;
			} else {
				totalHeight = totalHeight + (((ListView) absListView).getDividerHeight() * (count - 1));
			}

		} else if (absListView instanceof GridView) {
			int remain = count % lineNumber;
			if (remain > 0) {
				remain = 1;
			}
			if (mListAdapter.getCount() == 0) {
				totalHeight = verticalSpace;
			} else {
				View listItem = mListAdapter.getView(0, null, absListView);
				listItem.measure(w, h);
				int line = count / lineNumber + remain;
				totalHeight = line * listItem.getMeasuredHeight() + (line - 1) * verticalSpace;
			}

		}
		return totalHeight;

	}

	/**
	 * 测量这个view 最后通过getMeasuredWidth()获取宽度和高度.
	 * 
	 * @param view
	 *            要测量的view
	 * @return 测量过的view
	 */
	public static void measureView(View view) {
		ViewGroup.LayoutParams p = view.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		view.measure(childWidthSpec, childHeightSpec);
	}

	/**
	 * 获得这个View的宽度 测量这个view，最后通过getMeasuredWidth()获取宽度.
	 * 
	 * @param view
	 *            要测量的view
	 * @return 测量过的view的宽度
	 */
	public static int getViewWidth(View view) {
		measureView(view);
		return view.getMeasuredWidth();
	}

	/**
	 * 获得这个View的高度 测量这个view，最后通过getMeasuredHeight()获取高度.
	 * 
	 * @param view
	 *            要测量的view
	 * @return 测量过的view的高度
	 */
	public static int getViewHeight(View view) {
		measureView(view);
		return view.getMeasuredHeight();
	}

	/**
	 * 从父亲布局中移除自己
	 * 
	 * @param v
	 */
	public static void removeSelfFromParent(View v) {
		ViewParent parent = v.getParent();
		if (parent != null) {
			if (parent instanceof ViewGroup) {
				((ViewGroup) parent).removeView(v);
			}
		}
	}

	/**
	 * 描述：dip转换为px.
	 *
	 * @param context
	 *            the context
	 * @param dipValue
	 *            the dip value
	 * @return px值
	 */
	public static float dip2px(Context context, float dipValue) {
		DisplayMetrics mDisplayMetrics = AppUtils.getDisplayMetrics(context);
		return applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, mDisplayMetrics);
	}

	/**
	 * 描述：px转换为dip.
	 *
	 * @param context
	 *            the context
	 * @param pxValue
	 *            the px value
	 * @return dip值
	 */
	public static float px2dip(Context context, float pxValue) {
		DisplayMetrics mDisplayMetrics = AppUtils.getDisplayMetrics(context);
		return pxValue / mDisplayMetrics.density;
	}

	/**
	 * 描述：sp转换为px.
	 *
	 * @param context
	 *            the context
	 * @param spValue
	 *            the sp value
	 * @return sp值
	 */
	public static float sp2px(Context context, float spValue) {
		DisplayMetrics mDisplayMetrics = AppUtils.getDisplayMetrics(context);
		return applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, mDisplayMetrics);
	}

	/**
	 * 描述：px转换为sp.
	 *
	 * @param context
	 *            the context
	 * @param spValue
	 *            the sp value
	 * @return sp值
	 */
	public static float px2sp(Context context, float pxValue) {
		DisplayMetrics mDisplayMetrics = AppUtils.getDisplayMetrics(context);
		return pxValue / mDisplayMetrics.scaledDensity;
	}




	/**
	 * TypedValue官方源码中的算法，任意单位转换为PX单位
	 * 
	 * @param unit
	 *            TypedValue.COMPLEX_UNIT_DIP
	 * @param value
	 *            对应单位的值
	 * @param metrics
	 *            密度
	 * @return px值
	 */
	public static float applyDimension(int unit, float value, DisplayMetrics metrics) {
		switch (unit) {
		case TypedValue.COMPLEX_UNIT_PX:
			return value;
		case TypedValue.COMPLEX_UNIT_DIP:
			return value * metrics.density;
		case TypedValue.COMPLEX_UNIT_SP:
			return value * metrics.scaledDensity;
		case TypedValue.COMPLEX_UNIT_PT:
			return value * metrics.xdpi * (1.0f / 72);
		case TypedValue.COMPLEX_UNIT_IN:
			return value * metrics.xdpi;
		case TypedValue.COMPLEX_UNIT_MM:
			return value * metrics.xdpi * (1.0f / 25.4f);
		}
		return 0;
	}

	/**
	 * Add an OnGlobalLayoutListener for the view.
	 * This is just a convenience method for using {@code ViewTreeObserver.OnGlobalLayoutListener()}.
	 * This also handles removing listener when onGlobalLayout is called.
	 *
	 * @param view     the target view to add global layout listener
	 * @param runnable runnable to be executed after the view is laid out
	 */
	public static void addOnGlobalLayoutListener(final View view, final Runnable runnable) {
		ViewTreeObserver vto = view.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
					view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				} else {
					view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
				}
				runnable.run();
			}
		});
	}




}
