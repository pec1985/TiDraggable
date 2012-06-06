/**
 *   Copyright 2012 Pedro Enrique
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *	   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *   
 */
package ti.draggable;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.TiContext;
import org.appcelerator.titanium.TiDimension;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.util.TiConvert;
import org.appcelerator.titanium.view.TiCompositeLayout;
import org.appcelerator.titanium.view.TiUIView;
import org.appcelerator.titanium.view.TiCompositeLayout.LayoutArrangement;
import org.appcelerator.titanium.view.TiCompositeLayout.LayoutParams;

import android.app.Activity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

@Kroll.proxy(creatableInModule = DraggableModule.class)
public class ViewProxy extends TiViewProxy {
	private static final String LCAT = "TiDraggable";

	public TiCompositeLayout view;
	private ViewProxy _proxy;
	private TiCompositeLayout.LayoutParams _layout;
	private int oldTop = 0;
	private int oldLeft = 0;
	private String directionVertical = "neutral";
	private String directionHorizontal = "neutral";

	private int positionTop = 0;
	private int positionLeft = 0;
	private int tempTop = 0;
	private int tempLeft = 0;
	private int maxTop;
	private int minTop;
	private int maxLeft;
	private int minLeft;
	private boolean hasMaxTop = false;
	private boolean hasMinTop = false;
	private boolean hasMaxLeft = false;
	private boolean hasMinLeft = false;
	private boolean hasAxisX = false;
	private boolean hasAxisY = false;

	// Still working on this
	private boolean hasListenerStart = false;
	private boolean hasListenerMove = false;
	private boolean hasListenerEnd = false;
	
	public ViewProxy() {}

	public ViewProxy(TiContext tiContext) {
		this();
		_proxy = this;
	}
	
	public class PEDraggableView extends TiUIView {
		
		public PEDraggableView(TiViewProxy proxy) {
			super(proxy);
			LayoutArrangement arrangement = LayoutArrangement.DEFAULT;
			
			if (proxy.hasProperty(TiC.PROPERTY_LAYOUT)) {
				String layoutProperty = TiConvert.toString(proxy.getProperty(TiC.PROPERTY_LAYOUT));
				if (layoutProperty.equals(TiC.LAYOUT_HORIZONTAL)) {
					arrangement = LayoutArrangement.HORIZONTAL;
				} else if (layoutProperty.equals(TiC.LAYOUT_VERTICAL)) {
					arrangement = LayoutArrangement.VERTICAL;
				}
			}
			if (proxy.hasProperty("axis")) {
				String axis = TiConvert.toString(proxy.getProperty("axis"));
				Log.d(LCAT, "hasAxis " + axis);
				_proxy.setAxis(axis);
			}
			if (proxy.hasProperty("maxLeft")) {
				float n = TiConvert.toFloat(proxy.getProperty("maxLeft"));
				Log.d(LCAT, "maxLeft " + n);
				_proxy.setMaxLeft(n);
			}
			if (proxy.hasProperty("minLeft")) {
				float n = TiConvert.toFloat(proxy.getProperty("minLeft"));
				Log.d(LCAT, "minLeft " + n);
				_proxy.setMinLeft(n);
			}
			if (proxy.hasProperty("maxTop")) {
				float n = TiConvert.toFloat(proxy.getProperty("maxTop"));
				Log.d(LCAT, "maxTop " + n);
				_proxy.setMaxTop(n);
			}
			if (proxy.hasProperty("minTop")) {
				float n = TiConvert.toFloat(proxy.getProperty("minTop"));
				Log.d(LCAT, "minTop " + n);
				_proxy.setMinTop(n);
			}
			
			// This is our view
			view = new TiCompositeLayout(proxy.getActivity(), arrangement);
			
			/**
			 * Logs commented out from OnTouchListener
			 * It can really impact the performance
			 */
			OnTouchListener listener = new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// Log.d(LCAT, "Event: "+event.toString());
					
					// the LayoutParams does not exist when the view is created,
					// so let's define the variable here and store it
					_layout = _layout == null ? (LayoutParams) view.getLayoutParams() : _layout;
					
					// Same thing happens with the view itself
					positionLeft = positionLeft == 0 ? view.getLeft() : positionLeft;
					positionTop = positionTop == 0 ? view.getTop() : positionTop;
					
					// Get the "raw" x and y - which is the x and y in relation to the screen
					// And declare the ints and reuse them
					int eventX = Math.round(event.getRawX()),
						eventY = Math.round(event.getRawY()),
						_left = 0,
						_top = 0;
					
					// What to do in each case??
					switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							// Log.d(LCAT, "MotionEvent.ACTION_DOWN");
							
							// Check for event listeners here, we need to be sure that the JS is loaded
							// Also, store in boolean variable, we don't need to check every time, do we?
							// I also do my check here in the ACTION_DOWN because it only gets called once on each move
							if (hasListenerStart == false) {
								hasListenerStart = _proxy.hasListeners("start");
							}
							if (hasListenerMove == false) {
								hasListenerMove = _proxy.hasListeners("move");
							}
							if (hasListenerEnd == false) {
								hasListenerEnd = _proxy.hasListeners("end");
							}
							
							// Get the difference between the touch and the left/top of the view
							tempLeft = (positionLeft - eventX);
							tempTop = (positionTop - eventY);
							
							// Log.d(LCAT, "MotionEvent.ACTION_DOWN - tempLeft: "+tempLeft);
							// Log.d(LCAT, "MotionEvent.ACTION_DOWN - tempTop: "+tempTop);
							if (hasListenerStart) {
								KrollDict props = new KrollDict();
								props.put("left", positionLeft);
								props.put("top", positionTop);
								KrollDict center = new KrollDict();
								center.put("x", positionLeft + view.getWidth() / 2);
								center.put("y", positionTop + view.getHeight() / 2);
								props.put("center", center);
								_proxy.fireEvent("start", props);
							}
							
						break;
						case MotionEvent.ACTION_MOVE:
							// Log.d(LCAT, "MotionEvent.ACTION_MOVE");
							
							// If axis "y", leave the left position intact
							if (_proxy.hasAxisY) {
								_left = positionLeft;
								//Log.d(LCAT, "MotionEvent.ACTION_MOVE _proxy.hasAxisY - _left: "+_left);
							}
							// otherwise, adjust the "left" variable
							else {
								_left = (eventX + tempLeft);
								// Don't move more that maxLeft 
								if (_proxy.hasMaxLeft && _proxy.maxLeft < _left) {
									_left = _proxy.maxLeft;
								}
								// Don't move more that minLeft 
								if (_proxy.hasMinLeft && _proxy.minLeft > _left) {
									_left = _proxy.minLeft;
								}
								// Log.d(LCAT, "MotionEvent.ACTION_MOVE _proxy.hasAxisY NOT - _left: "+_left);
							}
							
							// If axis "x", leave the top position intact
							if (_proxy.hasAxisX) {
								_top = positionTop;
								// Log.d(LCAT, "MotionEvent.ACTION_MOVE _proxy.hasAxisX - _top: "+_top);
							}
							// otherwise, adjust the "top" variable
							else {
								_top = (eventY + tempTop);
								// Don't move more that maxTop 
								if (_proxy.hasMaxTop == true && _proxy.maxTop < _top) {
									_top = _proxy.maxTop;
								}
								// Don't move more that minTop 
								if (_proxy.hasMinTop == true && _proxy.minTop > _top) {
									_top = _proxy.minTop;
								}
								// Log.d(LCAT, "MotionEvent.ACTION_MOVE _proxy.hasAxisX NOT - _top: "+_top);
							}
							
							// set the new layout parameters
							_layout.optionLeft = new TiDimension(_left, TiDimension.TYPE_LEFT);
							_layout.optionTop = new TiDimension(_top, TiDimension.TYPE_LEFT);
							
							// now get the direction of the movement:
							// vertical
							if(oldTop > _top){
								directionVertical = "up"+"";
							} else if(oldTop < _top){
								directionVertical = "down"+"";
							} else {
								directionVertical = "neutral"+"";
							}
							// horizontal
							if(oldLeft > _left){
								directionHorizontal = "left"+"";
							} else if(oldLeft < _left){
								directionHorizontal = "right"+"";
							} else {
								directionHorizontal = "neutral"+"";
							}

							oldTop = _top;
							oldLeft = _left;
							
							// Log.d(LCAT, "MotionEvent.ACTION_MOVE _layout: "+_layout);
							
							// set the layout on the view
							view.setLayoutParams(_layout);
							
							if (hasListenerMove) {
								KrollDict props = new KrollDict();
								props.put("left", _left);
								props.put("top", _top);
								KrollDict center = new KrollDict();
								center.put("x", _left + view.getWidth() / 2);
								center.put("y", _top + view.getHeight() / 2);
								props.put("center", center);
								_proxy.fireEvent("move", props);
							}
							
						break;
						case MotionEvent.ACTION_UP:
							// Log.d(LCAT, "MotionEvent.ACTION_UP");
							
							// At the end of dragging, what should the left and top position be?
							_left = _proxy.hasAxisY ? positionLeft : (eventX + tempLeft);
							_top = _proxy.hasAxisX ? positionTop : (eventY + tempTop);
							
							// Log.d(LCAT, "MotionEvent.ACTION_UP - _left: " + _left);
							// Log.d(LCAT, "MotionEvent.ACTION_UP - _top:  " + _top);
							
							// Stop the view from going off the min and/or max top and/or left
							if (_proxy.hasMaxLeft == true && _proxy.maxLeft < _left) {
								_left = _proxy.maxLeft;
							}
							if (_proxy.hasMinLeft == true && _proxy.minLeft > _left) {
								_left = _proxy.minLeft;
							}
							if (_proxy.hasMaxTop == true && _proxy.maxTop < _top) {
								_top = _proxy.maxTop;
							}
							if (_proxy.hasMinTop == true && _proxy.minTop > _top) {
								_top = _proxy.minTop;
							}
							_layout.optionLeft = new TiDimension(_left, TiDimension.TYPE_LEFT);
							_layout.optionTop = new TiDimension(_top, TiDimension.TYPE_LEFT);
							view.setLayoutParams(_layout);
							
							if (hasListenerEnd) {

								KrollDict center = new KrollDict();
								KrollDict props = new KrollDict();
								center.put("x", _left + view.getWidth() / 2);
								center.put("y", _top + view.getHeight() / 2);
								
								props.put("left", _left);
								props.put("top", _top);
								props.put("directionHorizontal", directionHorizontal);
								props.put("directionVertical", directionVertical);
								
								props.put("center", center);
								_proxy.fireEvent("end", props);
							}
							// Log.d(LCAT, "MotionEvent.ACTION_UP - DONE");
							positionLeft = 0;
							positionTop = 0;
						break;
					}	
					// Not too sure about this one, true or false?
					return true;
				}
			};
			
			// Add the event to the view
			view.setOnTouchListener(listener);
			// Set the view to be the "native view"
			setNativeView(view);
			Log.d(LCAT, "Custom view created");
		}
		
		
		@Override
		public void registerForTouch() {
			
		}
	}
	
	/** 
	 * Public API
	 */
	// ------- Axis ------
	@Kroll.method @Kroll.setProperty
	public void setAxis(String axis) {
		if (axis.equals("x")) {
			this.hasAxisX = true;
		} else if (axis.equals("y")) {
			this.hasAxisY = true;
		}
	}
	@Kroll.method @Kroll.getProperty
	public boolean getAxisY() {
		return this.hasAxisY;
	}
	@Kroll.method @Kroll.getProperty
	public boolean getAxisX() {
		return this.hasAxisX;
	}

	// ------- Max Top ------
	@Kroll.method @Kroll.setProperty
	public void setMaxTop(float n) {
		this.hasMaxTop = true;
		this.maxTop = (int) n;
	}
	@Kroll.method @Kroll.getProperty
	public int getMaxTop() {
		return this.maxTop;
	}
	// ------- Min Top ------
	@Kroll.method @Kroll.setProperty
	public void setMinTop(float n) {
		this.hasMinTop = true;
		this.minTop = (int) n;
	}
	@Kroll.method @Kroll.getProperty
	public int getMinTop() {
		return this.minTop;
	}
	// ------- Max Left ------
	@Kroll.method @Kroll.setProperty
	public void setMaxLeft(float n) {
		this.hasMaxLeft = true;
		this.maxLeft = (int) n;
	}
	@Kroll.method @Kroll.getProperty
	public int getMaxLeft() {
		return this.maxLeft;
	}
	// ------- Min Left ------
	@Kroll.method @Kroll.setProperty
	public void setMinLeft(float n) {
		this.hasMinLeft = true;
		this.minLeft = (int) n;
	}
	@Kroll.method @Kroll.getProperty
	public int getMinLeft() {
		return this.minLeft;
	}

	@Override
	public TiUIView createView(Activity activity) {
		return new PEDraggableView(this);
	}
}