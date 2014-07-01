<?php

use Doctrine\ORM\Tools\Setup;
use Doctrine\ORM\EntityManager;
use Doctrine\ORM\Mapping\Driver\AnnotationDriver;
use Doctrine\Common\Annotations\AnnotationReader;
use Doctrine\Common\Annotations\AnnotationRegistry;

require_once "vendor/autoload.php";

$classLoader = new \Doctrine\Common\ClassLoader('Entities',__DIR__ . "/Entities");
$classLoader->register();
$isDevMode = true;

$apiVersion = "1.0";

//$config = Setup::createAnnotationMetadataConfiguration([__DIR__ . "/Entities"], $isDevMode);
$paths  = array(__DIR__ . '/Entities');
$config = Setup::createConfiguration($isDevMode);
$driver = new AnnotationDriver(new AnnotationReader(), $paths);

$connection = [
    'driver'   => 'pdo_mysql',
    'host'     => 'data.scavenge.sulli.ca',
    'dbname'   => 'scavengedata',
    'user'     => 'sulliscavenger',
    'password' => 'scavengersAssemble'
];

AnnotationRegistry::registerLoader('class_exists');
$config->setMetadataDriverImpl($driver);

$entityManager = EntityManager::create($connection, $config);

