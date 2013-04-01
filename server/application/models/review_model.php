<?php
class Review_model extends CI_Model{
	
	private $table = 'review';
	public $reviewId;
	public $reviewUser;
	public $reviewPlace;
	public $reviewPoint;

	function __construct(){
		$this->load->database();
	}

	public function listReviews(){
		return $this->db->select($this->table.".*, user.userAlias AS userAlias")
				->from($this->table)
				->join('user', 'user.userId = '.$this->table.'.reviewUser', 'left')
				->where(array('reviewPlace' => $this->reviewPlace))
				->get()
				->result_array();
	}

	public function insert(){
		$this->db->insert($this->table, array('reviewUser' => $this->reviewUser, 'reviewPlace' => $this->reviewPlace, 'reviewPoint' => $this->reviewPoint));
	}

	public function listByKey($key, $value){
		return $this->db->get_where($this->table, array($key => $value))->result();
	}
}
?>