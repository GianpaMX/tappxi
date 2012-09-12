define(["backbone", 'app'], function(Backbone, app){

  return new Module(function() {

    this.Request = Backbone.Model.extend({});
    
    this.Offer = Backbone.Model.extend({
    	url: function(){
    		return app.rest('newoffer');
    	}
    });

    console.log("load models for UserModule");
    
  });

});
