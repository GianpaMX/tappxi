define(["app", "stand/views", "stand/models", "stand/collections"], 
  function(app, Views, Models, Collections){
 
  var module = new Module(function() {
    
    Views.init();
    Models.init();
    Collections.init();
    
    this.Views = Views;	
    this.Models = Models;	
    this.Collections = Collections;	

    console.log("load StandModule");
  });

  app.addInitializer(function(options){

    var Controller = Backbone.Router.extend({
      routes : {
        "stand/list" : "list",
        "stand/edit/:id": "edit"
      },
      list: function(){
        module.init();
        app.vent.trigger("addNewTab", {
          'target':'tabcontainer1', 
          'actionId': 'stand-list',
          'title': 'Sitios de Taxis',
          'success': function(el){
        	  var stands = new module.Collections.Stands(); 
              var view = new module.Views.List({'el': el, 'collection': stands});
              stands.fetch().done(function(){
            	  view.render();  
              })
          }        
        });
      }
    });

    var controller = new Controller();

    app.vent.on('stand-edit', function(model){
      var id = model.get('id');
      controller.navigate('/stand/edit/' + id);
      app.vent.trigger("addNewTab", {
        'target':'tabcontainer2', 
        'actionId': 'stand-edit-' + id,
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
