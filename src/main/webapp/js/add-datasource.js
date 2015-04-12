YAHOO.namespace("or.datasource");

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
			var select = document.getElementById("datasourceSelect");
			select.options[select.length] = new Option(response[1], response[0], false, true)
                          
			document.getElementById("response").innerHTML = 'DataSource successfully added';
		} else {
			document.getElementById("response").innerHTML = response[0];
		}	
	};
		
	var handleFailure = function(o) {
		document.getElementById("response").innerHTML = "Submission failed: " + o.status;
	};

	// Instantiate the Dialog
	YAHOO.or.datasource.dialog = new YAHOO.widget.Dialog("datasourceDialog", 
															{ width : "500px",
															  fixedcenter : true,
															  visible : false, 
															  constraintoviewport : true,
															  buttons : [ { text:"Submit", handler:handleSubmit, isDefault:true },
																		  { text:"Cancel", handler:handleCancel } ]
															 } );
					
	// Validate the entries in the form 
	YAHOO.or.datasource.dialog.validate = function() {
		var data = this.getData();						
		if (data.name == "" || data.url == "") {
			alert("Please enter DataSource name and URL.");
			return false;
		} else {
			return true;
		}
	};

	// Wire up the success and failure handlers
	YAHOO.or.datasource.dialog.callback = { success: handleSuccess,
											failure: handleFailure };
					
	// Render the Dialog
	YAHOO.or.datasource.dialog.render();

	YAHOO.util.Event.addListener("showAddDataSource", "click", YAHOO.or.datasource.dialog.show, YAHOO.or.datasource.dialog, true);
					
}

YAHOO.util.Event.addListener(window, "load", init);