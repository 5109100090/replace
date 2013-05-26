from django.http import HttpResponse
import random
from random import randrange, choice
from place.models import Place
from similarity.ed import EditDistance
from type.models import Type
from authenticate.models import User
from review.models import Review
import json

def index(request):
    return HttpResponse("Hello")

def user(request):
    foods = [
                [ ["Ayam Bakar","Ikan Bakar"],["Ayam Goreng","Ikan Goreng"],["Ayam Presto"],["Ayam Rica Rica","Ayam Rica-Rica"],["Ayam Cabe Hijau","Ayam Cabai Hijau"] ],
                [ ["Soto Ayam","Sate Ayam"],["Soto Daging"],["Soto Madura"],["Soto Kudus"],["Soto Lamongan"],["Soto Kambing","Sate Kambing"] ],
                [ ["Nasi Goreng"],["Nasi Bakar"],["Nasi Uduk","Nasi Udhuk"],["Nasi Kuning"],["Nasi Pecel"],["Nasi Padang","Nasi Padhang"],["Nasi Tumpeng"] ],
                [ ["Mie Aceh","Mi Aceh"],["Mie Ayam","Mi Ayam"],["Mie Goreng","Mi Goreng"],["Mie Kuah","Mi Kuah"] ],
                [ ["Tahu Telor"],["Tahu Tek"],["Tahu Campur"],["Tahu Gimbal"],["Tahu Gejrot"],["Tahu Pong"],["Tahu Sumedang"] ]
            ]
    drinks = [
              [ ["Teh Manis"],["Teh Tawar"],["Teh Hijau"],["Teh Botol"],["Teh Susu"] ],
              [ ["Es Teh"],["Es Teh Manis"],["Es Teh Tawar"],["Es Jeruk"],["Es Sirup","Es Syrup"],["Es Campur"],["Es Leci","Es Lecy"],["Es Susu"] ],
              [ ["Teh Hangat","Teh Anget"],["Jeruk Hangat"],["Kopi Hangat"],["Susu Hangat"] ],
              [ ["Teh Panas"],["Jeruk Panas"],["Kopi Panas"],["Susu Panas"] ],
              [ ["Kopi"],["Kopi Susu"],["Kopi Luwak"],["Kopi Hitam"],["Kopi Putih"] ]
              ]
    books = [
             [ ["Harry Potter and the Philosopher's Stone","Harry Poter dan the Philosopher's Stone"],["Harry Potter and the Chamber of Secrets","Hary Potter dan the Chamber of Secrets"],["Harry Potter and the Prisoner of Azkaban","Hary Potter dan the Prisoner of Azkaban"],["Harry Potter and the Goblet of Fire","Hary Potter dan the Goblet of Fire"],["Harry Potter and the Order of the Phoenix","Hary Potter dan the Order of the Phoenix"],["Harry Potter and the Half-Blood Prince","Heri Potter dan the Half-Blood Prince"],["Harry Potter and the Deathly Hallows","Hary Potter dan the Deathly Hallows"],["Harry Potter","Heri Poter"] ],
             [ ["The Secrets of the Immortal Nicholas Flamel"],["The Secrets of the Immortal Nicholas Flamel : The Alchemyst"],["The Secrets of the Immortal Nicholas Flamel : The Magician"],["The Secrets of the Immortal Nicholas Flamel : The Sorceress"],["The Secrets of the Immortal Nicholas Flamel : The Necromancer"],["The Secrets of the Immortal Nicholas Flamel : The Warlock"],["The Secrets of the Immortal Nicholas Flamel : The Enchantress"] ],
             [ ["The Chronicles of Narnia"],["The Chronicles of Narnia : The Lion, the Witch and the Wardrobe","The Chronicles of Narnia - The Lion, the Witch and the Wardrobe","The Lion, the Witch and the Wardrobe"],["The Chronicles of Narnia : Prince Caspian","The Chronicles of Narnia - Prince Caspian","Prince Caspian"],["The Chronicles of Narnia : The Voyage of the Dawn Treader","The Chronicles of Narnia - The Voyage of the Dawn Treader","The Voyage of the Dawn Treader"],["The Chronicles of Narnia : The Silver Chair","The Chronicles of Narnia - The Silver Chair","The Silver Chair"],["The Chronicles of Narnia : The Horse and His Boy","The Chronicles of Narnia - The Horse and His Boy","The Horse and His Boy"],["The Chronicles of Narnia : The Magician's Nephew","The Chronicles of Narnia - The Magician's Nephew","The Magician's Nephew"],["The Chronicles of Narnia : The Last Battle","The Chronicles of Narnia - The Last Battle","The Last Battle"] ]
             ]
    movies = [
              [ ["Twilight","The Twilight","The Twilight Saga"],["The Twilight Saga : New Moon","The Twilight Saga - New Moon"],["The Twilight Saga : Eclipse","The Twilight Saga - Eclipse"],["The Twilight Saga : Breaking Dawn","The Twilight Saga - Breaking Dawn"] ],
              [ ["Star Trek : The Motion Picture","Star Trek - The Motion Picture"],["Star Trek II : The Wrath of Khan","Star Trek II - The Wrath of Khan"],["Star Trek III : The Search for Spock","Star Trek III - The Search for Spock"],["Star Trek IV : The Voyage Home","Star Trek IV - The Voyage Home"],["Star Trek V : The Final Frontier","Star Trek V - The Final Frontier"],["Star Trek VI : The Undiscovered Country","Star Trek VI - The Undiscovered Country"],["Star Trek Generations"],["Star Trek : First Contact","Star Trek - First Contact"],["Star Trek : Insurrection","Star Trek - Insurrection"],["Star Trek : Nemesis","Star Trek - Nemesis"],["Star Trek"],["Star Trek Into Darkness"] ],
              [ ["Star Wars Episode IV : A New Hope","Star Wars Episode IV - A New Hope"],["Star Wars Episode V : The Empire Strikes Back","Star Wars Episode V - The Empire Strikes Back"],["Star Wars Episode VI : Return of the Jedi","Star Wars Episode VI - Return of the Jedi"],["Star Wars Episode I : The Phantom Menace","Star Wars Episode I - The Phantom Menace"],["Star Wars Episode II : Attack of the Clones","Star Wars Episode II - Attack of the Clones"],["Star Wars Episode III : Revenge of the Sith","Star Wars Episode III - Revenge of the Sith"],["Star Wars : The Clone Wars","Star Wars - The Clone Wars"] ]
              ]
    occupations = {"L" : ["Maha siswa","Mahasiswa","Siswa"], "P" : ["Maha siswi","Mahasiswi","Siswi"] }
    
    response = ""
    
    for user in User.objects.all():
        user.userFoods = ",".join(userProcess(foods))
        user.userDrinks = ",".join(userProcess(drinks))
        user.userBooks = ",".join(userProcess(books))
        user.userMovies = ",".join(userProcess(movies))
        user.userOccupation = choice(occupations[user.userGender])
        #user.save()
        response += user.userAlias +"<br />- "+user.userFoods+"<br/>- "+user.userDrinks+"<br/>- "+user.userBooks+"<br/>- "+user.userMovies+"<br/>- "+user.userOccupation+"<p></p>"

    return HttpResponse(response)

def userProcess(mainList):
    nMainList = len(mainList)
    nRandomMainList = randrange(1, nMainList)
    lType = random.sample(xrange(nMainList), nRandomMainList)
    
    result = []
    for l in lType:
        subList = mainList[l]
        nSubList = len(subList)
        nRandomSubList = randrange(1, nSubList)
        randomizedList = random.sample(subList, nRandomSubList)
        selectedList = []
        for rl in randomizedList:
            selectedList.append(choice(rl))
        result.extend(selectedList)
    
    return result
        
    