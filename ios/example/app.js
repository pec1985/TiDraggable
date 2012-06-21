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


var Draggable = require('ti.draggable');

var WIDTH = 320;
var HEIGHT = 460;

var win = Ti.UI.createWindow({
	backgroundColor:'#ccc'
});

function Label(text){
	return Ti.UI.createLabel({height:20,left:10,right:10,backgroundColor:'white', textAlign:'center', text: text});
}

var horizontal = Draggable.createView({
	left:0,
	top:0,
	width:100,
	height:100,
	backgroundColor:'red',
    minLeft:0,
    maxLeft:WIDTH-100,
	axis:'x'
});


horizontal.add(Label('horizontal'));

var vertical = Draggable.createView({
	left:0,
	top:0,
	width:100,
	height:100,
	backgroundColor:'green',
    minTop:0,
	maxTop:HEIGHT-100,
	axis:'y'
});
vertical.add(Label('vertical'));

var free = Draggable.createView({
	top:0,
	left:0,
	width:100,
	height:100,
	canResize:true,
	canRotate:true,
	backgroundColor:'blue'
});
free.add(Label('free'));

free.addEventListener('end', function(e){
	Ti.API.info(e);
});
free.addEventListener('start', function(e){
	Ti.API.info(e);
});

var views = [];
var num = 0;
for(var i = 0; i < 20; i++){
	num = (i+1);
	if(num<100){
		num > 9 ? num='0'+num : num='00'+num;
	}

	views.push(
		i % 2 ?
		Ti.UI.createView({
			height:100,
			width:100,
			backgroundColor:"#"+((1<<24)*Math.random()|0).toString(16)		
		})
		:
		Ti.UI.createImageView({
			height:100,
			width:150,
			image:'http://pec1985.com/tests/album/WallPaper'+num+'.jpg'
		})
		
	);
}
var otherView = Ti.UI.createView({
	width:200,
	height:75,
	backgroundColor:'black'
});
otherView.add(Ti.UI.createLabel({color:'white',text:'this is a child label'}));

views.push(otherView);

var scroll = Draggable.createInfiniteScroll({
	height:100,
	bottom:0,
	left:0,
	right:0,
	views:views
});

scroll.addEventListener('click', function(e){
	Ti.API.info(e);
});

win.add(scroll);

win.add(vertical);
win.add(free);
win.add(horizontal);

win.open();


