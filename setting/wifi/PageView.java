package com.landsem.setting.wifi;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.LinearLayout;

import com.landsem.setting.R;

public class PageView extends LinearLayout implements OnClickListener {

	private final int rowCount;
	private int currentPage;
	private int currentPosition;
	private int maxPageIndex;
	private int maxCount;
	private LinearLayout content;
	private View footer;
	private Button previousBtn;
	private Button nextBtn;
	private View[] rows;
	private View mEmptyView;
	private Adapter mAdapter;

	public PageView(Context context, AttributeSet set) {
		super(context, set);
		rowCount = set.getAttributeIntValue(null, "row", 5);
		rows = new View[rowCount];
		inflate(context, R.layout.view_pageview, this);
		initViews();
	}

	private void initViews() {
		content = (LinearLayout) findViewById(R.id.content);
		footer = findViewById(R.id.footer);
		previousBtn = (Button) footer.findViewById(R.id.previous);
		previousBtn.setOnClickListener(this);
		nextBtn = (Button) footer.findViewById(R.id.next);
		nextBtn.setOnClickListener(this);
	}

	public void setAdapter(Adapter adapter) {
		mAdapter = adapter;
		reset(true);
	}

	public void notifyDatasetChanged() {
		reset(true);
	}

	@Override
	public void onClick(View v) {
		if (v == previousBtn) {
			currentPage--;
		} else if (v == nextBtn) {
			currentPage++;
		}
		reset(false);
	}

	private void reset(boolean isDataSetChanged) {
		resetCount(isDataSetChanged);
		resetFooter();
		resetContent();
	}

	private void resetCount(boolean isDataSetChanged) {
		if (isDataSetChanged) {
			maxCount = mAdapter.getCount();
			maxPageIndex = (maxCount - 1) / rowCount;
		}
		if (currentPage > maxPageIndex) {
			currentPage = maxPageIndex;
		}
		currentPosition = currentPage * rowCount;
	}

	private void resetFooter() {
		footer.setVisibility(maxPageIndex > 0 ? VISIBLE : INVISIBLE);
		nextBtn.setEnabled(currentPage < maxPageIndex);
		previousBtn.setEnabled(currentPage > 0);
	}

	private void resetContent() {
		content.setWeightSum(rowCount);
		if (mEmptyView != null) {
			mEmptyView.setVisibility(maxCount > 0 ? GONE : VISIBLE);
		}
		if (maxCount == 0) {
			View row = null;
			for (int i = 0; i < rowCount; i++) {
				row = rows[i];
				if (row != null) {
					row.setVisibility(INVISIBLE);
				}
			}
			return;
		}
		int bound = currentPosition + rowCount - 1;
		if (bound >= maxCount) {
			bound = maxCount - 1;
		}
		int rowIndex = 0;
		View oldView = null;
		View newView = null;
		for (int index = currentPosition; index <= bound; index++, rowIndex++) {
			oldView = getViewByRow(rowIndex);
			newView = mAdapter.getView(index, oldView, content);
			if (newView == null) {
				throw new NullPointerException();
			} else if (oldView != newView) {
				replaceRow(newView, oldView, rowIndex);
			}
			newView.setId((int) mAdapter.getItemId(index));
			newView.setVisibility(VISIBLE);
		}
		// if exceedRowCount is true,won't fit the below condition
		if (rowIndex < rowCount) {
			for (int index = rowIndex; index < rowCount; index++) {
				oldView = getViewByRow(index);
				if (oldView != null) {
					oldView.setId(-1);
					oldView.setVisibility(INVISIBLE);
				}
			}
		}
	}

	private View getViewByRow(int index) {
		if (index < 0 || index >= rowCount) {
			throw new IndexOutOfBoundsException();
		}
		return rows[index];
	}

	// private View createEmptyRow() {
	// View v = new View(mContext);
	// LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, 0,
	// 1);
	// v.setLayoutParams(params);
	// return v;
	// }

	private void replaceRow(View newView, View oldView, int rowIndex) {
		int index = -1;
		if (oldView != null && oldView.getParent() == content) {
			index = indexOfRow(oldView);
			content.removeView(oldView);
		}
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1);
		if (index > -1) {
			content.addView(newView, index, params);
		} else {
			content.addView(newView, params);
		}
		rows[rowIndex] = newView;
	}

	private int indexOfRow(View view) {
		if (view == null) {
			return -1;
		}
		for (int i = rowCount - 1; i > -1; i--) {
			if (view == rows[i]) {
				return i;
			}
		}
		return -1;
	}

	public void setEmptyView(View v) {
		if (mEmptyView != null && mEmptyView.getParent() == content) {
			content.removeView(mEmptyView);
		}
		mEmptyView = v;
		if (mEmptyView != null) {
			if (mEmptyView.getParent() != null) {
				ViewGroup parent = (ViewGroup) mEmptyView.getParent();
				parent.removeView(mEmptyView);
			}
			mEmptyView.setVisibility(maxCount > 0 ? GONE : VISIBLE);
			content.addView(v);
		}
	}

	public List<View> getRows() {
		ArrayList<View> result = new ArrayList<View>();
		View v = null;
		for (int i = 0; i < rowCount; i++) {
			v = rows[i];
			if (v != null && v.getVisibility() == VISIBLE) {
				result.add(v);
			}
		}
		return result;
	}

	public int getViewIndex(View view) {
		View v = null;
		for (int i = rowCount - 1; i > -1; i--) {
			v = rows[i];
			if (v != null && v == view) {
				return i;
			}
		}
		return -1;
	}

	// private int getRowCount() {
	// return exceedRowCount ? rowCount + 1 : rowCount;
	// }
}
