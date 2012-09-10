/**
 * CakePoison - Package: syam.CakePoison.Command
 * Created: 2012/09/10 19:46:21
 */
package syam.CakePoison.Command;

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
		try{
			plugin.getConfigs().loadConfig(false);
		}catch (Exception ex){
			log.warning(logPrefix+"an error occured while trying to load the config file.");
			ex.printStackTrace();
			return;
		}
		Actions.message(sender, null, "&aConfiguration reloaded!");
		return;
	}

	@Override
	public boolean permission() {
		return Permission.RELOAD.hasPerm(sender);
	}
}
