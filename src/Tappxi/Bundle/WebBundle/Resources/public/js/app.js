define(['backbone',"marionette", "vendor/URI"], function(Backbone, Marionette){

  var app = new Marionette.Application();

  app.addInitializer(function(options){

    app.addRegions({
      "topmenu": "#topmenu",
      "contextmenu": "#contextmenu",
      "tabcontainer1": "#tabcontainer1",
      "tabcontainer2": "#tabcontainer2"
    });
  });

  app.bind("initialize:after", function(options){
    if( Backbone.history ){
      console.log("history start");
      Backbone.history.start();
    }
  });
  
  app.rest = function(resource) {
    return URI("http://localhost/tappxi/web/app_dev.php/api/")
      .filename(resource)
      .search({
        't': (new Date()).getTime()
      });
  };

  return app;

});