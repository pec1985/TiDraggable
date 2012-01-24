/*
 *   Copyright 2012 Pedro Enrique
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *   
 */


Ti.Draggable = require('ti.draggable');
var win = Ti.UI.createWindow({
	backgroundColor:'#ccc'
});

function Label(text){
	return Ti.UI.createLabel({height:20,left:10,right:10,backgroundColor:'white', textAlign:'center', text: text});
}

var horizontal = Ti.Draggable.createView({
	left:0,
	top:0,
	width:100,
	height:100,
	backgroundColor:'red',

	maxLeft:win.size.width-100,
	axis:'x'
});


horizontal.add(Label('horizontal'));

var vertical = Ti.Draggable.createView({
	left:0,
	top:0,
	width:100,
	height:100,
	backgroundColor:'green',

	maxTop:win.size.height-100,
	axis:'y'
});
vertical.add(Label('vertical'));

var free = Ti.Draggable.createView({
	top:0,
	left:0,
	width:100,
	height:100,
	backgroundColor:'blue'
});
free.add(Label('free'));

free.addEventListener('end', function(e){
	Ti.API.info(e);
});
free.addEventListener('start', function(e){
	Ti.API.info(e);
});


win.add(vertical);
win.add(free);
win.add(horizontal);

win.open();


