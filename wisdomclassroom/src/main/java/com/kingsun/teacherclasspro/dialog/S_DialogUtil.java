package com.kingsun.teacherclasspro.dialog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.TextView;

import com.kingsun.teacherclasspro.R;

public class S_DialogUtil {
	private static Dialog mAlertDialog = null;
	private static Dialog progressDialog = null;

	/**
	 * 取消所有弹出的对话框
	 */
	public static void dismissDialog() {
		if (progressDialog != null)
			progressDialog.dismiss();
		progressDialog = null;
	}

	/**
	 * 显示默认进度对话框（不可取消）
	 * 
	 * @param title
	 * @param message
	 * @param onCancelListener
	 */
	public static void showProgressDialog( Context context) {
		dismissDialog();
		progressDialog = new Dialog(context, R.style.progress_dialog);
		progressDialog.setContentView(R.layout.s_mydialog);
		progressDialog.setCancelable(false);
		progressDialog.getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);
		TextView msg = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
		msg.setText("加载中...");
		progressDialog.show();
	}
	
	/**
	 * 显示默认进度对话框（不可取消）
	 * 
	 * @param title
	 * @param message
	 * @param onCancelListener
	 */
	public static void showCheckDialog( Context context) {
		dismissDialog();
		progressDialog = new Dialog(context, R.style.progress_dialog);
		progressDialog.setContentView(R.layout.s_mydialog);
		progressDialog.setCancelable(false);
		progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		TextView msg = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
		msg.setText("正在评测");
		progressDialog.show();
	}

	/**
	 * 显示默认进度对话框（可取消）
	 * 
	 * @param title
	 * @param message
	 * @param onCancelListener
	 */
	public static void showProgressDialogCancel(Context context) {
		dismissDialog();
		progressDialog = new Dialog(context, R.style.progress_dialog);
		progressDialog.setContentView(R.layout.s_mydialog);
		progressDialog.setCancelable(true);
		progressDialog.getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);
		TextView msg = (TextView) progressDialog
				.findViewById(R.id.id_tv_loadingmsg);
		msg.setText("加载中...");
		progressDialog.show();
	}

	/**
	 * 显示进度对话框（不可取消）
	 * 
	 * @param title
	 * @param message
	 * @param onCancelListener
	 */
	public static void showProgressDialog(Context context, String message) {
		dismissDialog();
		progressDialog = new Dialog(context, R.style.progress_dialog);
		progressDialog.setContentView(R.layout.s_mydialog);
		progressDialog.setCancelable(false);
		progressDialog.getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);
		TextView msg = (TextView) progressDialog
				.findViewById(R.id.id_tv_loadingmsg);
		msg.setText(message);
		progressDialog.show();
	}

	public static void setMessage(String message) {
		if (mAlertDialog != null)
			if (mAlertDialog instanceof ProgressDialog) {
				if (message != null) {
					((ProgressDialog) mAlertDialog).setMessage(message);
				}
			}
	}

	/**
	 * 显示”确定“ 对话框
	 * 
	 * @param title
	 * @param onOkClickListener
	 */
	public static void showOkAlertDialog(Context context, String title,
			OnClickListener onOkClickListener) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);
		alertDialogBuilder.setMessage(title);
		alertDialogBuilder.setCancelable(false);
		alertDialogBuilder.setPositiveButton(
				context.getString(R.string.s_confirm), onOkClickListener);
		mAlertDialog = alertDialogBuilder.show();
	}

	/**
	 * 显示”是“ ”否“ 对话框
	 * 
	 * @param title
	 * @param onYesClickListener
	 * @param onNoClickListener
	 */
	public static void showYesNoAlertDialog(Context context, String title,
			OnClickListener onYesClickListener,
			OnClickListener onNoClickListener) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);
		if (title != null) {
			alertDialogBuilder.setTitle(title);
		}
		if (onYesClickListener != null) {
			alertDialogBuilder.setPositiveButton(
					context.getString(R.string.s_yes), onYesClickListener);
		}
		if (onNoClickListener != null) {
			alertDialogBuilder.setNegativeButton(
					context.getString(R.string.s_no), onNoClickListener);
		}
		alertDialogBuilder.setCancelable(false);
		mAlertDialog = alertDialogBuilder.show();
	}

	/**
	 * 显示定义对话框
	 * 
	 * @param title
	 * @param onYesClickListener
	 * @param onNoClickListener
	 */
	public static void showCustomAlertDialog(Context context, String title,
			String confirmStr, String cancelStr,
			OnClickListener onYesClickListener,
			OnClickListener onNoClickListener) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);
		if (title != null) {
			alertDialogBuilder.setTitle(title);
		}

		if (onYesClickListener != null && confirmStr != null) {
			alertDialogBuilder
					.setPositiveButton(confirmStr, onYesClickListener);
		}
		if (onNoClickListener != null && cancelStr != null) {
			alertDialogBuilder.setNegativeButton(cancelStr, onNoClickListener);
		}
		alertDialogBuilder.setCancelable(false);
		mAlertDialog = alertDialogBuilder.show();
	}

	/**
	 * 显示定义对话框
	 * 
	 * @param title
	 * @param message
	 * @param confirmStr
	 * @param cancelStr
	 * @param onYesClickListener
	 * @param onNoClickListener
	 */
	public static void showCustomAlertDialogWithMessage(Context context,
			String title, String message, String confirmStr, String cancelStr,
			OnClickListener onYesClickListener,
			OnClickListener onNoClickListener) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);
		if (title != null) {
			alertDialogBuilder.setTitle(title);
		}
		if (message != null) {
			alertDialogBuilder.setMessage(message);
		}
		if (onYesClickListener != null && confirmStr != null) {
			alertDialogBuilder
					.setPositiveButton(confirmStr, onYesClickListener);
		}
		if (onNoClickListener != null && cancelStr != null) {
			alertDialogBuilder.setNegativeButton(cancelStr, onNoClickListener);
		}
		alertDialogBuilder.setCancelable(false);
		mAlertDialog = alertDialogBuilder.show();
	}

	/**
	 * 显示列表对话框
	 * 
	 * @param context
	 * @param title
	 * @param items
	 * @param onItemClickListener
	 * @param negativeTxt
	 * @param onNegativeClickListener
	 * @param positiveTxt
	 * @param onPositiveClickListener
	 */
	public static void showCustomListDialog(Context context, String title,
			String[] items,
			OnClickListener onItemClickListener,
			String negativeTxt, OnClickListener onNegativeClickListener,
			String positiveTxt, OnClickListener onPositiveClickListener) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title).setItems(items, onItemClickListener)
				.setCancelable(false)
				.setNegativeButton(negativeTxt, onNegativeClickListener)
				.setPositiveButton(positiveTxt, onPositiveClickListener);

		Dialog dialog = builder.show();
	}

	/**
	 * 显示列表对话框
	 * 
	 * @param context
	 * @param title
	 * @param items
	 * @param onItemClickListener
	 */
	public static void showListDialog(Context context, String title,
			String[] items, OnClickListener onItemClickListener) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title).setItems(items, onItemClickListener);

		Dialog dialog = builder.show();
	}

	/**
	 * 显示日期选择对话框
	 * 
	 * @param context
	 * @param callBack
	 * @param year
	 * @param monthOfYear
	 * @param dayOfMonth
	 */
	public static void showDatePickDialog(Context context,
			OnDateSetListener callBack, int year, int monthOfYear,
			int dayOfMonth) {
		new DatePickerDialog(context, callBack, year, monthOfYear, dayOfMonth)
				.show();
	}
}
