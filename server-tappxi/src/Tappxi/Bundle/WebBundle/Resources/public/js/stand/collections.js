define(["backbone", "stand/models", "app"], function(Backbone, Models, app){

  return new Module(function() {
    Models.init();
      
    var Stands = Backbone.Collection.extend({
      model: Models.Stand,
      url: function(){
    	  return app.rest('stand');
      }
      
    });

    this.Stands = Stands;

    console.log("load collections for StandModule");
  });

});
