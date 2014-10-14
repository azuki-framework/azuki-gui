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
 * このインターフェースは、タスクのリスナー機能を定義したインターフェースです。
 * 
 * @since 1.0.0
 * @version 1.0.0 2014/10/07
 * @author Kawakicchi
 */
public interface TaskListener {

	/**
	 * タスク開始時に呼びさされる。
	 * 
	 * @param event
	 */
	public void taskStarted(final TaskEvent event);

	/**
	 * タスク成功時に呼び出される。
	 * 
	 * @param event
	 */
	public void taskSuccessed(final TaskEvent event);

	/**
	 * タスク中断時に呼び出される。
	 * 
	 * @param event
	 */
	public void taskCanceled(final TaskEvent event);

	/**
	 * タスク異常時に呼び出される。
	 * 
	 * @param event
	 */
	public void taskFailed(final TaskEvent event);

	/**
	 * タスク終了時に呼び出される。
	 * <p>
	 * 正常・異常・中断後に呼び出される。
	 * </p>
	 * 
	 * @param event
	 */
	public void taskCompleted(final TaskEvent event);
}
