define([
        "app",
        "marionette", 
        "taxi/models", 
        "taxi/collections", 
        "text!taxi/templates.twig.html"
      ], 
function(app, Marionette, Models, Collections, text){

  return new Module(function() {

    Collections.init();
    Models.init();

    var templates = $(text);
    
    var TaxiListView = Marionette.ItemView.extend({
      template: templates.filter("#taxi-list-template"),
      serializeData: function(){
        return {
          'taxis': this.collection.toJSON(),
          'roles': this.options.roles
        };
      },
      events: {
        'click .edit-taxi': 'onEditTaxi',
      },
      onEditTaxi: function(e) {
        e.preventDefault();
        e.stopPropagation();
        var id = $(e.currentTarget).attr('data-model-id');
        app.vent.trigger('taxi-edit', this.collection.get(id));
      }
    });

    var TaxiEditView = Marionette.ItemView.extend({
      template: templates.filter("#taxi-edit-template")
    });

    this.List = TaxiListView;
    this.Edit = TaxiEditView;

    console.log("load views for TaxiModule");

  });

});
