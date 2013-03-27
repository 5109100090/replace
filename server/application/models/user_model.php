<?php
class User_model extends CI_Model
{
	private $table = 'user';
	public $userName;
	public $userPassword;

	function __construct(){
		$this->load->database();
	}

	public function login(){
		$q = $this->db->get_where($this->table, array('userName' => $this->userName, 'userPassword' => $this->userPassword)); 
		if($q->num_rows() == 1){
			return $q->row_array();
		}else{
			return 0;
		}
	}
}
?>