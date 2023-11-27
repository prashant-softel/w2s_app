package CommonUtilityFunctions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

public class MapUtility
{
	
	
	public static String convert(HashMap<Integer,Map<String,Object>> rows) {
        Gson gson = new Gson();
        String json = gson.toJson(rows);
        return json;
    }
	
	public static HashMap<String,String>  stringToHashMap(String mapString)
	{
		//converting string to hash map using delimeter ","
		
		HashMap<String,String> map = new HashMap<>();  
		if(mapString != null)
		{	
			String value = mapString.substring(1, mapString.length()-1); //remove curly brackets
			String[] keyValuePairs = value.split(",");              //split the string to create key-value pairs
			             
			
			if(keyValuePairs.length > 1)
			{	
				for(String pair : keyValuePairs)                        //iterate over the pairs
				{
			    String[] entry = pair.split("=");                   //split the pairs to get key and value 
			    map.put(entry[0].trim(), entry[1].trim());          //add them to the hashmap and trim whitespaces
				}
			}
		}	
		return map;

	}
	
	public static List<Object> HashMaptoList (HashMap<Integer,Map<String,Object>> objHashMap)
		{
		
		List<Object> objMapList = new ArrayList<Object>(objHashMap.values());
		return objMapList;
		}
}
