
<?php

 require_once('db.php'); 
   mysql_select_db($database_localhost,$localhost);
   
   $xcood = $_REQUEST['xkood'];
	$ycood = $_REQUEST['ykood'];
	$cereal = $_REQUEST['Cereal'];
	//$grain_flow = $_REQUEST['GrainFlow'];
	//$grain_price = $_REQUEST['GrainPrice'];
	//$grain_stock = $_REQUEST['GrainStock'];
	$grain_volume = $_REQUEST['volume'];
	$source_c = $_REQUEST['source_c'];
	$source_p = $_REQUEST['source_p'];
	$dest_p = $_REQUEST['dest_p'];
	$dest_c = $_REQUEST['dest_c'];
	
  
    $query2 = "SELECT * FROM product WHERE product_name = '".$Cereal." '"; 
  $rowx= mysql_query($query2);
  while($rowax = mysql_fetch_array($rowx))
  {
    $rowz=$rowax['product_id'];
  }
  
$query_search = "SELECT * FROM `crossborder_points` where ".$ycood."  between YMin and YMax and 
	  ".$xcood."  between XMin and XMax;";


	$rows= mysql_query($query_search);
			
	 

	while($rowa = mysql_fetch_array($rows)){
	
	$row=$rowa['border_id'];
	


	}
	  //inserts maize data into database
$sql=mysql_query("insert into crossborder_tradeflow (product_id, border_id, Source_Country, Destination_Country, volume, flow, date ) 
values('".$rowz."','".$row."','".$source_c."','".$dest_c."','".$grain_volume."','".$source_c."-".$dest_c."',now());");





if($sql --> 0)
	 {
	 echo "Y";
	 }
	else
	{
	echo "N";
	}

    

	

?>