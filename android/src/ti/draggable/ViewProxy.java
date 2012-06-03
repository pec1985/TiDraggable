package ti.draggable;


import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.TiContext;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.util.TiConvert;
import org.appcelerator.titanium.view.TiCompositeLayout;
import org.appcelerator.titanium.view.TiUIView;
import org.appcelerator.titanium.view.TiCompositeLayout.LayoutArrangement;

import android.app.Activity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

@Kroll.proxy(creatableInModule=DraggableModule.class, propertyAccessors = {
	TiC.PROPERTY_TITLE,
	TiC.PROPERTY_TITLEID,
	TiC.PROPERTY_COLOR,
	TiC.PROPERTY_ENABLED,
	TiC.PROPERTY_FONT,
	TiC.PROPERTY_IMAGE,
	TiC.PROPERTY_TEXT_ALIGN,
	TiC.PROPERTY_VERTICAL_ALIGN
})
public class ViewProxy extends TiViewProxy
{
	private static final String LCAT = "TiDraggable";

	public TiCompositeLayout view;
	private ViewProxy _proxy;
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
	
	
	public ViewProxy()
	{
	}
	public ViewProxy(TiContext tiContext)
	{
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
			if(proxy.hasProperty("axis")){
				String axis = TiConvert.toString(proxy.getProperty("axis"));
				Log.d(LCAT, "hasAxis "+axis);
				_proxy.setAxis(axis);
			}
			if(proxy.hasProperty("maxLeft")){
				float n = TiConvert.toFloat(proxy.getProperty("maxLeft"));
				Log.d(LCAT, "maxLeft "+n);
				_proxy.setMaxLeft(n);
			}
			if(proxy.hasProperty("minLeft")){
				float n = TiConvert.toFloat(proxy.getProperty("minLeft"));
				Log.d(LCAT, "minLeft "+n);
				_proxy.setMinLeft(n);
			}
			if(proxy.hasProperty("maxTop")){
				float n = TiConvert.toFloat(proxy.getProperty("maxTop"));
				Log.d(LCAT, "maxTop "+n);
				_proxy.setMaxTop(n);
			}
			if(proxy.hasProperty("minTop")){
				float n = TiConvert.toFloat(proxy.getProperty("minTop"));
				Log.d(LCAT, "minTop "+n);
				_proxy.setMinTop(n);
			}
			
			OnTouchListener listener = new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					
					// Remove for better performance
					// Log.d(LCAT, "Event: "+event.toString());
					
					// Grab the last position of the view if it's already stored, we don't
					// want to query the view every single time, could be expensive
					positionLeft = positionLeft == 0 ? view.getLeft() : positionLeft;
					positionTop = positionTop == 0 ? view.getTop() : positionTop;
					
					// Get the "raw" x and y - which is the x and y in relation to the screen
					int eventX = Math.round(event.getRawX());
					int eventY = Math.round(event.getRawY());
					
					// What to do in each case??
					switch(event.getAction()){
						case MotionEvent.ACTION_DOWN:
							// Get the difference between the toch and the left/top of the view
							tempLeft = (positionLeft - eventX);
							tempTop = (positionTop - eventY);
						break;
						case MotionEvent.ACTION_MOVE:
							// If axis "y" is false, only move left and right 
							if(_proxy.hasAxisY == false){
								int _left = (eventX+tempLeft);
								// Don't move more that maxLeft 
								if(_proxy.hasMaxLeft == true && _proxy.maxLeft < _left){
									_left = _proxy.maxLeft;
								}
								// Don't move more that minLeft 
								if(_proxy.hasMinLeft == true && _proxy.minLeft > _left){
									_left = _proxy.minLeft;
								}
								// Tell the proxy to set the "top" property
								_proxy.setPropertyAndFire("left", _left);		
							}
							// If axis "x" is false, only move up and down 
							if(_proxy.hasAxisX == false){
								int _top = (eventY + tempTop);
								// Don't move more that maxTop 
								if(_proxy.hasMaxTop == true && _proxy.maxTop < _top){
									_top = _proxy.maxTop;
								}
								// Don't move more that minTop 
								if(_proxy.hasMinTop == true && _proxy.minTop > _top){
									_top = _proxy.minTop;
								}
								// Tell the proxy to set the "top" property
								_proxy.setPropertyAndFire("top",_top);
							}
							break;
						case MotionEvent.ACTION_UP:
							// At the end of, what should the left and top position be?
							int _left = positionLeft = _proxy.hasAxisY == false ? (eventX + tempLeft) : positionLeft;
							int _top = positionTop = _proxy.hasAxisX == false ? (eventY + tempTop) : positionTop;
							
							// Stop the view from going off the min and/or max top and/or left
							if(_proxy.hasMaxLeft == true && _proxy.maxLeft < _left){
								_left = _proxy.maxLeft;
							}
							if(_proxy.hasMinLeft == true && _proxy.minLeft > _left){
								_left = _proxy.minLeft;
							}
							if(_proxy.hasMaxTop == true && _proxy.maxTop < _top){
								_top = _proxy.maxTop;
							}
							if(_proxy.hasMinTop == true && _proxy.minTop > _top){
								_top = _proxy.minTop;
							}
							// Tell the proxy to set the "top" property
							_proxy.setPropertyAndFire("left", _left);
							_proxy.setPropertyAndFire("top",_top);							
						break;
					}
					return false;
				}
				
			};
			
			// This is our view
			view = new TiCompositeLayout(proxy.getActivity(), arrangement);
			
			// Add the event to the view
			view.setOnTouchListener(listener);
			// Set the view to be the "native view"
			setNativeView(view);
			Log.d(LCAT, "Custom view created");
		}
		
		
		@Override
		public void registerForTouch(){
			
		}

	}
	
	/* @TODO
	 * This is what the events have to look like
	 *	NSDictionary *tiProps = [NSDictionary dictionaryWithObjectsAndKeys:
	 *								[NSNumber numberWithFloat:left], @"left",
	 *								[NSNumber numberWithFloat:top], @"top",
	 *								[self _center], @"center",
	 *								nil];
	 *	[self.proxy fireEvent:@"start" withObject:tiProps];
	 */

	// ------- Axis ------
	@Kroll.method @Kroll.setProperty
	public void setAxis(String axis){
		if(axis.equals("x")){
			this.hasAxisX = true;			
		} else
		if(axis.equals("y")){
			this.hasAxisY = true;			
		}
	}
	@Kroll.method @Kroll.getProperty
	public boolean getAxisY(){
		return this.hasAxisY;
	}
	@Kroll.method @Kroll.getProperty
	public boolean getAxisX(){
		return this.hasAxisX;
	}
	
	// ------- Max Top ------
	@Kroll.method @Kroll.setProperty
	public void setMaxTop(float n){
		this.hasMaxTop = true;
		this.maxTop = (int)n;
	}
	@Kroll.method @Kroll.getProperty
	public int getMaxTop(){
		return this.maxTop;
	}
	// ------- Min Top ------
	@Kroll.method @Kroll.setProperty
	public void setMinTop(float n){
		this.hasMinTop = true;
		this.minTop = (int)n;
	}
	@Kroll.method @Kroll.getProperty
	public int getMinTop(){
		return this.minTop;
	}
	// ------- Max Left ------
	@Kroll.method @Kroll.setProperty
	public void setMaxLeft(float n){
		this.hasMaxLeft = true;
		this.maxLeft = (int)n;
	}
	@Kroll.method @Kroll.getProperty
	public int getMaxLeft(){
		return this.maxLeft;
	}
	// ------- Min Left ------
	@Kroll.method @Kroll.setProperty
	public void setMinLeft(float n){
		this.hasMinLeft = true;
		this.minLeft = (int)n;
	}
	@Kroll.method @Kroll.getProperty
	public int getMinLeft(){
		return this.minLeft;
	}
	
	@Override
	public TiUIView createView(Activity activity)
	{
		return new PEDraggableView(this);
	}
	
}