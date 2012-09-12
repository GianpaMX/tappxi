define(["backbone", "main/models"], function(Backbone, Models){

  return new Module(function(){
    
    Models.init();

    console.log("load collections for MainModule");
  });

  return Module;

});
