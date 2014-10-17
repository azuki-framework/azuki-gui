package org.azkfw.gui.dialog.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * このアノテーションは、設定ダイアログを付与する場合に定義するアノテーションです。
 * 
 * @since 1.0.0
 * @version 1.0.0 2013/01/16
 * @author Kawakicchi
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigurationDialog {

	/**
	 * 設定ダイアログを取得する。
	 * 
	 * @return 設定ダイアログ
	 */
	public Class<? extends org.azkfw.gui.dialog.ConfigurationDialog> value();
}
