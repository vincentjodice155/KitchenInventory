<?php

define('DB_NAME','candy_manor');
define('DB_USER','root');
define('DB_PASSWORD','');
define('DB_HOST','localhost');

$conn = new mysqli(DB_HOST,DB_USER,DB_PASSWORD,DB_NAME);

$name = $_POST["name"];

$sql = "DELETE FROM new_test where name = '$name'";

$result = mysqli_query($conn,$sql);

if($result){
    echo "Data deleted";
}

else{
    echo "Failed";
}

mysqli_close($conn);
