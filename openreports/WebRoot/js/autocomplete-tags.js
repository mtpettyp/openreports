YAHOO.namespace("or.tags");

YAHOO.or.tags.ACJSArray = function() {      
	return {
		init: function() {            		
			var oACDS = new YAHOO.widget.DS_JSArray(tagsArray);		
				
			var oAutoComp = new YAHOO.widget.AutoComplete('tagsinput','tagscontainer', oACDS);
			oAutoComp.queryDelay = 0;
			oAutoComp.prehighlightClassName = "yui-ac-prehighlight";
			oAutoComp.typeAhead = true;
			oAutoComp.useShadow = true;            
      }
  };
}();

YAHOO.util.Event.addListener(this,'load',YAHOO.or.tags.ACJSArray.init);