/**
 * CakePoison - Package: syam.CakePoison.Cake
 * Created: 2012/09/10 22:01:33
 */
package syam.CakePoison.Cake;

import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import syam.CakePoison.CakePoison;
import syam.CakePoison.Util.Actions;

/**
 * CakeActions (CakeActions.java)
 * @author syam
 */
public class CakeActions {
	// Logger
    private static final Logger log = CakePoison.log;
    private static final String logPrefix = CakePoison.logPrefix;
    private static final String msgPrefix = CakePoison.msgPrefix;

    /**
     * 牛乳バケツでケーキを右クリックした
     * @param player クリックしたプレイヤー
     * @param block クリックしたケーキブロック
     */
    public static void clickWithMilkBucket(Player player, Block block){
    	Actions.debug("clicked with milk bucket!"); //debug

    	// クリックしたケーキが毒ケーキなら解毒する
    	if(CakeManager.isPoisonCake(block.getLocation())){
    		CakeManager.removePoisonCake(block.getLocation());
    		Actions.message(null, player, "&aこの毒ケーキを解毒しました！");
    	}

    	// バケツを空にする
    	player.setItemInHand(new ItemStack(Material.BUCKET, 1));
    }

    /**
     * 毒ポーションでケーキを右クリックした
     * @param player クリックしたプレイヤー
     * @param block クリックしたケーキブロック
     * @param level クリックした毒ポーションのレベル
     */
    public static void clickWithPoison(Player player, Block block, int level){
    	Actions.debug("clicked with poison potion!"); //debug

    	Integer oldLevel = CakeManager.getPoisonCake(block.getLocation());
    	int newLevel = (oldLevel == null) ? level : level + oldLevel;
    	CakeManager.setPoisonCake(block.getLocation(), newLevel);

    	Actions.message(null, player, "&aこれはレベル"+newLevel+"の&c毒ケーキ&aになりました！");
    	player.setItemInHand(new ItemStack(Material.GLASS_BOTTLE, 1));
    }

    /**
     * 毒ケーキを右クリック
     * @param player クリックしたプレイヤー
     * @param block クリックしたケーキブロック
     */
    public static void eatPoisonousCake(Player player, Block block){
    	Actions.debug("eat poisonous cake!"); //debug
    }
}
