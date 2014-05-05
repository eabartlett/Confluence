package com.example.confluence.profile;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.example.confluence.R;


public class NavigationLayout extends RelativeLayout {

	public NavigationLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.navigation_view, this, true);
	}

}
