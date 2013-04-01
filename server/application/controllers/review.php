<?php
class Review extends CI_Controller
{
	function __construct()
	{
		parent::__construct();
		$this->load->model(array('review_model'));
	}
	
	public function listReviews()
	{
		if($this->input->post('placeId'))
		{
			$model = new Review_model;
			$model->reviewPlace = $this->input->post('placeId');

			echo json_encode($model->listReviews());
		}
	}
}
?>