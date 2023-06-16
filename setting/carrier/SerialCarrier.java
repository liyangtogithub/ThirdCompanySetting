package com.landsem.setting.carrier;

import com.landsem.common.tools.StringUtils;
import com.landsem.setting.R;
import com.landsem.setting.SettingApp;
import com.landsem.setting.utils.CreateQRImage;
import com.landsem.setting.utils.DeviceUtil;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public final class SerialCarrier extends BaseCarrier {
	
	private static final long serialVersionUID = 1807141500023677334L;
	private ImageView cpuImageView;
	private ImageView imeiImageView;
	private TextView cpuTextView;
	private TextView imeiTextView;
	private View imeiLayout;

	public SerialCarrier(LayoutInflater inflater, int resource) {
		super(inflater, resource);
		initViews(contentView);
		displaySerial();
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	protected void initViews(View convertView) {
		cpuImageView = (ImageView) convertView.findViewById(R.id.cpuserial_image);
		imeiImageView = (ImageView) convertView.findViewById(R.id.imeiserial_image);
		cpuTextView = (TextView) convertView.findViewById(R.id.cpu_serial);
		imeiTextView = (TextView) convertView.findViewById(R.id.imei_serial);
		imeiLayout = convertView.findViewById(R.id.imei_layout);
		if(!SettingApp.getConfigManager().getMobileSupportState()) imeiLayout.setVisibility(View.GONE);
	}

	@Override
	protected void initListener() {

	}

	@Override
	protected void initViewsState() {
//		String cpuSerial = DeviceUtil.getCPUSerial();
//		if(!StringUtils.isBlank(cpuSerial)){
////			cpuSerial = "1235468fgh562dfg2456fghk326rg6f8h";
//			Bitmap bitmap = CreateQRImage.createQRImage(cpuSerial);
//			microcodeImageView.setImageBitmap(bitmap);
//			cpuSerialTextView.setText(cpuSerial);
//		}
	}
	
	private void displaySerial(){
		try {
			String cpuSerial = DeviceUtil.getCPUSerial();
			String imeiSerial = DeviceUtil.getImeiSerial(SettingApp.getInstance());
			if(StringUtils.isBlank(cpuSerial)) cpuSerial = "";
			if(StringUtils.isBlank(imeiSerial)) imeiSerial = "";
			Bitmap cpuSerialBitmap = CreateQRImage.createQRImage(cpuSerial);
			Bitmap imeiSerialBitmap = CreateQRImage.createQRImage(imeiSerial);
			cpuImageView.setImageBitmap(cpuSerialBitmap);
			imeiImageView.setImageBitmap(imeiSerialBitmap);
			cpuTextView.setText(cpuSerial);
			imeiTextView.setText(imeiSerial);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
