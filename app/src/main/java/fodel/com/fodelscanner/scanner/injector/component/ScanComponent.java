package fodel.com.fodelscanner.scanner.injector.component;

import dagger.Component;
import fodel.com.fodelscanner.scanner.injector.PerActivity;
import fodel.com.fodelscanner.scanner.injector.module.ScanModule;
import fodel.com.fodelscanner.scanner.ui.ScanActivity;

/**
 * Created by fula on 2017/7/13.
 */
@PerActivity
@Component(dependencies = AppComponent.class, modules = ScanModule.class)
public interface ScanComponent {

    ScanActivity inject(ScanActivity activity);
}
