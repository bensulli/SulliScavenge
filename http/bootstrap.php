<?php

use Doctrine\ORM\Tools\Setup;
use Doctrine\ORM\EntityManager;

require_once "vendor/autoload.php";
require_once "Models/Request.php";
require_once "Models/Response.php";

$isDevMode = true;

$apiVersion = "1.0";

$config = Setup::createAnnotationMetadataConfiguration([__DIR__ . "/Entities"], $isDevMode);

$connection = [
    'driver'   => 'pdo_mysql',
    'host'     => 'data.scavenge.sulli.ca',
    'dbname'   => 'scavengedata',
    'user'     => 'sulliscavenger',
    'password' => 'scavengersAssemble'
];

$entityManager = EntityManager::create($connection, $config);

$request = new Request();

$response = new Response(false, $apiVersion);

function render($code = false)
{
    global $response;
    header('Content-Type: application/json');
    if(!$code)
        $code = $response->getCode();
    http_response_code($code);
    echo $response->serialization();
}