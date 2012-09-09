define(['app',"main/views", "main/models", "main/collections"], 
  function(app, Views, Models, Collections){
 
  var module = new Module(function(){
    Views.init();
    Models.init();
    Collections.init();
    
    this.Views = Views; 
    this.Models = Models; 
    this.Collections = Collections; 

    console.log("load MainModule");
  });

  app.addInitializer(function(){
    module.init();
  });

  return module;

});
