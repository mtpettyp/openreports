function addHighlightOdd(evt)
{				
	var el = new YAHOO.util.Element(evt.currentTarget);	
	el.replaceClass("odd", "highlight");
}		

function addHighlightEven(evt)
{				
	var el = new YAHOO.util.Element(evt.currentTarget);	
	el.replaceClass("even", "highlight");
}

function addHighlightTriggered(evt)
{				
	var el = new YAHOO.util.Element(evt.currentTarget);	
	el.replaceClass("triggered", "highlight");
}

function removeHighlightOdd(evt)
{							
	var el = new YAHOO.util.Element(evt.currentTarget);	
	el.replaceClass("highlight","odd");	
}	
		
function removeHighlightEven(evt)
{							
	var el = new YAHOO.util.Element(evt.currentTarget);	
	el.replaceClass("highlight","even");
}	

function removeHighlightTriggered(evt)
{							
	var el = new YAHOO.util.Element(evt.currentTarget);	
	el.replaceClass("highlight", "triggered");
}	
		
function init()
{	
	var evenNode = new YAHOO.util.Element("evenNode");	
	evenNode = evenNode.getElementsByClassName("even");		
	YAHOO.util.Event.addListener(evenNode, "mouseover", addHighlightEven); 
	YAHOO.util.Event.addListener(evenNode, "mouseout", removeHighlightEven);
				
	var oddNode = new YAHOO.util.Element("oddNode");	
	oddNode = oddNode.getElementsByClassName("odd");					
	YAHOO.util.Event.addListener(oddNode, "mouseover", addHighlightOdd); 
	YAHOO.util.Event.addListener(oddNode, "mouseout", removeHighlightOdd);
	
	var triggeredNode = new YAHOO.util.Element("triggeredNode");	
	triggeredNode = triggeredNode.getElementsByClassName("triggered")			
	YAHOO.util.Event.addListener(triggeredNode, "mouseover", addHighlightTriggered); 
	YAHOO.util.Event.addListener(triggeredNode, "mouseout", removeHighlightTriggered);
}

YAHOO.util.Event.addListener(window, "load", init);