<%@ taglib prefix="s" uri="/struts-tags" %>

<s:include value="Banner.jsp" />

<script type="text/javascript">

  YAHOO.namespace("or.login");

  function init() {
          
  YAHOO.or.login.dialog = new YAHOO.widget.Dialog("dialog", 
                              {
                                fixedcenter : true,
                                visible : true, 
                                modal: false,
                                postmethod: "form",
                                constraintoviewport : true,
                                close: false   ,
                                width: "35em"                          
                               } );          

  YAHOO.or.login.dialog.render();          
}

YAHOO.util.Event.addListener(window, "load", init);
	
</script>

<div id="dialog" align="center">

  <div class="hd"><s:text name="login.title"/></div>
  <div class="bd">
  
  <form action="login.action" method="post" name="loginForm" >
  <table border="0">    
    <tr>
      <td class="boldText"><s:text name="label.username"/></td>
      <td><input type="text" name="userName" id="userName" size="45"></td>
    </tr>
    <tr>
      <td class="boldText"><s:text name="label.password"/></td>
      <td><input type="password"  name="password" size="45"></td>
    </tr>  
    <tr>
      <td align="center" class="dialogButtons" colspan="2">
      	<input class="standardButton" type="submit" value="<s:text name="login.submit"/>">
      </td>
    </tr>  
    <tr>
      <td class="error" colspan="2" align="center">
        <s:actionerror/>
      </td>
    </tr>
  </table>
  </form>
  
  </div>
  
</div>

</body>

</html>

