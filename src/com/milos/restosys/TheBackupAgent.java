package com.milos.restosys;

import android.app.backup.BackupAgentHelper;
import android.app.backup.FileBackupHelper;
import android.app.backup.SharedPreferencesBackupHelper;

/**
 * This class is backup agent
 * 
 * @author Milos
 *
 */
public class TheBackupAgent extends BackupAgentHelper {

	@Override
	public void onCreate() {
		super.onCreate();
		
		FileBackupHelper helper = new FileBackupHelper(this, "sharedPreferences");
        addHelper("backupKey", helper);
        
        SharedPreferencesBackupHelper prefHelper =
                new SharedPreferencesBackupHelper(this, "displayPref", "sharedPreferences");
        addHelper("prefBackupKey", prefHelper);
	}

}
