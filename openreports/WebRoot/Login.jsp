<%@ taglib prefix="s" uri="/struts-tags" %>

<s:include value="Banner.jsp" />

<script type="text/javascript">

  YAHOO.namespace("or.login");

  function init() {
          
  YAHOO.or.login.dialog = new YAHOO.widget.Dialog("dialog", 
                              { width : "475px",
                                fixedcenter : true,
                                visible : true, 
                                modal: true,
                                postmethod: "form",
                                constraintoviewport : true,
                                close: false                               
                               } );          

  YAHOO.or.login.dialog.render();          
}

YAHOO.util.Event.addListener(window, "load", init);
	
</script>

<div id="dialog">

  <div class="hd">Please Login</div>
  <div class="bd">
  
  <form action="login.action" method="post" name="loginForm" >
  <table border="0">    
    <tr>
      <td class="boldText">User Name</td>
      <td><input type="text" name="userName" id="userName" size="45"></td>
    </tr>
    <tr>
      <td class="boldText">Password</td>
      <td><input type="password"  name="password" size="45"></td>
    </tr>  
    <tr>
      <td align="center" class="dialogButtons" colspan="2">
      	<input class="standardButton" type="submit" value="Login">
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

