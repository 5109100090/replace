<?php
class Place_model extends CI_Model
{
	private $table = "place";
	public $placeId;
	public $placeName;
	public $placeDesc;
	public $placeLat;
	public $placeLng;
	public $placeType;

	function __construct()
	{
		$this->load->database();
	}

	public function listInRange($distance = 1000)
	{
		return $this->db->query("
			SELECT placeName,
				(
					6371000 *
					acos(
						cos( radians(" . $this->placeLat . ") ) *
						cos( radians(placeLat) ) *
						cos( radians(placeLng) - radians(" . $this->placeLng . ") ) +
						sin( radians(" . $this->placeLat . ") ) *
						sin( radians(placeLat) )
					)
				) AS placeDistance
			FROM " . $this->table . "
			HAVING placeDistance < $distance
			ORDER BY placeDistance")->result_array();
	}
}
?>