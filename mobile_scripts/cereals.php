<?php

 require_once('db.php'); 
 mysql_select_db($database_localhost,$localhost);
   
	$xcood = $_REQUEST['xkood'];
	$ycood = $_REQUEST['ykood'];
	$wsale_bp = $_REQUEST['W_BP'];
	//$wsale_sp = $_REQUEST['W_SP'];
	$retail_bp = $_REQUEST['R_BP'];
	$imei = $_REQUEST['imei'];
	$Cereal = $_REQUEST['Cereal'];
       
	   
  $query2 = "SELECT * FROM product WHERE product_name = '".$Cereal." '"; 
  $rowx= mysql_query($query2);
  while($rowax = mysql_fetch_array($rowx))
  {
    $rowz=$rowax['product_id'];
  }
  
  $query_search = "select * from vw_login_details where  ".$ycood."  between
		   extent_Y_MIN and extent_Y_MAX and 
		  ".$xcood."  between extent_X_MIN and extent_X_MAX";
  $rows= mysql_query($query_search);
			
  while($rowa = mysql_fetch_array($rows))
  {
    $row=$rowa['market_id'];
  }
  
  //validation of the + or - 10%
  $query_jana="SELECT * FROM daily_trading_info where market_id = '".$row."' and product_id = '".$rowz."' order by date desc limit 1";

  $jana_rows= mysql_query($query_jana);
			
  while($jana_rowa = mysql_fetch_array($jana_rows))
  {
    $jana_wp=$jana_rowa['wholesale_Price'];
	$jana_rp=$jana_rowa['retail_Price'];
  }
  
  $ukweli_wp_min =$jana_wp-( 0.1*$jana_wp);
  $ukweli_wp_max =$jana_wp+( 0.1*$jana_wp);
  
  $ukweli_rp_min =$jana_wp-( 0.1*$jana_wp);
  $ukweli_rp_max =$jana_wp-( 0.1*$jana_wp);
  
  if($ukweli_wp_min>$wsale_bp ){
  echo "Z";
  //$range="OUT OF RANGE";
  }else 
  if($ukweli_wp_max<$wsale_bp){
  echo "Z";
  //$range="OUT OF RANGE";
  }else 
  if($ukweli_rp_min>$retail_bp){
  echo "Z";
 // $range="OUT OF RANGE";
  }else if($ukweli_rp_max<$retail_bp){
  echo "Z";
  //$range="OUT OF RANGE";
  } else {
 // echo "Y";
  $range="OK";
  }
 /* $leo="select curdate();";
  
  $pili="SELECT * FROM daily_trading_info where market_id = '".$row."' and product_id = '".$rowz."' and date='".$leo."' order by date desc limit 1";
  if($pili --> 0)
 {
    echo "J";

 }else
 {
    //echo "N";
 }*/
 //inserts Cereals data into database
 $sql = mysql_query("INSERT INTO daily_trading_info (product_id, market_id, retail_Price, wholesale_Price,  date, comments,publish,imei )
		  VALUES('".$rowz."','".$row."', '".$retail_bp."',  '".$wsale_bp."', curdate(),'".$range."','0' ,'".$imei."')");

 $rowzote= mysql_query($sql);

 $rowax = mysql_num_rows($rowzote);
 /*if($rowax --> 0)
 {
    echo "Y";
	
 }
 else
 {
    echo "N";
 }*/
  mysql_close();
?>