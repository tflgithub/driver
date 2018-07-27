package fodel.com.fodelscanner.scanner.injector.component;

import dagger.Component;
import fodel.com.fodelscanner.scanner.injector.PerActivity;
import fodel.com.fodelscanner.scanner.injector.module.FinalizeModule;
import fodel.com.fodelscanner.scanner.ui.HandWriteActivity;

/**
 * Created by fula on 2017/7/13.
 */
@PerActivity
@Component(dependencies = AppComponent.class,modules = FinalizeModule.class)
public interface FinalizeComponent {

    HandWriteActivity inject(HandWriteActivity activity);
}
