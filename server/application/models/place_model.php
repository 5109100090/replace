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

	public function getDetail(){
		return $this->db->select($this->table.".*, type.typeName AS typeName")
				->from($this->table)
				->join('type', 'type.typeId = '.$this->table.'.placeType', 'left')
				->where(array('placeId' => $this->placeId))
				->get()
				->row_array();
		//return $this->db->get_where($this->table, array('placeId' => $this->placeId))->row_array();
	}

	public function listInRange($distance = 1000)
	{
		return $this->db->query("
			SELECT placeId, placeName,
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