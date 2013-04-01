<?php
class Place extends CI_Controller {

	public function __construct()
	{
		parent::__construct();
		$this->load->library(array('edit_distance','table'));
		$this->load->model(array('place_model'));
	}

	public function process(){
		if($this->input->post('userId') && $this->input->post('typeId') && $this->input->post('currentLat') && $this->input->post('currentLng') && $this->input->post('range')){
			$model = new Place_model;
			$model->placeLat =  $this->input->post('currentLat');
			$model->placeLng =  $this->input->post('currentLng');
			echo json_encode($model->listInRange($this->input->post('typeId'), $this->input->post('range')));
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
}

/* End of file welcome.php */
/* Location: ./application/controllers/welcome.php */