<?php
//authentication credentials
$hostname_localhost ="hostname";
$database_localhost ="database_name";
$username_localhost ="db_username";
$password_localhost ="db_password";

//connect to database
$localhost = mysql_connect($hostname_localhost,$username_localhost,$password_localhost)
or
trigger_error(mysql_error(),E_USER_ERROR);
?>
