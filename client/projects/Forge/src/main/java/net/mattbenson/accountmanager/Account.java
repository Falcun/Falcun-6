package net.mattbenson.accountmanager;

public abstract class Account implements IAccount {
}

interface IAccount {
	void removeAccount();
	void saveAccount();
	
	String getUsername();
	
	String loadUser();
}
