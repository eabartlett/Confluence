package com.example.confluence.profile;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.example.confluence.R;

/** 
 * This class represents the navigation layout used to provide access
 * to a user's profile page and notifications.
 * 
 * NOTE: As of 6/6/14, this class is deprecated because we didn't have 
 * time to implement notifications. 
 * @author brian
 *
 */
public class NavigationLayout extends RelativeLayout {

	public NavigationLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.navigation_view, this, true);
	}

}
