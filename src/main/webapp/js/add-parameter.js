YAHOO.namespace("or.parameter");

function init() {
					
	// Define event handlers for Dialog
	var handleSubmit = function() {
		document.getElementById("response").innerHTML = 'Processing Request...';
		this.submit();
	};
	var handleCancel = function() {
		this.cancel();
	};
		
	var handleSuccess = function(o) {
		var response = o.responseText;
		var response = response.split("|");
                        
		if (response.length > 1) {
			var select = document.getElementById("parameterSelect");
			select.options[select.length] = new Option(response[1], response[0], false, true)
                          
			document.getElementById("response").innerHTML = 'Parameter successfully added';
		} else {
			document.getElementById("response").innerHTML = response[0];
		}	
	};
		
	var handleFailure = function(o) {
		document.getElementById("response").innerHTML = "Submission failed: " + o.status;
	};

	// Instantiate the Dialog
	YAHOO.or.parameter.dialog = new YAHOO.widget.Dialog("parameterDialog", 
															{ width : "500px",
															  fixedcenter : true,
															  visible : false, 
															  constraintoviewport : true,
															  buttons : [ { text:"Submit", handler:handleSubmit, isDefault:true },
																		  { text:"Cancel", handler:handleCancel } ]
															 } );
					
	// Validate the entries in the form 
	YAHOO.or.parameter.dialog.validate = function() {
		var data = this.getData();						
		if (data.name == "" || data.description == "") {
			alert("Please enter Parameter name and description.");
			return false;
		} else {
			return true;
		}
	};

	// Wire up the success and failure handlers
	YAHOO.or.parameter.dialog.callback = { success: handleSuccess,
											failure: handleFailure };
					
	// Render the Dialog
	YAHOO.or.parameter.dialog.render();

	YAHOO.util.Event.addListener("showAddParameter", "click", YAHOO.or.parameter.dialog.show, YAHOO.or.parameter.dialog, true);
					
}

YAHOO.util.Event.addListener(window, "load", init);