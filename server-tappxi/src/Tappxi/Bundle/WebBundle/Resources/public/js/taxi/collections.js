define(["backbone", "taxi/models", "app"], function(Backbone, Models, app){

  return new Module(function() {
    Models.init();
      
    var Taxis = Backbone.Collection.extend({
      model: Models.Taxi,
      url: function(){
    	  return app.rest('taxi');
      }
      
    });

    this.Taxis = Taxis;

    console.log("load collections for TaxiModule");
  });

});
