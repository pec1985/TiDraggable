Demo Video:

<iframe src="http://www.twitvid.com/embed.php?guid=LINLT&autoplay=0" title="Twitvid video player " class="twitvid-player" type="text/html" width="480" height="360" frameborder="0"></iframe>

---

## How to install the module:

1. Create a new property in tiapp.xml:

```
    <module version="0.9" platform="android">ti.draggable</module>
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

## Event Listeners (not working yet)

1. start: will fire on touch start
2. stop: will fire on when the view has finished moving

## TODOS
- Event listeners
- Animation
- More....