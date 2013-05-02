<?php
class User extends CI_Controller
{
	function __construct()
	{
		parent::__construct();
		$this->load->model(array('user_model'));
	}
	
	public function login()
	{
		if($this->input->post('userName') && $this->input->post('userPassword'))
		{
			$model = new User_model;
			$model->userName = $this->input->post('userName');
			$model->userPassword = md5($this->input->post('userPassword'));

			echo json_encode($model->login());
		}
	}
}
?>