<?php

use Doctrine\ORM\Tools\Setup;
use Doctrine\ORM\EntityManager;

require_once "vendor/autoload.php";
$isDevMode = true;

$config = Setup::createAnnotationMetadataConfiguration([__DIR__ . "/Entities"], $isDevMode);

$connection = [
    'driver'   => 'pdo_mysql',
    'host'     => 'data.scavenge.sulli.ca',
    'dbname'   => 'scavengedata',
    'user'     => 'sulliscavenger',
    'password' => 'scavengersAssemble'
];

$entityManager = EntityManager::create($connection, $config);