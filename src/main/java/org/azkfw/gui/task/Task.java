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
 * このインターフェースは、タスク機能を定義するインターフェースです。
 * 
 * @since 1.0.0
 * @version 1.0.0 2014/10/06
 * @author Kawakicchi
 */
public interface Task {

	/**
	 * タスク名を取得する。
	 * 
	 * @return タスク名
	 */
	public String getName();

	/**
	 * 初期化処理
	 */
	public void initialize();

	/**
	 * タスク処理
	 * 
	 * @return 結果
	 */
	public TaskResult execute();

	/**
	 * 解放処理
	 */
	public void destory();

	/**
	 * リスナーを追加する。
	 * 
	 * @param listener リスナー
	 */
	public void addTaskListener(final TaskListener listener);
}
