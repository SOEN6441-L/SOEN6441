package mapelements;

public class Continent {
	public int continentID;
	public String continentName;
	public int controlNum;

	public Continent(int ID, String name) {
		this.continentID = ID;
		this.continentName = name;
	}

	public void changeName(String name) {
		this.continentName = name;
	}
	public void changeControlNum(int control){
		this.controlNum = control;
	}

}
