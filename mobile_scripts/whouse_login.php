<?php
   //database authentication
   require_once('db.php'); 
   mysql_select_db($database_localhost,$localhost);
	 
   //retrieve post data	 
   $useremail = $_POST['UserEmail'];
   $password = $_POST['Password'];
   $xcood = $_POST['xkood'];
   $ycood = $_POST['ykood'];
   
   //database query
   $query_search = "select * from vw_login_details where username = '".$useremail."' AND password = '"
		   .$password. "' and   '".$ycood."'  between extent_Y_MIN and extent_Y_max and 
		   '".$xcood."'  between extent_X_MIN and extent_X_max";
   $query_exec = mysql_query($query_search) or die(mysql_error());
   $rows = mysql_num_rows($query_exec);
   
   //return query success
   if($rows -->0)
   {
	 echo "N";
   }
   else
   {
	echo "Y";
   }
?>
