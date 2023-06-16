package com.landsem.setting.activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.landsem.common.tools.FileUtils;
import com.landsem.common.tools.ListUtils;
import com.landsem.common.tools.LogManager;
import com.landsem.common.tools.PreferencesUtils;
import com.landsem.common.tools.StringUtils;
import com.landsem.setting.Constant.ClientId;
import com.landsem.setting.Constant.Key;
import com.landsem.setting.R;
import com.landsem.setting.SettingApp;
import com.landsem.setting.adapter.CustomBaseAdapter;
import com.landsem.setting.upgrade.IOUtils;
import com.ls.config.ConfigManager;
import com.ls.config.ConfigManager.LS_CONFIG_ID;

@SuppressLint({ "HandlerLeak", "UseSparseArrays", "ShowToast" })
public class CarLogoSelect extends Activity implements OnItemClickListener {

	private static final long serialVersionUID = 1L;
	private static final String TAG = CarLogoSelect.class.getSimpleName();
	private static final int RESULT_OK = 0;
	private static final int LOGO_PADDING = 5;
	private static final String LOGO_PATH = "/system/media/";
	private static final String LOGO_PATH_BOOT_STRAP = "/dev/block/by-name/lsdata";
	public static final String NAME_ZIP = "initlogo";
	public static final String PATH_LOGO_ZIP = LOGO_PATH + NAME_ZIP + ".zip";
	// system/media/initlogo.zip
	private List<String> logoImagesPath = new ArrayList<String>();
	private int currentId = 0;
	private Handler handler;
	private GridView mGridView;
	private TextView mTextViewCarlogoNone;
	private ImageView carlogo_IV;
	private ImageAdapter mImageAdapter;
	private ConfigManager configmanager;
	private LogoLoadingTask LogoLoadingTask;
//	private int normalColor = Color.rgb(0x80, 0x80, 0x80);
//	private int selectColor = Color.rgb(0x38, 0xCC, 0xFC);
	private String logoName;
	private ProgressBar loadLogoProgressBar;
	private Map<String, Integer> logoIdMap = new HashMap<String, Integer>();
	private int screenWidth;
	private int screenHeight;
	private int WRAP_CONTENT = LayoutParams.WRAP_CONTENT;
	Handler stopHandle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.carlogo_layout);
		initUI();
		
		LogoLoadingTask = new LogoLoadingTask();
		handler = new LoadLogoHandler();
		loadLogo();
		Message msg = handler.obtainMessage();
		handler.sendMessageDelayed(msg, 500);
	}

	private void initUI() {
		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
		screenWidth = mDisplayMetrics.widthPixels;
		screenHeight = mDisplayMetrics.heightPixels;
		LogManager.d(TAG, "screenWidth :  " + screenWidth
				+ "       screenHeight :  " + screenHeight);
		if (screenWidth == 666 && screenHeight == 400)
			WRAP_CONTENT = 165;
		LogManager.d(TAG, "WRAP_CONTENT :   " + WRAP_CONTENT);
		mGridView = (GridView) findViewById(R.id.carlogo_gallery);
		mTextViewCarlogoNone = (TextView) findViewById(R.id.carlogo_none);
		loadLogoProgressBar = (ProgressBar) findViewById(R.id.load_logo);
		carlogo_IV= (ImageView) findViewById(R.id.carlogo_IV);
		mImageAdapter = new ImageAdapter(this, logoImagesPath);
		configmanager = new ConfigManager(this.getApplicationContext());
		mGridView.setAdapter(mImageAdapter);
		mGridView.setOnItemClickListener(this);
		if (SettingApp.CLIENT_ID == ClientId.SUO_HANG) {
			logoName = PreferencesUtils.getString(getApplicationContext(),
					Key.LOGO_NAME, "qijing.bmp");
		} else {
			logoName = PreferencesUtils.getString(getApplicationContext(),
					Key.LOGO_NAME, "nitlogo.bmp");
		}
		LogManager.d(TAG, "logoName :  " + logoName);
	}

	/**
	 * 把此名字的图片从压缩包里，读到外部默认路径下
	 */
	private class confirmLogo extends Thread {
		public void run() {
			if (!ListUtils.isEmpty(logoImagesPath)) {
				String logoName = logoImagesPath.get(currentId);
				// 把此名字的图片从压缩包里，读到外部路径下
				try {
					takeOutPhotoFromZip(logoName);
				} catch (Exception e) {
					e.printStackTrace();
				}
				LogManager.d(TAG, "logoName :   " + logoName);
				int resultCode = copyLogoImages(getMyPhotoPath());
				if (resultCode == RESULT_OK) {
					PreferencesUtils.putString(getApplicationContext(),
							Key.LOGO_NAME, logoImagesPath.get(currentId));
				}
				LogManager.d(TAG, "logoName :   " + logoName
						+ "         resultCode:   " + resultCode);
				if (null != stopHandle) {
					stopHandle.sendEmptyMessage(0);
				}
			}
		}
	}

	private void loadLogo() {
		if (SettingApp.CLIENT_ID == ClientId.SUO_HANG) {
			logoIdMap.put("qijing.bmp", R.drawable.qijing);
		} else {
			logoIdMap.put("initlogo.bmp", R.drawable.initlogo);
		}
		
		logoIdMap.put("aerfaluomiou.bmp", R.drawable.aerfaluomiou);
		logoIdMap.put("aodi.bmp", R.drawable.aodi);
		logoIdMap.put("baojun.bmp", R.drawable.baojun);
		logoIdMap.put("baoma.bmp", R.drawable.baoma);
		logoIdMap.put("baoshijie.bmp", R.drawable.baoshijie);
		logoIdMap.put("beijingqiche.bmp", R.drawable.beijingqiche);
		logoIdMap.put("beiqi.bmp", R.drawable.beiqi);
		logoIdMap.put("benchi.bmp", R.drawable.benchi);
		logoIdMap.put("benteng.bmp", R.drawable.benteng);
		logoIdMap.put("bentian.bmp", R.drawable.bentian);
		logoIdMap.put("biaozhi.bmp", R.drawable.biaozhi);
		logoIdMap.put("bieke.bmp", R.drawable.bieke);
		logoIdMap.put("biyadi.bmp", R.drawable.biyadi);
		logoIdMap.put("changan.bmp", R.drawable.changan);
		logoIdMap.put("changan1.bmp", R.drawable.changan1);
		logoIdMap.put("changcheng.bmp", R.drawable.changcheng);
		logoIdMap.put("changhe.bmp", R.drawable.changhe);
		logoIdMap.put("chuanqi.bmp", R.drawable.chuanqi);
		logoIdMap.put("chenglong.bmp", R.drawable.chenglong);
		logoIdMap.put("dafa.bmp", R.drawable.dafa);
		logoIdMap.put("dazhong.bmp", R.drawable.dazhong);
		logoIdMap.put("dodge.bmp", R.drawable.dodge);
		logoIdMap.put("dongnan.bmp", R.drawable.dongnan);
		logoIdMap.put("ds.bmp", R.drawable.ds);
		logoIdMap.put("dongfeng.bmp", R.drawable.dongfeng);
		logoIdMap.put("feiyata.bmp", R.drawable.feiyata);
		logoIdMap.put("fengtianbeimei.bmp", R.drawable.fengtianbeimei);
		logoIdMap.put("fengxing.bmp", R.drawable.fengxing);
		logoIdMap.put("fongtian.bmp", R.drawable.fongtian);
		logoIdMap.put("fudi.bmp", R.drawable.fudi);
		logoIdMap.put("fute.bmp", R.drawable.fute);
		logoIdMap.put("futian.bmp", R.drawable.futian);
		logoIdMap.put("gmc.bmp", R.drawable.gmc);
		logoIdMap.put("hafei.bmp", R.drawable.hafei);
		logoIdMap.put("hafo.bmp", R.drawable.hafo);
		logoIdMap.put("haima.bmp", R.drawable.haima);
		logoIdMap.put("heibao.bmp", R.drawable.heibao);
		logoIdMap.put("hengtong.bmp", R.drawable.hengtong);
		logoIdMap.put("huangguan.bmp", R.drawable.huangguan);
		logoIdMap.put("huanghaikeche.bmp", R.drawable.huanghaikeche);
		logoIdMap.put("huatai.bmp", R.drawable.huatai);
		logoIdMap.put("hanteng.bmp", R.drawable.hanteng);
		logoIdMap.put("huansu.bmp", R.drawable.huansu);
		logoIdMap.put("iveco.bmp", R.drawable.iveco);
		logoIdMap.put("jeep.bmp", R.drawable.jeep);
		logoIdMap.put("jianghuai.bmp", R.drawable.jianghuai);
		logoIdMap.put("jiangling.bmp", R.drawable.jiangling);
		logoIdMap.put("jiaoqiche.bmp", R.drawable.jiaoqiche);
		logoIdMap.put("jiebao.bmp", R.drawable.jiebao);
		logoIdMap.put("jili.bmp", R.drawable.jili);
		logoIdMap.put("jinbei.bmp", R.drawable.jinbei);
		logoIdMap.put("kadilake.bmp", R.drawable.kadilake);
		logoIdMap.put("kairui.bmp", R.drawable.kairui);
		logoIdMap.put("kelaisile.bmp", R.drawable.kelaisile);
		logoIdMap.put("lanbojini.bmp", R.drawable.lanbojini);
		logoIdMap.put("laosilaisi.bmp", R.drawable.laosilaisi);
		logoIdMap.put("leinuo.bmp", R.drawable.leinuo);
		logoIdMap.put("lianhua.bmp", R.drawable.lianhua);
		logoIdMap.put("lianhuaqiche.bmp", R.drawable.lianhuaqiche);
		logoIdMap.put("liebao.bmp", R.drawable.liebao);
		logoIdMap.put("lifan.bmp", R.drawable.lifan);
		logoIdMap.put("lifeng.bmp", R.drawable.lifeng);
		logoIdMap.put("lingzhi.bmp", R.drawable.lingzhi);
		logoIdMap.put("linken.bmp", R.drawable.linken);
//		logoIdMap.put("logo0.bmp", R.drawable.logo0);
		logoIdMap.put("lufeng.bmp", R.drawable.lufeng);
		logoIdMap.put("luhu.bmp", R.drawable.luhu);
		logoIdMap.put("luofu.bmp", R.drawable.luofu);
		logoIdMap.put("mashaladi.bmp", R.drawable.mashaladi);
		logoIdMap.put("mazhida.bmp", R.drawable.mazhida);
		logoIdMap.put("mini.bmp", R.drawable.mini);
		logoIdMap.put("minjue.bmp", R.drawable.minjue);
		logoIdMap.put("mogen.bmp", R.drawable.mogen);
		logoIdMap.put("nasijie.bmp", R.drawable.nasijie);
		logoIdMap.put("nusheng.bmp", R.drawable.nusheng);
		logoIdMap.put("oubao.bmp", R.drawable.oubao);
		logoIdMap.put("ouge.bmp", R.drawable.ouge);
		logoIdMap.put("ouman.bmp", R.drawable.ouman);
		logoIdMap.put("qichen.bmp", R.drawable.qichen);
		logoIdMap.put("qirui.bmp", R.drawable.qirui);
		logoIdMap.put("qiya.bmp", R.drawable.qiya);
		logoIdMap.put("richan.bmp", R.drawable.richan);
		logoIdMap.put("rongwei.bmp", R.drawable.rongwei);
		logoIdMap.put("ruilin.bmp", R.drawable.ruilin);
		logoIdMap.put("ruimai.bmp", R.drawable.ruimai);
		logoIdMap.put("saibo.bmp", R.drawable.saibo);
		logoIdMap.put("sanling.bmp", R.drawable.sanling);
		logoIdMap.put("shabo.bmp", R.drawable.shabo);
		logoIdMap.put("shanghaihuapu.bmp", R.drawable.shanghaihuapu);
		logoIdMap.put("shanqi.bmp", R.drawable.shanqi);
		logoIdMap.put("shijue.bmp", R.drawable.shijue);
		logoIdMap.put("shuanglong.bmp", R.drawable.shuanglong);
		logoIdMap.put("shuixing.bmp", R.drawable.shuixing);
		logoIdMap.put("sibanu.bmp", R.drawable.sibanu);
		logoIdMap.put("sikeda.bmp", R.drawable.sikeda);
		logoIdMap.put("smart.bmp", R.drawable.smart);
		logoIdMap.put("suzuki.bmp", R.drawable.suzuki);
		logoIdMap.put("sichuanyema1.bmp", R.drawable.sichuanyema1);
		logoIdMap.put("sichuanyema2.bmp", R.drawable.sichuanyema2);
		logoIdMap.put("sitaier.bmp", R.drawable.sitaier);
		logoIdMap.put("ufo.bmp", R.drawable.ufo);
		logoIdMap.put("vaux.bmp", R.drawable.vaux);
		logoIdMap.put("weiwang.bmp", R.drawable.weiwang);
		logoIdMap.put("woewo.bmp", R.drawable.woewo);
		logoIdMap.put("wulin.bmp", R.drawable.wulin);
		logoIdMap.put("wulinqiche.bmp", R.drawable.wulinqiche);
		logoIdMap.put("xiali.bmp", R.drawable.xiali);
		logoIdMap.put("xiandai.bmp", R.drawable.xiandai);
		logoIdMap.put("xiyate.bmp", R.drawable.xiyate);
		logoIdMap.put("xuefulan.bmp", R.drawable.xuefulan);
		logoIdMap.put("xuetielong.bmp", R.drawable.xuetielong);
		logoIdMap.put("yema.bmp", R.drawable.yema);
		logoIdMap.put("yingfeinidi.bmp", R.drawable.yingfeinidi);
		logoIdMap.put("yinglun.bmp", R.drawable.yinglun);
		logoIdMap.put("yiqi.bmp", R.drawable.yiqi);
		logoIdMap.put("yuejin.bmp", R.drawable.yuejin);
		logoIdMap.put("zhonghua.bmp", R.drawable.zhonghua);
		logoIdMap.put("zhongtai.bmp", R.drawable.zhongtai);
	}

	@Override
	protected void onPause() {
		int count = mGridView.getCount();
		for (int i = 0; i < count; i++) {
			ImageView imageView = (ImageView) mGridView.getChildAt(i);
			if (null != imageView && null != imageView.getDrawable())
				imageView.getDrawable().setCallback(null);
		}
		super.onPause();
	}

	public final class LoadLogoHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			LogoLoadingTask.execute();
		}

	}

	/**
	 * 车标适配器
	 */
	private final class ImageAdapter extends CustomBaseAdapter<String> {

		public ImageAdapter(Context context, List<String> entitys) {
			super(context, entitys);
		}

		// 返回视图
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView = null;
			Bitmap bitmap = null;
			try {
				if (convertView == null) {
					imageView = new ImageView(mContext);
					imageView.setPadding(LOGO_PADDING, LOGO_PADDING,
							LOGO_PADDING, LOGO_PADDING);
					GridView.LayoutParams layoutParams = new GridView.LayoutParams(
							LayoutParams.WRAP_CONTENT, WRAP_CONTENT);
					imageView.setLayoutParams(layoutParams);
//					imageView.setScaleType(ImageView.ScaleType.FIT_XY);
				} else {
					imageView = (ImageView) convertView;
				}
				int bitmapId = logoIdMap.get(logoImagesPath.get(position));
				bitmap = BitmapFactory.decodeResource(getResources(), bitmapId);
				imageView.setImageBitmap(bitmap);
				if (position == currentId) {
					imageView.setBackgroundResource(R.drawable.logo_checked);
				} else {
					imageView.setBackgroundResource(R.drawable.logo_no_check);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return imageView;
		}

	}
	/**
	 * 遍历压缩包里的图片名字
	 */
	private final class LogoLoadingTask extends
			AsyncTask<Object, Object, List<String>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (logoImagesPath.isEmpty()) {
				if (null != loadLogoProgressBar)
					loadLogoProgressBar.setVisibility(View.VISIBLE);
			}
		}

		@Override
		protected List<String> doInBackground(Object... arg0) {

			try {
				File fileDir = new File(PATH_LOGO_ZIP);
				if (fileDir.exists()) {
					// 把压缩包里的遍历出来
					logoImagesPath = listZipFiles();
					LogManager.d("doInBackground() images.isEmpty() = "
							+ logoImagesPath.isEmpty());

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return logoImagesPath;
		}

		/**
		 * 遍历压缩包里的图片名字
		 */
		private List<String> listZipFiles() throws Exception {
			List<String> images = new ArrayList<String>();
			ZipFile zf = new ZipFile(PATH_LOGO_ZIP);
			InputStream in = new FileInputStream(PATH_LOGO_ZIP);
			ZipInputStream zin = new ZipInputStream(in);
			ZipEntry ze;
			while ((ze = zin.getNextEntry()) != null) {
				if (ze.isDirectory()) {
				} else {
					long size = ze.getSize();
					if (size > 0) {
						String name = ze.getName();
						name = name.substring((name.indexOf("/") + 1));
						if (!StringUtils.isEmpty(name) && name.endsWith("bmp")) {
							images.add(name);
							if (!StringUtils.isEmpty(logoName)
									&& name.equals(logoName)) {
								currentId = images.size() - 1;
							}
						}
					}
				}
			}
			if (!images.isEmpty() && currentId != 0) {
				String selectName = images.remove(currentId);
				images.add(0, selectName);
				currentId = 0;
			}
			return images;
		}

		@Override
		protected void onPostExecute(List<String> result) {
			if (null != loadLogoProgressBar)
				loadLogoProgressBar.setVisibility(View.INVISIBLE);
			if (null != result && !result.isEmpty()) {
				mImageAdapter.setList(result);
				mImageAdapter.notifyDataSetChanged();
				setCurrentLogoImage(currentId);
			}else {
				mTextViewCarlogoNone.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		currentId = position;
		setCarLogo();	
		mImageAdapter.notifyDataSetChanged();
		
	}
	
	/**
	 * 把此名字的图片从压缩包里，读到外部默认路径下
	 */
	private void setCarLogo() {
		if (ListUtils.isEmpty(logoImagesPath)) {
			LogManager.d(TAG, "run() logoImagesPath: null" );
			return;
		}
		if (null != loadLogoProgressBar) {
			loadLogoProgressBar.setVisibility(View.VISIBLE);
		}
		new confirmLogo().start();
		stopHandle = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (null != loadLogoProgressBar) {
					loadLogoProgressBar.setVisibility(View.INVISIBLE);
				}
				setCurrentLogoImage(currentId);
			}

		};
	}
	
	/**
	 * 将车标拷贝到系统里
	 */
	private int copyLogoImages(String srcPath) {
		int result = -1;
		if (!StringUtils.isEmpty(srcPath)) {
			File srcFile = new File(srcPath);
			LogManager.d("copyLogoImages  srcFile.exists() = "
					+ srcFile.exists());
			if (srcFile.exists()) {
				String desFilePath = LOGO_PATH_BOOT_STRAP;
				File desFile = new File(desFilePath);
				LogManager.d("copyLogoImages  desFile.exists() = "
						+ desFile.exists());
				if (desFile.exists()) {
					if (null != configmanager) {
						configmanager.copyFile(desFilePath, srcPath);
						result = RESULT_OK;
					}
				}
			}
		}
		return result;
	}
	/**
	 * 如果默认路径下有车标，设置当前车标
	 */
	private void setCurrentLogoImage(int position) {
		if (new File(getMyPhotoPath()).exists()) {
			int bitmapId = logoIdMap.get(logoImagesPath.get(position));
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), bitmapId);
			carlogo_IV.setImageBitmap(bitmap);
		}
	}
	
	/**
	 * 把此名字的图片从压缩包里，读到外部默认路径下
	 */
	@SuppressLint("NewApi")
	private void takeOutPhotoFromZip(String name) throws Exception {
		InputStream in = new FileInputStream(PATH_LOGO_ZIP);
		ZipInputStream zin = new ZipInputStream(in);
		File myfile = new File(getMyPhotoPath());
		if (myfile.exists()) {
			myfile.delete();
		}
		FileOutputStream outputStream = new FileOutputStream(myfile);
		printTime();
		LogManager.d(TAG, "takeOutPhotoFromZip :  begin ");
		byte[] buffer = new byte[1024];
		int len = -1;
		ZipEntry entry = null;
		while ((entry = zin.getNextEntry()) != null) {
			if (name.equals(entry.getName())) {
				while ((len = zin.read(buffer)) != -1) {
					outputStream.write(buffer, 0, len);
				}
				break;
			}
		}
		LogManager.d(TAG, "takeOutPhotoFromZip :  stop ");
		printTime();
		in.close();
		outputStream.close();
		zin.closeEntry();

	}

	private void printTime() {
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy年MM月dd日    HH:mm:ss     ");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		LogManager.d(TAG, "takeOutPhotoFromZip :   " + str);
	}

	private String getMyPhotoPath() {
		try {
			String photoPath = "/data/data/" + this.getPackageName()
					+ "/myphoto.bmp";
			LogManager.d(TAG, "photoPath :   " + photoPath);
			return photoPath;
		} catch (Exception e) {
		}
		return null;
	}

}