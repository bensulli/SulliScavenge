<?php

use Doctrine\ORM\Tools\Setup;
use Doctrine\ORM\EntityManager;

require_once "vendor/autoload.php";
$isDevMode = true;

$config = Setup::createAnnotationMetadataConfiguration([__DIR__ . "/src/Entities"], $isDevMode);

$connection = [
    'driver' => 'pdo_mysql'
];

$entityManager = EntityManager::create($connection, $config);