<?php

 require_once('db.php'); 
 mysql_select_db($database_localhost,$localhost);
   
	$xcood = $_REQUEST['xkood'];
	$ycood = $_REQUEST['ykood'];
	$jina = $_REQUEST['jina'];
	$size = $_REQUEST['size'];
	$crop = $_REQUEST['crop'];
	$mahali = $_REQUEST['mahali'];
	//$Cereal = $_REQUEST['Cereal'];
       


 //inserts Cereals data into database
 $sql = mysql_query("INSERT INTO yield_estimates (Name, Farm_size, Crop, Place,  x_cordinate, y_cordinate )
		  VALUES('".$jina."','".$size."', '".$crop."',  '".$mahali."', '".$xcood."', '".$ycood."' )");


 if($sql --> 0)
 {
    echo "Y";
 }
 else
 {
    echo "N";
 }
?>