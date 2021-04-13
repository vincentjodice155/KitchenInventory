<?php


define('DB_NAME','candy_manor');
define('DB_USER','root');
define('DB_PASSWORD','');
define('DB_HOST','localhost');

$conn = new mysqli(DB_HOST,DB_USER,DB_PASSWORD,DB_NAME);

if(mysqli_connect_errno()){
    die('Failed to connect with database'.mysqli_connect_error());
}

$stmt = $conn->prepare("SELECT name,alert,quantity, popular FROM new_test;");

$stmt->execute();

$stmt->bind_result($name, $alert, $quantity, $popular);
    
$chocolates = array(); 
    
while($stmt->fetch()){
    $chocolate  = array();
    $chocolate['name'] = $name; 
    $chocolate['alert'] = $alert; 
    $chocolate['popular'] = $popular; 
    $chocolate['quantity'] = $quantity; 
    
    array_push($chocolates, $chocolate); 
}

echo json_encode($chocolates);
?>