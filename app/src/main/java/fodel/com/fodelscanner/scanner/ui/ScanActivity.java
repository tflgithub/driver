package fodel.com.fodelscanner.scanner.ui;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cn.xtouch.tfl.alert.AlertView;
import com.cn.xtouch.tfl.alert.OnDismissListener;
import com.cn.xtouch.tfl.alert.OnItemClickListener;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fodel.com.fodelscanner.BuildConfig;
import fodel.com.fodelscanner.Constants;
import fodel.com.fodelscanner.R;
import fodel.com.fodelscanner.scanner.api.entity.response.ResFinalize;
import fodel.com.fodelscanner.scanner.api.entity.response.ResScanBean;
import fodel.com.fodelscanner.scanner.event.ScannerEvent;
import fodel.com.fodelscanner.scanner.injector.component.DaggerScanComponent;
import fodel.com.fodelscanner.scanner.injector.module.ScanModule;
import fodel.com.fodelscanner.scanner.mvp.model.ScanContract;
import fodel.com.fodelscanner.scanner.mvp.presenter.ScanPresenter;
import fodel.com.fodelscanner.scanner.ui.fragment.ScannedFragment;
import fodel.com.fodelscanner.service.PrinterService;
import fodel.com.fodelscanner.utils.ToastUtils;
import fodel.com.fodelscanner.xzing.CaptureActivity;

public class ScanActivity extends BaseActivity<ScanPresenter> implements ScanContract.View, OnItemClickListener {

    @ViewInject(R.id.et_scan)
    EditText editText;
    private String type, name, id;
    @ViewInject(R.id.btn)
    Button button;
    @ViewInject(R.id.btn_scan)
    Button scanButton;
    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;
    private String editString = "";
    private AlertView quitAlertView;
    @ViewInject(R.id.tab_layout)
    TabLayout tabLayout;
    @ViewInject(R.id.pager)
    ViewPager viewPager;
    private MediaPlayer mediaPlayer1, mediaPlayer2, mediaPlayer3, mediaPlayer4;
    BluetoothAdapter bluetoothAdapter;

    @Override
    protected void initView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        type = getIntent().getStringExtra("type");
        name = getIntent().getStringExtra("name");
        id = getIntent().getStringExtra("id");
        button.setText(getString(R.string.verify));
        String title = null;
        switch (type) {
            case Constants.PICKUP_AT_WAREHOUSE:
                title = getString(R.string.pick_up_for) + " " + name;
                break;
            case Constants.DROP_RETURN_AT_WAREHOUSE:
                title = getString(R.string.check_in_scan_for) + " " + name;
                break;
            case Constants.PICKUP_RETURN_AT_SHOP:
                scanButton.setVisibility(View.VISIBLE);
                title = getString(R.string.return_pick_up_for) + " " + name;
                break;
            case Constants.DROP_AT_SHOP:
                title = getString(R.string.check_in_scan_for) + " " + name;
                break;
            case Constants.PICKUP_AT_INTERNAL_WAREHOUSE:
                name = "wharehouse";
                title = getString(R.string.warehouse_pick_up);
                break;
            case Constants.DROP_AT_INTERNAL_WAREHOUSE:
                name = "wharehouse";
                title = getString(R.string.warehouse_check_in);
                break;
        }
        initToolBar(toolbar, title, true);

        tabLayout.addTab(tabLayout.newTab().setText("Scanned"));
        tabLayout.addTab(tabLayout.newTab().setText("Remaining"));
        fragments.add(ScannedFragment.newInstance());
        fragments.add(ScannedFragment.newInstance());
        PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        initMediaPlayer();
        //bluetooth
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();// 请求打开
    }


    private void initMediaPlayer() {
        mediaPlayer1 = MediaPlayer.create(this, R.raw.beep);
        mediaPlayer2 = MediaPlayer.create(this, R.raw.scan_failure);
        mediaPlayer3 = MediaPlayer.create(this, R.raw.already_scan);
        mediaPlayer4 = MediaPlayer.create(this, R.raw.scan_pieces);
        //Mandatory modification of system sound to the maximum.
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0);
    }

    List<Fragment> fragments = new ArrayList<>();

    class PagerAdapter extends FragmentStatePagerAdapter {

        int mNumOfTabs;

        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }


    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        mPresenter.getScan(type, id);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    return;
                }
                editString = s.toString();
                editText.setSelection(editString.length());
            }
        });
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    editText.setText("");
                    if (!editString.isEmpty()) {
                        //兼容awok 错误的awb
                        if (editString.contains("/")) {
                            String str[] = editString.split("/");
                            editString = str[0];
                        }
                        //兼容awok 错误的awb
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        doExecuteScan(editString);
                                    }
                                });
                            }
                        }).start();
                    }
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    return true;
                }
                return false;
            }
        });
    }

    private Handler handler = new Handler();

    private void playSound(MediaPlayer mediaPlayer) {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(0);
            try {
                mediaPlayer.prepare();
            } catch (IllegalStateException ee) {

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mediaPlayer.start();
    }

    private synchronized void doExecuteScan(String str) {
        int status = executeScan(str);
        switch (status) {
            case 0:
                tabLayout.getTabAt(0).setText("Scanned" + " " + count(resScanBean.scanned));
                tabLayout.getTabAt(1).setText("Remaining" + " " + count(resScanBean.remaining));
                mPresenter.scan(editString, type, id);
                String url = BuildConfig.API_URL + "/api/v0.2/shipments/label/image/small/" + id + "/" + editString;
                PrinterService.downloadAndPrint(url);
                playSound(mediaPlayer1);
                ((ScannedFragment) fragments.get(0)).setData(resScanBean.scanned, true);
                ((ScannedFragment) fragments.get(1)).setData(resScanBean.remaining, false);
                break;
            case 1:
                showError("Already scanned");
                playSound(mediaPlayer3);
                break;
            case 2:
                playSound(mediaPlayer2);
                showError("Invalid awb");
                break;
            case 3:
                playSound(mediaPlayer4);
                ((ScannedFragment) fragments.get(1)).setData(resScanBean.remaining, false);
                break;
        }
    }

    Map<String, ResScanBean.Bill> scannedMap = new HashMap<>();

    /**
     * 执行扫描
     *
     * @param str
     * @return 0 扫描成功 1 已经扫描 2 AWB不存在 3 扫描AWB块
     */
    private int executeScan(String str) {
        if (checkAwb(str, resScanBean.scanned)) return 1;//已扫描
        if (!checkAwb(str, resScanBean.remaining)) return 2;//不存在
        //处理剩下的部分
        for (int i = 0; i < resScanBean.remaining.size(); i++) {
            for (int j = 0; j < resScanBean.remaining.get(i).awbs.size(); j++) {
                if (resScanBean.remaining.get(i).awbs.get(j).awb.equals(str)) {
                    if (!piecesIsScanOver(str, resScanBean.remaining.get(i).awbs)) {
                        scanPieces(str, resScanBean.remaining.get(i).awbs);
                        return 3;
                    }
                    ResScanBean.Bill.Awb awb = resScanBean.remaining.get(i).awbs.remove(j);
                    if (scannedMap.get(resScanBean.remaining.get(i).site_id) != null) {
                        scannedMap.get(resScanBean.remaining.get(i).site_id).awbs.add(awb);
                    } else {
                        scannedMap.put(resScanBean.remaining.get(i).site_id, generateAwb(awb, resScanBean.remaining.get(i).site_id, resScanBean.remaining.get(i).collection_point_name));
                    }
                    break;
                }
            }
            if (resScanBean.remaining.get(i).awbs.isEmpty()) {
                resScanBean.remaining.remove(i);
            }
        }
        resScanBean.scanned.clear();
        //处理已扫描的部分
        for (Map.Entry<String, ResScanBean.Bill> entry : scannedMap.entrySet()) {
            resScanBean.scanned.add(entry.getValue());
        }
        return 0;
    }

    //对还没有扫描的商店重新生成一个新的awb显示在已扫描部分
    private ResScanBean.Bill generateAwb(ResScanBean.Bill.Awb awb, String siteId, String name) {
        ArrayList<ResScanBean.Bill.Awb> awbs = new ArrayList<>();
        awbs.add(awb);
        ResScanBean.Bill bill = new ResScanBean.Bill();
        bill.site_id = siteId;
        bill.collection_point_name = name;
        bill.awbs = awbs;
        return bill;
    }

    //判断扫描的订单块是否已经扫描完成
    private boolean piecesIsScanOver(String awb, ArrayList<ResScanBean.Bill.Awb> awbs) {
        boolean state = false;
        for (ResScanBean.Bill.Awb currentAwb : awbs) {
            if (currentAwb.awb.equals(awb)) {
                if (currentAwb.pieces > 1) {
                    if (currentAwb.pieces - currentAwb.scanNumber == 1) {
                        state = true;
                    }
                } else {
                    state = true;
                }
                break;
            }
        }
        return state;
    }

    //扫描块
    private void scanPieces(String awb, ArrayList<ResScanBean.Bill.Awb> awbs) {
        for (int i = 0; i < awbs.size(); i++) {
            if (awbs.get(i).awb.equals(awb)) {
                ResScanBean.Bill.Awb currentAwb = awbs.get(i);
                currentAwb.scanNumber = currentAwb.scanNumber + 1;
                awbs.set(i, currentAwb);
                break;
            }
        }
    }

    //检查是否已经扫描过/是否存在
    private boolean checkAwb(String str, ArrayList<ResScanBean.Bill> bills) {
        boolean isExist = false;
        for (ResScanBean.Bill bill : bills) {
            for (ResScanBean.Bill.Awb awb1 : bill.awbs) {
                if (awb1.awb.equals(str)) {
                    isExist = true;
                    break;
                }
            }
        }
        return isExist;
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void scan(ScannerEvent scannerEvent) {
        editString = scannerEvent.message;
        //兼容awok 错误的awb
        if (editString.contains("/")) {
            String str[] = editString.split("/");
            editString = str[0];
        }
        //兼容awok 错误的awb
        doExecuteScan(editString);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().removeAllStickyEvents();
        EventBus.getDefault().unregister(this);
        mediaPlayer1.release();
        mediaPlayer2.release();
        mediaPlayer3.release();
        mediaPlayer1 = null;
        mediaPlayer2 = null;
        mediaPlayer3 = null;
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Message message) {
        new AlertView(message.obj.toString(), null, null, null, new String[]{"Ok"}, null, this, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                ((AlertView) o).dismiss();
            }
        }).setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(Object o) {
                mPresenter.getScan(type, id);
            }
        }).show();
    }

    @Override
    protected int getResourcesId() {
        return R.layout.activity_scan;
    }

    @Override
    protected void initInjector() {
        DaggerScanComponent.builder().appComponent(getApplicationComponent()).scanModule(new ScanModule(this, this)).build().inject(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void showLoading() {
        showProgressDialog(this, getString(R.string.loading));
    }

    @Override
    public void dismiss() {
        dismissProgressDialog();
    }

    @Override
    public void showError(String msg) {
        editText.setText("");
        editText.requestFocus();
        ToastUtils.showShort(this, msg);
    }

    @OnClick({R.id.btn, R.id.btn_scan, R.id.ib_scan})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn:
                List<String> list = awbToList();
                if (!list.isEmpty()) {
                    if (button.getText().equals(getString(R.string.verify))) {
                        mPresenter.verify(list, type, id);
                    }
                    if (button.getText().equals(getString(R.string.finalize))) {
                        mPresenter.finalize(list, type, id);
                    }
                }
                break;
            case R.id.btn_scan:
                Intent intent2 = new Intent(ScanActivity.this, CameraActivity.class);
                startActivityForResult(intent2, Constants.SCAN_ORC_REQUEST);
                break;
            case R.id.ib_scan:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ScanActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
                    } else {
                        startActivity(new Intent(ScanActivity.this, CaptureActivity.class));
                    }
                } else {
                    startActivity(new Intent(ScanActivity.this, CaptureActivity.class));
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivityForResult(new Intent(ScanActivity.this, CaptureActivity.class), Constants.SCAN_BAR_OR_QR_CODE_REQUEST);
            } else {
                //用户拒绝权限
                Toast.makeText(this, " No Permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private List<String> awbToList() {
        List<String> list = new ArrayList<>();
        if (scannedMap.isEmpty()) {
            return list;
        }
        for (Map.Entry<String, ResScanBean.Bill> entry : scannedMap.entrySet()) {
            for (ResScanBean.Bill.Awb awb : entry.getValue().awbs) {
                list.add(awb.awb);
            }
        }
        return list;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                showQuit();
                break;
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showQuit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showQuit() {
        quitAlertView = new AlertView("Confirm Quit", "Confirm Quit Scan?", null, "No", null, new String[]{"Yes"}, this, AlertView.Style.Alert, this);
        quitAlertView.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.ENABLE_BLUETOOTH_REQUEST:
                    startService(new Intent(ScanActivity.this, PrinterService.class));
                    break;
//                case Constants.SCAN_BAR_OR_QR_CODE_REQUEST:
//                    Bundle bundle = data.getExtras();
//                    String scanResult = bundle.getString(CaptureActivity.INTENT_EXTRA_KEY_SCAN);
//                    doScan(scanResult);
//                    break;
            }
        }
    }

    @Override
    public void onItemClick(Object o, int position) {
        if (o == quitAlertView) {
            if (position == 0) {
                finish();
            }
        }
    }

    @Override
    public void target(List<String> list) {
        Intent intent = new Intent(ScanActivity.this, HandWriteActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("type", type);
        intent.putExtra("id", id);
        intent.putStringArrayListExtra("param", (ArrayList<String>) list);
        startActivity(intent);
    }

    @Override
    public void afterVerify(ResFinalize resFinalize) {
        if (!resFinalize.remaining.isEmpty()) {
            String tip;
            if (resFinalize.remaining.size() == 1) {
                tip = resFinalize.remaining.size() + " parcel is missing";
            } else {
                tip = resFinalize.remaining.size() + " parcels are missing";
            }
            new AlertView("Alert", tip, null, "Back", new String[]{"Ok"}, null, this, AlertView.Style.Alert, new OnItemClickListener() {
                @Override
                public void onItemClick(Object o, int position) {
                    if (position == 0) {
                        if (type.equals(Constants.PICKUP_AT_INTERNAL_WAREHOUSE) || type.equals(Constants.DROP_AT_INTERNAL_WAREHOUSE)) {
                            button.setText(getString(R.string.finalize));
                        } else {
                            target(awbToList());
                        }
                    }
                    ((AlertView) o).dismiss();
                }
            }).show();
        } else {
            if (type.equals(Constants.PICKUP_AT_INTERNAL_WAREHOUSE) || type.equals(Constants.DROP_AT_INTERNAL_WAREHOUSE)) {
                button.setText(getString(R.string.finalize));
            } else {
                target(awbToList());
            }
        }
    }

    @Override
    public void afterFinalize(final ResFinalize resFinalize) {
        if (!resFinalize.remaining.isEmpty()) {
            showAlert(resFinalize.remaining);
        } else if (!resFinalize.failure.isEmpty()) {
            showAlert(resFinalize.failure);
        } else {
            new AlertView("Success", null, null, null, new String[]{"Ok"}, null, this, AlertView.Style.Alert, new OnItemClickListener() {
                @Override
                public void onItemClick(Object o, int position) {
                    ((AlertView) o).dismiss();
                }
            }).setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(Object o) {
                    EventBus.getDefault().post("");
                }
            }).show();
        }
    }

    private void showAlert(List<ResFinalize.Awb> awbList) {
        String tip;
        if (awbList.size() == 1) {
            tip = awbList.size() + " parcel is not verified";
        } else {
            tip = awbList.size() + " parcels are not verified";
        }
        new AlertView("Alert", tip, null, null, new String[]{"Ok"}, null, this, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                ((AlertView) o).dismiss();
            }
        }).show();
    }

    @Override
    public void showFail(String msg, String[] content) {
        new AlertView("Ops", null, null, "Ok", content, null, this, AlertView.Style.ListAlert, this).show();
    }

    public ResScanBean resScanBean;

    @Override
    public void showScan(ResScanBean resScanBean) {
        if (resScanBean != null) {
            this.resScanBean = resScanBean;
            for (ResScanBean.Bill bill : resScanBean.scanned) {
                scannedMap.put(bill.site_id, bill);
            }
            ((ScannedFragment) fragments.get(0)).setData(resScanBean.scanned, true);
            ((ScannedFragment) fragments.get(1)).setData(resScanBean.remaining, false);
            tabLayout.getTabAt(0).setText("Scanned" + " " + count(resScanBean.scanned));
            tabLayout.getTabAt(1).setText("Remaining" + " " + count(resScanBean.remaining));
            if (resScanBean.need_print) {
                connectPrinter();
            } else {
                PrinterService.isConnected = false;
            }
        }
    }

    private void connectPrinter() {
        if (bluetoothAdapter == null) {
            new AlertView("Your device does not support bluetooth,cannot connect to printer.", null, null, null, new String[]{"Ok"}, null, this, AlertView.Style.Alert, null).show();
            return;
        }
        if (!bluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, Constants.ENABLE_BLUETOOTH_REQUEST);
        } else {
            startService(new Intent(ScanActivity.this, PrinterService.class));
        }
    }

    private int count(List<ResScanBean.Bill> list) {
        int count = 0;
        for (ResScanBean.Bill bill : list) {
            count = count + bill.awbs.size();
        }
        return count;
    }
}
