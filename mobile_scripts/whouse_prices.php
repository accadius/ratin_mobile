<?php

 require_once('db.php'); 
   mysql_select_db($database_localhost,$localhost);
   
   $xcood = $_REQUEST['xkood'];
	$ycood = $_REQUEST['ykood'];
	$price_maize = $_REQUEST['Maize'];
	$price_beans = $_REQUEST['Beans'];
	$price_rice = $_REQUEST['Rice'];
	$price_wheat = $_REQUEST['Wheat'];
	$price_millet = $_REQUEST['Millet'];
	$price_sorghum = $_REQUEST['Sorghum'];
	
	
  
$query_search = "select * from vw_login_details where  ".$ycood."  between extent_Y_MIN and extent_Y_MAX and 
	  ".$xcood."  between extent_X_MIN and extent_X_MAX";


	$rows= mysql_query($query_search);
			
	 

	while($rowa = mysql_fetch_array($rows)){
	
	$row=$rowa['market_id'];
	


	

	  //inserts maize data into database
$sql=mysql_query("insert into daily_warehouse_info (product_id, whouse_id, sell_price) values
('P001','".$row."', '".$price_maize."' )");


$r=mysql_query($sql);

	  //inserts Beans data into database
$sql1=mysql_query("insert into daily_warehouse_info (product_id, whouse_id, sell_price) values
('P006','".$row."', '".$price_beans."' )");
$r=mysql_query($sql1);

	  //inserts Wheat data into database
$sql2=mysql_query("insert into daily_warehouse_info (product_id, whouse_id, sell_price) values
('P003','".$row."', '".$price_wheat."' )");
$r=mysql_query($sql2);

	  //inserts Rice data into database
$sql3=mysql_query("insert into daily_warehouse_info (product_id, whouse_id, sell_price) values
('P004','".$row."', '".$price_rice."' )");


$r=mysql_query($sql3);

 //inserts Millet data into database
$sql4=mysql_query("insert into daily_warehouse_info (product_id, whouse_id, sell_price) values
('P005','".$row."', '".$price_millet."' )");


$r=mysql_query($sql4);

 //inserts Sorghum data into database
$sql5=mysql_query("insert into daily_warehouse_info (product_id, whouse_id, sell_price) values
('P002','".$row."', '".$price_sorghum."' )");


$r=mysql_query($sql5);


}
if($r --> 0)
	 {
	 echo "Y";
	 }
	else
	{
	echo "N";
	}

    

	

?>