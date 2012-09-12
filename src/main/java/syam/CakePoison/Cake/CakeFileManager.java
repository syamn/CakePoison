/**
 * CakePoison - Package: syam.CakePoison.Cake
 * Created: 2012/09/10 21:17:29
 */
package syam.CakePoison.Cake;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.print.PrintService;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import syam.CakePoison.CakePoison;
import syam.CakePoison.Util.Actions;
import syam.CakePoison.Util.TextFileHandler;
import syam.CakePoison.Util.Util;

/**
 * CakeFileManager (CakeFileManager.java)
 * @author syam
 */
public class CakeFileManager {
	// Logger
    private static final Logger log = CakePoison.log;
    private static final String logPrefix = CakePoison.logPrefix;
    private static final String msgPrefix = CakePoison.msgPrefix;

    private static final String cakeDataPath = "plugins/CakePoison/list.ncsv";

    /**
     * 毒ケーキリストをファイルに読み込む
     * @return 成功ならtrue
     */
    public static boolean loadData(){
    	TextFileHandler dataFile = new TextFileHandler(cakeDataPath);
    	Map<Location, Integer> newMap = new HashMap<Location, Integer>();

    	try{
    		List<String> list = dataFile.readLines();
    		String[] data;
    		String[] coord;

    		int line = 0;
    		while (!list.isEmpty()){
    			line++;
    			String thisLine = list.remove(0);

    			// デリミタで分ける
    			data = thisLine.split("#");
    			coord = data[0].split(",");

    			// 形式チェック
    			if (data.length != 2 || coord.length != 4){
    				log.warning(logPrefix+ "Skipping line "+line+": incorrect format.");
    				continue;
    			}

    			// ワールドチェック
    			World world = Bukkit.getWorld(coord[0]);
    			if (world == null){
    				log.warning(logPrefix+ "Skipping line "+line+": no world defined. (World: "+coord[0]+")");
    				continue;
    			}

    			// レベルチェック
    			if (!Util.isInteger(data[1])){
    				log.warning(logPrefix+ "Skipping line "+line+": could not convert to numeric. ("+data[1]+")");
    				continue;
    			}

    			Location loc = new Location(world, new Double(coord[1]), new Double(coord[2]), new Double(coord[3]));

    			// ブロックチェック
    			if (loc.getBlock() == null || loc.getBlock().getType() != Material.CAKE_BLOCK){
    				log.warning(logPrefix+ "Skipping line "+line+": block is not CAKE_BLOCK. ("+Actions.getBlockLocationString(loc)+")");
    				continue;
    			}

    			// マップに追加
    			newMap.put(loc, Integer.parseInt(data[1]));
    		}

    		// set newMap
    		CakeManager.setPoisonCakeMap(newMap);
    	}
    	catch (FileNotFoundException ex){
    		log.warning(logPrefix+ "Could not find poisonous cakes list file!");
    		return false;
    	}
    	catch (IOException ex){
    		log.warning(logPrefix+ "Could not read poisonous cakes list file!");
    		ex.printStackTrace();
    		return false;
    	}
    	return true;
    }

    /**
     * 毒ケーキリストをファイルに保存する
     * @return 成功ならtrue
     */
    public static boolean saveData(){
    	TextFileHandler dataFile = new TextFileHandler(cakeDataPath);

    	// raw write data
    	List<String> lines = new ArrayList<String>();

    	for (Entry<Location, Integer> cake : CakeManager.getPoisonCakeMap().entrySet()){
    		Location loc = cake.getKey();
    		int level = cake.getValue();

    		// ブロックチェック
			if (loc.getBlock() == null || loc.getBlock().getType() != Material.CAKE_BLOCK){
				log.warning(logPrefix+ "Skipping save block (" + Actions.getBlockLocationString(loc) + "): Is not CAKE_BLOCK.");
				continue;
			}

    		lines.add(loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ() + "#" + level); // add raw line
    	}

    	try{
    		dataFile.writeLines(lines);
    	}catch(IOException ex){
    		log.severe(logPrefix+ "Could not save poisonous cakes data!");
    		ex.printStackTrace();
    		return false;
    	}

    	return true;
    }
}
