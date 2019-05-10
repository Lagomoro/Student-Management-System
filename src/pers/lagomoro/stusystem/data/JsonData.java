package pers.lagomoro.stusystem.data;

public abstract class JsonData{
	
	@Override
	public String toString() {
		return JsonDataController.toJson(this);
	}
	
}
