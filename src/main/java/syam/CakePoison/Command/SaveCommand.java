/**
 * CakePoison - Package: syam.CakePoison.Command
 * Created: 2012/09/12 20:25:02
 */
package syam.CakePoison.Command;

import syam.CakePoison.Cake.CakeFileManager;
import syam.CakePoison.Enum.Permission;
import syam.CakePoison.Util.Actions;

/**
 * SaveCommand (SaveCommand.java)
 * @author syam
 */
public class SaveCommand extends BaseCommand{
	public SaveCommand(){
		bePlayer = false;
		name = "save";
		argLength = 0;
		usage = "<- save poisonous cake list";
	}

	@Override
	public void execute() {
		if (CakeFileManager.saveData()){
			Actions.message(sender, null, "&a毒ケーキリストの保存に成功しました！");
		}else{
			Actions.message(sender, null, "&c毒ケーキリスト保存中にエラーが発生しました！");
		}
	}

	@Override
	public boolean permission() {
		return Permission.SAVE.hasPerm(sender);
	}
}
