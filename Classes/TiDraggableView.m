//
//  TiDraggableView.m
//  draggable
//
//  Created by Pedro Enrique on 1/21/12.
//  Copyright (c) 2012 Appcelerator. All rights reserved.
//

#import "TiDraggableView.h"
#import "TiUtils.h"

@implementation TiDraggableView

#pragma mark view resize

-(void)frameSizeChanged:(CGRect)frame bounds:(CGRect)bounds
{

	// get the width and height of "self" which is a TiUIView (DraggableView)
	
	width = frame.size.width;
	height = frame.size.height;

	// minTop and minLeft are the origin of the view
	// @TODO: allow developer re-asign these variables
	minTop = frame.origin.y;
	minLeft = frame.origin.x;

}

// ========================================================================

#pragma mark touch events

// touchStart event
-(void) touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
	
	// get the center of the view
	beginCenter = self.center;
	
	touchStart = [touches anyObject];
	locationStart = [touchStart locationInView:self.superview];

	// get the touch point in relation to it's parent
	offsetX = locationStart.x - beginCenter.x;
	offsetY = locationStart.y - beginCenter.y;
	
	// boolean to see if the view has moved for the touchEnd event
	hasMoved = false;
}

// touchMove event
-(void)touchesMoved:(NSSet *)touches withEvent:(UIEvent *)event
{
	// boolean to know if the view has moved. 
	hasMoved = true;
	
	// get the coordinates while moving around
	touchMove = [touches anyObject];
	location = [touchMove locationInView:self.superview];

	// get the center of the view to see in chich direction we're moving (before the move)
	oldLeft = self.center.x;
	oldTop = self.center.y;

	// in which axis to move
	if(axis && [axis isEqualToString:@"x"])
	{
		location.x -= offsetX;
		location.y = beginCenter.y;
	}
	else
	if(axis && [axis isEqualToString:@"y"])
	{
		location.y -= offsetY;
		location.x = beginCenter.x;
	}
	else 
	{
		location.x -= offsetX;
		location.y -= offsetY;
	}

	// relocate the view
	self.center = location;
	
	// get the center of the view to see in chich direction we're moving (after the move)
	newLeft = self.center.x;
	newTop = self.center.y;
}

// touchEnd event
-(void)touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event
{	
	// get the coordinates of the view
	
	float left = self.frame.origin.x;
	float top = self.frame.origin.y;
	
	// do this is the view has moved:
	if(hasMoved == true)
	{
		// IF we have a maxLeft, reposition, reposition accordingly
		if(oldLeft > newLeft && maxLeft)
		{
			left = minLeft;
		}
		else if(maxLeft)
		{
			left = maxLeft;
		}
	
		// IF we have a maxTop, reposition accordingly
		if(oldTop > newTop && maxTop)
		{
			top = minTop;
		}
		else if(maxTop)
		{
			top = maxTop;
		}
	} else {
		
		// if the view has NOT moved, move it (like a click event)
		if(maxTop)
		{
			top = top == maxTop ? minTop : maxTop;
		}
		if(maxLeft)
		{
			left = left == maxLeft ? minLeft : maxLeft;
		}
	}
	
	// animate the view
	[UIView beginAnimations:@"end_dragging" context:nil];
	self.frame = CGRectMake(left, top, width, height);	
	[UIView commitAnimations];
	
	
	// @TODO: add an event listener with the new position of the view
		
	// reset the hasMoved flag
	hasMoved = false;

}

// ========================================================================

#pragma Mark JavaScript properties

-(void)setAxis_:(id)args
{
	ENSURE_SINGLE_ARG(args, NSString);
	axis = args;
}

-(void)setMaxTop_:(id)args
{
	ENSURE_SINGLE_ARG(args, NSNumber);
	maxTop = [TiUtils floatValue:args];
}

-(void)setMaxLeft_:(id)args
{
	ENSURE_SINGLE_ARG(args, NSNumber);
	maxLeft = [TiUtils floatValue:args];
}


@end
