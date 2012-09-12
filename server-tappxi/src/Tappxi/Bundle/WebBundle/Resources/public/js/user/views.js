define([
        "app",
        "marionette", 
        "user/models", 
        "user/collections", 
        "text!user/templates.twig.html"
      ], 
function(app, Marionette, Models, Collections, text){

  return new Module(function() {

    Collections.init();
    Models.init();

    var templates = $(text);
    
    var UserListView = Marionette.ItemView.extend({
      template: templates.filter("#user-list-template"),
      serializeData: function(){
        return {
          'users': this.collection.toJSON(),
          'roles': this.options.roles
        };
      },
      events: {
        'click .edit-user': 'onEditUser',
      },
      onEditUser: function(e) {
        e.preventDefault();
        e.stopPropagation();
        var id = $(e.currentTarget).attr('data-model-id');
        app.vent.trigger('user-edit', this.collection.get(id));
      }
    });

    var UserEditView = Marionette.ItemView.extend({
      template: templates.filter("#user-edit-template")
    });

    this.List = UserListView;
    this.Edit = UserEditView;

    console.log("load views for UserModule");

  });

});
