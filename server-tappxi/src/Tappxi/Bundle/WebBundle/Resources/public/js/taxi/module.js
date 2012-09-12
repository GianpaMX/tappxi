define(["app", "taxi/views", "taxi/models", "taxi/collections"], 
  function(app, Views, Models, Collections){
 
  var module = new Module(function() {
    
    Views.init();
    Models.init();
    Collections.init();
    
    this.Views = Views;	
    this.Models = Models;	
    this.Collections = Collections;	

    console.log("load TaxiModule");
  });

  app.addInitializer(function(options){

    var Controller = Backbone.Router.extend({
      routes : {
        "taxi/list" : "list",
        "taxi/edit/:id": "edit"
      },
      list: function(){
        module.init();
        app.vent.trigger("addNewTab", {
          'target':'tabcontainer1', 
          'actionId': 'taxi-list',
          'title': 'Sitios de Taxis',
          'success': function(el){
        	  var taxis = new module.Collections.Taxis(); 
              var view = new module.Views.List({'el': el, 'collection': taxis});
              taxis.fetch().done(function(){
            	  view.render();  
              })
          }        
        });
      }
    });

    var controller = new Controller();

    app.vent.on('taxi-edit', function(model){
      var id = model.get('id');
      controller.navigate('/taxi/edit/' + id);
      app.vent.trigger("addNewTab", {
        'target':'tabcontainer2', 
        'actionId': 'taxi-edit-' + id,
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
