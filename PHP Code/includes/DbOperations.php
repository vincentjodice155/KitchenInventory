<?php

    class DbOperations{
        //CRUD method
        private $con;
        
        function __construct(){

            require_once dirname(__FILE__).'/DbConnect.php';

            $db = new DbConnect();

            $this->con = $db->connect();
        }
        //Create
        function createUser($name,$alert,$quantity,$popular){
            $stmt = $this->con->prepare("INSERT INTO new_test (name,alert,quantity,popular) VALUES (?, ?, ?, ?)");
            $stmt -> bind_param("siii",$name,$alert,$quantity,$popular);
            
            if($stmt->execute())
                return true;
                return false;
        }

        function deleteChocolate($name){
            $stmt = $this->con->prepare("DELETE FROM new_test WHERE name = ?");
            $stmt -> bind_param("s",$name);

            if($stmt->execute()){
            return true;
            return false;
            }
        }
        
    }
