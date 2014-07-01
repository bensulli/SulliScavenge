<?php
require_once "bootstrap.web.php";

$query = $request->getQueryPayload();

$userRepo = $entityManager->getRepository('User');
$teamRepo = $entityManager->getRepository('Team');

if(isset($query->user) && isset($query->team)) {

    $response->setCode(200)->setStatus("OK");

    $user = $userRepo->findOneBy(['username'=>trim($query->user)]);
    $team = $teamRepo->findOneBy(['team_name'=>trim($query->team)]);

    if(!$team) {
        $existingTeam = false;
        $team = new Team();
        $team->setTeamName($query->team)
             ->setCreated(new \DateTime('now'))
             ->setLastUpdate(new \DateTime('now'))
             ->setTotalMembers(0)
             ->setTotalCodesFound(0);

        $entityManager->persist($team);
        $entityManager->flush();
    } else {
        $existingTeam = true;
    }

    if(!$user) {
        // New User
        $user = new User();
        $user->setUsername(trim($query->user))
             ->setTeam($team)
             ->setCreated(new \DateTime('now'))
             ->setCodesFound(0)
             ->setLastUpdate(new \DateTime('now'));

        $entityManager->persist($user);
        $entityManager->flush();

        if(!$existingTeam) {
            $response->addData('code', 0);
            $response->setMessage('New User and New Team');
        } else {
            $response->addData('code', 1);
            $response->setMessage('New User, Existing Team');
        }

    } else if($user->getTeam()->getTeamName() != $team->getTeamName()) {
        // Existing User, Using New Team
        $user->setTeam($team);
        $user->setLastUpdate(new \DateTime('now'));

        $entityManager->persist($user);
        $entityManager->flush();

        if(!$existingTeam) {
            $response->addData('code', 2);
            $response->setMessage('Existing User, New Team');
        } else {
            $response->addData('code', 3);
            $response->setMessage('Existing User, using different Team');
        }
    } else {
        // user and team are the same, no changes
        $response->addData('code', 4);
        $response->setMessage('Existing User using Same Team as Before');
    }

    $response->addData('user', $user);

    render();

} else if(isset($query->user) && !isset($query->team)) {

    $user = $userRepo->findOneBy(['username'=>$query->user]);
    if($user) {
        $response->setCode(200)->setStatus('OK')->setMessage('User Found')->addData('user', $user);
    } else {
        $response->setMessage('User Not Found')->setCode(404);
    }
    render();

} else if(isset($query->team) && !isset($query->user)) {

    $team = $teamRepo->findOneBy(['team_name'=>$query->team]);
    if($team) {
        $response->setCode(200)->setStatus('OK')->setMessage('Team Found')->addData('team', $team);
        $users = $userRepo->findBy(['team'=>$team]);
        $response->addData('team_users', $users);
    } else {
        $response->setMessage('Team Not Found')->setCode(404);
    }
    render();

} else {

    $response->setMessage("User and Team Keys Missing")
             ->setCode(404);
    render();

}