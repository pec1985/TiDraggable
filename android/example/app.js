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

var win = Ti.UI.createWindow({
	backgroundColor:'#ccc',
	fullscreen: false,
	exitOnClose: true
});

var size = {
	height: Ti.Platform.displayCaps.platformHeight - 40,
	width: Ti.Platform.displayCaps.platformWidth
};

function Label(text,touchy){
	return Ti.UI.createLabel({
		height:30,left:10,right:10,
		backgroundColor:'white',
		textAlign:'center',
		text: text,
		touchEnabled:false
	});
}

var horizontal = Draggable.createView({
	left:0,
	top:0,
	width:100,
	height:100,
	backgroundColor:'red',
	minLeft: 0,
	maxLeft: size.width-100,
	axis:'x'
});

horizontal.add(Label('horizontal'));

var vertical = Draggable.createView({
	left:0,
	top:0,
	width:100,
	height:100,
	backgroundColor:'green',
	minTop: 0,
	maxTop: size.height-100,
	axis:'y'
});
vertical.add(Label('vertical'));

var free = Draggable.createView({
	top:0,
	left:0,
	width:100,
	height:100,
	backgroundColor:'blue'
});

free.add(Label('free'));

win.add(vertical);
win.add(free);
win.add(horizontal);

free.addEventListener('start', function(e){
	Ti.API.info('-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-');
	Ti.API.info('Event "start"');
	Ti.API.info('left: '+e.left);
	Ti.API.info('top:  '+e.top);
	Ti.API.info('center:'+JSON.stringify(e.center));
});

free.addEventListener('move', function(e){
	Ti.API.info('-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-');
	Ti.API.info('Event "move"');
	Ti.API.info('left: '+e.left);
	Ti.API.info('top:  '+e.top);
	Ti.API.info('center:'+JSON.stringify(e.center));
});

free.addEventListener('end', function(e){
	Ti.API.info('-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-');
	Ti.API.info('Event "end"');
	Ti.API.info('left: '+e.left);
	Ti.API.info('top:  '+e.top);
	Ti.API.info('directionVertical: '+ e.directionVertical);
	Ti.API.info('directionHorizontal: '+ e.directionHorizontal);
	Ti.API.info('center:'+JSON.stringify(e.center));
});

var unlockBackgroundView = Ti.UI.createView({
	bottom: 0,
	left: 0,
	right: 0,
	height: 90,
	touchEnabled:false,
	backgroundColor: '#666'
});
var unlockPlaceHolder = Ti.UI.createView({
	left:10,
	top:10,
	bottom:10,
	right:10,
	touchEnabled:false,
	backgroundColor: '#fff'
});
unlockBackgroundView.add(unlockPlaceHolder);
var unlockView = Draggable.createView({
	top:0,
	bottom:0,
	left:0,
	width:100,
	backgroundColor: '#999',
	borderRadius: 10,
	axis: 'x',
	minLeft: 0,
	maxLeft: size.width-120
});

unlockView.addEventListener('end', function(e){

	// to unlock it should be: size.width - 100
	// let's take 20 off from the margins: 10 + 10 = 20
	// let's take another 10 for some slack
	// width - 100 - 20 - 10 = width - 130

	if(e.left >= size.width-130){
		alert('Unlocked!!');
	}
	unlockView.animate({left: 0, duration:250}, function(){
		unlockView.left = 0;
	});
});

unlockPlaceHolder.add(unlockView);
win.add(unlockBackgroundView);


var drawer = Draggable.createView({
	width: 300,
	left: size.width-50,
	height: 500,
	backgroundColor: 'black',
	minLeft: size.width-300,
	maxLeft: size.width-50,
	axis: 'x'
});

var table = Ti.UI.createTableView({
	backgroundColor: 'white',
	data: [
		{title: 'Hello World 1'},
		{title: 'Hello World 1'},
		{title: 'Hello World 1'},
		{title: 'Hello World 1'},
		{title: 'Hello World 1'},
		{title: 'Hello World 1'},
		{title: 'Hello World 1'},
		{title: 'Hello World 1'},
		{title: 'Hello World 1'},
		{title: 'Hello World 1'},
		{title: 'Hello World 1'},
		{title: 'Hello World 1'},
		{title: 'Hello World 1'},
		{title: 'Hello World 1'},
		{title: 'Hello World 1'},
		{title: 'Hello World 1'},
		{title: 'Hello World 1'},
		{title: 'Hello World 1'},
		{title: 'Hello World 1'},
		{title: 'Hello World 1'},
		{title: 'Hello World 1'}
	],
	left: 50,
	right:0,
	top:0,
	bototm: 0
});

drawer.addEventListener('end', function(e){
	var val = 0;
	if(e.directionHorizontal == 'right'){
		val = size.width - 50;
	} else if(e.directionHorizontal == 'right'){
		val = size.width - 300;
	} else if(e.left == size.width - 300){
		val = size.width - 50;
	} else {
		val = size.width - 300;
	}
	drawer.animate({left: val, duration: 250}, function(){
		drawer.left = val;
	});

	Ti.API.info(e.directionHorizontal);
	Ti.API.info(e.directionVertical);
});

drawer.add(table);

win.add(drawer);
win.open();
