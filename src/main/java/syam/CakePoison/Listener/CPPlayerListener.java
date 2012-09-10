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
    @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerInteract(final PlayerInteractEvent event){
    	Player player = event.getPlayer();

    	// allow only syamn to use this section for developing
    	// TODO: remove this developer check
    	if (!player.equals(Bukkit.getPlayer("syamn")))
    		return; //debug

    	if (event.getAction() == Action.RIGHT_CLICK_BLOCK){
    		Block block = event.getClickedBlock();
    		if (block.getType() == Material.CAKE_BLOCK){
    			/* ケーキを右クリック */

    			// 通常のケーキなら何もしない
    			if (!CakeManager.isPoisonCake(block.getLocation())){
    				return;
    			}

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
						if (CakeManager.isPoisonCake(block.getLocation())){
							CakeActions.eatPoisonousCake(player, block);
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
