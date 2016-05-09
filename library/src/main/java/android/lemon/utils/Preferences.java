package android.lemon.utils;

import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 初始化这个 对象 ， 然后 使用就是调用这里面的方法
 *
 * @author Kevin
 * 
 */
public class Preferences {
	private final double TIMES = 100000000000000.0;
	Context context = null;
	SharedPreferences sp = null;
	Editor editor = null;

	public Preferences(Context context) {
		this.context = context;
		sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
	}

	public void setPreferences(SharedPreferences sharedPreferences){
		sp = sharedPreferences;
	}

	public Preferences(Context context,String name) {
		this.context = context;
		sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
	}




	/**
	 * 保存 true 与 false
	 * 
	 * @param name
	 * @param value
	 */
	public void putBoolean(String name, Boolean value) {
		editor = sp.edit();
		editor.putBoolean(name, value);
		editor.commit();
	}

	/**
	 * 保存数值到配置文件中
	 * 
	 * @param name
	 * @param value
	 */
	public void putInt(String name, int value) {
		editor = sp.edit();
		editor.putInt(name, value);
		editor.commit();
	}

	/**
	 * 保存字符串到配置文件中
	 * 
	 * @param name
	 * @param value
	 */
	public void putString(String name, String value) {
		editor = sp.edit();
		editor.putString(name, value);
		editor.commit();
	}

	/**
	 * 得到数值
	 * 
	 * @param name
	 * @return
	 */
	public int getInt(String name) {
		return sp.getInt(name, 0);
	}

	/**
	 * 得到里面所有内容
	 * 
	 * @return
	 */
	public Map<String, ?> getAll() {

		Map<String, ?> map = sp.getAll();

		return map;

	}
	
	/** 是否存在
	 * @param key
	 * @return
	 */
	public boolean isExits(String key){
		return sp.contains(key);
	}
	

	/**
	 * 得到数值
	 * 
	 * @param name
	 * @return
	 */
	public int getInt(String name, int defaultValue) {
		return sp.getInt(name, defaultValue);
	}

	/**
	 * 得到字符串
	 * 
	 * @param name
	 * @return
	 */
	public String getString(String name) {
		return sp.getString(name, "");
	}

	/**
	 * 得到字符串
	 * 
	 * @param name
	 * @return
	 */
	public String getString(String name, String defaultValue) {
		return sp.getString(name, defaultValue);
	}

	/**
	 * 得到布尔值
	 * 
	 * @param name
	 * @return
	 */
	public Boolean getBoolean(String name) {
		return sp.getBoolean(name, true);
	}

	/**
	 * 得到布尔值
	 * 
	 * @param name
	 * @param defaultStatus
	 * @return
	 */
	public Boolean getBoolean(String name, boolean defaultValue) {
		return sp.getBoolean(name, defaultValue);
	}

	/**
	 * 得到double
	 * 
	 * @param key
	 * @return
	 */
	public double getDouble(String key) {
		return sp.getLong(key, -1) / TIMES;
	}

	/**
	 * 得到double
	 * 
	 * @param key
	 * @return
	 */
	public double getDouble(String key, int defaultValue) {
		return sp.getLong(key, defaultValue) / TIMES;
	}

	/**
	 * 得到float
	 * 
	 * @param key
	 * @return
	 */
	public float getFloat(String key) {
		return sp.getFloat(key, -1f);
	}

	/**
	 * 得到float
	 * 
	 * @param key
	 * @return
	 */
	public float getFloat(String key, float defaultValue) {
		return sp.getFloat(key, defaultValue);
	}

	/**
	 * 得到Long
	 * 
	 * @param key
	 * @return
	 */
	public long getLong(String key, long defaultValue) {
		return sp.getLong(key, defaultValue);
	}

	/**
	 * 放置Flaot
	 * 
	 * @return
	 */
	public void putFlaot(String key, float value) {
		Editor editor = sp.edit();
		editor.putFloat(key, value);
		editor.commit();
	}

	/**
	 * 放置Flaot
	 * 
	 * @return
	 */
	public void putLong(String key, long value) {
		Editor editor = sp.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	/**
	 * 得到double
	 * 
	 * @param key
	 * @return
	 */
	public void putDouble(String key, double value) {
		value = value * TIMES;
		Editor editor = sp.edit();
		editor.putLong(key, (long) value);
		editor.commit();
	}

	public void clear() {
		Editor editor = sp.edit();
		editor.clear();
		editor.commit();
	}

	/** 注册偏好设置改变监听
	 * @param changeListener
	 */
	public void registerChange(SharedPreferences.OnSharedPreferenceChangeListener changeListener){
		sp.registerOnSharedPreferenceChangeListener(changeListener);
	}

}
