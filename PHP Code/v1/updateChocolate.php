<?php

define('DB_NAME','candy_manor');
define('DB_USER','root');
define('DB_PASSWORD','');
define('DB_HOST','localhost');

$conn = mysqli_connect(DB_HOST,DB_USER,DB_PASSWORD,DB_NAME);

$name = $_POST["name"];
$alert = $_POST["alert"];
$quantity = $_POST["quantity"];
$popular = $_POST["popular"];

$sql = "UPDATE new_test SET name = '$name', alert = '$alert', quantity = '$quantity', popular= '$popular' where name = '$name'";

$results = mysqli_query($conn,$sql);

if($results){
    echo "Data Updated";
}

else{
    echo "Failed";
}

mysqli_close($conn);