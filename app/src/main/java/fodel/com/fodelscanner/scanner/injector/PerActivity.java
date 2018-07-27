package fodel.com.fodelscanner.scanner.injector;
import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by fula on 2017/7/13.
 */
@Scope
@Retention(RUNTIME)
public @interface PerActivity {
}
