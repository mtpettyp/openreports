YAHOO.namespace("or.reportfile");

function init() {		
			
	var uploadCallback = 
	{ 
	  upload: function(o) {
	  	var response = o.responseText;
		var response = response.split("|");
                        
		if (response.length > 1) {
			var select = document.getElementById("fileSelect");
			select.options[select.length] = new Option(response[1], response[0], false, true)
                          
			document.getElementById("response").innerHTML = 'File successfully uploaded';
		} else {
			document.getElementById("response").innerHTML = response[0];
		}	
	  }
	}  
	
	var handleUpload = function() {	
		document.getElementById("response").innerHTML = 'Processing Request...';
				
		var uploadForm = document.getElementById("reportUploadForm");
		
		YAHOO.util.Connect.setForm(uploadForm, true, true); 
		var cObj = YAHOO.util.Connect.asyncRequest('post', uploadForm.action, uploadCallback);

		this.cancel();
	};	
	
	var handleCancel = function() {	
		this.cancel();
	};		
	

	// Instantiate the Dialog
	YAHOO.or.reportfile.dialog = new YAHOO.widget.Dialog("reportFileDialog", 
															{ width : "475px",
															  fixedcenter : true,
															  visible : false, 
															  constraintoviewport : true,
															  postmethod: "manual",
															  buttons : [ { text:"Upload", handler:handleUpload, isDefault:true },
																		  { text:"Cancel", handler:handleCancel } ]
															 } );
					
	// Render the Dialog
	YAHOO.or.reportfile.dialog.render();

	YAHOO.util.Event.addListener("showAddReportFile", "click", YAHOO.or.reportfile.dialog.show, YAHOO.or.reportfile.dialog, true);
					
}

YAHOO.util.Event.addListener(window, "load", init);