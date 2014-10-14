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
 * このクラスは、タスク結果情報を保持するクラスです。
 * 
 * @since 1.0.0
 * @version 1.0.0 2014/10/06
 * @author Kawakicchi
 */
public class TaskResult {

	private boolean result;

	private boolean cancel;

	private String message;

	private String detail;

	private TaskResult(final boolean aResult, final boolean aCancel, final String aMessage, final String aDetail) {
		result = aResult;
		cancel = aCancel;
		message = aMessage;
		detail = aDetail;
	}

	public static TaskResult success() {
		return new TaskResult(true, false, null, null);
	}

	public static TaskResult cancel() {
		return new TaskResult(true, true, null, null);
	}

	public static TaskResult error() {
		return new TaskResult(false, false, null, null);
	}

	public static TaskResult error(final String aMessage) {
		return new TaskResult(false, false, aMessage, null);
	}

	public static TaskResult error(final String aMessage, final String aDetail) {
		return new TaskResult(false, false, aMessage, aDetail);
	}

	public boolean isResult() {
		return result;
	}

	public boolean isCancel() {
		return cancel;
	}

	public String getMessage() {
		return message;
	}

	public String getDetail() {
		return detail;
	}
}
