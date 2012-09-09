define(["backbone", "request/models", "app"], function(Backbone, Models, app){

  return new Module(function() {
    Models.init();
      
    var Requests = Backbone.Collection.extend({
      model: Models.Request,
      url: function(){
    	  return app.rest('request');
      }
      
    });

    this.Requests = Requests;

    console.log("load collections for RequestModule");
  });

});
