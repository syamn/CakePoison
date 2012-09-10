/**
 * CakePoison - Package: syam.CakePoison.Cake
 * Created: 2012/09/10 20:44:26
 */
package syam.CakePoison.Cake;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Location;

import syam.CakePoison.CakePoison;

/**
 * CakeManager (CakeManager.java)
 * @author syam
 */
public class CakeManager {
	// Logger
    private static final Logger log = CakePoison.log;
    private static final String logPrefix = CakePoison.logPrefix;
    private static final String msgPrefix = CakePoison.msgPrefix;

    public static CakePoison plugin;
    public CakeManager(final CakePoison plugin){
    	this.plugin = plugin;
    }


    private static Map<Location, Integer> cakeMap = new HashMap<Location, Integer>();

    /**
     * 毒ケーキかどうか判定
     * @param loc 判定するブロック座標
     * @return 毒ケーキならtrue
     */
    public static boolean isPoisonCake(Location loc){
    	return cakeMap.containsKey(loc);
    }

    /**
     * 毒ケーキレベルを取得
     * @param loc チェックする対象座標
     * @return 毒ケーキなら非null, null なら毒ケーキでない
     */
    public static Integer getPoisonCake(Location loc){
    	return cakeMap.get(loc);
    }

    /**
     * 毒ケーキを設定
     * @param loc 座標
     * @param level 毒レベル
     */
    public static void setPoisonCake(Location loc, int level){
    	if (loc == null) return;
    	cakeMap.put(loc, level);
    }

    /**
     * 毒ケーキを削除
     * @param loc 座標
     */
    public static void removePoisonCake(Location loc){
    	cakeMap.remove(loc);
    }

    /* getter / setter */

    public static Map<Location, Integer> getPoisonCakeMap(){
    	return cakeMap;
    }

    public static void setPoisonCakeMap(Map<Location, Integer> newMap){
    	cakeMap.clear();
    	cakeMap.putAll(newMap);
    }
}
