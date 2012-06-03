//
//  InfiniteScrollView.m
//  ScrollTest
//
//  Created by Pedro Enrique on 1/28/12.
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

#import "InfiniteScrollView.h"

@implementation InfiniteScrollView

-(id)initWithFrame:(CGRect)frame
{
	if (self = [super initWithFrame:frame])
	{
		self.contentSize = CGSizeMake(5000, self.frame.size.height);
		
		leftSide = 0;
		
		visibleViews = [[NSMutableArray alloc] init];
		containerView = [[UIView alloc] init];
		containerView.frame = CGRectMake(0, 0, self.contentSize.width, self.contentSize.height/2);
		[self addSubview:containerView];
		
//		[containerView setUserInteractionEnabled:NO];
		
		contentWidth = [self contentSize].width;
		[self setShowsHorizontalScrollIndicator:NO];
	}
	return self;
}

-(void)resizeWithFrame:(CGRect)frame
{
	self.contentSize = CGSizeMake(5000, frame.size.height);
	containerView.frame = CGRectMake(0, 0, self.contentSize.width, self.contentSize.height);
	contentWidth = [self contentSize].width;
}

-(void)addView:(UIView *)view withSize:(CGSize)size
{
	[view setFrame:CGRectMake(leftSide, 0, size.width, size.height)];
	[visibleViews addObject:view];
	[containerView addSubview:view];
	leftSide += size.width;
}

- (void)recenterIfNecessary
{
	CGPoint currentOffset = [self contentOffset];
	CGFloat centerOffsetX = (contentWidth - [self bounds].size.width) / 2.0;
	CGFloat distanceFromCenter = fabs(currentOffset.x - centerOffsetX);
	
	if (distanceFromCenter > (contentWidth / 4.0))
	{
		self.contentOffset = CGPointMake(centerOffsetX, currentOffset.y);
		for (UIView *view in visibleViews)
		{
			//			CGPoint center = [containerView convertPoint:view.center toView:self];
			CGPoint center = view.center;
			center.x += (centerOffsetX - currentOffset.x);
			//			view.center = [self convertPoint:center toView:containerView];
			view.center = center;
		}
	}
}

- (void)placeNewViewOnRight:(CGFloat)rightEdge
{
	UIView *view = [visibleViews objectAtIndex:0];
	
	CGRect frame = [view frame];
	frame.origin.x = rightEdge;
	[view setFrame:frame];
	
	[visibleViews removeObject:view];
	[visibleViews addObject:view];
}

- (void)placeNewViewOnLeft:(CGFloat)leftEdge
{
	UIView *view = [visibleViews lastObject];
	
	CGRect frame = [view frame];
	frame.origin.x = leftEdge - frame.size.width;
	[view setFrame:frame];
	
	[visibleViews removeObject:view];
	[visibleViews insertObject:view atIndex:0];
}

- (void)rearrangeViewsFromMinX:(CGFloat)minimumVisibleX toMaxX:(CGFloat)maximumVisibleX
{
	if([visibleViews count] == 0)
		return;
	UIView *view = [visibleViews lastObject];
	CGFloat rightEdge = CGRectGetMaxX([view frame]);
	if (rightEdge < maximumVisibleX)
	{
		[self placeNewViewOnRight:rightEdge];
	}

	view = [visibleViews objectAtIndex:0];
	CGFloat leftEdge = CGRectGetMinX([view frame]);
	if (leftEdge > minimumVisibleX)
	{
		[self placeNewViewOnLeft:leftEdge];
	}
}

- (void)layoutSubviews
{
	[super layoutSubviews];
	
	[self recenterIfNecessary];
	
	CGRect visibleBounds = [self convertRect:[self bounds] toView:containerView];
	CGFloat minimumVisibleX = CGRectGetMinX(visibleBounds);
	CGFloat maximumVisibleX = CGRectGetMaxX(visibleBounds);
	
	[self rearrangeViewsFromMinX:minimumVisibleX toMaxX:maximumVisibleX];
}

@end