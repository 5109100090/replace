<?php
class Type extends CI_Controller
{
	function __construct()
	{
		parent::__construct();
		$this->load->model(array('type_model'));
	}
	
	public function listAll()
	{
		echo json_encode($this->type_model->listAll());
	}
}
?>