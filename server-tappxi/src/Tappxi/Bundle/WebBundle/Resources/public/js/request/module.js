define(["app", "request/views", "request/models", "request/collections"], 
  function(app, Views, Models, Collections){
 
  var module = new Module(function() {
    
    Views.init();
    Models.init();
    Collections.init();
    
    this.Views = Views;	
    this.Models = Models;	
    this.Collections = Collections;	

    console.log("load requestModule");
  });

  app.addInitializer(function(options){
	var currentInverval = null;
    var Controller = Backbone.Router.extend({
      routes : {
        "request/list" : "list",
        "request/edit/:id": "edit"
      },
      list: function(){
        module.init();
        app.vent.trigger("addNewTab", {
          'target':'tabcontainer1', 
          'actionId': 'request-list',
          'title': 'Peticiones Pendientes',
          'success': function(el){
        	  var requests = new module.Collections.Requests();
              var view = new module.Views.List({'el': el, 'collection': requests});
              if(currentInverval){
            	  clearInterval(currentInverval);
              }
              requests.fetch().done(function(){
            	  view.render();  
              });
              currentInverval = setInterval(function(){
        		  requests.fetch().done(function(){
                	  view.render();  
                  });  
        	  }, 3000);
          }        
        });
      }
    });

    var controller = new Controller();

    app.vent.on('request-edit', function(model){
      var id = model.get('id');
      controller.navigate('/request/edit/' + id);
      app.vent.trigger("addNewTab", {
        'target':'tabcontainer2', 
        'actionId': 'request-edit-' + id,
        'title': model.get('user').name,
        'success': function(el) {
          var view = new module.Views.Edit({'model': model, 'el': el});
          view.render();
        }
      });

    });

  });

  return module;

});
