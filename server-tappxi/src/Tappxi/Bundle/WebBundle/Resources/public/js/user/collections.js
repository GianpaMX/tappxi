define(["backbone", "user/models"], function(Backbone, Models){

  return new Module(function() {
    Models.init();
      
    var Users = Backbone.Collection.extend({
      model: Models.User
    });

    this.Users = Users;

    console.log("load collections for UserModule");
  });

});
