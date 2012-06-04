Demo Video:

http://www.screenr.com/2Y08

---

## How to install the module:

1. Create a new property in tiapp.xml:

```
    <module version="1.0" platform="android">ti.draggable</module>
```

2. Copy the zip file found in the ```dist``` folder to the root of your project.
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

Keep in mind that the draggable view has the draggable properties already build in; if nothing is specified, it will be draggable all over the screen.

## Draggable View Properties:

### These will make the view stop at their corresponding positions

* maxLeft: (int)
* minLeft: (int)
* maxTop: (int)
* minTop: (int)

### This will make the view stick to it's axis:

* axis: (String) "x" or "y"

## Event Listeners

1. start: will fire on touch start
2. move: will fire on touch move
3. stop: will fire on touch end


## How to build the module from source
1. Open the ```.classpath``` file and modify the parameters
2. Do the same thing with the build.properties
3. Read the Android Module Development Guide found in Appcelerator's wiki

## TODOS
Let's add more functionality to this module. I don't have a list of "todos", but please, feel free to add to it. Be creative, have ideas, submit pull requests.