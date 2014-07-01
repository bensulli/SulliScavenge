<?php

use Doctrine\ORM\Tools\Setup;
use Doctrine\ORM\EntityManager;

require_once "vendor/autoload.php";

$classLoader = new \Doctrine\Common\ClassLoader('Entities',__DIR__ . "/Entities");
$classLoader->register();
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

