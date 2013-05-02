<?php
class Place extends CI_Controller {

	public function __construct()
	{
		parent::__construct();
		$this->load->library(array('edit_distance','dempster_shafer'));
		$this->load->model(array('review_model','user_model','place_model'));
		$this->load->helper(array('place'));
	}

	public function process(){
		if($this->input->post('userId') && $this->input->post('typeId') && $this->input->post('currentLat') && $this->input->post('currentLng') && $this->input->post('range')){
			$model = new Place_model;
			$model->placeLat =  $this->input->post('currentLat');
			$model->placeLng =  $this->input->post('currentLng');
			echo json_encode($model->listInRange($this->input->post('typeId'), $this->input->post('range')));
		}
	}

	public function process1(){
		$reviews = new Review_model;
		$users = new User_model;
		$places = new Place_model;

		$activeUser = $users->getByKey('userId', 1);
		$activeUserProperty = array(
			'userFoods' => explode(',', $activeUser->userFoods),
			'userDrinks' => explode(',', $activeUser->userDrinks),
			'userBooks' => explode(',', $activeUser->userBooks),
			'userMovies' => explode(',', $activeUser->userMovies),
			'userMusics' => explode(',', $activeUser->userMusics),
			'userOccupation' => explode(',', $activeUser->userOccupation),
			'userDOB' => explode(',', $activeUser->userDOB),
			'userGender' => explode(',', $activeUser->userGender),
		);

		$ds = array(); //dempster-shafer table
		$ed = new Edit_distance();
		$places->placeLat =  '-7.27957';
		$places->placeLng =  '112.79751';
		foreach($places->listInRange(1, 1000) as $place){
			echo '<h1>'.$place['placeName'].'</h1>';
			$reviews->reviewPlace = $place['placeId'];
			foreach($reviews->listReviews() as $review){
				if($review['userId'] != $activeUser->userId){
					$currenteUser = array(
						'userFoods' => explode(',', $review['userFoods']),
						'userDrinks' => explode(',', $review['userDrinks']),
						'userBooks' => explode(',', $review['userBooks']),
						'userMovies' => explode(',', $review['userMovies']),
						'userMusics' => explode(',', $review['userMusics']),
						'userOccupation' => explode(',', $review['userOccupation']),
						'userDOB' => explode(',', $review['userDOB']),
						'userGender' => explode(',', $review['userGender']),
					);

					foreach($activeUserProperty as $keyProperty => $property){
						foreach($property as $p1){
							foreach($currenteUser[$keyProperty] as $p2){
								$ed->str1 = $p1;
								$ed->str2 = $p2;
								$edValue = $ed->similarity();
								echo $review['userAlias'].' => '.$keyProperty.' ('.$p1.' & '.$p2.') : '.$edValue.'<br/>';
							}
						}
					}
				}
				//echo $review['userAlias'].' => userDOB ('.$activeUser->userDOB.' & '.$review['userDOB'].') : '.(date('Y', strtotime($activeUser->userDOB)) - date('Y', strtotime($review['userDOB']))).'<br/>';
				//echo $review['userAlias'].' => userGender ('.$activeUser->userGender.' & '.$review['userGender'].') : '.($activeUser->userGender == $review['userGender'] ? 1 : 0).'<br/>';
			}
		}
	}

	public function process2(){
		$reviews = new Review_model;
		$users = new User_model;
		$places = new Place_model;
		$ed = new Edit_distance;

		$activeUser = $users->getByKey('userId', 1);
		$activeUserProperty = array(
			'userFoods' => explode(',', $activeUser->userFoods),
			'userDrinks' => explode(',', $activeUser->userDrinks),
			'userBooks' => explode(',', $activeUser->userBooks),
			'userMovies' => explode(',', $activeUser->userMovies),
			'userMusics' => explode(',', $activeUser->userMusics),
			'userOccupation' => $activeUser->userOccupation,
			'userDOB' => $activeUser->userDOB,
			'userGender' => $activeUser->userGender,
		);

		$ds = array(); //dempster-shafer table
		$places->placeLat = '-7.27957';
		$places->placeLng = '112.79751';
		foreach($places->listInRange(1, 1000) as $place){
			echo '<h1>'.$place['placeName'].'</h1>';
			$reviews->reviewPlace = $place['placeId'];
			foreach($reviews->listReviews() as $review){
				if($review['userId'] != $activeUser->userId){
					echo '<p>'.$review['userAlias'].'</p>';
					$currenteUser = array(
						'userFoods' => explode(',', $review['userFoods']),
						'userDrinks' => explode(',', $review['userDrinks']),
						'userBooks' => explode(',', $review['userBooks']),
						'userMovies' => explode(',', $review['userMovies']),
						'userMusics' => explode(',', $review['userMusics']),
						'userOccupation' => $review['userOccupation'],
						'userDOB' => $review['userDOB'],
						'userGender' => $review['userGender'],
					);

					$propertySimiliarityValue = array();
					foreach($activeUserProperty as $keyProperty => $property){
						if(!in_array($keyProperty, array('userOccupation','userDOB','userGender'))){
							$before = array();
							$beforeCandidate = array();
							$propertyValue = array();
							$propertySymbol = 'A';
							foreach($property as $p1){
								if(!in_array($p1, $propertyValue)){
									$propertyValue[$propertySymbol] = $p1;
									$propertyValueFlip[$p1] = $propertySymbol;
									$propertySymbol++;
								}

								foreach($currenteUser[$keyProperty] as $p2){
									if(!in_array($p2, $propertyValue)){
										$propertyValue[$propertySymbol] = $p2;
										$propertyValueFlip[$p2] = $propertySymbol;
										$propertySymbol++;
									}

									$ed->str1 = $p1;
									$ed->str2 = $p2;
									$edValue = $ed->similarity();

									//echo $review['userAlias'].' => '.$keyProperty.' ('.$propertyValueFlip[$p1].':'.$p1.' & '.$propertyValueFlip[$p2].':'.$p2.') : '.$edValue.'<br/>';
									$newEvidence = array('key' => $propertyValueFlip[$p1].','.$propertyValueFlip[$p2], 'value' => $edValue, 't' => 1 - $edValue);
									if(sizeof($before) == 0){
										//first iteration condition
										$before[$propertyValueFlip[$p1].','.$propertyValueFlip[$p2]] = $edValue;
										$before['t'] = 1 - $edValue;
									}else{
										$ds = array(); //dempster-shafer table
										$beforeCandidate = array();
										foreach($before as $keyBefore => $b){
											$newEvidenceExplode = explode(',', $newEvidence['key']);
											$oldEvidenceExplode = explode(',', $keyBefore);

											$intersection = array_unique(array_intersect($newEvidenceExplode, $oldEvidenceExplode));
											$keyCol1 = ($keyBefore == 't' ? $newEvidence['key'] : (sizeof($intersection) == 0 ? 'x' : implode(',', $intersection)));
											$keyCol2 = $keyBefore;
											$valCol1 = $b * $newEvidence['value'];
											$valCol2 = $b * $newEvidence['t'];

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
											$ds[$keyBefore] = array(
												$keyCol1 => $valCol1,
												$keyCol2 => $valCol2
											);
											//*/
										}

										//echo'newEvidence : ';print_r($newEvidence);echo'<br />';
										//echo'ds table :<br /><pre>';print_r($ds);echo'</pre>';
										//echo'beforeCandidate (1) :<br /><pre>';print_r($beforeCandidate);echo'</pre>';
										$x = (array_key_exists('x', $beforeCandidate) ? $beforeCandidate['x'] : 0);
										/*/
										if($x == 1){
											echo'newEvidence : ';print_r($newEvidence);echo'<br />';
											echo'ds table :<br /><pre>';print_r($ds);echo'</pre>';
											echo'beforeCandidate (1) :<br /><pre>';print_r($beforeCandidate);echo'</pre>';
											echo $p1.$p2.'<br>';
										}
										//*/
										//echo 'x => '.$x.'<br>';
										if($x != 0 && $x != 1){
											unset($beforeCandidate['x']);
											array_walk($beforeCandidate, 'dsCombination', $x);
											//echo'beforeCandidate (2) :<br /><pre>';print_r($beforeCandidate);echo'</pre>';
										}
										//set new before using ds equation
										$before = $beforeCandidate;
									}
								}
							}
							$propertySimiliarityValue[$keyProperty] = max($before);
						}else{
							$ed->str1 = $property;
							$ed->str2 = $currenteUser[$keyProperty];
							$propertySimiliarityValue[$keyProperty] = $ed->similarity();
						}
						//echo '<pre>';print_r($before);echo'</pre>';
						echo $keyProperty.' : '.$propertySimiliarityValue[$keyProperty].'<br />';
					}
				}
			}
		}
	}

	public function process3(){
		$reviews = new Review_model;
		$users = new User_model;
		$places = new Place_model;
		$ed = new Edit_distance;
		$ds = new Dempster_shafer;

		$activeUser = $users->getByKey('userId', 1);
		$activeUserProperty = array(
			'userFoods' => explode(',', $activeUser->userFoods),
			'userDrinks' => explode(',', $activeUser->userDrinks),
			'userBooks' => explode(',', $activeUser->userBooks),
			'userMovies' => explode(',', $activeUser->userMovies),
			'userMusics' => explode(',', $activeUser->userMusics),
			'userOccupation' => $activeUser->userOccupation,
			'userDOB' => $activeUser->userDOB,
			'userGender' => $activeUser->userGender,
		);

		$places->placeLat = '-7.27957';
		$places->placeLng = '112.79751';
		foreach($places->listInRange(1, 1000) as $place){
			echo '<h1>'.$place['placeName'].'</h1>';
			$reviews->reviewPlace = $place['placeId'];
			foreach($reviews->listReviews() as $review){
				if($review['userId'] != $activeUser->userId){
					echo '<p>'.$review['userAlias'].'</p>';
					$currenteUser = array(
						'userFoods' => explode(',', $review['userFoods']),
						'userDrinks' => explode(',', $review['userDrinks']),
						'userBooks' => explode(',', $review['userBooks']),
						'userMovies' => explode(',', $review['userMovies']),
						'userMusics' => explode(',', $review['userMusics']),
						'userOccupation' => $review['userOccupation'],
						'userDOB' => $review['userDOB'],
						'userGender' => $review['userGender'],
					);

					$propertySimiliarityValue = array();
					foreach($activeUserProperty as $keyProperty => $property){
						if(!in_array($keyProperty, array('userOccupation','userDOB','userGender'))){
							$before = array();
							$beforeCandidate = array();
							$propertyValue = array();
							$propertySymbol = 'A';
							$similarityValue = array();
							foreach($property as $p1){
								if(!in_array($p1, $propertyValue)){
									$propertyValue[$propertySymbol] = $p1;
									$propertyValueFlip[$p1] = $propertySymbol;
									$propertySymbol++;
								}

								foreach($currenteUser[$keyProperty] as $p2){
									if(!in_array($p2, $propertyValue)){
										$propertyValue[$propertySymbol] = $p2;
										$propertyValueFlip[$p2] = $propertySymbol;
										$propertySymbol++;
									}

									$ed->str1 = $p1;
									$ed->str2 = $p2;
									$edValue = $ed->similarity();
									$similarityValue[$propertyValueFlip[$p1].','.$propertyValueFlip[$p2]] = $edValue;
								}
							}
							$propertySimiliarityValue[$keyProperty] = $ds->process($similarityValue);
						}else{
							$ed->str1 = $property;
							$ed->str2 = $currenteUser[$keyProperty];
							$propertySimiliarityValue[$keyProperty] = $ed->similarity();
						}
						//echo '<pre>';print_r($before);echo'</pre>';
						echo $keyProperty.' : '.$propertySimiliarityValue[$keyProperty].'<br />';
					}
					//array_walk($propertySimiliarityValue, 'tuningFormula');
					echo 'final ds value : '.$ds->process($propertySimiliarityValue).'<br />';
				}
			}
		}
	}

	public function getDetail(){
		if($this->input->post('placeId')){
			$model = new Place_model;
			$model->placeId =  $this->input->post('placeId');
			echo json_encode(array($model->getDetail()));
		}
	}

	public function index()
	{
		$ed = new Edit_distance();
		$ed->str1 = "watching movie";
		$ed->str2 = "watch movie";
		echo $ed->similarity();
		$model = new Place_model;

		//03-28 14:18:38.370: I/System.out(2233): lat : 1.609325408935547E-5 | lon : -326.3999891281128

		//$model->placeLat = -7.27957;
		//$model->placeLng = 112.79751;
		$model->placeLat = "1.6093254089355";
		$model->placeLng = "-326.39998912";
		echo $this->table->generate($model->listInRange(2000));
	}

	function tes(){
		/*/
		$data = array(
			'A' => 0.80760762181818,
			'B' => 0.7974537037037,
			'C' => 0.59030635573845,
			'D' => 0.80722318081752,
			'E' => 1,
			'F' => 0.28571428571429,
			'G' => 0.6,
			'H' => 1,
		);
		//*/
		$data = array(
			'userFoods' => 0.80760762181818,
			'userDrinks' => 0.7974537037037,
			'userBooks' => 0.59030635573845,
			'userMovies' => 0.80722318081752,
			'userMusics' => 1,
			'userOccupation' => 0.28571428571429,
			'userDOB' => 0.6,
			'userGender' => 1,
		);
		//*/
		//foreach($data as $key => $d) $data[$key] = (2*$d*0.99)/(1+($d*0.99));
		//array_walk($data,'tuningFormula');
		$ds = new Dempster_shafer;
		echo $ds->process($data, TRUE);
	}
}

/* End of file welcome.php */
/* Location: ./application/controllers/welcome.php */