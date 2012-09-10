/**
 * CakePoison - Package: syam.CakePoison
 * Created: 2012/09/10 19:08:42
 */
package syam.CakePoison;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import syam.CakePoison.Cake.CakeFileManager;
import syam.CakePoison.Cake.CakeManager;
import syam.CakePoison.Command.BaseCommand;
import syam.CakePoison.Command.HelpCommand;
import syam.CakePoison.Listener.CPPlayerListener;
import syam.CakePoison.Util.Metrics;

/**
 * CakePoison (CakePoison.java)
 * @author syam
 */
public class CakePoison extends JavaPlugin{
	// ** Logger **
	public final static Logger log = Logger.getLogger("Minecraft");
	public final static String logPrefix = "[CakePoison] ";
	public final static String msgPrefix = "&6[CakePoison] &f";

	// ** Listener **
	CPPlayerListener playerListener = new CPPlayerListener(this);

	// ** Commands **
	public static List<BaseCommand> commands = new ArrayList<BaseCommand>();

	// ** Private Classes **
	private ConfigurationManager config;
	private CakeManager cm;

	// ** Instance **
	private static CakePoison instance;

	// ** Hookup Plugins **
	/**
	 * プラグイン起動処理
	 */
	@Override
	public void onEnable(){
		instance  = this;
		PluginManager pm = getServer().getPluginManager();
		config = new ConfigurationManager(this);

		// loadconfig
		try{
			config.loadConfig(true);
		}catch (Exception ex){
			log.warning(logPrefix+"an error occured while trying to load the config file.");
			ex.printStackTrace();
		}

		// プラグインを無効にした場合進まないようにする
		if (!pm.isPluginEnabled(this)){
			return;
		}

		// Regist Listeners
		pm.registerEvents(playerListener, this);

		// コマンド登録
		registerCommands();

		// マネージャ
		cm = new CakeManager(this);

		// メッセージ表示
		PluginDescriptionFile pdfFile=this.getDescription();
		log.info("["+pdfFile.getName()+"] version "+pdfFile.getVersion()+" is enabled!");

		setupMetrics(); // mcstats
	}

	/**
	 * プラグイン停止処理
	 */
	@Override
	public void onDisable(){
		// 毒ケーキリスト書き込み
		CakeFileManager.saveData();

		// メッセージ表示
		PluginDescriptionFile pdfFile=this.getDescription();
		log.info("["+pdfFile.getName()+"] version "+pdfFile.getVersion()+" is disabled!");
	}

	/**
	 * コマンドを登録
	 */
	private void registerCommands(){
		// Intro Commands
		commands.add(new HelpCommand());

		// General Commands

		// Admin Commands

	}

	/**
     * Metricsセットアップ
     */
    private void setupMetrics(){
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException ex) {
            log.warning(logPrefix+"cant send metrics data!");
            ex.printStackTrace();
        }
    }

	/**
	 * コマンドが呼ばれた
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String args[]){
		if (cmd.getName().equalsIgnoreCase("cakepoison")){
			if(args.length == 0){
				// 引数ゼロはヘルプ表示
				args = new String[]{"help"};
			}

			outer:
			for (BaseCommand command : commands.toArray(new BaseCommand[0])){
				String[] cmds = command.name.split(" ");
				for (int i = 0; i < cmds.length; i++){
					if (i >= args.length || !cmds[i].equalsIgnoreCase(args[i])){
						continue outer;
					}
					// 実行
					return command.run(this, sender, args, commandLabel);
				}
			}
			// 有効コマンドなし ヘルプ表示
			new HelpCommand().run(this, sender, args, commandLabel);
			return true;
		}
		return false;
	}

	/* getter */
	/**
	 * 毒ケーキマネージャを返す
	 * @return CakeManager
	 */
	public CakeManager getManager() {
		return cm;
	}

	/**
	 * 設定マネージャを返す
	 * @return ConfigurationManager
	 */
	public ConfigurationManager getConfigs() {
		return config;
	}

	/**
	 * インスタンスを返す
	 * @return BookEditorインスタンス
	 */
	public static CakePoison getInstance(){
		return instance;
	}
}
