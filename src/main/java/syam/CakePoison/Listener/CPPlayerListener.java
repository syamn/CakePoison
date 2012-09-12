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

    //player eat ckes
    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerEatCake(final PlayerInteractEvent event){
    	Player player = event.getPlayer();

    	// allow only syamn to use this section for developing
    	// TODO: remove this developer check
    	if (!player.equals(Bukkit.getPlayer("syamn")))
    		return; //debug

    	if (event.getAction() == Action.RIGHT_CLICK_BLOCK ||
    			event.getAction() == Action.LEFT_CLICK_BLOCK){
    		Block block = event.getClickedBlock();

    		// ケーキブロック以外は返す
    		if (block.getType() != Material.CAKE_BLOCK){
    			return;
    		}

    		Integer level = CakeManager.getPoisonCake(block.getLocation());
			if (level != null){
				CakeActions.eatPoisonousCake(player, block, level);

				event.setCancelled(true);
    			event.setUseInteractedBlock(Result.DENY);
    			event.setUseItemInHand(Result.DENY);
			}
    	}
    }

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
    	if (event.getClickedBlock() == null)
    		return;

    	Block block = event.getClickedBlock();


    	if (event.getAction() == Action.RIGHT_CLICK_BLOCK){
    		ItemStack is = player.getItemInHand();
    		// 牛乳バケツ
    		if (is.getType() == Material.MILK_BUCKET){
    			CakeActions.clickWithMilkBucket(player, block);

    			event.setCancelled(true);
    			event.setUseInteractedBlock(Result.DENY);
    			event.setUseItemInHand(Result.DENY);
    		}
    		// 毒ポーション
    		if (is.getType() == Material.POTION){
    			Potion potion = Potion.fromItemStack(is);
    			if (potion.getType() == PotionType.POISON && !potion.isSplash()){
					CakeActions.clickWithPoison(player, block, potion.getLevel());

	    			event.setCancelled(true);
	    			event.setUseInteractedBlock(Result.DENY);
	    			event.setUseItemInHand(Result.DENY);
				}
    		}
    	}

    	if (true) return;//debug

    	if (event.getAction() == Action.RIGHT_CLICK_BLOCK){

    		if (block.getType() == Material.CAKE_BLOCK){
    			/* ケーキを右クリック */

    			boolean cancell = true;

    			// 手に持っているアイテムで分岐
    			switch(player.getItemInHand().getType()){
    				// 牛乳バケツ
    				case MILK_BUCKET:
    					CakeActions.clickWithMilkBucket(player, block);
    					break;

    				// ポーション
    				case POTION:
    					Potion potion = Potion.fromItemStack(player.getItemInHand());
    					if (potion.getType() == PotionType.POISON && !potion.isSplash()){
    						CakeActions.clickWithPoison(player, block, potion.getLevel());
    						break;
    					}
    					// 毒ポーション以外はデフォルト処理へ継続させる
    					// break;

    				// その他
					default:
						// 毒ケーキを食べた
						Integer level = CakeManager.getPoisonCake(block.getLocation());
						if (level != null){
							CakeActions.eatPoisonousCake(player, block, level);
						}
						// 通常のケーキを食べた
						else{
							cancell = false;
						}
    			}

    			if (cancell){
    				event.setCancelled(true);
    				event.setUseInteractedBlock(Result.DENY);
    				event.setUseItemInHand(Result.DENY);
    			}
    		}
    	}
    }
}
