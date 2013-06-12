 $(document).ready(function(){
	// set the height of the content area according to the browser height
	var bottomBufferHeight = 220;
	var windowHeight = $(window).height();
	
	$('#center').height(windowHeight - bottomBufferHeight);
});
 
 $(document).ready(function(){
     $(window).resize(function() {
             var bottomBufferHeight = 220;
             var windowHeight = $(window).height();

             $('#center').height(windowHeight - bottomBufferHeight);
     })});
