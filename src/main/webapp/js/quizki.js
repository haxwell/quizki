
requirejs.config({
    //By default load any module IDs from js
    baseUrl: 'js',
    //except, if the module ID starts with "app",
    //load it from the js/app directory. paths
    //config is relative to the baseUrl, and
    //never includes a ".js" extension since
    //the paths config could be for a directory.
    paths: {
        pkgs: '/pkgs',
        jquery: '/pkgs/jquery'
    },
    waitSeconds: 999
});

// Start the main app logic.
requirejs(['jquery','pkgs/backbone.js/backbone','pkgs/underscore.js/underscore', 'pkgs/bootstrap/js/bootstrap'],
function   ($,        backbone,   underscore) {
    //jQuery, canvas and the app/sub module are all
    //loaded and can be used here now.
});

//$('body').on('dblclick', function () { alert("body dblclick!"); });