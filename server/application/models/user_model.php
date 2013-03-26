<?php
class User_model extends CI_Model
{
	private $table = 'user';
	public $user_name;
	public $user_password;

	function __construct()
	{
		$this->load->database();
	}

	public function login()
	{
		$q = $this->db->get_where($this->table, array('user_name' => $this->user_name, 'user_password' => $this->user_password)); 
		if($q->num_rows() == 1)
		{
			return json_encode($q->result_array());
		}
		else
		{
			return 0;
		}
	}
}
?>