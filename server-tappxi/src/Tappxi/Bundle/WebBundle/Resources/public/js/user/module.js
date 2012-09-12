define(["app", "user/views", "user/models", "user/collections", "local_database/module"], 
  function(app, Views, Models, Collections, database){
 
  var module = new Module(function() {
    
    Views.init();
    Models.init();
    Collections.init();
    
    this.Views = Views;	
    this.Models = Models;	
    this.Collections = Collections;	

    console.log("load UserModule");
  });

  app.addInitializer(function(options){

    var Controller = Backbone.Router.extend({
      routes : {
        "user/list" : "list",
        "user/edit/:id": "edit"
      },
      list: function(){
        module.init();
        app.vent.trigger("addNewTab", {
          'target':'tabcontainer1', 
          'actionId': 'user-list',
          'title': 'Super Listado de Usuarios',
          'success': function(el){
            database.getCollection(['user', 'role']).done(function(users, roleCollection){
              
              var roles = {};
              roleCollection.each(function(role){
                roles[role.get('id')] = role.get('name');
              });

              var view = new module.Views.List({'el': el, 'collection': users, 'roles': roles});  
              view.render();

            });
          }        
        });
      }, 
      edit: function(id) {
        module.init();
        database.getCollection('user').done(function(userCollection) {
          app.vent.trigger('user-edit', userCollection.get(id));    
        });
      }
    });

    var controller = new Controller();

    app.vent.on('user-edit', function(model){
      var id = model.get('id');
      controller.navigate('/user/edit/' + id);
      app.vent.trigger("addNewTab", {
        'target':'tabcontainer2', 
        'actionId': 'user-edit-' + id,
        'title': model.get('username'),
        'success': function(el) {
          var view = new module.Views.Edit({'model': model, 'el': el});
          view.render();
        }
      });

    });

  });

  return module;

});
