//
//  TiDraggableView.m
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

#import "TiDraggableView.h"
#import "TiDraggableViewProxy.h"
#import "TiUtils.h"
#import "TiRect.h"
#import "TiPoint.h"

@implementation TiDraggableView

-(void)dealloc
{
	[super dealloc];
}

-(NSDictionary *)_center
{
	return [NSDictionary dictionaryWithObjectsAndKeys:
							[NSNumber numberWithFloat:self.center.x], @"x", 
							[NSNumber numberWithFloat:self.center.y], @"y",
							nil];
}

- (id)init
{
    self = [super init];
    if (self) {
        UIPanGestureRecognizer *panRecognizer = [[UIPanGestureRecognizer alloc] initWithTarget:self action:@selector(panDetected:)];
        [self addGestureRecognizer:panRecognizer];
        [panRecognizer release];
    }
    return self;
}

#pragma mark view resize

-(void)frameSizeChanged:(CGRect)frame bounds:(CGRect)bounds
{

	// get the width and height of "self" which is a TiUIView (DraggableView)
	
	width = frame.size.width;
	height = frame.size.height;

	if(!firstTime)
	{
		firstTime = YES;
		// minTop and minLeft are the origin of the view
		minTop = minTop == 0.0 ? frame.origin.y : minTop;
		minLeft = minLeft == 0.0 ? frame.origin.x : minLeft;
	
	}
	[super frameSizeChanged:frame bounds:bounds];

}


// ========================================================================

#pragma mark touch events
- (void)panDetected:(UIPanGestureRecognizer *)panRecognizer
{
	if([self.proxy _hasListeners:@"start"] && [panRecognizer state] == UIGestureRecognizerStateBegan)
	{
        left = self.frame.origin.x;
        top = self.frame.origin.y;
		NSDictionary *tiProps = [NSDictionary dictionaryWithObjectsAndKeys:
                                 [NSNumber numberWithFloat:left], @"left",
                                 [NSNumber numberWithFloat:top], @"top",
                                 [self _center], @"center",
                                 nil];
		[self.proxy fireEvent:@"start" withObject:tiProps];
	}
    
    CGPoint translation = [panRecognizer translationInView:self.superview];
    CGPoint imageViewPosition = self.center;
    
    
    if(axis && [axis isEqualToString:@"x"])
	{
		imageViewPosition.x += translation.x;
		imageViewPosition.y = imageViewPosition.y;
	}
	else
        if(axis && [axis isEqualToString:@"y"])
        {
            imageViewPosition.x = imageViewPosition.x;
            imageViewPosition.y += translation.y;
        }
        else 
        {
            imageViewPosition.x += translation.x;
            imageViewPosition.y += translation.y;
        }
    
    if(hasMaxLeft || hasMaxTop || hasMinLeft || hasMinTop)
    {
        CGSize size = self.frame.size;
        if(hasMaxLeft && imageViewPosition.x - size.width/2 > maxLeft)
        {
            imageViewPosition.x = maxLeft + size.width/2;
        } else
        if(hasMinLeft && imageViewPosition.x - size.width/2 < minLeft)
        {
            imageViewPosition.x = minLeft + size.width/2;
        } else
        if(hasMaxTop && imageViewPosition.y - size.height/2 > maxTop)
        {
            imageViewPosition.y = maxTop + size.height/2;
        } else
        if(hasMinTop && imageViewPosition.y - size.height/2 < minTop)
        {
            imageViewPosition.y = minTop + size.height/2;
        }
    }
    
    
    self.center = imageViewPosition;
    [panRecognizer setTranslation:CGPointZero inView:self.superview];
    
    // MOVE LISTENER
    if([self.proxy _hasListeners:@"move"] && [panRecognizer state] == UIGestureRecognizerStateChanged)
	{
        left = self.frame.origin.x;
        top = self.frame.origin.y;
		NSDictionary *tiProps = [NSDictionary dictionaryWithObjectsAndKeys:
                                 [NSNumber numberWithFloat:left], @"left",
                                 [NSNumber numberWithFloat:top], @"top",
                                 [self _center], @"center",
                                 nil];
		[self.proxy fireEvent:@"move" withObject:tiProps];								
	}
    
	if([self.proxy _hasListeners:@"end"] && [panRecognizer state] == UIGestureRecognizerStateEnded)
	{
        left = self.frame.origin.x;
        top = self.frame.origin.y;
		NSDictionary *tiProps = [NSDictionary dictionaryWithObjectsAndKeys:
                                 [NSNumber numberWithFloat:left], @"left",
                                 [NSNumber numberWithFloat:top], @"top",
                                 [self _center], @"center",
                                 nil];
		[self.proxy fireEvent:@"end" withObject:tiProps];								
	}
    
}

// ========================================================================

#pragma mark JavaScript properties

-(void)setAxis_:(id)args
{
	ENSURE_SINGLE_ARG(args, NSString);
	axis = args;
}

-(void)setMaxTop_:(id)args
{
	ENSURE_SINGLE_ARG(args, NSNumber);
    hasMaxTop = YES;
	maxTop = [TiUtils floatValue:args];
}

-(void)setMaxLeft_:(id)args
{
	ENSURE_SINGLE_ARG(args, NSNumber);
    hasMaxLeft = YES;
	maxLeft = [TiUtils floatValue:args];
}

-(void)setMinTop_:(id)args
{
	ENSURE_SINGLE_ARG(args, NSNumber);
    hasMinTop = YES;
	minTop = [TiUtils floatValue:args];
}

-(void)setMinLeft_:(id)args
{
	ENSURE_SINGLE_ARG(args, NSNumber);
    hasMinLeft = YES;
	minLeft = [TiUtils floatValue:args];
}
- (void)pinchDetected:(UIPinchGestureRecognizer *)pinchRecognizer
{
    CGFloat scale = pinchRecognizer.scale;
    self.transform = CGAffineTransformScale(self.transform, scale, scale);
    pinchRecognizer.scale = 1.0;

}

- (void)rotationDetected:(UIRotationGestureRecognizer *)rotationRecognizer
{
    CGFloat angle = rotationRecognizer.rotation;
    self.transform = CGAffineTransformRotate(self.transform, angle);
    rotationRecognizer.rotation = 0.0;

}

-(void)setCanResize_:(id)args
{
    if([TiUtils boolValue:args] == YES)
    {
        UIPinchGestureRecognizer *pinchRecognizer = [[UIPinchGestureRecognizer alloc] initWithTarget:self action:@selector(pinchDetected:)];
        [pinchRecognizer setDelegate:self];
        [self addGestureRecognizer:pinchRecognizer];
        [pinchRecognizer release];       
    }
}

-(void)setCanRotate_:(id)args
{
    if([TiUtils boolValue:args] == YES)
    {
        UIRotationGestureRecognizer *rotationRecognizer = [[UIRotationGestureRecognizer alloc] initWithTarget:self action:@selector(rotationDetected:)];
        [rotationRecognizer setDelegate:self];
        [self addGestureRecognizer:rotationRecognizer];
        [rotationRecognizer release];
    }
}

-(BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldRecognizeSimultaneouslyWithGestureRecognizer:(UIGestureRecognizer *)otherGestureRecognizer
{
    return YES;
}

@end
