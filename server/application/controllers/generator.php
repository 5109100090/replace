<?php
class Generator extends CI_Controller{
	function __construct(){
		parent::__construct();
		$this->load->model(array('user_model','place_model','review_model'));
	}

	private $foods = array('Nasi Goreng','Soto','Sate','Bakso','Mie Ayam','Rendang','Tahu Telor','Ayam Goreng','Bebek Goreng','Seafood','Ayam Bakar','Nasi Pecel','Steak','Nasi Padang','Bubur Ayam','Mie Aceh');
	private $drinks = array('Es Teh','Teh Hangat','Teh Tawar','Teh Hangat Tawar','Teh Hijau','Air Putih','Es Jeruk','Jeruk Hangat','Es Campur','Es Sirup','Jus Jeruk','Jus Apel','Jus Melon','Jus Semangka','Jus Alpukat','Es Leci');
	private $books = array('Komik','Novel','Biografi','Bisnis','Manajemen','Desain','Fiksi','Ensiklopedi','Fashion','Fiksi','Filsafat','Fotografi','Kesehatan','Kisah Nyata','Komputer','Gaya Hidup','Majalah','Jurnalisme','Musik','Non Fiksi','Non Profit','Keluarga','Perkebunan','Peternakan');
	private $movies = array('Action','Adventure','Animation','Biography','Comedy','Crime','Documentary','Drama','FamilyFantasy','Film-Noir','Game-Show','History','Horror','Music','Musical','Mystery','News','Reality-TV','Romance','Sci-Fi','Sport','Talk-Show','Thriller','War','Western');
	private $musics = array('acoustic','ambient','blues','classical','country','electronic','emo','folk','hardcore','hip hop','indie','jazz','latin','metal','pop','pop punk','punk','reggae','rnb','rock','soul','world','60s','70s','80s','90s');
	private $occupation = array('Pelajar','Pengajar','Dokter','Pegawai Negri Sipil','Pegawai Kantor','Wiraswasta','Pemain Film','Penyanyi','Pelukis');

	public function user(){
		$data = array();
		$obj = array($this->foods, $this->drinks, $this->movies, $this->musics);
		$model = new User_model;
		echo '<pre>';
		foreach($model->listAll() as $user){
			$user->userFoods = $this->userGenerator($this->foods);
			$user->userDrinks = $this->userGenerator($this->drinks);
			$user->userBooks = $this->userGenerator($this->books);
			$user->userMovies = $this->userGenerator($this->movies);
			$user->userMusics = $this->userGenerator($this->musics);
			$user->userOccupation = $this->occupation[rand(0,count($this->occupation)-1)];
			$user->userDOB = rand(1985, 1995).'-'.rand(1,12).'-'.rand(1,29);

			$data[] = $user;
			print_r($user);echo '<br>';
		}
		$model->update($data);
		//echo '<pre>'.print_r($data);
	}

	private function userGenerator($array){
		$result = '';
		$temp = array();
		$n = count($array)-1;
		for($i=0; $i<rand(3,7); $i++){
			$randomIndex = rand(0,$n);
			for($j=0; $j<count($temp)-1; $j++){
				if($randomIndex == $temp[$j]){
					$randomIndex = rand(0,$n);
					$j=0;
				}
			}
			$temp[] = $randomIndex;
		}

		$i = 0;
		foreach($temp as $t){
			$result .= $array[$t].($i == count($temp)-1 ? '' : ',');
			$i++;
		}
		return $result;
	}

	public function review(){
		$places = new Place_model;
		$users = new User_model;
		$reviews = new Review_model;

		$usersList = $users->listAll();
		$nUsers = count($usersList)-1;
		foreach($places->listAll() as $place){

			//jumlah review untuk satu tempat
			$nReview = rand(5, 10);
			$usersReview = array();
			for($i=0; $i<$nReview; $i++){
				$randPoint = rand(1,5);
				$randUSer = rand(0, $nUsers);
				while(true){
					if(in_array($randUSer, $usersReview)){
						$randUSer = rand(0, $nUsers);
					}else{
						$usersReview[] = $randUSer;
						break;
					}
				}
				$reviews->reviewUser = $usersList[$randUSer]->userId;
				$reviews->reviewPlace = $place->placeId;
				$reviews->reviewPoint = $randPoint;
				$reviews->insert();
				//echo 'reviewUser : '.$usersList[$randUSer]->userAlias.' reviewPlace : '.$place->placeId.' reviewPoint : '.$randPoint.'<br/>';
			}
			//echo '<br/>&nbsp;<br/>';
		}
	}
}
?>