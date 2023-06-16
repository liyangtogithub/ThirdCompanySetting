package com.landsem.setting.wifi;
//package com.tcl.navigator.setting.network.wifi;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.content.Context;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.LinearLayout;
//
//import com.tcl.navigator.setting.R;
//
//public class WifiTable extends LinearLayout implements OnClickListener {
//	private static final String tag = "WifiRows";
//	private final static int rowCount = 3;
//	private int position;
//	// private List<AccessPoint> data;
//	private SimpleAdapter mAdapter;
//	private List<WifiRow> rows = new ArrayList<WifiRow>(rowCount);
//	private OnItemClickListener mItemListener;
//	private View mEmptyView;
//
//	public WifiTable(Context context, SimpleAdapter adapter) {
//		super(context);
//		this.mAdapter = adapter;
//		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//		setDividerDrawable(context.getResources().getDrawable(R.drawable.list_divider));
//		setShowDividers(LinearLayout.SHOW_DIVIDER_BEGINNING|LinearLayout.SHOW_DIVIDER_MIDDLE|LinearLayout.SHOW_DIVIDER_END);
//		setOrientation(VERTICAL);
//		WifiRow row = null;
//		for (int i = 0; i < rowCount; i++) {
//			row = new WifiRow(context);
//			row.setOnClickListener(this);
//			rows.add(row);
//			addView(row);
//		}
//		refreshAll();
//	}
//
//	public int getPosition() {
//		return position;
//	}
//
//	@Override
//	public void onClick(View v) {
//		if (mItemListener == null || v.getId() == -1) {
//			return;
//		}
//		mItemListener.onItemClick((AccessPoint) mAdapter.getItem(v.getId()));
//	}
//
//	public void setPage(int pageIndex) {
//		if (pageIndex < 0) {
//			throw new IllegalArgumentException("pageIndex can only be positive");
//		}
//		setPosition(pageIndex * rowCount);
//	}
//
//	public void setPosition(int position) {
//		// int size = mAdapter.getCount();
//		// if (position < 0 || position >= size) {
//		// throw new IndexOutOfBoundsException();
//		// }
//		this.position = position;
//		refreshAll();
//	}
//
//	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
//		mItemListener = onItemClickListener;
//	}
//
//	public void refreshAll() {
//		int count = mAdapter.getCount();
//		if (mEmptyView != null) {
//			mEmptyView.setVisibility(count > 0 ? GONE : VISIBLE);
//		}
//		if (count == 0) {
//			for (WifiRow row : rows) {
//				row.setVisibility(GONE);
//			}
//			return;
//		}
//		int bound = position + rowCount;
//
//		if (bound >= count) {
//			bound = count - 1;
//		}
//		int rowIndex = 0;
//		WifiRow row = null;
//		for (int index = position; index < bound; index++, rowIndex++) {
//			row = rows.get(rowIndex);
//			row.setId(index);
//			row.setVisibility(VISIBLE);
//			row.refresh(mAdapter.getItem(index));
//		}
//		if (rowIndex < rowCount - 1) {
//			for (int i = rowIndex; i < rowCount; i++) {
//				row = rows.get(rowIndex);
//				row.setId(-1);
//				row.setVisibility(INVISIBLE);
//			}
//		}
//
//	}
//
//	public void setEmptyView(View emptyView) {
//		if (emptyView == null) {
//			mEmptyView = null;
//			return;
//		}
//		if (emptyView.getParent() != null) {
//			ViewGroup parent = (ViewGroup) emptyView.getParent();
//			parent.removeView(emptyView);
//		}
//		emptyView.setVisibility(mAdapter.getCount() > 0 ? GONE : VISIBLE);
//		addView(emptyView);
//		mEmptyView = emptyView;
//	}
//
//	public static interface OnItemClickListener {
//		void onItemClick(AccessPoint ap);
//	}
//
//	public static abstract class SimpleAdapter extends BaseAdapter {
//		@Override
//		public abstract int getCount();
//
//		@Override
//		public abstract AccessPoint getItem(int position);
//
//		@Override
//		public long getItemId(int position) {
//			return 0;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			return null;
//		}
//	}
//
//}
