<?php
class Login_controller extends CI_Controller
{
	function __construct()
	{
		parent::__construct();
		$this->load->model(array('user_model'));
	}

	public function login()
	{
		if($this->input->post('user_name') && $this->input->post('user_password'))
		{
			$model = new User_model;
			$model->user_name = $this->input->post('user_name');
			$model->user_password = md5($this->input->post('user_password'));

			echo $model->login();
		}
	}
}
?>