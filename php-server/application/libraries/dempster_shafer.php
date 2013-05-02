<?php
class Dempster_shafer{
	private $ci = null;
	function __construct(){
		$this->ci =& get_instance();
		$this->ci->load->helper(array('place'));
	}

	public function process($data, $debug = FALSE){
		$before = array();
		$dsTable = array();
		foreach($data as $key => $simValue){
			if($simValue == -999){
				continue;
			}
			
			if(sizeof($before) == 0){
				$before[$key] = $simValue;
				$before['t'] = 1 - $simValue;
				continue;
			}

			$newEvidence = array('key' => $key, 'value' => $simValue);
			$beforeCandidate = array();
			foreach($before as $keyBefore => $b){
				$newEvidenceExplode = explode(',', $newEvidence['key']);
				$oldEvidenceExplode = explode(',', $keyBefore);

				$intersection = array_unique(array_intersect($newEvidenceExplode, $oldEvidenceExplode));
				$keyCol1 = ($keyBefore == 't' ? $newEvidence['key'] : (sizeof($intersection) == 0 ? 'x' : implode(',', $intersection)));
				$keyCol2 = $keyBefore;
				$valCol1 = $b * $newEvidence['value'];
				$valCol2 = $b * (1 - $newEvidence['value']);

				if(array_key_exists($keyCol1, $beforeCandidate)){
					$beforeCandidate[$keyCol1] += $valCol1;
				}else{
					$beforeCandidate[$keyCol1] = $valCol1;
				}

				if(array_key_exists($keyCol2, $beforeCandidate)){
					$beforeCandidate[$keyCol2] += $valCol2;
				}else{
					$beforeCandidate[$keyCol2] = $valCol2;
				}

				// debug ds table
				//*/
				$dsTable[$keyBefore] = array(
					$keyCol1 => $valCol1,
					$keyCol2 => $valCol2
				);
				//*/
			}

			$x = (array_key_exists('x', $beforeCandidate) ? $beforeCandidate['x'] : 0);
			if($x >= 1) return -999;
			if($x > 0 && $x < 1){
				unset($beforeCandidate['x']);
				array_walk($beforeCandidate, 'dsCombination', $x);
				//echo'beforeCandidate (2) :<br /><pre>';print_r($beforeCandidate);echo'</pre>';
			}
			if($debug){
				echo'newEvidence : ';print_r($newEvidence);echo'<br />';
				echo'ds table :<br /><pre>';print_r($dsTable);echo'</pre>';
				echo'beforeCandidate :<br /><pre>';print_r($beforeCandidate);echo'</pre>';
			}
			$before = $beforeCandidate;
		}
		return max($before);
	}
}
?>