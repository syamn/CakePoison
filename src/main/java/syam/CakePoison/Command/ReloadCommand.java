/**
 * CakePoison - Package: syam.CakePoison.Command
 * Created: 2012/09/10 19:46:21
 */
package syam.CakePoison.Command;

import syam.CakePoison.Cake.CakeFileManager;
import syam.CakePoison.Enum.Permission;
import syam.CakePoison.Util.Actions;

/**
 * ReloadCommand (ReloadCommand.java)
 * @author syam
 */
public class ReloadCommand extends BaseCommand{
	public ReloadCommand(){
		bePlayer = false;
		name = "reload";
		argLength = 0;
		usage = "<- reload config.yml";
	}

	@Override
	public void execute() {
		// config.yml
		try{
			plugin.getConfigs().loadConfig(false);
		}catch (Exception ex){
			if (player != null) Actions.message(null, player, "&c設定ファイルの再読み込み中にエラーが発生しました！");
			log.warning(logPrefix+"an error occured while trying to load the config file.");
			ex.printStackTrace();
			return;
		}
		Actions.message(sender, null, "&a設定を再読み込みしました！");

		// Poisonous cake list
		if (CakeFileManager.loadData()){
			Actions.message(sender, null, "&a毒ケーキリストの読み込みに成功しました！");
		}else{
			Actions.message(sender, null, "&c毒ケーキリスト読み込み中にエラーが発生しました！");
		}
	}

	@Override
	public boolean permission() {
		return Permission.RELOAD.hasPerm(sender);
	}
}
