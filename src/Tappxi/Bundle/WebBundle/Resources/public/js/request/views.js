define([
        "app",
        "marionette", 
        "request/models", 
        "request/collections", 
        "text!request/templates.twig.html"
      ], 
function(app, Marionette, Models, Collections, text){

  return new Module(function() {

    Collections.init();
    Models.init();

    var templates = $(text);
    
    var RequestListView = Marionette.ItemView.extend({
      template: templates.filter("#request-list-template"),
      serializeData: function(){
        return {
          'requests': this.collection.toJSON(),
          'roles': this.options.roles
        };
      },
      events: {
        'click .edit-request': 'onEditRequest',
      },
      onEditRequest: function(e) {
        e.preventDefault();
        e.stopPropagation();
        var id = $(e.currentTarget).attr('data-model-id');
        app.vent.trigger('request-edit', this.collection.get(id));
      }
    });

    var RequestEditView = Marionette.ItemView.extend({
      template: templates.filter("#request-edit-template")
    });

    this.List = RequestListView;
    this.Edit = RequestEditView;

    console.log("load views for RequestModule");

  });

});
