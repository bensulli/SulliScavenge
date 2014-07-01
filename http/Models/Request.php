<?php

class Request {

    protected $server_name;
    protected $server_address;
    protected $method;
    protected $port;
    protected $request_scheme;
    protected $script_name;
    protected $protocol;
    protected $query_string;
    protected $get;
    protected $post;
    protected $payload;
    protected $uri;
    protected $request_time;

    public function __construct()
    {
        $this->server_name = $_SERVER['SERVER_NAME'];
        $this->server_address = $_SERVER['SERVER_ADDR'];
        $this->method = $_SERVER['REQUEST_METHOD'];
        $this->port = $_SERVER['SERVER_PORT'];
        $this->request_scheme = $_SERVER['REQUEST_SCHEME'];
        $this->script_name = $_SERVER['SCRIPT_NAME'];
        $this->protocol = $_SERVER['SERVER_PROTOCOL'];
        $this->query_string = $_SERVER['QUERY_STRING'];
        $this->uri = $_SERVER['REQUEST_URI'];
        $this->request_time = $_SERVER['REQUEST_TIME'];
        $this->get = (object)$_GET;
        $this->post = (object)$_POST;
    }

    /**
     * @return mixed
     */
    public function getQueryPayload()
    {
        return $this->get;
    }

    /**
     * @return mixed
     */
    public function getMethod()
    {
        return $this->method;
    }

    /**
     * @return mixed
     */
    public function getPayload()
    {
        return $this->payload;
    }

    /**
     * @return mixed
     */
    public function getPort()
    {
        return $this->port;
    }

    /**
     * @return mixed
     */
    public function getPostPayload()
    {
        return $this->post;
    }

    /**
     * @return mixed
     */
    public function getProtocol()
    {
        return $this->protocol;
    }

    /**
     * @return mixed
     */
    public function getQueryString()
    {
        return $this->query_string;
    }

    /**
     * @return mixed
     */
    public function getRequestScheme()
    {
        return $this->request_scheme;
    }

    /**
     * @return mixed
     */
    public function getRequestTime()
    {
        return $this->request_time;
    }

    /**
     * @return mixed
     */
    public function getScriptName()
    {
        return $this->script_name;
    }

    /**
     * @return mixed
     */
    public function getServerAddress()
    {
        return $this->server_address;
    }

    /**
     * @return mixed
     */
    public function getServerName()
    {
        return $this->server_name;
    }

    /**
     * @return mixed
     */
    public function getUri()
    {
        return $this->uri;
    }


}