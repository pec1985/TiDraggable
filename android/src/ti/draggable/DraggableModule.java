/**
 *   Copyright 2012 Pedro Enrique
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *   
 */

package ti.draggable;

import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;

import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.kroll.common.TiConfig;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

@Kroll.module(name="Draggable", id="ti.draggable")
public class DraggableModule extends KrollModule
{

	// Standard Debugging variables
	private static final String LCAT = "TiDraggable";
	private static final boolean DBG = TiConfig.LOGD;

	// You can define constants with @Kroll.constant, for example:
	// @Kroll.constant public static final String EXTERNAL_NAME = value;
	
	public DraggableModule()
	{
		super();
	}

	@Kroll.onAppCreate
	public static void onAppCreate(TiApplication app)
	{
	}
	
	// Still testing, DO NOT USE
	
	@Kroll.method
	public void makeDraggable(TiViewProxy viewproxy){
		final TiViewProxy _proxy = viewproxy;
		OnTouchListener listener = new OnTouchListener() {
			int positionTop = 0;
			int positionLeft = 0;
			int tempTop = 0;
			int tempLeft = 0;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				positionLeft = positionLeft == 0 ? v.getLeft() : positionLeft;
				positionTop = positionTop == 0 ? v.getTop() : positionTop;
				
				int eventX = Math.round(event.getRawX());
				int eventY = Math.round(event.getRawY());
				switch(event.getAction()){
					case MotionEvent.ACTION_DOWN:
						tempLeft = (positionLeft - eventX);
						tempTop = (positionTop - eventY);
					break;
					case MotionEvent.ACTION_MOVE:
						_proxy.setPropertyAndFire("left",(eventX + tempLeft));
						_proxy.setPropertyAndFire("top",(eventY + tempTop));							
						break;
					case MotionEvent.ACTION_UP:

					break;
				}
				return true;
			}
			
		};
		viewproxy.getOrCreateView().getNativeView().setOnTouchListener(listener);
		
	}
}

