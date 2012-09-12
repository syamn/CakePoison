/**
 * CakePoison - Package: syam.CakePoison.Listener
 * Created: 2012/09/10 19:49:34
 */
package syam.CakePoison.Listener;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import syam.CakePoison.CakePoison;
import syam.CakePoison.Cake.CakeActions;
import syam.CakePoison.Cake.CakeManager;
import syam.CakePoison.Util.Actions;

/**
 * CPPlayerListener (CPPlayerListener.java)
 * @author syam
 */
public class CPPlayerListener implements Listener{
	// Logger
    private static final Logger log = CakePoison.log;
    private static final String logPrefix = CakePoison.logPrefix;
    private static final String msgPrefix = CakePoison.msgPrefix;

    private final CakePoison plugin;

    public CPPlayerListener(final CakePoison plugin){
        this.plugin = plugin;
    }

    /* ********************************************* */

    // player clicked air/block
    @EventHandler(priority=EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerInteract(final PlayerInteractEvent event){
    	Player player = event.getPlayer();

    	// allow only syamn to use this section for developing
    	// TODO: remove this developer check
    	if (!player.equals(Bukkit.getPlayer("syamn")))
    		return; //debug

    	//TODO: 左クリックでも通常のケーキは食べられるらしい。 現状毒ケーキは右クリックにしか反応しない。
    	//TODO: フードレベルチェックが必要。 現状毒ケーキはフードレベルが最大でも食べることができてしまう。


    	Block block = event.getClickedBlock();
    	if (block == null || block.getType() != Material.CAKE_BLOCK){
    		return;
    	}

    	boolean action = false;

    	if (event.getAction() == Action.RIGHT_CLICK_BLOCK){
    		ItemStack is = player.getItemInHand();
    		// 牛乳バケツ
    		if (is.getType() == Material.MILK_BUCKET){
    			CakeActions.clickWithMilkBucket(player, block);
    			action = true;
    		}
    		// 毒ポーション
    		if (is.getType() == Material.POTION){
    			Potion potion = Potion.fromItemStack(is);
    			if (potion.getType() == PotionType.POISON && !potion.isSplash()){
					CakeActions.clickWithPoison(player, block, potion.getLevel());
					action = true;
				}
    		}
    	}

    	if (!action){
    		Integer level = CakeManager.getPoisonCake(block.getLocation());
			if (level != null){
				CakeActions.eatPoisonousCake(player, block, level);
				action = true;
			}
    	}

    	if (action){
    		event.setCancelled(true);
    		event.setUseInteractedBlock(Result.DENY);
    		event.setUseItemInHand(Result.DENY);
    	}
    }
}
