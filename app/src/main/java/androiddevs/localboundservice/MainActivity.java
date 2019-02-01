package androiddevs.localboundservice;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androiddevs.localboundservice.BoundServices;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Intent _intent;
    boolean mServiceStatus;
    BoundServices mBoundService;
    private EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.btnBindService).setOnClickListener(this);
        findViewById(R.id.btnUnBoundServices).setOnClickListener(this);
        findViewById(R.id.btnCalculateFact).setOnClickListener(this);
        input = (EditText)findViewById(R.id.etInput);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btnBindService:
                if (!mServiceStatus) {
                    _intent= new Intent(MainActivity.this,BoundServices.class);
                    bindService(_intent, sc, BIND_AUTO_CREATE);
                    mServiceStatus = true;
                    Toast.makeText(getApplicationContext(), "Service Bound Successfully", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(), "Service Already Bound", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnUnBoundServices:

                if (mServiceStatus) {
                    _intent= new Intent(MainActivity.this,BoundServices.class);
                    unbindService(sc);
                    mServiceStatus = false;
                    Toast.makeText(getApplicationContext(), "Service UnBound Successfully", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(), "Service Not Bound, Please Bound Service First", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btnCalculateFact:
                if (mServiceStatus) {
                    if (input.getText().toString().equalsIgnoreCase("")){
                        Toast.makeText(getApplicationContext(),"Enter Number", Toast.LENGTH_SHORT).show();
                    }else {
                        int num = Integer.parseInt(input.getText().toString());
                        int result = mBoundService.factorial(num);
                        Toast.makeText(getApplicationContext(), "result: " + result, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Service Not Bound, Please Bound Service First", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
        }
    }

    ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
            Toast.makeText(getApplicationContext(), "Service Disconnected", Toast.LENGTH_SHORT).show();
            mServiceStatus = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // TODO Auto-generated method stub
            BoundServices.LocalBind binder = (BoundServices.LocalBind) service;
            mBoundService = binder.getService();
            mServiceStatus = true;
            Toast.makeText(getApplicationContext(), "Service Connected", Toast.LENGTH_SHORT).show();
        }
    };
}

