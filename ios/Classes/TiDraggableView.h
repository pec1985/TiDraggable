//
//  TiDraggableView.h
//  draggable
//
//  Created by Pedro Enrique on 1/21/12.
//  Copyright 2012 Pedro Enrique
//
//  Licensed under the Apache License, Version 2.0 (the "License");
//  you may not use this file except in compliance with the License.
//  You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.

#import "TiUIView.h"

@interface TiDraggableView : TiUIView
{

	// variables used in the DraggableView
	BOOL firstTime;

	CGPoint beginCenter;

	CGPoint offset;
	CGPoint locationStart;
	CGPoint location;
	
	UITouch *touchMove;
	UITouch *touchStart;
	
	float width;
	float height;
	float left;
	float top;
	
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
