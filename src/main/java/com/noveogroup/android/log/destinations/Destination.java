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

import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

/**
 * Created by nick on 3/1/16.
 */
public abstract class Destination {

	private final static Class DEFAULT_FALLBACK[] = {
		LogCatDestination.class,
		SystemOutDestination.class
	};

	public final static Destination getDefault(){
		return getInstance(LogCatDestination.class, new Properties());
	}

	private final static Class getNextFallback(Class[] fallback, Class failed){
		for(int i=0; i<fallback.length; i++){
			if(failed.equals(fallback[i]) && i+1 < fallback.length){
				return fallback[i+1];
			}
		}
		return fallback[fallback.length - 1];
	}

	Destination(Properties props) throws UnsupportedOperationException{

	}

	public final static Destination getAvailableDestination(Class destination, Properties props){
		Destination dst = null;
		boolean gotIt = false;

		while(!gotIt){
			try {
				dst = (Destination) destination.getDeclaredConstructor(Properties.class).newInstance(props);
				gotIt = true;
			} catch (Exception e){
				// try next fallback
				destination = getNextFallback(DEFAULT_FALLBACK, destination);
			}
			/*
			catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {

				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}*/
		}
		return dst;
	}

	public final static Destination getInstance(Class destination, Properties props){
		Destination dst = getAvailableDestination(destination, props);

		if(dst == null){
			dst = new SystemOutDestination(props);
		}
		return dst;
	}

	abstract public void println(Logger.Level level, String tag, String msg);

	abstract public String getStackTraceString(Throwable thr);

	abstract protected boolean isAvailable();


}
