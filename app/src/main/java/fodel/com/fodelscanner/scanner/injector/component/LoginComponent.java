package fodel.com.fodelscanner.scanner.injector.component;

import dagger.Component;
import fodel.com.fodelscanner.scanner.injector.PerActivity;
import fodel.com.fodelscanner.scanner.injector.module.UserModule;
import fodel.com.fodelscanner.scanner.ui.LoginActivity;

/**
 * Created by fula on 2017/7/24.
 */
@PerActivity
@Component(dependencies = AppComponent.class, modules = UserModule.class)
public interface LoginComponent {

    LoginActivity inject(LoginActivity activity);
}
