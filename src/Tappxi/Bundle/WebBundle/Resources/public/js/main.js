require.config( {
  paths:{
    underscore: 'vendor/underscore',
    backbone: 'vendor/backbone',
    marionette: 'vendor/backbone.marionette',
    bootstrap: 'vendor/bootstrap',
    taffy: 'vendor/taffy',
    store: 'vendor/store'
  }
} );

require([ 
  "jquery", 
  "underscore", 
  "backbone", 
  "marionette", 
  "vendor/backbone.extensions",
  "bootstrap",
  "store",
  "taffy",
  "vendor/twig",
  "vendor/URI",
  "vendor/spin",
  "app", 
  "main/module",
  "user/module"
  ], function () {
    var app = require('app');
    var MainModule = require("main/module");

    MainModule.init();

    console.log("load app");

    app.addInitializer(function() {
      app.topmenu.show(new MainModule.Views.Menu());
      app.tabcontainer1.show(new MainModule.Views.Tab1());
      app.tabcontainer2.show(new MainModule.Views.Tab2());
    });

    app.start();
});