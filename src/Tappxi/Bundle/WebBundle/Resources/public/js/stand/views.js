define([
        "app",
        "marionette", 
        "stand/models", 
        "stand/collections", 
        "text!stand/templates.twig.html"
      ], 
function(app, Marionette, Models, Collections, text){

  return new Module(function() {

    Collections.init();
    Models.init();

    var templates = $(text);
    
    var StandListView = Marionette.ItemView.extend({
      template: templates.filter("#stand-list-template"),
      serializeData: function(){
        return {
          'stands': this.collection.toJSON(),
          'roles': this.options.roles
        };
      },
      events: {
        'click .edit-stand': 'onEditStand',
      },
      onEditStand: function(e) {
        e.preventDefault();
        e.stopPropagation();
        var id = $(e.currentTarget).attr('data-model-id');
        app.vent.trigger('stand-edit', this.collection.get(id));
      }
    });

    var StandEditView = Marionette.ItemView.extend({
      template: templates.filter("#stand-edit-template")
    });

    this.List = StandListView;
    this.Edit = StandEditView;

    console.log("load views for StandModule");

  });

});
