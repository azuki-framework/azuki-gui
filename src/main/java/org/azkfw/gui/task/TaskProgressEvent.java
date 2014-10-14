/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.azkfw.gui.task;

/**
 * このクラスは、タスクプログレスのイベント情報を保持するクラスです。
 * 
 * @since 1.0.0
 * @version 1.0.0 2014/10/07
 * @author Kawakicchi
 */
public class TaskProgressEvent {

	/** タスク */
	private Task task;

	/** 進捗率 (0.0 - 100.0) */
	private double percent;

	/** メッセージ */
	private String message;

	/** 詳細 */
	private String detail;

	/** キャンセルフラグ */
	private boolean cancel;

	public TaskProgressEvent(final Task aTask) {
		task = aTask;
		percent = 0.f;
		message = "";
		detail = "";
		cancel = false;
	}

	/**
	 * タスクを取得する。
	 * 
	 * @return タスク
	 */
	public Task getTask() {
		return task;
	}

	/**
	 * 進捗率を設定する。
	 * 
	 * @param aPercent 進捗率(0-100%)
	 */
	public void setPercent(final double aPercent) {
		percent = aPercent;
	}

	/**
	 * 進捗率を取得する。
	 * 
	 * @return 進捗率(0-100%)
	 */
	public double getPercent() {
		return percent;
	}

	/**
	 * メッセージを設定する。
	 * 
	 * @param aMessage メッセージ
	 */
	public void setMessage(final String aMessage) {
		message = aMessage;
	}

	/**
	 * メッセージを取得する。
	 * 
	 * @return
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * 詳細を設定する。
	 * 
	 * @param aDetail 詳細
	 */
	public void setDetail(final String aDetail) {
		detail = aDetail;
	}

	/**
	 * 詳細を取得する。
	 * 
	 * @return 詳細
	 */
	public String getDetail() {
		return detail;
	}

	/**
	 * キャンセルする。
	 */
	public void cancel() {
		cancel = true;
	}

	/**
	 * キャンセルを判断する。
	 * 
	 * @return 判断
	 */
	public boolean isCancel() {
		return cancel;
	}
}
