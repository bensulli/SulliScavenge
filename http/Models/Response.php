<?php

class Response {
    protected $status;
    protected $apiVersion;
    protected $data;
    protected $message;
    protected $code;

    public function __construct($status, $apiVersion)
    {
        $this->status = $status;
        $this->apiVersion = $apiVersion;
        $this->message = "";
        $this->code = 500;
        $this->data = array();
    }

    /**
     * @param string $message
     * @return Response
     */
    public function setMessage($message)
    {
        $this->message = $message;
        return $this;
    }

    /**
     * @return string
     */
    public function getMessage()
    {
        return $this->message;
    }

    /**
     * @param int $code
     * @return Response
     */
    public function setCode($code)
    {
        $this->code = $code;
        return $this;
    }

    /**
     * @return int
     */
    public function getCode()
    {
        return $this->code;
    }

    /**
     * @param mixed $status
     * @return Response
     */
    public function setStatus($status)
    {
        $this->status = $status;
        return $this;
    }

    /**
     * @return mixed
     */
    public function getStatus()
    {
        return $this->status;
    }

    /**
     * @param $key
     * @param $value
     */
    public function addData($key, $value) {
        $this->data[$key] = $value;
    }

    /**
     * @param $key
     */
    public function removeData($key) {
        unset($this->data[$key]);
    }

    /**
     * @param $data
     */
    public function setData($data) {
        $this->data = $data;
    }

    /**
     * @return mixed
     */
    public function serialization() {
        $serializer = \JMS\Serializer\SerializerBuilder::create()->build();
        return $serializer->serialize($this, "json");
    }

    /**
     * @param $status
     * @param $apiVersion
     * @return Response
     */
    public static function Create($status, $apiVersion) {
        return new Response($status, $apiVersion);
    }
}