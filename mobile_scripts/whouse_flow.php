<?php

 require_once('db.php'); 
   mysql_select_db($database_localhost,$localhost);
   
   $xcood = $_REQUEST['xkood'];
	$ycood = $_REQUEST['ykood'];
	$cereal = $_REQUEST['Cereal'];
	$grain_flow = $_REQUEST['GrainFlow'];
	//$grain_price = $_REQUEST['GrainPrice'];
	$grain_stock = $_REQUEST['GrainStock'];
	$grain_volume = $_REQUEST['GrainVolume'];
	
	
	  $query2 = "SELECT * FROM product WHERE product_name = '".$cereal." '";
  $rowx= mysql_query($query2);
  while($rowax = mysql_fetch_array($rowx))
  {
    $rowz=$rowax['product_id'];
  }
	
	
  
$query_search = " select * from warehouse where  '".$ycood."' between y_max and y_min and '".$xcood."' between x_min and x_max";


	$rows= mysql_query($query_search);
			
	 

	while($rowa = mysql_fetch_array($rows)){
	
	$row=$rowa['whouse_id'];
	
}

$query_jana="SELECT * FROM warehouse_data_flow where whouse_id = '".$row."' and product_id = '".$cereal."' order by time desc limit 1";

  $jana_rows= mysql_query($query_jana);
			
  while($jana_rowa = mysql_fetch_array($jana_rows))
  {
    $jana_wp=$jana_rowa['stock'];
	//$jana_rp=$jana_rowa['retail_Price'];
  

}
if ($grain_flow=="IN"){
$finale=$jana_wp+$grain_volume;
}else 
if ($grain_flow=="OUT"){
$finale=$jana_wp-$grain_volume;
}else{
}


	  //inserts maize data into database
$sql=mysql_query("insert into warehouse_data_flow (product_id, whouse_id, direction, time, date,  stock, volume ) values
('".$rowz."','".$row."', '".$grain_flow."',now(),curdate(),  '".$finale."', '".$grain_volume."' )") ;

if($sql-->0)
	 {
	 echo "Y";
	 }
	else
	{
	echo "N";
	}

    

	

?>