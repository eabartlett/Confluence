package com.example.confluence.answers;

import java.util.ArrayList;

import com.example.confluence.dbtypes.Answer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class AnswerArrayAdapter extends ArrayAdapter<Answer> {

	public AnswerArrayAdapter(Context context, int resource, ArrayList<Answer> arrayList) {
		super(context, resource, arrayList);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Answer answer = (Answer) getItem(position);
		return new AnswerLayout(getContext(), answer);
	}
	

}
