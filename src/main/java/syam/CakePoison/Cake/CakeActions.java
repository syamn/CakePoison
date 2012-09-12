/**
 * CakePoison - Package: syam.CakePoison.Cake
 * Created: 2012/09/10 22:01:33
 */
package syam.CakePoison.Cake;

import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Cake;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

import syam.CakePoison.CakePoison;
import syam.CakePoison.Enum.Permission;
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
    	// Check permission
    	if (Permission.DETOXIFYING.hasPerm(player)){
	    	// クリックしたケーキが毒ケーキなら解毒する
	    	if(CakeManager.isPoisonCake(block.getLocation())){
	    		CakeManager.removePoisonCake(block.getLocation());
	    		Actions.message(null, player, "&aこの毒ケーキを解毒しました！");

	    		// Logging
		    	if (CakePoison.getInstance().getConfigs().logToFile){
			    	Actions.log(CakePoison.getInstance().getConfigs().logFilePath,
			    			"Player " + player.getName() + " detoxifying poisonous cake! ("+Actions.getBlockLocationString(block.getLocation()) +")");
		    	}
    		}

	    	// バケツを空にする
	    	player.setItemInHand(new ItemStack(Material.BUCKET, 1));
    	}
    	// not send no perm message
    }

    /**
     * 毒ポーションでケーキを右クリックした
     * @param player クリックしたプレイヤー
     * @param block クリックしたケーキブロック
     * @param level クリックした毒ポーションのレベル
     */
    public static void clickWithPoison(Player player, Block block, int level){
    	// Check permission
    	if (Permission.POISONING.hasPerm(player)){
	    	Integer oldLevel = CakeManager.getPoisonCake(block.getLocation());
	    	int newLevel = (oldLevel == null) ? level : level + oldLevel;
	    	CakeManager.setPoisonCake(block.getLocation(), newLevel);

	    	Actions.message(null, player, "&aこれはレベル"+newLevel+"の&c毒ケーキ&aになりました！");
	    	player.setItemInHand(new ItemStack(Material.GLASS_BOTTLE, 1));

	    	// Logging
	    	if (CakePoison.getInstance().getConfigs().logToFile){
		    	Actions.log(CakePoison.getInstance().getConfigs().logFilePath,
		    			"Player " + player.getName() + " make " + newLevel + " level poisonous cake! ("+Actions.getBlockLocationString(block.getLocation()) +")");
	    	}
    	}
    	// send no perm message
    	else{
    		Actions.message(null, player, "&c毒ケーキにする権限がありません！");
    	}
    }

    /**
     * 毒ケーキを右クリックした
     * @param player クリックしたプレイヤー
     * @param block クリックしたケーキブロック
     */
    public static void eatPoisonousCake(Player player, Block block, int level){
    	// Check permission
    	if (!Permission.IGNORE.hasPerm(player)){
	    	Potion potion = new Potion(PotionType.POISON, level);
	    	potion.apply(player);

	    	Actions.message(null, player, "&cこれは毒ケーキでした！");
    	}
    	// has Ignore permission
    	else{
    		if (player.getHealth() < 18){
    			player.setHealth(player.getHealth() + 2);
    		}else{
    			player.setHealth(20);
    		}

    		Actions.message(null, player, "&aこれは毒ケーキですが、あなたは権限によって守られています！");
    	}

    	// Block update
    	updateEatCakeBlock(block);
    }

    /**
     * ケーキブロックのデータ値を変更し、食べられる回数を減らす
     * @param block 対象のケーキブロック
     */
    private static void updateEatCakeBlock(Block block){
    	Byte data = block.getData();
    	data = (byte) (data + 1);
    	if (data >= 0 && data <= 5){
    		block.setData(data);
    	}else{
    		block.setType(Material.AIR);
    		CakeManager.removePoisonCake(block.getLocation());
    	}
    }
}
