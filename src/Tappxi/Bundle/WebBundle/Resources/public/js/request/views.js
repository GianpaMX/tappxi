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
      template: templates.filter("#request-edit-template"),
      events: {
    	  'submit': 'onSubmit',
      },
      onSubmit: function(e){
    	  console.log(this.model.toJSON());
    	  var eta = this.$('#eta').val();
    	  var fare = this.$('#fare').val();
    	  e.preventDefault();
          e.stopPropagation();
    	  var offer = new Models.Offer();
    	  
    	  $.post(offer.url(), {
    		  eta: eta,
    		  fare: fare,
    		  request_id: this.model.get('id')
    	  }).done(function(){
    		  
    	  });
      }
    });

    this.List = RequestListView;
    this.Edit = RequestEditView;

    console.log("load views for RequestModule");

  });

});
