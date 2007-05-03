<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<s:include value="Banner.jsp" />

<s:actionerror/> 

<script type="text/javascript" src="js/yui/autocomplete-min.js"></script>
<script type="text/javascript" src="js/autocomplete-tags.js"></script>

<script type="text/javascript">
  var tagsArray = [$tags];            
</script>

<br/>

<div align="center">
  
   <div id="instructions" style="width: 700px;">
      <form action="search.action">            
        <div id="tagsautocomplete"> 
             <b>Search by tag:</b>     
             <input id="tagsinput" name="search" value="<s:property value="search"/>"> 
             <input type="submit" value="Submit">
             <div id="tagscontainer"></div> 
         </div>         
      </form>
      <br/>
    </div>
    
  <s:set name="results" value="results" scope="request" />  
  
  <display:table name="results" class="displayTag" sort="list" decorator="searchResultsTableDecorator">
    <display:column property="name" title="Name" sortable="true"/>
    <display:column property="action" title="Action"/>    
    <display:column property="tags" title="Tags"/> 
  </display:table>  
  <br>  
</div>

<s:include value="Foot.jsp" />

