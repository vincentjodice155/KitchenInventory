<?php

require_once '../includes/DbOperations.php';
$response = array();

if($_SERVER['REQUEST_METHOD'] =='POST'){
    
    if(isset($_POST['name']) && isset($_POST['alert']) && isset($_POST['quantity']) && isset($_POST['popular'])){
        //operate the data further

        $db = new DbOperations();

            if($db->createUser($_POST['name'],$_POST['alert'],$_POST['quantity'],$_POST['popular'])){
                $response['error'] = false;
                $response['message']= "User registered successfully";
             }
        
            else{
                $response['error'] = true;
                $response['message']= "Some error occured please try again";
            }

   }else{
        $resonse['error'] = true;
        $response['message'] = "Required fields are missing";
    } 
}else{
    $response['error'] = true;
    $response['message'] = "Invalid Request";
}

echo json_encode($response);