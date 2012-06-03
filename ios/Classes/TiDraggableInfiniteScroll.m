//
//  TiDraggableInfiniteScroll.m
//  draggable
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

#import "TiDraggableInfiniteScroll.h"
#import "TiUtils.h"
#import "TiViewProxy.h"

@implementation TiDraggableInfiniteScroll

-(void)dealloc
{
	RELEASE_TO_NIL(clickGestureRecognizer);
	RELEASE_TO_NIL(arryOfViews);
	RELEASE_TO_NIL(infiniteScroll);
	[super dealloc];
}

-(InfiniteScrollView *)infiniteScroll
{
	if(infiniteScroll == nil)
	{
		infiniteScroll = [[InfiniteScrollView alloc] initWithFrame:CGRectZero];
		clickGestureRecognizer = [[UITapGestureRecognizer alloc]
														  initWithTarget:self action:@selector(handleClick:)];
		clickGestureRecognizer.numberOfTapsRequired = 1; 
		[infiniteScroll addGestureRecognizer:clickGestureRecognizer];

		[self addSubview:infiniteScroll];
	}
	return infiniteScroll;
}

-(void)handleClick:(UITapGestureRecognizer*)recognizer
{
	NSMutableDictionary *tiEvent = [NSMutableDictionary dictionary];
	id view = recognizer.view;

	CGPoint loc = [recognizer locationInView:view];
	id subview = [view hitTest:loc withEvent:nil];

	if([subview isKindOfClass: [TiUIView class]])
	{
		TiUIView * _subview = subview;
		TiProxy * _proxy = _subview.proxy;
		[tiEvent setObject:_proxy forKey:@"view"];
		[tiEvent
			setObject	: [NSString stringWithFormat:@"%i", [arryOfViews indexOfObject:_proxy]]
			forKey		: @"index"
		];
	}
	[self.proxy fireEvent:@"click" withObject:tiEvent];
	
}


#pragma mark view resize

-(void)frameSizeChanged:(CGRect)frame bounds:(CGRect)bounds
{
	[TiUtils setView:[self infiniteScroll] positionRect:bounds];
	[infiniteScroll resizeWithFrame:frame];
}

-(void)setViews_:(id)args
{
	ENSURE_ARRAY(args);
	if(infiniteScroll == nil)
	{
		[self infiniteScroll];
	}
	if(arryOfViews == nil)
	{
		arryOfViews = [[NSMutableArray alloc] init];
	}
	for(id views in args)
	{
		if([views isKindOfClass:[TiViewProxy class]])
		{
			TiViewProxy *viewProx = views;
			UIView *_view = viewProx.view;
			CGSize _size = _view.frame.size;
	
			[_view setAutoresizingMask:UIViewAutoresizingNone];
	
			[infiniteScroll addView:_view withSize:_size];
			[arryOfViews addObject:viewProx];
		}
	}
}

@end
