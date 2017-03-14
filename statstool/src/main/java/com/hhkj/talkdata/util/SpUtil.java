package com.hhkj.talkdata.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;


/**
 * SharePreference工具
 * @author user
 */
public class SpUtil {

	private static final String TAG = "SpUtil";
	private static final int INVALID = -1;

	public static void saveSharedPreferences(Context context, String key, Object value, String spName) {
		if (context == null) {
			return;
		}
		Editor hexinEditor = context.getSharedPreferences(spName, Context.MODE_PRIVATE).edit();
		if (value instanceof String) {
			hexinEditor.putString(key, (String) value);
		} else if (value instanceof Integer) {
			hexinEditor.putInt(key, (Integer) value);
		} else if (value instanceof Boolean) {
			hexinEditor.putBoolean(key, (Boolean) value);
		} else if (value instanceof Long) {
			hexinEditor.putLong(key, (Long) value);
		} else if (value instanceof Float) {
			hexinEditor.putFloat(key, (Float) value);
		} else {
			Log.d(TAG, "saveSharedPreferences -> type error ");
		}

		hexinEditor.commit();
	}

	public static void saveSharedPreferences(Context context, String[] keys, String[] values, String spName) {
		if (context ==null || keys == null || values == null || keys.length != values.length) {
			return;
		}
		Editor hexinEditor = context.getSharedPreferences(spName, Context.MODE_PRIVATE).edit();
		for (int i = 0; i < keys.length; i++) {
			String key = keys[i];
			String value = values[i];
			hexinEditor.putString(key, value);
		}
		hexinEditor.commit();
	}

	public static String getStringValue(Context context, String spName, String key) {
		return getStringValue(context, spName, key, "");
	}

	public static String getStringValue(Context context, String spName, String key, String defaultValue) {
		if (context ==null || spName.isEmpty() || key.isEmpty()) {
			Log.d(TAG, "getStringValue error");
			return defaultValue;
		}

		SharedPreferences hexinSharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);

		if (hexinSharedPreferences != null) {
			defaultValue = hexinSharedPreferences.getString(key, defaultValue);
		}
		return defaultValue;
	}

	public static int getIntValue(Context context, String spName, String key) {
		return getIntValue(context, spName, key, INVALID);
	}

	public static int getIntValue(Context context, String spName, String key, int defaultValue) {
		if (context == null || spName.isEmpty() || key.isEmpty()) {
			Log.d(TAG, "getIntValue error");
			return defaultValue;
		}

		SharedPreferences hexinSharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);

		if (hexinSharedPreferences != null) {
			defaultValue = hexinSharedPreferences.getInt(key, defaultValue);
		}
		return defaultValue;
	}

	public static float getFloatValue(Context context, String spName, String key) {
		return getFloatValue(context, spName, key, INVALID);
	}

	public static float getFloatValue(Context context, String spName, String key, float defaultValue) {
		if (context == null || spName.isEmpty() || key.isEmpty()) {
			Log.d(TAG, "getFloatValue error");
			return defaultValue;
		}

		SharedPreferences hexinSharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);

		if (hexinSharedPreferences != null) {
			defaultValue = hexinSharedPreferences.getFloat(key, defaultValue);
		}
		return defaultValue;
	}

	public static long getLongValue(Context context, String spName, String key) {
		return getLongValue(context, spName, key, INVALID);
	}

	public static long getLongValue(Context context, String spName, String key, long defaultValue) {
		if (context == null || spName.isEmpty() || key.isEmpty()) {
			Log.d(TAG, "getLongValue error");
			return defaultValue;
		}

		SharedPreferences hexinSharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);

		if (hexinSharedPreferences != null) {
			defaultValue = hexinSharedPreferences.getLong(key, defaultValue);
		}
		return defaultValue;
	}

	public static boolean getBooleanValue(Context context, String spName, String key) {
		return getBooleanValue(context, spName, key, false);
	}

	public static boolean getBooleanValue(Context context, String spName, String key, boolean defaultValue) {
		if (context == null || spName.isEmpty() || key.isEmpty()) {
			Log.d(TAG, "getBooleanValue error");
			return defaultValue;
		}

		SharedPreferences hexinSharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);

		if (hexinSharedPreferences != null) {
			defaultValue = hexinSharedPreferences.getBoolean(key, defaultValue);
		}
		return defaultValue;
	}

	public static void reomveValue(Context context, String spName, String key) {
		if (context == null || spName.isEmpty() || key.isEmpty()) {
			Log.d(TAG, "reomveValue error");
			return;
		}
		Editor hexinEditor = context.getSharedPreferences(spName, Context.MODE_PRIVATE).edit();
		if (hexinEditor != null) {
			hexinEditor.remove(key);
		}
		hexinEditor.commit();
	}

}
