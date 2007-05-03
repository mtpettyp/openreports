YAHOO.namespace("or.groups");    
 
var handleCancel = function() { 
	this.cancel();
};  
 
var addReports = function () {  
	var availableReports = document.getElementById("availableReports");  	
	var inputHtml = '<li><input type="checkbox" name="reportIds" value="{0}" checked />{1}</li>';
   	var inputTemplate = new YAHOO.ext.DomHelper.Template(inputHtml);

   	for (i=0; i < availableReports.length; i++) {
    	if (availableReports.options[i].selected == true) {
       		inputTemplate.append('currentReports', [availableReports.options[i].value, availableReports.options[i].text]);       
       		var option = getEl(availableReports.options[i].id); 
       		option.remove();       		
       		i=0;
     	}
	}    
	
	this.cancel();
}
 
var filterReports = function () {
	var filterByTag = document.getElementById('filterByTag');    
	var filterEl = getEl('filter');  
    var	filter = filterEl.dom.value.toLowerCase();   	
   	
   	for (i=0; i < tags.length; i++) {
   		if (filterByTag.checked && filter.length > 0 && tags[i].toLowerCase().indexOf(filter) == 0) {   		  
   	  		filterEl.dom.value = tags[i];   		  
   	  		break;
   		}
   	}
   	   		
   	var availableReports = document.getElementById('availableReports');      
   	for (i=0; i < availableReports.length; i++) {  
   		var hidden = false; 
    	if (!filterByTag.checked && availableReports.options[i].text.toLowerCase().indexOf(filter) != 0) {
    		hidden = true;
    	}
    	if (filterByTag.checked && availableReports.options[i].id.toLowerCase().indexOf(filter) < 0) { 
    		hidden = true;
    	}     	
    	if (hidden) {
       		var option = getEl(availableReports.options[i].id);        		 
       		option.addClass('hidden');         		  		
     	} else {
       		var option = getEl(availableReports.options[i].id); 
       		option.removeClass('hidden');       		
     	}
	}   
}

function init() {
 
   	var filter = getEl('filter');
   	filter.bufferedListener('keyup', this.filterReports, this, 500);		
   
   	YAHOO.or.groups.dialog = new YAHOO.widget.Dialog("addReportDialog", 
                             { width : "350px",                               
                               fixedcenter : true,
                               visible : false, 
                               constraintoviewport : true,
                               postmethod: "manual",
                               buttons : [ { text:"Add", handler:addReports },
                                     { text:"Cancel", handler:handleCancel } ]
                              } );
         
   	YAHOO.or.groups.dialog.render();
   	
   	YAHOO.util.Event.addListener("showAddReport", "click", YAHOO.or.groups.dialog.show, YAHOO.or.groups.dialog, true);			
}

YAHOO.util.Event.addListener(window, "load", init);