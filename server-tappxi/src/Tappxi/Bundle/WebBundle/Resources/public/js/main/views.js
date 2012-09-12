define([
        "app",
        "marionette", 
        "main/models", 
        "main/collections", 
        "text!main/templates.twig.html"
      ], 
function(app, Marionette, Models, Collections, text){

  return new Module(function() {
    
    Collections.init();
    Models.init();
    
    var addNewTab = function(data, success){
      var ulMenu = this.$el.find('ul.nav-tabs');
      var tabPaneId = 'tab-pane-' + data.actionId;

      if( this.ids.indexOf(data.actionId) != -1 ){
        var a = this.$el.find('a[data-target=#' + tabPaneId + ']');
        var scrollNavTabs = a.closest(".scroll-nav-tabs");
        var li = a.closest('li');
        a.tab('show');
        var scroll = 0;
        li.prevAll().each(function(i,item) {
          scroll += $(item).width();
        });
        this.setScroll(scroll);
        return;
      }
      
      var a = $('<a>', {
        'text': data.title,
        'data-toggle': "tab",
        'data-target': '#' + tabPaneId
      });
      var closeButton = $('<button>', {
        'class': 'close',
        'type': 'button',
        'html': '&times;'
      });
      var li = $('<li>',{
        'data-action-id': data.actionId
      }).append(closeButton).append(a);
      
      ulMenu.append(li);
      this.ids.push(data.actionId);

      ulMenu.parent().scrollLeft(this.getMaxScroll());

      var divTabs = this.$el.find('div.tab-content');
      divTabs.find('div.tab-pane');
      var newDiv = $('<div>', {
        'class': "tab-pane",
        'id': tabPaneId
      });
      divTabs.append(newDiv);
      a.on('click', function(e){
        $(this).tab('show');
      });
      
      var self = this;
      closeButton.on('click', function(e){
        if( li.hasClass('active') ){
          li.prev('li').find('a[data-toggle]').tab('show').length 
            || li.next('li').find('a[data-toggle]').tab('show');  
        }
        newDiv.remove();
        li.remove();
        var index = self.ids.indexOf(data.actionId);
        delete self.ids[index];
      });
      a.tab('show');

      if( _.isFunction(success) ){
        var opts = {
          lines: 13, // The number of lines to draw
          length: 7, // The length of each line
          width: 4, // The line thickness
          radius: 10, // The radius of the inner circle
          rotate: 0, // The rotation offset
          color: '#000', // #rgb or #rrggbb
          speed: 1, // Rounds per second
          trail: 60, // Afterglow percentage
          shadow: false, // Whether to render a shadow
          hwaccel: false, // Whether to use hardware acceleration
          className: 'spinner', // The CSS class to assign to the spinner
          zIndex: 2e9 // The z-index (defaults to 2000000000)
        };
        var spinner = new Spinner(opts).spin();
        var spinDiv = $('<div>').css({
          width: '100px',
          height: '100px',
          margin: '0 auto',
          verticalAlign: 'middle'
        }).append($(spinner.el).css({top:50,left:50}));
        newDiv.append(spinDiv);
        success(newDiv);
      }
    };

    var TabScroll = Marionette.ItemView.extend({
      onScrollLeft: function() {
        if( this.scrollLeftTimeout ) {
          return;
        }
        var div = this.$('.nav-tabs').parent();
        var fn = function() {
          var left = div.scrollLeft();
          if (left > 0) {
            div.scrollLeft(left - 35);    
          }
          return fn;
        };
        this.scrollLeftTimeout = setInterval(fn(), 200);
      },
      onFinishScrollLeft: function() {
        clearTimeout(this.scrollLeftTimeout);
        this.scrollLeftTimeout = null;
      },
      onScrollRight: function() {
        if( this.scrollRightTimeout ) {
          return;
        }
        var div = this.$('.nav-tabs').parent();
        var max = this.getMaxScroll();
        
        var fn = function() {
          var left = div.scrollLeft();
          if (left <= max) {  
            div.scrollLeft(left + 35);  
          }
          return fn;
        };
        this.scrollRightTimeout = setInterval(fn(), 200);
      },
      onFinishScrollRight: function() {
        clearTimeout(this.scrollRightTimeout);
        this.scrollRightTimeout = null;
      }, 
      getMaxScroll: function() {
        var ul = this.$('.nav-tabs');
        var div = ul.parent();
        var max = - div.width();
        ul.children('li').each(function(i, li){
          max += $(li).width();
        });
        return max;
      },
      setScroll: function(position) {
        var div = this.$('.nav-tabs').parent();
        var max = this.getMaxScroll();
        if ( position < 0 ) {
          position = 0;
        } else if ( position > max ) {
          position = max;
        }
        div.scrollLeft(position);  
      }
    });

    var templates = $(text);

    var Tab1View = TabScroll.extend({
      ids: [],
      template: templates.filter("#tab1-template"),
      appEvents: {
        'addNewTab': 'addTab'
      },
      events: {
        'mouseenter .scroll-left': 'onScrollLeft',
        'mouseleave .scroll-left': 'onFinishScrollLeft',
        'mouseenter .scroll-right': 'onScrollRight',
        'mouseleave .scroll-right': 'onFinishScrollRight'
      },
      addTab: function(data) {
        if( data.target == 'tabcontainer1'){
          addNewTab.call(this, data, data.success);
        }
      }
    });

    var Tab2View = TabScroll.extend({
      ids: [],
      template: templates.filter("#tab2-template"),
      appEvents: {
        'addNewTab': 'addTab'
      },
      events: {
        'mouseenter .scroll-left': 'onScrollLeft',
        'mouseleave .scroll-left': 'onFinishScrollLeft',
        'mouseenter .scroll-right': 'onScrollRight',
        'mouseleave .scroll-right': 'onFinishScrollRight'
      },
      addTab: function(data) {
        if( data.target == 'tabcontainer2'){
          addNewTab.call(this, data, data.success);
        }
      }
    });

    var MenuView = Marionette.ItemView.extend({
      template: templates.filter("#menu-template")
    });

    this.Menu = MenuView;
    this.Tab1 = Tab1View;
    this.Tab2 = Tab2View;
    
  });

});
