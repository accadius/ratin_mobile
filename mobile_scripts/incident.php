
<?php 
   require_once('db.php'); 
   mysql_select_db($database_localhost,$localhost);
	 
	$title = $_POST['title'];
	$place = $_POST['mahali'];
	$inc = $_POST['incd'];
	$lat = $_POST['xkood'];
	$lon = $_POST['ykood'];
	$imei = $_POST['imei'];
	
	
	 $query_search = "insert into  incident (title,  place, incident, lat, longi, date, imei)
	 values('".$title."','".$place."','".$inc."','".$lat."','".$lon."',now(), '".$imei."')";
	 $query_exec = mysql_query($query_search) or die(mysql_error());
	 $rows = mysql_num_rows($query_exec);
	// echo $descr;
	/* if($rows --> 0)
	 {
	 echo "N";
	 }
	else
	{
	echo "Y";
	}*/
	 mysql_close();
	?>