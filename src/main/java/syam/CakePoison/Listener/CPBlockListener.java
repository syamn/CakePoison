/**
 * CakePoison - Package: syam.CakePoison.Listener
 * Created: 2012/09/12 20:36:37
 */
package syam.CakePoison.Listener;

import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;

import syam.CakePoison.CakePoison;
import syam.CakePoison.Cake.CakeManager;
import syam.CakePoison.Util.Actions;

/**
 * CPBlockListener (CPBlockListener.java)
 * @author syam
 */
public class CPBlockListener implements Listener{
	// Logger
    private static final Logger log = CakePoison.log;
    private static final String logPrefix = CakePoison.logPrefix;
    private static final String msgPrefix = CakePoison.msgPrefix;

    private final CakePoison plugin;

    public CPBlockListener(final CakePoison plugin){
        this.plugin = plugin;
    }

    /* ********************************************* */

    @EventHandler(priority=EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockBreak(final BlockBreakEvent event){
    	// ケーキブロック以外は返す
    	if (event.getBlock().getType() != Material.CAKE_BLOCK)
    		return;

    	// 毒ケーキならリストから削除
    	Block block = event.getBlock();
    	Player player = event.getPlayer();

    	if (CakeManager.isPoisonCake(block.getLocation())){
    		CakeManager.removePoisonCake(block.getLocation());
    		Actions.message(null, player, "&a毒ケーキを破壊しました！");
    	}
    }
}
