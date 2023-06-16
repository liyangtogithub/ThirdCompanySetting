package com.landsem.setting.view;

import com.landsem.setting.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("InflateParams")
public final class CustomToast extends Toast {
	
	private static CustomToast instance;
//  private static ImageView imageView ;  
    private static TextView textView;  

	public CustomToast(Context context) {
		super(context);
	}
	
    public static CustomToast makeText(Context context, int resId, CharSequence text, int duration) {  
    	CustomToast result = new CustomToast(context);  
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);   
        View layout = inflater.inflate(R.layout.custom_toast_layout, null);  
//      ImageView imageView = (ImageView) layout.findViewById(R.id.toast_icon);  
        TextView textView = (TextView) layout.findViewById(R.id.toast_info);  
//      imageView.setImageResource(resId);  
        textView.setText(text);  
        result.setView(layout);  
        result.setGravity(Gravity.CENTER_VERTICAL, 0, 0);  
        result.setDuration(duration);  
        return result;  
    } 
    
    public static CustomToast makeText(Context context, CharSequence text) {  
    	CustomToast result = new CustomToast(context);  
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);   
        View layout = inflater.inflate(R.layout.custom_toast_layout, null);  
//      ImageView imageView = (ImageView) layout.findViewById(R.id.toast_icon);  
        TextView textView = (TextView) layout.findViewById(R.id.toast_info);  
//      imageView.setImageResource(resId);  
        textView.setText(text);  
        result.setView(layout);  
        result.setGravity(Gravity.CENTER_VERTICAL, 0, 0);  
        result.setDuration(Toast.LENGTH_SHORT);  
        return result;  
    } 
    
    public static CustomToast makeText(Context context, int textId) {
    	if(null==instance){
    		instance = new CustomToast(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);   
            View layout = inflater.inflate(R.layout.custom_toast_layout, null);  
            textView = (TextView) layout.findViewById(R.id.toast_info);  
            instance.setView(layout);  
            instance.setGravity(Gravity.CENTER_VERTICAL, 0, 0);  
            instance.setDuration(Toast.LENGTH_SHORT);  
    	}
        textView.setText(textId);
        return instance;  
    } 

}
