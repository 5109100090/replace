<?php
class Place_model extends CI_Model
{
	private $table = "place";
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

	public function list_in_range($distance = 1000)
	{
		return $this->db->query("
			SELECT place_name,
				(
					6371000 *
					acos(
						cos( radians(" . $this->place_lat . ") ) *
						cos( radians( place_lat ) ) *
						cos( radians(place_lng) - radians(" . $this->place_lng . ") ) +
						sin( radians(" . $this->place_lat . ") ) *
						sin( radians(place_lat) )
					)
				) AS distance
			FROM " . $this->table . "
			HAVING distance < $distance
			ORDER BY distance");
	}
}
?>