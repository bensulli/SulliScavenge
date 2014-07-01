<?php
require_once __DIR__.'/bootstrap.php';
require_once __DIR__."/Models/Request.php";
require_once __DIR__."/Models/Response.php";

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