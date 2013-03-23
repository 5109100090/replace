<?php
class Place_model extends CI_Model
{
	public $place_id;
	public $place_name;
	public $place_desc;
	public $place_lat;
	public $place_lng;
	public $place_type;

	function __construct()
	{
		$this->load->database();
	}
}
?>