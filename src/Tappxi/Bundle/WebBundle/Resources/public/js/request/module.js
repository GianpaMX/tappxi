define(["app", "Request/views", "Request/models", "Request/collections"], 
  function(app, Views, Models, Collections){
 
  var module = new Module(function() {
    
    Views.init();
    Models.init();
    Collections.init();
    
    this.Views = Views;	
    this.Models = Models;	
    this.Collections = Collections;	

    console.log("load RequestModule");
  });

  app.addInitializer(function(options){

    var Controller = Backbone.Router.extend({
      routes : {
        "Request/list" : "list",
        "Request/edit/:id": "edit"
      },
      list: function(){
        module.init();
        app.vent.trigger("addNewTab", {
          'target':'tabcontainer1', 
          'actionId': 'Request-list',
          'title': 'Sitios de Taxis',
          'success': function(el){
        	  var Requests = new module.Collections.Requests(); 
              var view = new module.Views.List({'el': el, 'collection': Requests});
              Requests.fetch().done(function(){
            	  view.render();  
              })
          }        
        });
      }
    });

    var controller = new Controller();

    app.vent.on('Request-edit', function(model){
      var id = model.get('id');
      controller.navigate('/Request/edit/' + id);
      app.vent.trigger("addNewTab", {
        'target':'tabcontainer2', 
        'actionId': 'Request-edit-' + id,
        'title': model.get('name'),
        'success': function(el) {
          var view = new module.Views.Edit({'model': model, 'el': el});
          view.render();
        }
      });

    });

  });

  return module;

});
