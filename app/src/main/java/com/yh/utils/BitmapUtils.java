package com.yh.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class BitmapUtils {


	/***
	 * 图片的缩放方法
	 * 
	 * @param bgimage
	 *            ：源图片资源
	 * @param newWidth
	 *            ：缩放后宽度
	 * @param newHeight
	 *            ：缩放后高度
	 * @return
	 */
	public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
			double newHeight) {
		// 获取这个图片的宽和高
		float width = bgimage.getWidth();
		float height = bgimage.getHeight();
		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();
		// 计算宽高缩放率
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
				(int) height, matrix, true);
		return bitmap;
	}


	public static Bitmap getLoacalBitmap(String url) {
		try {
			FileInputStream fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}


	/*
	* @description 计算图片的压缩比率
	* @param options 参数
	* @param reqWidth 目标的宽度
	* @param reqHeight 目标的高度
	* @return
	*/

	private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// 源图片的高度和宽度
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			// 计算出实际宽高和目标宽高的比率
			final int halfHeight = height / 2;
			final int halfWidth = width / 2;
			while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}
		return inSampleSize;
	}
	/**
	 * @description 通过传入的bitmap，进行压缩，得到符合标准的bitmap
	 *
	 * @param src
	 * @param dstWidth
	 * @param dstHeight
	 * @return
	 */
	private static Bitmap createScaleBitmap(Bitmap src, int dstWidth, int dstHeight, int inSampleSize) {
		//如果inSampleSize是2的倍数，也就说这个src已经是我们想要的缩略图了，直接返回即可。
		if (inSampleSize % 2 == 0) {
			return src;
		}
		// 如果是放大图片，filter决定是否平滑，如果是缩小图片，filter无影响，我们这里是缩小图片，所以直接设置为false
		Bitmap dst = Bitmap.createScaledBitmap(src, dstWidth, dstHeight, false);
		if (src != dst) { // 如果没有缩放，那么不回收
			src.recycle(); // 释放Bitmap的native像素数组
		}
		return dst;
	}
	/**
	 * @description 从Resources中加载图片
	 *
	 * @param res
	 * @param resId
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; // 设置成了true,不占用内存，只获取bitmap宽高
		BitmapFactory.decodeResource(res, resId, options); // 读取图片长款
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight); // 调用上面定义的方法计算inSampleSize值
		// 使用获取到的inSampleSize值再次解析图片
		options.inJustDecodeBounds = false;
		Bitmap src = BitmapFactory.decodeResource(res, resId, options); // 载入一个稍大的缩略图
		return createScaleBitmap(src, reqWidth, reqHeight, options.inSampleSize); // 进一步得到目标大小的缩略图
	}
	/**
	 * @description 从SD卡上加载图片
	 *
	 * @param pathName
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static Bitmap decodeSampledBitmapFromFile(String pathName, int reqWidth, int reqHeight)throws Exception{
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathName, options);
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		options.inJustDecodeBounds = false;
		Bitmap src = BitmapFactory.decodeFile(pathName, options);
		return createScaleBitmap(src, reqWidth, reqHeight, options.inSampleSize);
	}
	
	
}
