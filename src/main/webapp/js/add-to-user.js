YAHOO.namespace("or.users");    
 
var handleCancel = function() { 
	this.cancel();
};  
 
var addGroups = function () {  
	var availableGroups = document.getElementById("availableGroups");  	
	var inputHtml = '<li><input type="checkbox" name="groupIds" value="{0}" checked />{1}</li>';
   	var inputTemplate = new YAHOO.ext.DomHelper.Template(inputHtml);

   	for (i=0; i < availableGroups.length; i++) {
    	if (availableGroups.options[i].selected == true) {
       		inputTemplate.append('currentGroups', [availableGroups.options[i].value, availableGroups.options[i].text]);       
       		var option = getEl(availableGroups.options[i].id); 
       		option.remove();       		
       		i=0;
     	}
	}    
	
	this.cancel();
}
 
var filterReports = function () {
	var filterByTag = document.getElementById('filterByTag');
	var filter = getEl('filter');  
   	filter = filter.dom.value.toLowerCase();
 
   	var availableGroups = document.getElementById("availableGroups");      
   	for (i=0; i < availableGroups.length; i++) {   
    	var hidden = false; 
    	if (!filterByTag.checked && availableGroups.options[i].text.toLowerCase().indexOf(filter) != 0) {
    		hidden = true;
    	}
    	if (filterByTag.checked && availableGroups.options[i].id.toLowerCase().indexOf(filter) < 0) { 
    		hidden = true;
    	}     	
    	if (hidden) {
       		var option = getEl(availableGroups.options[i].id); 			  
       		option.addClass('hidden');     
     	} else {
       		var option = getEl(availableGroups.options[i].id); 
       		option.removeClass('hidden');       		
     	}
	}   
}

function init() {
 
   	var filter = getEl('filter');
   	filter.bufferedListener('keyup', this.filterReports, this, 500);		
   
   	YAHOO.or.users.dialog = new YAHOO.widget.Dialog("addGroupDialog", 
                             { width : "350px",
                               fixedcenter : true,
                               visible : false, 
                               constraintoviewport : true,
                               postmethod: "manual",
                               buttons : [ { text:"Add", handler:addGroups },
                                     { text:"Cancel", handler:handleCancel } ]
                              } );
         
   	YAHOO.or.users.dialog.render();
   	YAHOO.util.Event.addListener("showAddGroup", "click", YAHOO.or.users.dialog.show, YAHOO.or.users.dialog, true);			
}

YAHOO.util.Event.addListener(window, "load", init);