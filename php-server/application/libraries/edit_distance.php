<?php
class Edit_distance {
    
    public $str1, $str2;
    private $length1, $length2;

    public function similarity(){
        $this->length1 = strlen($this->str1);
        $this->length2 = strlen($this->str2);
        $d = $this->process();
        $x = 1 - ( $d / (max($this->length1, $this->length2)) );
        return $x;
    }
    
    private function process(){
        $m = array();
        for($i = 0; $i < $this->length1 + 1; $i++){
            for($j = 0; $j < $this->length2 + 1; $j++){
                if($i == 0){
                    $m[$i][$j] = $j;
                }else if($j == 0){
                    $m[$i][$j] = $i;
                }else{
                    if(substr($this->str1, $i - 1, 1) == substr($this->str2, $j - 1, 1)){
                        $m[$i][$j] = $m[$i-1][$j-1];
                    }else{
                        $m[$i][$j] = 1 + min( $m[$i][$j-1], $m[$i-1][$j-1], $m[$i-1][$j] );
                    }
                }
            }
        }
        
        return $m[$this->length1][$this->length2];
    }
}

?>