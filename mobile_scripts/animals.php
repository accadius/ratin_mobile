<?php

 require_once('db.php'); 
   mysql_select_db($database_localhost,$localhost);
   
   $xcood = $_REQUEST['xkood'];
	$ycood = $_REQUEST['ykood'];
	$price_eggs = $_REQUEST['Eggs'];
	$price_milk = $_REQUEST['Milk'];
	$price_pork = $_REQUEST['Pork'];
	$price_beef = $_REQUEST['Beef'];
	
	$jah="SELECT * FROM `daily_trading_info` where date=curdate();";
	 
	 if ($jah --> 0)
{
   
$query_search = "select * from vw_login_details where  ".$ycood."  between extent_Y_MIN and extent_Y_MAX and 
	  ".$xcood."  between extent_X_MIN and extent_X_MAX";
	$rows= mysql_query($query_search);
	while($rowa = mysql_fetch_array($rows)){
	
	$row=$rowa['market_id'];
	
	
	
		// mysql_result($query, 0, 'title');
	//$row="".$rowa['market_id']."";
		
	//$row=mysql_result($rows, 2, 'market_id');
	 
	 
	  //inserts maize data into database
$sql=mysql_query("insert into daily_trading_info (product_id, market_id, price) values
('P15','".$row."', '".$price_eggs."' )");


$r=mysql_query($sql);

	  //inserts Beans data into database
$sql1=mysql_query("insert into daily_trading_info (product_id, market_id, price) values
('P16','".$row."', '".$price_milk."' )");
$r=mysql_query($sql1);

	  //inserts Wheat data into database
$sql2=mysql_query("insert into daily_trading_info (product_id, market_id, price) values
('P17','".$row."', '".$price_pork."' )");
$r=mysql_query($sql2);

	  //inserts Rice data into database
$sql3=mysql_query("insert into daily_trading_info (product_id, market_id, price) values
('P18','".$row."', '".$price_beef."' )");


$r=mysql_query($sql3);


}
if($r --> 0)
	 {
	 echo "Y";
	 }
	else
	{
	echo "N";
	}

}else{
   echo "Z";
	//die('ushindwe');
	exit();
	}
?>