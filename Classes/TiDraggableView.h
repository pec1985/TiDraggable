//
//  TiDraggableView.h
//  draggable
//
//  Created by Pedro Enrique on 1/21/12.
//  Copyright (c) 2012 Appcelerator. All rights reserved.
//

#import "TiUIView.h"

@interface TiDraggableView : TiUIView
{

	// variables used in the DraggableView
	
	CGPoint beginCenter;

	CGPoint offset;
	CGPoint locationStart;
	CGPoint location;
	
	UITouch *touchMove;
	UITouch *touchStart;
	
	float width;
	float height;
	
	float offsetX;
	float offsetY;
	
	float oldLeft;
	float newLeft;
	float oldTop;
	float newTop;
	
	float maxLeft;
	float minLeft;
	float maxTop;
	float minTop;
	
	NSString *axis;
	
	bool hasMoved;
}

@end
