define(["app"], 
  function(app){
 
  var Module = {
    databases: [],
    collections: [],

    reload: function(dbs) {
      if (!_.isArray(dbs) ) {
        dbs = [dbs];
      }
      var self = this;
      dbs.forEach(function(db) {
        delete self.databases[db];
        delete self.collections[db];
      });
      return this.getCollection(dbs);
    },

    getCollection: function(dbs) {
      var collections = this.collections;
      var deferred = $.Deferred(); 
      if (!_.isArray(dbs) ) {
        dbs = [dbs];
      }     
      this.getTable(dbs).done(function(){
        var args = Array.prototype.slice.call(arguments);
        var cols = [];
        for (var i = 0; i < dbs.length ; i++) {
          var taffyDb = args[i];
          collections[dbs[i]] = cols[i] = new Backbone.Collection(taffyDb({}).get());
        };
        deferred.resolve.apply(deferred, cols);
      });
      return deferred.promise();
    },

    getTable: function(dbs){
      var databases = this.databases;
      if (!_.isArray(dbs) ) {
        dbs = [dbs];
      }
      var promises = dbs.map(function(db){
        if (databases[db]) {
          return $.Deferred().resolve({"db": db}).promise();
        }
        return $.getJSON(app.rest(db));
      });

      var deferred = $.Deferred();
      $.when.apply($, promises).done(function(){
        
        var responses = promises.length == 1 ? [arguments] : arguments;
        var i = -1;
        var taffiesDb = dbs.map(function(db){
          i = i + 1;
          if (databases[db]) {
            return databases[db];
          } else {
            databases[db] = TAFFY(responses[i][0].response.models);
            return databases[db];
          }
        });

        deferred.resolve.apply(deferred, taffiesDb);
      });

      return deferred.promise();
    }
  };

  return Module;

});
