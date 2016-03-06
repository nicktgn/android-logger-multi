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

import android.util.Log;

import java.util.Properties;

/**
 * Created by nick on 3/1/16.
 */
class LogCatDestination extends Destination {

	LogCatDestination(Properties props) throws UnsupportedOperationException {
		super(props);
		if(!isAvailable()){
			throw new UnsupportedOperationException("LogCat destination is not available");
		}
	}

	@Override
	public void println(Logger.Level level, String tag, String msg) {

		Log.println(level.intValue(), tag, msg);
	}

	@Override
	public String getStackTraceString(Throwable thr) {
		return Log.getStackTraceString(thr);
	}

	@Override
	protected boolean isAvailable() {
		try{
			Log.getStackTraceString(new Throwable());
			return true;
		} catch(Exception e){
			return false;
		}
	}
}
