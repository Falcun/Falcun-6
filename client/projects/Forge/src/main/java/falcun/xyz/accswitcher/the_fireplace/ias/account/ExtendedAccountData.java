package falcun.xyz.accswitcher.the_fireplace.ias.account;

import falcun.xyz.accswitcher.com.github.mrebhan.ingameaccountswitcher.tools.alt.AccountData;
/**
 * @author The_Fireplace
 * @deprecated Inconvenient. Insure (saved user passwords). Used only for conversion from old accounts to new accounts.
 */
@Deprecated
public class ExtendedAccountData extends AccountData {
	private static final long serialVersionUID = -909128662161235160L;
	public Boolean premium;
	public int[] lastused;
	public int useCount;
}
