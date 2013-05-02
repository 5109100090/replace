<?php
function dsCombination(&$item, $key, $b){
	$item = $item / (1 - $b);
}

function tuningFormula(&$sim, $key, $weight = 0.99){
	$sim = (2*$sim*$weight)/(1+($sim*$weight));
}
?>