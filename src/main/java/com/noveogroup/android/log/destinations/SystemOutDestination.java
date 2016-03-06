/*
 * Copyright (c) 2016 Nick Tsygankov (nicktgn@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Except as contained in this notice, the name(s) of the above copyright holders
 * shall not be used in advertising or otherwise to promote the sale, use or
 * other dealings in this Software without prior written authorization.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.noveogroup.android.log.destinations;

import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.destinations.utils.FastPrintWriter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.util.Properties;

/**
 * Created by nick on 3/1/16.
 */
class SystemOutDestination extends Destination{

	private static final String LVL = "";
	private static final String LVL_V = "V";
	private static final String LVL_I = "I";
	private static final String LVL_D = "D";
	private static final String LVL_W = "W";
	private static final String LVL_E = "E";
	private static final String LVL_A = "A";

	private static final String LVL_TAG_SEPARATOR = "/";
	private static final String TAG_MSG_SEPARATOR = ": ";

	protected SystemOutDestination(Properties props) throws UnsupportedOperationException {
		super(props);
	}

	private String logLevelToString(Logger.Level level){
		switch (level){
			case VERBOSE:
				return LVL_V;
			case INFO:
				return LVL_I;
			case DEBUG:
				return LVL_D;
			case WARN:
				return LVL_W;
			case ERROR:
				return LVL_E;
			case ASSERT:
				return LVL_A;
		}
		return LVL;
	}

	private String systemOutString(Logger.Level level, String tag, String msg){
		StringBuilder sb = new StringBuilder();
		sb.append(logLevelToString(level))
			.append(LVL_TAG_SEPARATOR)
			.append(tag)
			.append(TAG_MSG_SEPARATOR)
			.append(msg);
		return sb.toString();
	}

	@Override
	public void println(Logger.Level level, String tag, String msg) {
		System.out.println(systemOutString(level, tag, msg));
	}

	@Override
	public String getStackTraceString(Throwable throwable) {
		if (throwable == null) {
			return "";
		}
		// This is to reduce the amount of log spew that apps do in the non-error
		// condition of the network being unavailable.
		Throwable t = throwable;
		while (t != null) {
			if (t instanceof UnknownHostException) {
				return "";
			}
			t = t.getCause();
		}
		StringWriter sw = new StringWriter();
		PrintWriter pw = new FastPrintWriter(sw, false, 256);
		throwable.printStackTrace(pw);
		pw.flush();
		return sw.toString();
	}

	@Override
	protected boolean isAvailable() {
		return true;
	}
}
