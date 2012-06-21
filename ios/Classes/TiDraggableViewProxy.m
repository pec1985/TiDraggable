//
//  TiDraggableViewProxy.m
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

#import "TiDraggableViewProxy.h"
#import "TiDraggableView.h"
#import "TiPoint.h"
@implementation TiDraggableViewProxy


-(id)left
{
    TiDraggableView *v = (TiDraggableView *)[self view];
    return [NSNumber numberWithFloat:v.frame.origin.x];
}

-(id)center
{
    TiDraggableView *v = (TiDraggableView *)[self view];
    return [[[TiPoint alloc] initWithPoint:v.center] autorelease];
}

-(id)top
{
    TiDraggableView *v = (TiDraggableView *)[self view];
    return [NSNumber numberWithFloat:v.frame.origin.y];
}

@end
