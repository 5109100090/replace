<?php
class User_model extends CI_Model
{
	private $table = 'user';
	public $userId;
	public $userName;
	public $userPassword;

	function __construct(){
		$this->load->database();
	}

	public function get(){
		return $this->db->get_where($this->table, array('userId' => $this->userId))->row();
	}

	public function login(){
		$q = $this->db->get_where($this->table, array('userName' => $this->userName, 'userPassword' => $this->userPassword)); 
		if($q->num_rows() == 1){
			return $q->row_array();
		}else{
			return 0;
		}
	}

	public function listAll(){
		return $this->db->get($this->table)->result();
	}

	public function update($data){
		$this->db->update_batch($this->table, $data, 'userId'); 
	}
}
?>