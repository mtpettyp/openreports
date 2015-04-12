YAHOO.namespace("or.chart");

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
			var select = document.getElementById("chartSelect");
			select.options[select.length] = new Option(response[1], response[0], false, true)
                          
			document.getElementById("response").innerHTML = 'Chart successfully added';
		} else {
			document.getElementById("response").innerHTML = response[0];
		}	
	};
		
	var handleFailure = function(o) {
		document.getElementById("response").innerHTML = "Submission failed: " + o.status;
	};

	// Instantiate the Dialog
	YAHOO.or.chart.dialog = new YAHOO.widget.Dialog("chartDialog", 
															{ width : "500px",
															  fixedcenter : true,
															  visible : false, 
															  constraintoviewport : true,
															  buttons : [ { text:"Submit", handler:handleSubmit, isDefault:true },
																		  { text:"Cancel", handler:handleCancel } ]
															 } );
					
	// Validate the entries in the form 
	YAHOO.or.chart.dialog.validate = function() {
		var data = this.getData();						
		if (data.name == "" || data.description == "" || data.query == "") {
			alert("Name, description, and query are required.");
			return false;
		} else {
			return true;
		}
	};

	// Wire up the success and failure handlers
	YAHOO.or.chart.dialog.callback = { success: handleSuccess,
											failure: handleFailure };
					
	// Render the Dialog
	YAHOO.or.chart.dialog.render();

	YAHOO.util.Event.addListener("showAddChart", "click", YAHOO.or.chart.dialog.show, YAHOO.or.chart.dialog, true);
					
}

YAHOO.util.Event.addListener(window, "load", init);