Demo Video:

http://www.screenr.com/S3as

---

## How to install the module:

1. Create a new property in tiapp.xml:
```
    <module version="1.0" platform="iphone">ti.draggable</module>
```
2. Copy the zip file to the root of your project.
3. require the module in your application:
```
Ti.Draggable = require('ti.draggable');
```
4. Clean your build directory
5. Build.

## How to use the module
To create a draggable view, simple create it like so:
```
   var view = Ti.Draggable.createView({ _props });
```

To create an infite scroll view, create it like this:
```
   var scroll = Ti.Draggable.createInfiniteScroll({ _props });
```

Treat both of these views as a normal Ti.UI.Views.
For the dragable view, keep in mind that it has the draggable properties already build in; if nothing is specified, it will be draggable all over the screen.

## Draggable View Properties:

### These will make the view slide to their coresponding positions

* maxLeft: (int)
* minLeft: (int)
* maxTop: (int)
* minTop: (int)

### This will make the view stick to it's axis:

* axis: (String) "x" or "y"

## Event Listeners

1. start: will fire on touch start
2. top: will fire on when the view has finished moving


## Infinite Scroll View Properties:

### One property at the moment:

* views: (array of views)

## Event Listeners

1. click: will fire on click. It contains an index number of the view clicked as well as a reference to the it.

---

## TODOS
Let's add more functionality to this module. I don't have a list of "todos", but please, feel free to add to it. Be creative, have ideas, submit pull requests.