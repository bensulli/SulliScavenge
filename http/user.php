<?php
require_once "bootstrap.php";

$query = $request->getQueryPayload();

$userRepo = $entityManager->getRepository('User');
$teamRepo = $entityManager->getRepository('Team');

if(isset($query->user) && isset($query->team)) {

    $response->setCode(200)->setStatus("OK");
    render();

} else {
    if(!isset($query->user)) {
        $response->setMessage("User Key Missing")
                 ->setCode(404);
        render();
    } else if(!isset($query->team)) {
        $response->setMessage("Team Key Missing")
                 ->setCode(404);
        render();
    }
}