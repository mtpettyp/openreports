<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<s:include value="Banner.jsp" />

<s:actionerror/> 

<link rel="stylesheet" type="text/css" href="css/yui/autocomplete.css" />
<script type="text/javascript" src="js/yui/autocomplete-min.js"></script>
<script type="text/javascript" src="js/autocomplete-tags.js"></script>

<script type="text/javascript">  
  var tagsArray = [<s:property value='tags' escape="false"/>];            
</script>

<style type="text/css">
#tagsautocomplete {
   width:30em;
   text-align:left; 
   padding-bottom: 5px; 
}
</style>

<br/>

<div align="center">	 
     <form action="search.action" class="dialog-form" style="width: 500px;">
      <b><s:text name="listSearchResults.title"/></b>               
       <div id="tagsautocomplete">                       
            <input id="tagsinput" name="search" value="<s:property value="search"/>">                       
            <div id="tagscontainer"></div>              
       </div>
       <br/>
       <input type="submit" value="<s:text name="button.submit"/>">                     
     </form>            
</div>    
    
<br/>

<div align="center">     
    
  <s:set name="results" value="results" scope="request" />  
  
  <display:table name="results" class="displayTag" sort="list" decorator="searchResultsTableDecorator">
    <display:column property="name" titleKey="label.name" sortable="true"/>
    <display:column property="action" titleKey="label.action"/>    
    <display:column property="tags" titleKey="label.tags"/> 
  </display:table>  
  <br>  
</div>

<s:include value="Foot.jsp" />

