Demo Video:

http://www.screenr.com/S3as

---

## How to install the module:

1. Create a new property in tiapp.xml:

```
    <module version="1.2" platform="iphone">ti.draggable</module>
```

2. Copy the zip file to the root of your project.
3. require the module in your application:

```
var Draggable = require('ti.draggable');
```

4. Clean your build directory
5. Build.

## How to use the module
To create a draggable view, simple create it like so:
```
   var view = Draggable.createView({ _props });
```

To create an infinite scroll view, create it like this:
```
   var scroll = Draggable.createInfiniteScroll({ _props });
```

Treat both of these views as a normal Ti.UI.Views.
For the draggable view, keep in mind that it has the draggable properties already build in; if nothing is specified, it will be draggable all over the screen.

## Draggable View Properties:

### These will make the view slide to their corresponding positions

* maxLeft: (int)
* minLeft: (int)
* maxTop: (int)
* minTop: (int)

### This will make the view stick to it's axis:

* axis: (String) "x" or "y"

### canRotate ( bool ) { default: false }

* Wether the view can rotate with two fingers

### canResize ( bool ) { default: false }

* Wether the view can resize with two fingers

## Event Listeners

1. start: will fire on touch start
2. stop: will fire on when the view has finished moving


## Infinite Scroll View Properties:

### One property at the moment:

* views: (array of views)

## Event Listeners

1. click: will fire on click. It contains an index number of the view clicked as well as a reference to the it.

---

## What's New in Version 1.2

1. Rewrote entire gesture logic
2. Removed animation to make the behavior the same as the android version   
(this can be done in javascript)
3. Added rotation and resizing features


## What's New in Version 1.1

1. Fixed bug where DraggableView would go to its original position after device rotation.
2. Call super on touchmove, touchstart, touchend, and framesizechange

## TODOS
Let's add more functionality to this module. I don't have a list of "todos", but please, feel free to add to it. Be creative, have ideas, submit pull requests.