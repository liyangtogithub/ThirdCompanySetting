package com.landsem.setting.carrier;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import com.landsem.setting.R;
import com.landsem.setting.SettingApp;
import com.landsem.setting.activities.MainActivity;
import com.landsem.setting.entity.Language;
import android.app.Activity;
import android.app.ActivityManagerNative;
import android.app.IActivityManager;
import android.app.backup.BackupManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class LanguageCarrier extends BaseCarrier implements OnCheckedChangeListener {

	private static final long serialVersionUID = -4829820993292533856L;
	private List<Language> languageList = new ArrayList<Language>();
	private RadioGroup languageFormat;
	private RadioButton zhCNButton;
	private RadioButton zhrTWButton;
	private RadioButton enrUSButton;
	private Context context;
	private int checkedId;
	private Activity activity;

	public LanguageCarrier(LayoutInflater inflater, int resource) {
		super(inflater, resource);
		context = SettingApp.getContext();
		initLanguageList();
		initViews(contentView);
		initListener();

	}
	
	public void setActivity(Activity activity) {
		this.activity = activity;
	}



	private void initLanguageList() {
		languageList.add(new Language(R.id.zh_cn, "zh_cn", new Locale("zh", "cn")));
		languageList.add(new Language(R.id.zh_rtw, "zh_tw", new Locale("zh", "tw")));
		languageList.add(new Language(R.id.en_rus, "en_us", new Locale("en", "us")));
	}

	@Override
	protected void initViews(View convertView) {
		languageFormat = (RadioGroup) convertView.findViewById(R.id.language_format);
		zhCNButton = (RadioButton) convertView.findViewById(R.id.zh_cn);
		zhrTWButton = (RadioButton) convertView.findViewById(R.id.zh_rtw);
		enrUSButton = (RadioButton) convertView.findViewById(R.id.en_rus);
		switch (SettingApp.CLIENT_ID) {
		case ClientId.ID_HCSJ:
			zhrTWButton.setVisibility(View.GONE);
			enrUSButton.setVisibility(View.GONE);
			View zhRtwSplit = convertView.findViewById(R.id.zh_rtw_split);
			View enRwsSplit = convertView.findViewById(R.id.en_rus_split);
			if(null!=zhRtwSplit) zhRtwSplit.setVisibility(View.GONE);
			if(null!=enRwsSplit) enRwsSplit.setVisibility(View.GONE);
			break;

		default:
			break;
		}

		initViewsState();
	}

	@Override
	protected void initListener() {
		languageFormat.setOnCheckedChangeListener(this);
	}

	@Override
	protected void initViewsState() {
		Configuration conf = context.getResources().getConfiguration();
		String languageLabl = conf.locale.getLanguage() + "_"+ conf.locale.getCountry();
		for (Language language : languageList) {
			if (language.appLable.equalsIgnoreCase(languageLabl)) {
				checkedId = language.resource;
				break;
			}
		}
		doChangeLanguage(checkedId);
	}

	private void doChangeLanguage(int checkedId) {
		switch (checkedId) {
		case R.id.zh_cn:
			zhCNButton.setChecked(true);
			break;
		case R.id.zh_rtw:
			zhrTWButton.setChecked(true);
			break;
		case R.id.en_rus:
			enrUSButton.setChecked(true);
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View view) {

	}

	@Override
	public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
		Locale locale = null;
		for (Language language : languageList) {
			if (language.resource == checkedId) {
				locale = language.locale;
				checkedId = language.resource;
				break;
			}
		}
		try {
			IActivityManager mIActivityManager = ActivityManagerNative.getDefault();
			Configuration configuration = mIActivityManager.getConfiguration();
			configuration.locale = locale;
			configuration.userSetLocale = true;
			MainActivity.changeLanguage = true;
			mIActivityManager.updateConfiguration(configuration);
			BackupManager.dataChanged("com.android.providers.settings");
			if(null!=activity) activity.finish();
			MainActivity.localeChange = true;
			Intent intent = new Intent(SettingApp.getInstance(), MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			SettingApp.getInstance().startActivity(intent);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

}
