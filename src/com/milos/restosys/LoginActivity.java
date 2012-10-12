package com.milos.restosys;

import android.os.Bundle;
import android.app.Activity;
import android.app.backup.BackupManager;
import android.view.Menu;
import android.widget.Toast;

public class LoginActivity extends Activity {
	
	BackupManager manager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        manager = new BackupManager(this);
        manager.dataChanged();
        
        Toast.makeText(this, "Backed up!", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_login, menu);
        return true;
    }
}
