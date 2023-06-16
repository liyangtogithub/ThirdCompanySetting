package com.landsem.setting.activities;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.landsem.common.tools.LogManager;
import com.landsem.setting.R;
import com.landsem.setting.receiver.SDCardReceiver;
import com.ls.config.ConfigManager;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GradeMapActivity extends Activity implements OnClickListener {

	public static final int PROGRESS_SIZE = 100;
	public static final int MASSAGE_ING = 1;
	public static final int MASSAGE_SUCCESS = 2;
	public static final int MASSAGE_FAIL = 3;
	public static final String MAP_PATH_NAME = "mapPath";
	long progress = 0;
	long progressSize = 0;
	ProgressBar progress_maping;
	Thread UpThread;
	TextView tv_station;
	Context mContext;
	boolean UpThreadAlive =true;
	public static final int READ_SIZE = (1024 * 1024);
	public   String mapPath = SDCardReceiver.UP_SOURCE_FILE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_upgrade_map);
		mContext = this;
		initView();
		Intent intent = getIntent();
		mapPath = intent.getStringExtra(MAP_PATH_NAME);
		UpThread = new Thread(new UpThread());
		UpThread.start();
	}

	class UpThread implements Runnable {
		public void run() {
			try {
				    getMapSize();
					upHandler.sendEmptyMessageDelayed(MASSAGE_ING, 500);
					// 获取源文件夹当前下的文件或目录
					File[] file = (new File(mapPath)).listFiles();
					for (int i = 0; i < file.length; i++) {
						if (file[i].isFile()) {
							// 复制文件
							copyFile(file[i], new File(SDCardReceiver.UP_DEST_FILE_PATH
									+ file[i].getName()));
						}
						if (file[i].isDirectory()) {
							// 复制目录
							String sourceDir = mapPath + File.separator
									+ file[i].getName();
							String targetDir = SDCardReceiver.UP_DEST_FILE_PATH
									+ File.separator + file[i].getName();
							copyDirectiory(sourceDir, targetDir);
						}
					}
					upHandler.removeMessages(MASSAGE_ING);
					upHandler.sendEmptyMessageDelayed(MASSAGE_SUCCESS, 1000);
			} catch (Exception e) {
				upHandler.removeMessages(MASSAGE_ING);
				upHandler.sendEmptyMessageDelayed(MASSAGE_FAIL, 1000);
				e.printStackTrace();
			}
		}
	};

	private void getMapSize() {
		File fileSource = new File(mapPath);
		progressSize = getFolderSize(fileSource)/ READ_SIZE;
		LogManager.d("size all " + progressSize);
	}

	/**
	 * 获取文件夹大小
	 * 
	 * @param file
	 *            File实例
	 * @return long
	 */
	public long getFolderSize(java.io.File file) {

		long size = 0;
		try {
			java.io.File[] fileList = file.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				if (fileList[i].isDirectory()) {
					size = size + getFolderSize(fileList[i]);
					
				} else {
					size = size + fileList[i].length();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (size<0 ) {
			size = Long.MAX_VALUE;
		}
		
		return size;
	}

	public void copyFile(File sourceFile, File targetFile) throws IOException {
		// 新建文件输入流并对它进行缓冲
		FileInputStream input = new FileInputStream(sourceFile);
		BufferedInputStream inBuff = new BufferedInputStream(input);
		// 新建文件输出流并对它进行缓冲
		FileOutputStream output = new FileOutputStream(targetFile);
		BufferedOutputStream outBuff = new BufferedOutputStream(output);

		// 缓冲数组
		byte[] b = new byte[READ_SIZE];
		int len;
		while (UpThreadAlive && (len = inBuff.read(b)) != -1) {
			outBuff.write(b, 0, len);
			progress++;
		}

		// 刷新此缓冲的输出流
		outBuff.flush();

		// 关闭流
		inBuff.close();
		outBuff.close();
		output.close();
		input.close();
	}

	// 复制文件夹
	public void copyDirectiory(String sourceDir, String targetDir)
			throws IOException {
		// 新建目标目录
		(new File(targetDir)).mkdirs();
		// 获取源文件夹当前下的文件或目录
		File[] file = (new File(sourceDir)).listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {
				// 源文件
				File sourceFile = file[i];
				// 目标文件
				File targetFile = new File(
						new File(targetDir).getAbsolutePath() + File.separator
								+ file[i].getName());
				copyFile(sourceFile, targetFile);
			}
			if (file[i].isDirectory()) {
				// 准备复制的源文件夹
				String dir1 = sourceDir + "/" + file[i].getName();
				// 准备复制的目标文件夹
				String dir2 = targetDir + "/" + file[i].getName();
				copyDirectiory(dir1, dir2);
			}
		}
	}

	Handler upHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case MASSAGE_ING:
				upHandler.sendEmptyMessageDelayed(MASSAGE_ING, 500);
				if (progressSize == 0) {
					return;
				}
				long showProgressValue =  progress * PROGRESS_SIZE / progressSize;
				if (showProgressValue >= 100) {
					LogManager.d("MASSAGE_ING  progress " + progress+",  progressSize :"+progressSize);
					showProgressValue = 99;
				}
				tv_station.setText("" + showProgressValue);
				progress_maping.setProgress((int) showProgressValue);
				break;
			case MASSAGE_FAIL:
				upHandler.removeMessages(MASSAGE_ING);
				UpThreadAlive =false;
				tv_station.setText(R.string.copy_file_failed);
				break;
			case MASSAGE_SUCCESS:
				upHandler.removeMessages(MASSAGE_ING);
				progress_maping.setProgress(PROGRESS_SIZE);
				tv_station.setText(R.string.str_complete);
				break;
			}

		}
	};

	private void initView() {
		progress_maping = (ProgressBar) findViewById(R.id.progress_maping);
		progress_maping.setMax(PROGRESS_SIZE);
		tv_station = (TextView) findViewById(R.id.tv_station);
		tv_station.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {

		switch (arg0.getId()) {
		case R.id.tv_station:
			if (mContext.getResources().getString(R.string.str_complete)
					.equals(tv_station.getText().toString())) {
				finish();
			}
			break;

		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (upHandler != null) {
			upHandler.removeMessages(MASSAGE_ING);
			upHandler.removeMessages(MASSAGE_FAIL);
			upHandler.removeMessages(MASSAGE_SUCCESS);
		}
		if (UpThread != null) {
			UpThread.interrupt();
		}
		UpThreadAlive =false;
		LogManager.d("onDestroy  UpThreadAlive: " + UpThreadAlive);
	}

	public static void goToGradeMapActivity(Context context, String mapPathString) {
		Intent intent = new Intent(context, GradeMapActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(MAP_PATH_NAME, mapPathString);
		context.startActivity(intent);
	}
}
