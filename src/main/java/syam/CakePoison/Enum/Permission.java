/**
 * CakePoison - Package: syam.CakePoison.Enum
 * Created: 2012/09/10 19:47:21
 */
package syam.CakePoison.Enum;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import syam.CakePoison.CakePoison;

/**
 * Permission (Permission.java)
 * @author syam
 */
public enum Permission {
	/* 権限ノード */

	// Poisoning, Detoxifying
	POISONING ("user.poisoning"),
	DETOXIFYING ("user.detoxifying"),

	// Ignore poison
	IGNORE ("admin.ignore"),

	// Admin Commands
	RELOAD ("admin.reload"),
	SAVE ("admin.save"),
	;

	// ノードヘッダー
	final String header = "cakepoison.";

	private String node;

	/**
	 * コンストラクタ
	 * @param node ノード
	 */
	Permission(final String node){
		this.node = header + node;
	}

	/**
	 * 指定した名前のプレイヤーが権限を持っているか
	 * @param playerName name
	 * @return boolean
	 */
	public boolean hasPerm(String playerName){
		Player target = CakePoison.getInstance().getServer().getPlayer(playerName);
		return target != null && hasPerm(target);
	}

	/**
	 * 指定したプレイヤーが権限を持っているか
	 * @param player Player
	 * @return boolean
	 */
	public boolean hasPerm(Player player){
		return player.hasPermission(this.node);
	}

	/**
	 * 指定したCommandSenderが権限を持っているか
	 * @param sender CommandSender
	 * @return boolean
	 */
	public boolean hasPerm(CommandSender sender){
		return sender.hasPermission(this.node);
	}

	/* getter */
	/**
	 * 権限ノードを取得する
	 * @return 権限ノード
	 */
	public String getNode(){
		return this.node;
	}
}