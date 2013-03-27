<?php
class Type_model extends CI_Model
{
	private $table = 'type';

	function __construct(){
		$this->load->database();
	}

	public function listAll(){
		return $this->db->query("SELECT type.typeName AS typeName, COUNT(place.placeId) AS typeTotal
				FROM place
				RIGHT JOIN `type` ON type.typeId = place.placeType
				GROUP BY type.typeId
				ORDER BY type.typeId")->result_array();
	}
}
?>